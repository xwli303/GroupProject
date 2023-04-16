package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.datamanagement.ICovidReader;

import edu.upenn.cit594.processor.CovidProcessor;
import edu.upenn.cit594.processor.ICovidProcessor;
import edu.upenn.cit594.processor.IPopulationProcessor;
import edu.upenn.cit594.processor.PopulationProcessor;

import edu.upenn.cit594.util.PopulationData;

import java.time.LocalDate;
import java.util.*;


public class Main {
    public static void main(String[] args) {

        ICovidReader covidReader = new CovidReader();
        IPopulationProcessor populationProcessor = new PopulationProcessor("population.csv");
        ICovidProcessor covidProcessor = new CovidProcessor("covid_data.csv");

        Set<PopulationData> populationData = populationProcessor.getZipPopulation();
        //total population
        int totalPopulation = populationProcessor.getTotalPopulation();
        System.out.println(totalPopulation);

        //vax per capita
        LocalDate date = LocalDate.parse("2021-03-25");
        Map<Integer, Double> dateVaxData = covidProcessor.getZipVaxDataPerCapita(date, "full", populationData);
        for (Map.Entry<Integer, Double> entry : dateVaxData.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }



    }



}