package edu.upenn.cit594.processor;
import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.datamanagement.ICovidReader;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PopulationData;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CovidProcessor implements ICovidProcessor{
    private String filename;
    public static List<CovidData> covidData;

    public CovidProcessor (String filename ) throws IOException {
        this.filename = filename;
        this.covidData = this.getAllCovidData();
    }
    private ICovidReader covidReader = new CovidReader();

    public List<CovidData> getAllCovidData() throws IOException {
        List<CovidData> allCovidData = new ArrayList<>();
        //determine file type
        if(this.filename.endsWith(".csv")){
            allCovidData = covidReader.readCsvFile(this.filename);
        }
        else if(this.filename.endsWith(".json")){
            allCovidData = covidReader.readJsonFile(this.filename);
        }
        covidData = allCovidData;
        return allCovidData;
    };

    public Map<Integer, Double> getZipVaxDataPerCapita(String date, String vaxType, Set<PopulationData> populationData) throws IOException {
        LocalDate inputDate = this.convertToDate(date);
        List<CovidData> dateCovidData = this.getDataByDate(inputDate);

        //match each record with zip
        Map<Integer, Double> zipVaxData = new HashMap<>();

        //return empty map if date out of range or no data exists for date
        if(dateCovidData == null || dateCovidData.size() == 0){
            return zipVaxData;
        }

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
                DecimalFormat df = new DecimalFormat("#.0000");
                vaxPerCapita = Double.parseDouble(df.format(vaxPerCapita));

                zipVaxData.put(matchingZipPop.getZipCode(), vaxPerCapita);
            }
        }
        return zipVaxData;
    }

    public static int getPosCasesByZipDate(String date, String zipCode){
        int posCases = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inputDate = LocalDate.parse(date, formatter);

        CovidData inputDateCovidData = covidData.stream()
                .filter(data -> data.getVaccination().equals(inputDate) && data.getZipCode() == Integer.parseInt(zipCode))
                .findFirst()
                .orElse(null);
        if(inputDateCovidData != null){
            posCases = inputDateCovidData.getPositive();
        }
        return posCases;
    }

    private List<CovidData> getDataByDate(LocalDate inputDate) throws IOException {
        //filter covidData for the input data
        return covidData.stream()
                .filter(data -> data.getVaccination().equals(inputDate))
                .collect(Collectors.toList());
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
