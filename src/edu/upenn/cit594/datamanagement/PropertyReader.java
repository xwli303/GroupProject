package edu.upenn.cit594.datamanagement;

import java.util.List;
import java.util.Map;

import util.PropertyData;

public interface PropertyReader {

	public Map<String, List<PropertyData>> readPropertyFile();
}
