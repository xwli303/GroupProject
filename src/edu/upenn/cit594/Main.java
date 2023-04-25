package edu.upenn.cit594;
import edu.upenn.cit594.datamanagement.PropertyReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.*;
import edu.upenn.cit594.ui.Menu;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args)  {
        Map<String, String> arguments = new HashMap<>();
        for (String arg : args) {
            Pattern pattern = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");
            Matcher matcher = pattern.matcher(arg);
            //if arguments to main do not match pattern --name=value
            if (!matcher.matches()) {
                System.err.println("Error: invalid argument format: " + arg);
                return;
            }
            String name = matcher.group("name");
            String value = matcher.group("value");
            //name of arg is not one of the names listed above
            if (!Arrays.asList("covid", "properties", "population", "log").contains(name)) {
                System.err.println("Error: invalid argument name: " + name);
                return;
            }
            //name of arg is used more than once
            if (arguments.containsKey(name)) {
                System.err.println("Error: argument " + name + " is used more than once.");
                return;
            }
            arguments.put(name, value);
        }

        String covid = arguments.get("covid");
        String properties = arguments.get("properties");
        String population = arguments.get("population");
        String log = arguments.get("log");

        //if covid file format is not .csv or json
        if(!covid.toLowerCase().endsWith(".csv") && !covid.toLowerCase().endsWith(".json")){
            System.err.println("Error: covid file has invalid extension: " + covid + ". Must be .csv or .json");
            return;
        }

        // initialize logger
        Logger logger = Logger.getInstance();
        try {
			logger.setDestination(log);
		} catch (IOException e1) {
			return;
		}
        
     // log command line arguments
        logger.log("Command Line Arguments: " + covid + " " + properties + " " + population + " " + log);

        // initialize processors and menu
        Menu menu = new Menu();
        if(covid != null){
            ICovidProcessor covidProcessor;
            try {
                covidProcessor = new CovidProcessor(covid);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            menu.covidProcessor = covidProcessor;
        }

        if(population != null){
            IPopulationProcessor populationProcessor = null;
            try {
                populationProcessor = new PopulationProcessor(population);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            menu.populationProcessor = populationProcessor;
        }

        if(properties != null){
            PropertyReader propertyReader;
            try {
                propertyReader = new PropertyReader(properties);
            } catch (IOException e) {
                System.err.println("Error: could not open property file: " + properties);
                return;
            }
            PropertyProcessor propertyProcessor = new PropertyProcessor(propertyReader);
            LivableAreaAverage livableAreaAverage = new LivableAreaAverage();
            MarketValueAverage  marketValueAverage = new MarketValueAverage();
            menu.propertyProcessor = propertyProcessor;
            menu.livableAreaAverage = livableAreaAverage;
            menu.marketValueAverage = marketValueAverage;
        }


        // show menu
        try {
            menu.ShowMenu();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
