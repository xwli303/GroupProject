package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ICovidReader {

    List<CovidData> readCsvFile(String filename) throws IOException;

    List<CovidData> readJsonFile(String filename) throws IOException;
}
