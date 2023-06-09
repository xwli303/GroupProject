package edu.upenn.cit594.processor;
import edu.upenn.cit594.util.PropertyData;

public class LivableAreaAverage implements IAverageComparator {

    public LivableAreaAverage() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean containsNumericValue(PropertyData pd) {
        try {
            Double.parseDouble(pd.getTotalLivableArea());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public double getNumericValue(PropertyData pd) {
        return Double.parseDouble(pd.getTotalLivableArea());
    }

}
