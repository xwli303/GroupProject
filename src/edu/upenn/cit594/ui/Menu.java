package edu.upenn.cit594.ui;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    public IPopulationProcessor populationProcessor;
    public ICovidProcessor covidProcessor;
    public PropertyProcessor propertyProcessor;
    public LivableAreaAverage livableAreaAverage;
    public MarketValueAverage marketValueAverage;
    public Logger logger = Logger.getInstance();
    public Menu(){

    }
    public void ShowMenu () {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Select an action:");
            System.out.println("0. Exit the program.");
            System.out.println("1. Show available actions.");
            System.out.println("2. Show the total population for all ZIP Codes.");
            System.out.println("3. Show the total vaccinations per capita for each ZIP Code for the specified date.");
            System.out.println("4. Show the average market value for properties in a specified ZIP Code.");
            System.out.println("5. Show the average total livable area for properties in a specified ZIP Code.");
            System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code.");
            System.out.println("7. Show the the positive cases per total livable area for a specified ZIP code");

            //validate input
            int choice = -1;
            boolean validInput = false;
            while (!validInput) {
                System.out.println("Enter your choice (0-7): ");
                String input = scanner.nextLine();
                logger.log("Main Menu User Input: " + input);
                try {
                    choice = Integer.parseInt(input);
                    if (choice >= 0 && choice <= 7) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid input: Input must be between 0 and 7.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid input. Please enter a valid integer.");
                }
            }

            switch (choice) {
                case 0:
                    exit = true;
                    break;
                case 1:
                    System.out.println("BEGIN OUTPUT");
                    System.out.println("Available actions:");
                    System.out.println("1. Show the available actions.");
                    System.out.println("2. Show the total population for all ZIP Codes.");
                    System.out.println("3. Show the total vaccinations per capita for each ZIP Code for the specified date.");
                    System.out.println("4. Show the average market value for properties in a specified ZIP Code.");
                    System.out.println("5. Show the average total livable area for properties in a specified ZIP Code.");
                    System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code.");
                    System.out.println("7. Show the results of your custom feature.");
                    System.out.println("END OUTPUT");

                    break;
                case 2:
                    // Perform action 2
                    System.out.println("BEGIN OUTPUT");
                    int totalPopulation = this.populationProcessor.getTotalPopulation();
                    System.out.println(totalPopulation);
                    System.out.println("END OUTPUT");
                    break;
                case 3:
                    // Perform action 3
                    boolean prompt = true;
                    while (prompt) {
                        System.out.println("Enter a vaccine type (full/partial): ");
                        String userInputType = scanner.nextLine();
                        logger.log("Vaccine Per Capita User Input Type: " + userInputType);

                        //validate type
                        boolean validType = userInputType.equalsIgnoreCase("full") || userInputType.equalsIgnoreCase("partial");

                        //reprompt if type input is empty or invalid
                        if (!userInputType.isEmpty() && validType) {
                            boolean datePrompt = true;
                            while (datePrompt) {
                                System.out.println("Enter a date (YYYY-MM-DD): ");
                                String userInputDate = scanner.nextLine();
                                logger.log("Vaccine Per Capita User Input Date: " + userInputDate);

                                //validate date input
                                boolean isValidDate = isValidDateFormat(userInputDate);
                                //if date is empty or invalid, reprompt
                                if (!userInputDate.isEmpty() && isValidDate) {
                                    Map<Integer, Double> zipVax =
                                            this.covidProcessor.getZipVaxDataPerCapita(userInputDate, userInputType, this.populationProcessor.getZipPopulation());
                                    if(zipVax.size() == 0){
                                        System.out.println("BEGIN OUTPUT");
                                        //if no records for date (out of range or no data), return 0
                                        System.out.println(0);
                                    }
                                    else{
                                        System.out.println("BEGIN OUTPUT");

                                        for (Map.Entry<Integer, Double> entry : zipVax.entrySet()) {
                                            Integer key = entry.getKey();
                                            Double value = entry.getValue();
                                            System.out.println(key + " " + String.format("%.4f", value));
                                        }
                                    }
                                    prompt = false;
                                    datePrompt = false;
                                } else {
                                    System.out.println("Invalid input. Please enter a date (YYYY-MM-DD): ");
                                }
                            }
                        }

                    }
                    System.out.println("END OUTPUT");
                    break;

                case 4:
                    // Perform action 4 - average market value for specifiec zip code
                    System.out.println("BEGIN OUTPUT");
                    System.out.println("Enter a 5-digit zip code:");
                    String zipCode = scanner.nextLine();
                    logger.log("Average Market Value User Input Zip Code: " + zipCode);

                    int averageMarketValue = this.propertyProcessor.calculateAverage(zipCode, this.marketValueAverage);
                    System.out.println(averageMarketValue);
                    System.out.println("END OUTPUT");
                    break;
                case 5:
                    // Perform action 5 - average total livable area
                    System.out.println("BEGIN OUTPUT");
                    System.out.println("Enter a 5-digit zip code:");
                    String input = scanner.nextLine();
                    logger.log("Average Total Livable Area User Input Zip Code: " + input);

                    int avgLivableArea = this.propertyProcessor.calculateAverage(input, this.livableAreaAverage);
                    System.out.println(avgLivableArea);
                    System.out.println("END OUTPUT");
                    break;
                case 6:
                    // Perform action 6 - total market value per capita
                    System.out.println("BEGIN OUTPUT");
                    System.out.println("Enter a 5-digit zip code:");
                    String zip = scanner.nextLine();
                    logger.log("Total Market Value Per Capita User Input Zip Code: " + zip);

                    int mvPerCapita = this.propertyProcessor.calculateMarketValuePerCapita(zip, this.marketValueAverage);
                    System.out.println(mvPerCapita);
                    System.out.println("END OUTPUT");
                    break;
                case 7:
                    // Perform action 7
                    System.out.println("BEGIN OUTPUT");

                    System.out.println("END OUTPUT");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid action.");
            }
        }

        scanner.close();
    }

    private boolean isValidDateFormat(String inputDate) {
        boolean isValid = true;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter.parse(inputDate);
        } catch (DateTimeParseException e) {
            isValid = false;
        }
        return isValid;
    }

}

