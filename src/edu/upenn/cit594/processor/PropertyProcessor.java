package edu.upenn.cit594.processor;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.upenn.cit594.datamanagement.IPropertyReader;
import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertyData;

public class PropertyProcessor implements IPropertyProcessor {

	private IPropertyReader propReader;
	private Map<String, List<PropertyData>> propMap;

	public PropertyProcessor(IPropertyReader pReader) {
		propReader = pReader;
		propMap = propReader.readPropertyFile();
	}

	public int calculateAverage(String zip, IAverageComparator avc) {
		double sum = 0.0;
		int numOfProp = 0;

		List<PropertyData> propZipList = propMap.get(zip);
		if ((propZipList == null) || (propZipList.size() == 0)) { return 0; } 

		numOfProp = propZipList.size();

		for (PropertyData pd : propZipList) {
			if (avc.containsNumericValue(pd)) {
				sum += avc.getNumericValue(pd);
			}

		}

		return (int) (sum/numOfProp); 
	}

	public int calculateMarketValuePerCapita(String zip, IAverageComparator avc) {
		double sum = 0.0;
		Set<PopulationData> popSet = PopulationProcessor.populationData;

		int population = 0;// replace this with population tally 
		try {
			int z = Integer.parseInt(zip);

			for (PopulationData zPop : popSet) {
				if (zPop.getZipCode() == z) { 
					population = zPop.getPopulation();

					break; 
				}
			}

			if ((population == 0) || (!propMap.containsKey(zip))) { return 0; }

			List<PropertyData> propZipList = propMap.get(zip);
			if ((propZipList == null) || (propZipList.size() == 0)) { return 0; } 

			for (PropertyData pd : propZipList) {
				if (avc.containsNumericValue(pd)) {
					sum += avc.getNumericValue(pd);
				}

			}

		} catch (NumberFormatException e) {
			return 0;
		}


		return (int) sum/population;
	}

	public double calculateInfectedArea(String date, String zip, IAverageComparator avc) {
		double sum = 0.0;
		Set<PopulationData> popSet = PopulationProcessor.populationData;
		int population = 0;// replace this with population tally 
		int positiveCases = 0;
		double infectedArea = 0.0;
		double posPop = 0.0;
		try {
			int z = Integer.parseInt(zip);
			for (PopulationData zPop : popSet) {
				if (zPop.getZipCode() == z) { 
					population = zPop.getPopulation();
					break; 
				}
			}
			
			positiveCases = CovidProcessor.getPosCasesByZipDate(date, zip);
			
			if ((population == 0) || (positiveCases == 0) ||(!propMap.containsKey(zip))) { return 0.0; }
			posPop = (double)positiveCases/population;
			
			List<PropertyData> propZipList = propMap.get(zip);
			if ((propZipList == null) || (propZipList.size() == 0)) { return 0.0; } 



			for (PropertyData pd : propZipList) {
				if (avc.containsNumericValue(pd)) {
					sum += avc.getNumericValue(pd);
				}
			}
			
			infectedArea = (posPop * sum);
			DecimalFormat df = new DecimalFormat("#.00");
			infectedArea = Double.parseDouble(df.format(infectedArea));
			
		} catch (NumberFormatException e) {
			return 0.0;
		}


		return infectedArea;
	}



}
