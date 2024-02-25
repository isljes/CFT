package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Filter {

    private final String SUFFIX = ".txt";
    private final String DEFAULT_NAME_FOR_INTS_FILE="integers";
    private final String DEFAULT_NAME_FOR_FLOATS_FILE="floats";
    private final String DEFAULT_NAME_FOR_STRINGS_FILE="strings";

    private String pathOfResults;
    private String prefix;
    private boolean fullStats;
    private boolean append;
    private Stats stats;
    private List<String> inFiles;
    private String resultingPathToIntegers;
    private String resultingPathToFloats;
    private String resultingPathToStrings;
    private File integersFile;
    private File floatsFile;
    private File stringsFile;
    private BufferedWriter intsBW;
    private BufferedWriter floatsBW;
    private BufferedWriter stringsBW;


    public Filter() {
        pathOfResults=".";
        prefix="";
        fullStats=false;
        append=false;
        inFiles = new ArrayList<>();
    }
    public void start(){
        resultingPathToIntegers = pathOfResults + File.separator + prefix + DEFAULT_NAME_FOR_INTS_FILE + SUFFIX;
        resultingPathToFloats = pathOfResults + File.separator + prefix + DEFAULT_NAME_FOR_FLOATS_FILE + SUFFIX;
        resultingPathToStrings = pathOfResults + File.separator + prefix + DEFAULT_NAME_FOR_STRINGS_FILE + SUFFIX;
        stats=new Stats(fullStats);
        filter();
    }

    private void filter()  {
        try {
            for (String inFile : inFiles) {
                try (var inBR = new BufferedReader(new FileReader(inFile))) {
                    String line;
                    while ((line = inBR.readLine()) != null) {
                        switch (filterElementByType(line)) {
                            case INT -> intsBW.write(line+"\n");
                            case FLOAT -> floatsBW.write(line+"\n");
                            case STRING -> stringsBW.write(line+"\n");
                            case UNKNOWN -> System.out.println("Incorrect data : "+line);
                        }
                    }
                }catch (FileNotFoundException e){
                    System.out.println("\n"+e.getMessage()+"\n");
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            }
        }finally {

            try {
                if (intsBW!=null){
                    intsBW.close();}
                if (floatsBW!=null){
                    floatsBW.close();}
                if (stringsBW!=null){
                    stringsBW.close();}
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                stats.printStats();
            }

        }

    }


    private ElementsType filterElementByType(String line) throws IOException {
        Pattern ints = Pattern.compile("^[-+]?[0-9]+$");
        Pattern floats = Pattern.compile("^[-+]?[0-9]+\\.[0-9]+(E[-+][0-9]+)?$");
        Pattern strings = Pattern.compile("^(\\s*[a-zA-zА-Яа-я]+\\s*)*$");

        if (ints.matcher(line).matches()) {
            if(integersFile==null){
                integersFile=new File(resultingPathToIntegers);
                intsBW=new BufferedWriter(new FileWriter(integersFile,append));
                stats.addFileToStats(ElementsType.INT,integersFile);
            }
            return ElementsType.INT;
        } else if (floats.matcher(line).matches()) {
            if(floatsFile==null){
                floatsFile=new File(resultingPathToFloats);
                floatsBW=new BufferedWriter(new FileWriter(floatsFile,append));
                stats.addFileToStats(ElementsType.FLOAT,floatsFile);
            }
            return ElementsType.FLOAT;
        } else if (strings.matcher(line).matches()) {
            if(stringsFile==null){
                stringsFile=new File(resultingPathToStrings);
                stringsBW=new BufferedWriter(new FileWriter(stringsFile,append));
                stats.addFileToStats(ElementsType.STRING,stringsFile);
            }
            return ElementsType.STRING;
        }

        return ElementsType.UNKNOWN;
    }

    public void setInFiles(String... files){
        for(String file:files){
            inFiles.add(file);
        }
    }

    public void setPathOfResults(String pathOfResults) {
        this.pathOfResults = pathOfResults;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setFullStats(boolean fullStats) {
        this.fullStats = fullStats;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

}
