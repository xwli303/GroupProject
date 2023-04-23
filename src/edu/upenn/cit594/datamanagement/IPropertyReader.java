package edu.upenn.cit594.datamanagement;

import java.util.List;
import java.util.Map;

import edu.upenn.cit594.util.PropertyData;

public interface IPropertyReader {

	public Map<String, List<PropertyData>> readPropertyFile();
}
