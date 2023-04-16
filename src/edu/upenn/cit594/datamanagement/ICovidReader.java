package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidData;

import java.io.IOException;
import java.util.List;

public interface ICovidReader {

    List<CovidData> readCsvFile(String filename);

    List<CovidData> readJsonFile(String filename);
}
