package edu.upenn.cit594.processor;
import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.datamanagement.ICovidReader;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PopulationData;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.*;


public class CovidProcessor implements  ICovidProcessor{
    private String filename;

    public CovidProcessor (String filename){
        this.filename = filename;
    }
    private ICovidReader covidReader = new CovidReader();

    public List<CovidData> getAllCovidData(){
        List<CovidData> covidData = new ArrayList<>();
        //determine file type
        if(this.filename.endsWith(".csv")){
            covidData = covidReader.readCsvFile(this.filename);
        }
        else if(this.filename.endsWith(".json")){
            covidData = covidReader.readJsonFile(this.filename);
        }
        else {
            //error out - invalid file extension
        }
        return covidData;
    };

    public Map<Integer, Double> getZipVaxDataPerCapita(String date, String vaxType, Set<PopulationData> populationData){
        LocalDate inputDate = this.convertToDate(date);
        List<CovidData> dateCovidData = this.getDataByDate(inputDate);
        //match each record with zip
        //Map<PopulationData, CovidData> zipCovidData = this.matchDataByZip(populationData, dateCovidData );
        Map<Integer, Double> zipVaxData = new HashMap<>();

        //loop thru date's covid data and match with population data by zipcode
        for (CovidData covid: dateCovidData) {
            PopulationData matchingZipPop = populationData.stream()
                    .filter(obj -> obj.getZipCode() == covid.getZipCode())
                    .findFirst()
                    .orElse(null);
            if(matchingZipPop != null){
                int vaxCount = 0;
                //determine type of vaccine (full or partial) to calculate vax per capita with
                if(vaxType.toLowerCase().equals("partial")){
                    vaxCount = covid.getPartial();
                } else if (vaxType.toLowerCase().equals("full")) {
                    vaxCount = covid.getFull();
                }
                //calculate partial or full by capita
                double vaxPerCapita = (double)vaxCount / matchingZipPop.getPopulation();

                zipVaxData.put(matchingZipPop.getZipCode(), vaxPerCapita);
            }
        }

        return zipVaxData;
    }

    public Map<Integer, Integer> getZipPositiveCases(Set<PopulationData> populationData) {
        Map<Integer, Integer> zipPositive = new HashMap<>();
        List<CovidData> covidData = this.getAllCovidData();
        for (CovidData covid : covidData) {
            PopulationData matchingZipPop = populationData.stream()
                    .filter(obj -> obj.getZipCode() == covid.getZipCode())
                    .findFirst()
                    .orElse(null);
            if (matchingZipPop != null) {
                zipPositive.put(matchingZipPop.getZipCode(), covid.getPositive());
            }
        }
        return zipPositive;
    }


    private List<CovidData> getDataByDate(LocalDate inputDate){
        List<CovidData> allCovidData = this.getAllCovidData();
        //filter covidData for the input data
        List<CovidData> inputDateCovidData = allCovidData.stream()
                .filter(data -> data.getVaccination().equals(inputDate))
                .toList();

        return inputDateCovidData;
    }

    //match DateCovidDate with Zipcode, may not be needed
    private Map<PopulationData, CovidData> matchDataByZip (Set<PopulationData> population, List<CovidData> covidData){
        Map<PopulationData, CovidData> zipMap = new HashMap<>();

        for (CovidData covid: covidData) {
            PopulationData matchingZipPop = population.stream()
                    .filter(obj -> obj.getZipCode() == covid.getZipCode())
                    .findFirst()
                    .orElse(null);
            if(matchingZipPop != null){

                zipMap.put(matchingZipPop, covid);
            }
        }
        return zipMap;
    }

    private LocalDate convertToDate(String userInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(userInput, formatter);
        return date;
    }

}
