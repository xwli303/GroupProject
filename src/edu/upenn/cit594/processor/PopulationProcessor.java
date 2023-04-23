package edu.upenn.cit594.processor;
import edu.upenn.cit594.datamanagement.IPopulationReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.util.PopulationData;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PopulationProcessor implements IPopulationProcessor{
    private String filename;
    public static Set<PopulationData> populationData = new HashSet<>();
    public PopulationProcessor (String filename){
        this.filename = filename;
    }

    private IPopulationReader populationReader = new PopulationReader();
    //call this method to get population zipcode population
    public Set<PopulationData> getZipPopulation () throws IOException {
        Set zipPopulation = new HashSet<>();
        zipPopulation = populationReader.readCsvFile(this.filename);
        populationData = zipPopulation;
        return zipPopulation;
    }

    public int getTotalPopulation() throws IOException {
        Set<PopulationData> zipPopulation = this.getZipPopulation();
        int totalPopulation = 0;
        for (PopulationData zip : zipPopulation
             ) {
            totalPopulation += zip.getPopulation();
        }
        return totalPopulation;
    }

    public static Set<PopulationData> getAllPopulationData(){
        return populationData;
    }
}
