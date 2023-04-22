package edu.upenn.cit594;


import edu.upenn.cit594.datamanagement.CSVPropertyReader;
import edu.upenn.cit594.datamanagement.PropertyReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.*;
import edu.upenn.cit594.ui.Menu;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.err.println("Error: invalid number of arguments.");
            return;
        }

        String covid = args[0];
        String properties = args[1];
        String population = args[2];
        String log = args[3];

        Logger logger = Logger.getInstance();
        ICovidProcessor covidProcessor = new CovidProcessor(covid);
        IPopulationProcessor populationProcessor = new PopulationProcessor(population);
        //property
        PropertyReader propertyReader = new CSVPropertyReader(properties);
        PropertyProcessor propertyProcessor = new PropertyProcessor(propertyReader);
        LivableAreaAverage livableAreaAverage = new LivableAreaAverage();
        MarketValueAverage marketValueAverage = new MarketValueAverage();

        //log command line arguments
        logger.log("Command Line Arguments: " + covid + " " + properties + " " + log);

        Menu menu = new Menu(populationProcessor, covidProcessor, propertyProcessor, livableAreaAverage, marketValueAverage);
        menu.ShowMenu();
    }



}