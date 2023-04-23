package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.PopulationData;
import java.io.IOException;
import java.util.Set;

public interface IPopulationReader {

    Set<PopulationData> readCsvFile(String filename) throws IOException;
}
