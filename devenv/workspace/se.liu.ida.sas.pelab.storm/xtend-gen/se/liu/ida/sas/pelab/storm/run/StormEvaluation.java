package se.liu.ida.sas.pelab.storm.run;

import hu.bme.mit.inf.measurement.utilities.configuration.BaseConfiguration;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class StormEvaluation {
  private static final Pattern pattern = Pattern.compile("^System failure probability at timebound 1 is ([01]\\.\\d*)$");

  public static StormRunInfo evalueate(final BaseConfiguration cfg, final Function0<Pair<String, List<String>>> generator) {
    try {
      HashMap<String, Double> results = CollectionLiterals.<String, Double>newHashMap();
      double cumTransformationTime = 0.0;
      double cumAnalysisTime = 0.0;
      final long start = System.nanoTime();
      final Pair<String, List<String>> value = generator.apply();
      final long end = System.nanoTime();
      final String basemodel = value.getKey();
      final List<String> tops = value.getValue();
      double _cumTransformationTime = cumTransformationTime;
      cumTransformationTime = (_cumTransformationTime + (((end - start) / 1000.0) / 1000.0));
      for (final String top : tops) {
        {
          long _timeoutS = cfg.getTimeoutS();
          long _multiply = (_timeoutS * 1000);
          double remainingtime_ms = (_multiply - (cumAnalysisTime + cumTransformationTime));
          if ((remainingtime_ms > 0)) {
            final long time = Math.round(Math.ceil((remainingtime_ms / 1000.0)));
            final long trafoStart = System.nanoTime();
            String _stormFile = cfg.getStormFile();
            final File file = new File(_stormFile);
            InputOutput.<String>println(file.getAbsolutePath());
            file.createNewFile();
            final FileWriter writer = new FileWriter(file);
            StringConcatenation _builder = new StringConcatenation();
            _builder.append(top);
            _builder.newLineIfNotEmpty();
            _builder.append(basemodel);
            _builder.newLineIfNotEmpty();
            writer.write(_builder.toString());
            writer.flush();
            writer.close();
            final long trafoEnd = System.nanoTime();
            final long analysisStart = System.nanoTime();
            String _stormFile_1 = cfg.getStormFile();
            String _string = Long.valueOf(time).toString();
            final ProcessBuilder builder = new ProcessBuilder("storm-dft", "-dft", _stormFile_1, 
              "--timebound", "1", 
              "--bdd", 
              "--precision", "1e-09", 
              "--timeout", _string);
            final Process process = builder.start();
            InputStream _inputStream = process.getInputStream();
            final Scanner io = new Scanner(_inputStream);
            while (io.hasNextLine()) {
              {
                final String line = io.nextLine();
                StringConcatenation _builder_1 = new StringConcatenation();
                _builder_1.append("DEBUG: ");
                _builder_1.append(line);
                InputOutput.<String>println(_builder_1.toString());
                final Matcher match = StormEvaluation.pattern.matcher(line);
                boolean _find = match.find();
                if (_find) {
                  results.put(top, Double.valueOf(Double.parseDouble(match.group(1))));
                }
              }
            }
            final long analysisEnd = System.nanoTime();
            double _cumAnalysisTime = cumAnalysisTime;
            cumAnalysisTime = (_cumAnalysisTime + (((analysisEnd - analysisStart) / 1000.0) / 1000.0));
            double _cumTransformationTime_1 = cumTransformationTime;
            cumTransformationTime = (_cumTransformationTime_1 + (((trafoEnd - trafoStart) / 1000.0) / 1000.0));
          }
        }
      }
      long _timeoutS = cfg.getTimeoutS();
      long _multiply = (_timeoutS * 1000);
      double remainingtime_ms = (_multiply - (cumAnalysisTime + cumTransformationTime));
      final boolean timeout = (remainingtime_ms < 0);
      return new StormRunInfo(cumTransformationTime, cumAnalysisTime, results, timeout);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
