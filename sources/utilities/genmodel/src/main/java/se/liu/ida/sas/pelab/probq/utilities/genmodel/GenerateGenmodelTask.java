package se.liu.ida.sas.pelab.probq.utilities.genmodel;

import java.util.Objects;

public class GenerateGenmodelTask {
    public static void main(String[] args){
        String ecore = arg("--ecore", "-e", args);
        String genmodel = arg("--genmodel", "-g", args);
        String modeldir = arg("--modeldir", "-m", args, null);
        String basepackage = arg("--basepackage", "-b", args, null);
        try{
            (new GenerateGenmodel(ecore, genmodel, modeldir, basepackage)).execute();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
        
    }

    private static String arg(String longKey, String shortKey, String[] args){
        for(String arg : args){
            String[] data = arg.split("=", 2);
            if(Objects.equals(data[0], shortKey) || Objects.equals(data[0], longKey)){
                return data[1];
            }
        }
        throw new RuntimeException("Argument not found: "+shortKey+" or "+longKey);
    }
    private static String arg(String longKey, String shortKey, String[] args, String defaultValue){
        for(String arg : args){
            String[] data = arg.split("=", 2);
            if(Objects.equals(data[0], shortKey) || Objects.equals(data[0], longKey)){
                return data[1];
            }
        }
        return defaultValue;
    }
}