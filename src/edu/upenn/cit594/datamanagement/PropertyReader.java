package edu.upenn.cit594.datamanagement;
import java.util.List;
import java.util.Map;
import edu.upenn.cit594.util.PropertyData;

public interface PropertyReader {

	Map<String, List<PropertyData>> readPropertyFile();
}
