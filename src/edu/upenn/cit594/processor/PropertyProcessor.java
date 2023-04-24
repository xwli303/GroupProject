package edu.upenn.cit594.processor;
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

		for (PropertyData pd : propZipList) {
			if (avc.containsNumericValue(pd)) {
				sum += avc.getNumericValue(pd);
				numOfProp++;
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
			//System.out.println("population is " + population);
			positiveCases = CovidProcessor.getPosCasesByZipDate(date, zip);
			//System.out.println("positive cases are " + positiveCases);
			
			if ((population == 0) || (positiveCases == 0) ||(!propMap.containsKey(zip))) { return 0.0; }
			posPop = (double)positiveCases/population;
			//System.out.println("pc/pop " + (posPop));
			
			List<PropertyData> propZipList = propMap.get(zip);
			if ((propZipList == null) || (propZipList.size() == 0)) { return 0.0; } 



			for (PropertyData pd : propZipList) {
				if (avc.containsNumericValue(pd)) {
					sum += avc.getNumericValue(pd);
					//System.out.println("in loop sum is " + sum);
				}
			}
			//System.out.println("sum is " + sum);
			
			infectedArea = (posPop * sum);
			//System.out.println("infected area is" + infectedArea);
		} catch (NumberFormatException e) {
			return 0.0;
		}


		return infectedArea;
	}



}
