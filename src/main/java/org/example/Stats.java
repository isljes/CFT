package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Stats {

    private Map<ElementsType, File> outFiles;

    private boolean fullStats;

    private long countInt;
    private long maxInt;
    private long minInt;
    private long sumInt;

    private long countFloat;
    private double maxFloat;
    private double minFloat;
    private double sumFloat;

    private long countString;
    private int maxString;
    private int minString;

    public Stats(boolean fullStats) {
        this.countInt = 0;
        this.maxInt = Long.MIN_VALUE;
        this.minInt = Long.MAX_VALUE;
        this.sumInt = 0;

        this.countFloat = 0;
        this.maxFloat = Double.MIN_VALUE;
        this.minFloat = Double.MAX_VALUE;
        this.sumFloat = 0;

        this.countString = 0;
        this.maxString = -1;
        this.minString = Integer.MAX_VALUE;

        this.fullStats = fullStats;
    }

    public void addFileToStats(ElementsType elementsType,File file) {
        if (outFiles == null) {
            outFiles = new HashMap<>();
        }
        outFiles.put(elementsType, file);
    }


    public void printStats(){
        if (outFiles != null) {
            writeStats();
            if (fullStats) printFullStats();
            else printShortStats();
        }
    }

    private void writeStats() {
        for (Map.Entry<ElementsType, File> entry : outFiles.entrySet()) {
            try (var bufferedReader = new BufferedReader(new FileReader(entry.getValue()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    switch (entry.getKey()) {
                        case INT -> stats(Long.parseLong(line));
                        case FLOAT -> stats(Double.parseDouble(line));
                        case STRING -> stats(line);
                    }
                }
            }catch (NumberFormatException e){
                System.out.println("One or more output files contains incorrect data");
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private void stats(Long currInt) {
        countInt++;
        sumInt += currInt;
        if (currInt < minInt) minInt = currInt;
        if (currInt > maxInt) maxInt = currInt;
    }

    private void stats(Double currFloat) {
        countFloat++;
        sumFloat += currFloat;
        if (currFloat < minFloat) minFloat = currFloat;
        if (currFloat > maxFloat) maxFloat = currFloat;
    }

    private void stats(String currString) {
        countString++;
        if (currString.length() < minString) minString = currString.length();
        if (currString.length() > maxString) maxString = currString.length();
    }

    private void printFullStats() {
        if (countInt > 0) {
            System.out.printf("""
                            Statistics for integers:
                            \tcount %s
                            \tmin %s
                            \tmax %s
                            \tsum %s
                            \tavg %s

                            """
                    , countInt, minInt, maxInt, sumInt, sumInt / countInt);
        }

        if (countFloat > 0) {
            System.out.printf("""
                            Statistics for floats:
                            \tcount %s
                            \tmin %s
                            \tmax %s
                            \tsum %s
                            \tavg %s

                            """
                    , countFloat, minFloat, maxFloat, sumFloat, sumFloat / countFloat);
        }

        if (countString > 0) {
            System.out.printf("""
                            Statistics for strings:
                            \tcount %s
                            \tmin %s
                            \tmax %s

                            """
                    , countString, minString, maxString);
        }
    }

    private void printShortStats() {
        if (countInt > 0) {
            System.out.printf(
                    """
                            Statistics for integers:
                            \tcount %s

                            """, countInt);
        }

        if (countFloat > 0) {
            System.out.printf(
                    """
                            Statistics for floats:
                            \tcount %s

                            """, countFloat);
        }

        if (countString > 0) {
            System.out.printf(
                    """
                            Statistics for strings:
                            \tcount %s

                            """, countString);
        }

    }
}
