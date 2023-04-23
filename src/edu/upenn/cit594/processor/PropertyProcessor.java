package edu.upenn.cit594.processor;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.upenn.cit594.datamanagement.PropertyReader;
import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertyData;

public class PropertyProcessor {

	private PropertyReader propReader;
	private Map<String, List<PropertyData>> propMap;

	public PropertyProcessor(PropertyReader pReader) {
		propReader = pReader;
		propMap = propReader.readPropertyFile();
	}

	public int calculateAverage(String zip, AverageComparator avc) {
		double sum = 0.0;
		int numOfProp = 0;

		List<PropertyData> propZipList = propMap.get(zip);
		if ((propZipList == null) || (propZipList.size() == 0)) { return 0; } 

		for (PropertyData pd : propZipList) {
			if (avc.containsNumericValue(pd)) {
				System.out.println("val is " + avc.getNumericValue(pd));
				sum += avc.getNumericValue(pd);
				System.out.println("sum is " + sum);
				numOfProp++;
				System.out.println("num of prop is " + numOfProp);
			}

		}

		return (int) (sum/numOfProp); 
	}

	public int calculateMarketValuePerCapita(String zip, AverageComparator avc) {
		double sum = 0.0;
		Set<PopulationData> popSet = PopulationProcessor.getAllPopulationData();
		//System.out.println("size of popset is " + popSet.size());
		int population = 0;// replace this with population tally 
		//System.out.println("population is " + population);
		try {
			int z = Integer.parseInt(zip);
			//System.out.println("zip code is " + z);
			
			for (PopulationData zPop : popSet) {
				//System.out.println("Zip code population is " + zPop.getPopulation());
				if (zPop.getZipCode() == z) { 
					population = zPop.getPopulation();
					System.out.println("population is now " + population);
					break; 
				}
			}
			System.out.println("population is now " + population);
			
			if ((population == 0) || (!propMap.containsKey(zip))) { return 0; }

			List<PropertyData> propZipList = propMap.get(zip);
			if ((propZipList == null) || (propZipList.size() == 0)) { return 0; } 

			for (PropertyData pd : propZipList) {
				if (avc.containsNumericValue(pd)) {
					sum += avc.getNumericValue(pd);
				}

			}
			//System.out.println("sum is now " + sum);
		} catch (NumberFormatException e) {
			return 0;
		}


		return (int) sum/population;
	}
	
	public double calculateInfectedArea(String date, String zip, AverageComparator avc) {
		double sum = 0.0;
		Set<PopulationData> popSet = PopulationProcessor.getAllPopulationData();
		int population = 0;// replace this with population tally 
		int positiveCases = 0;
		double infectedArea = 0.0;
		try {
			int z = Integer.parseInt(zip);
			for (PopulationData zPop : popSet) {
				if (zPop.getZipCode() == z) { 
					population = zPop.getPopulation();
					break; 
				}
			}
			
			// get positive cases

			if ((population == 0) || (positiveCases == 0) ||(!propMap.containsKey(zip))) { return 0.0; }

			List<PropertyData> propZipList = propMap.get(zip);
			if ((propZipList == null) || (propZipList.size() == 0)) { return 0.0; } 
			
			

			for (PropertyData pd : propZipList) {
				if (avc.containsNumericValue(pd)) {
					sum += avc.getNumericValue(pd);
				}
			}
			
			infectedArea = ((positiveCases/population) * sum)/sum;
		} catch (NumberFormatException e) {
			return 0.0;
		}

		
		return infectedArea;
	}



}
