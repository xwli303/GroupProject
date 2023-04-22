package edu.upenn.cit594.ui;

import edu.upenn.cit594.processor.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    public IPopulationProcessor populationProcessor;
    public ICovidProcessor covidProcessor;
    public PropertyProcessor propertyProcessor;
    public LivableAreaAverage livableAreaAverage;
    public MarketValueAverage marketValueAverage;
    public Menu(IPopulationProcessor populationProcessor, ICovidProcessor covidProcessor, PropertyProcessor propertyProcessor,
                LivableAreaAverage livable, MarketValueAverage marketValue){
        this.populationProcessor = populationProcessor;
        this.covidProcessor = covidProcessor;
        this.propertyProcessor = propertyProcessor;
        this.livableAreaAverage = livable;
        this.marketValueAverage = marketValue;
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
            System.out.println("7. Show the results of your custom feature.");

            //validate input
            int choice = -1;
            boolean validInput = false;
            while (!validInput) {
                System.out.print("Enter your choice (0-7): ");
                String input = scanner.nextLine();
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
                    System.out.println("Available actions:");
                    System.out.println("1. Show the available actions.");
                    System.out.println("2. Show the total population for all ZIP Codes.");
                    System.out.println("3. Show the total vaccinations per capita for each ZIP Code for the specified date.");
                    System.out.println("4. Show the average market value for properties in a specified ZIP Code.");
                    System.out.println("5. Show the average total livable area for properties in a specified ZIP Code.");
                    System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code.");
                    System.out.println("7. Show the results of your custom feature.");
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
                    System.out.println("BEGIN OUTPUT");
                    boolean prompt = true;
                    while (prompt) {
                        System.out.print("Enter a vaccine type (full/partial): ");
                        String userInputType = scanner.nextLine();

                        if (!userInputType.isEmpty()) {
                            boolean datePrompt = true;
                            while (datePrompt) {
                                System.out.print("Enter a date (YYYY-MM-DD): ");
                                String userInputDate = scanner.nextLine();
                                if (!userInputDate.isEmpty()) {
                                    Map<Integer, Double> zipVax =
                                            this.covidProcessor.getZipVaxDataPerCapita(userInputDate, userInputType, this.populationProcessor.getZipPopulation());
                                    for (Map.Entry<Integer, Double> entry : zipVax.entrySet()) {
                                        Integer key = entry.getKey();
                                        Double value = entry.getValue();
                                        System.out.println(key + " " + value);
                                    }
                                    prompt = false;
                                    datePrompt = false;
                                } else {
                                    System.out.print("Invalid input. Please enter a date (YYYY-MM-DD): ");
                                }
                            }
                        }

                    }
                    System.out.println("END OUTPUT");
                    break;

                case 4:
                    // Perform action 4
                    System.out.println("BEGIN OUTPUT");
                    System.out.println("Enter a 5-digit zip code:");
                    String zipCode = scanner.nextLine();
                    int averageMarketValue = this.propertyProcessor.calculateAverage(zipCode, this.marketValueAverage);
                    System.out.println(averageMarketValue);
                    System.out.println("END OUTPUT");
                    break;
                case 5:
                    // Perform action 5
                    System.out.println("BEGIN OUTPUT");
                    System.out.println("Enter a 5-digit zip code:");
                    String input = scanner.nextLine();
                    int avgLivableArea = this.propertyProcessor.calculateAverage(input, this.livableAreaAverage);
                    System.out.println(avgLivableArea);
                    System.out.println("END OUTPUT");
                    break;
                case 6:
                    // Perform action 6
                    System.out.println("BEGIN OUTPUT");
                    System.out.println("Enter a 5-digit zip code:");
                    String zip = scanner.nextLine();
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

}

