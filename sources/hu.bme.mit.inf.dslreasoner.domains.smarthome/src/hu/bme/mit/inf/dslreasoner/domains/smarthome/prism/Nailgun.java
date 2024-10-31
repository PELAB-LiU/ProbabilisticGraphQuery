package hu.bme.mit.inf.dslreasoner.domains.smarthome.prism;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

import hu.bme.mit.inf.measurement.utilities.Config;

@SuppressWarnings("all")
@Deprecated
public class Nailgun implements Runnable {
   public static Nailgun INSTANCE;

   private static final Object sync = new Object();

   public static Nailgun init(Config cfg) {
      final Nailgun _nailgun = new Nailgun(cfg);
      return Nailgun.INSTANCE = _nailgun;
   }

   public static Nailgun terminate() {
      Nailgun _xblockexpression = null;
      {
         if(Nailgun.INSTANCE != null) {
            Nailgun.INSTANCE.server.destroy();
         }
         _xblockexpression = Nailgun.INSTANCE = null;
      }
      return _xblockexpression;
   }

   public static void test() {
      Nailgun.INSTANCE.testrun();
   }

   private final Process server;

   private final LinkedBlockingQueue<Double> data = new LinkedBlockingQueue<Double>();

   private final String nailgun;

   public Nailgun(Config cfg) {
      try {
         final ProcessBuilder p = new ProcessBuilder("prism","-ng","-javamaxmem",cfg.asString("prism-mem"));
         this.server = p.start();
         final Thread runner = new Thread(this);
         runner.setDaemon(true);
         runner.start();
         this.nailgun = this.stringEnvironment("CFG_NAILGUN", "ng-nailgun");
      } catch(final Throwable _e) {
         throw Exceptions.sneakyThrow(_e);
      }
   }

   @Override
   public void run() {
      try {
         final InputStream _inputStream = this.server.getInputStream();
         final InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
         final BufferedReader reader = new BufferedReader(_inputStreamReader);
         final String _readLine = reader.readLine();
         final String _plus = "DEBUG-BEFORE: " + _readLine;
         InputOutput.<String> println(_plus);
         String line = reader.readLine();
         while(line != null) {
            {
               System.out.println("DEBUG: " + line);
               final boolean _contains = line.contains("Error");
               if(_contains) {
                  InputOutput.<String> println(line);
                  throw new RuntimeException("Error during prism analysis");
               }
               final boolean _startsWith = line.startsWith("Result: ");
               if(_startsWith) {
                  final double reward = Double.parseDouble(line.replaceAll("[^\\d.]", ""));
                  InputOutput.<String> println("NAILGUN STREAM: " + Double.valueOf(reward));
                  this.data.put(Double.valueOf(reward));
               }
               line = reader.readLine();
            }
         }
      } catch(final Throwable _e) {
         //throw Exceptions.sneakyThrow(_e);
      }
   }

   private String stringEnvironment(final String variable, final String defaultValue) {
      final Map<String, String> env = System.getenv();
      final String value = env.get(variable);
      if(value == null) {
         return defaultValue;
      } else {
         return value;
      }
   }

   private void testrun() {
      try {
         synchronized(Nailgun.sync) {
            this.data.clear();
            System.out.println("TEST CALLED.");
            final Process client = new ProcessBuilder(this.nailgun, "prism.PrismCL", "testmodel.prism", "-pf",
                  "R{\"utility\"}=? [I=1]").start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String line = reader.readLine();
            while(line != null) {
               System.out.println("TEST STDOUT: " + line);
               line = reader.readLine();
            }
            reader = new BufferedReader(new InputStreamReader(client.getErrorStream()));
            line = reader.readLine();
            line = reader.readLine();
            while(line != null) {
               System.out.println("TEST STDERR:" + line);
               line = reader.readLine();
            }
            client.waitFor();
            System.out.println("TEST CALL COMPLETED");

            if(data.isEmpty()) {
               throw new RuntimeException("Test reading failed.");
            }
            data.clear();
         }
      } catch(final Throwable _e) {
         throw Exceptions.sneakyThrow(_e);
      }
   }

   public Double verify(final String model, final int time) {
      try {
         synchronized(Nailgun.sync) {
            this.data.clear();
            final StringConcatenation _builder = new StringConcatenation();
            _builder.append("R{\"utility\"}=? [I=");
            _builder.append(time);
            _builder.append("]");
            final String _string = _builder.toString();
            System.out.println("Nailgun called.");
            final Process client = new ProcessBuilder(this.nailgun, "prism.PrismCL", model, "-pf", _string).start();

            client.waitFor();
            System.out.println("Nailgun finished.");
            final Double result = this.data.take();
            InputOutput.<String> println("NAILGUN reading result: " + result);
            return result;
         }
      } catch(final Throwable _e) {
         throw Exceptions.sneakyThrow(_e);
      }
   }
}
