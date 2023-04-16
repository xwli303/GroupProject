package edu.upenn.cit594.processor;

import java.util.List;
import java.util.Map;

import datamangement.PropertyReader;
import util.PropertyData;

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
		if (propZipList.size() == 0) { return 0; } 
		
		for (PropertyData pd : propZipList) {
			if (avc.containsNumericValue(pd)) {
				sum += avc.getNumericValue(pd);
				numOfProp++;
			}

		}
		
		return (int) (sum/numOfProp); 
	}
	
	public int calculateMarketValuePerCapita(String zip, AverageComparator avc) {
		double sum = 0.0;
		int population = 10; // replace this with population tally 
		
		// check if population is 0, if so return 0
		
		if (!propMap.containsKey(zip)) { return 0; }
		
		List<PropertyData> propZipList = propMap.get(zip);
		if (propZipList.size() == 0) { return 0; } 
		
		for (PropertyData pd : propZipList) {
			if (avc.containsNumericValue(pd)) {
				sum += avc.getNumericValue(pd);
			}

		}
		
		return (int) sum/population;
	}
}
