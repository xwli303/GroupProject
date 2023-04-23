package edu.upenn.cit594.processor;
import edu.upenn.cit594.util.PropertyData;

public class MarketValueAverage implements IAverageComparator {

    public MarketValueAverage() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean containsNumericValue(PropertyData pd) {
        try {
            Double.parseDouble(pd.getMarketValue());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public double getNumericValue(PropertyData pd) {
        return Double.parseDouble(pd.getMarketValue() );
    }

}
