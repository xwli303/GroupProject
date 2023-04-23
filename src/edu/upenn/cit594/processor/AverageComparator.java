package edu.upenn.cit594.processor;
import edu.upenn.cit594.util.PropertyData;

public interface AverageComparator {

    public boolean containsNumericValue(PropertyData pd);

    public double getNumericValue(PropertyData pd);

}
