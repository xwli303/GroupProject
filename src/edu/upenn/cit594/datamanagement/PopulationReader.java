package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PopulationData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PopulationReader implements IPopulationReader {
    private Logger logger = Logger.getInstance();

    public Set<PopulationData> readCsvFile(String filename) {
        Set<PopulationData> data = new HashSet<>();
        BufferedReader reader = null;
        try {
            logger.log("Population Reader opening file: " + filename);
            reader = new BufferedReader(new FileReader(filename));

            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // skip header line
                }
                List<String> tokens = Arrays.asList(line.split(","));
                int zipCode = Integer.parseInt(tokens.get(0).replaceAll("\"", ""));
                int population = Integer.parseInt(tokens.get(1).replaceAll("\"", ""));
                PopulationData record = new PopulationData(zipCode, population);

                data.add(record);
            }
            reader.close();

        } catch (IOException e) {
            //log
        }
        return data;
    }
}
