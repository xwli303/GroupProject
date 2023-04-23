package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PopulationData;

import java.io.IOException;
import java.util.Set;

public interface IPopulationProcessor {
    Set<PopulationData> getZipPopulation () throws IOException;
    int getTotalPopulation() throws IOException;



}
