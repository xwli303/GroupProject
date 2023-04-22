package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PopulationData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICovidProcessor {
    List<CovidData> getAllCovidData();

    Map<Integer, Double> getZipVaxDataPerCapita(String inputDate, String vaxType, Set<PopulationData> populationData);

    Map<Integer, Integer> getZipPositiveCases(Set<PopulationData> populationData);
}
