package edu.upenn.cit594.processor;
import java.util.List;
import java.util.Map;
import edu.upenn.cit594.datamanagement.PropertyReader;
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
