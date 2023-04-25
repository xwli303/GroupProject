package edu.upenn.cit594.processor;
public interface IPropertyProcessor {
    int calculateAverage(String zip, IAverageComparator avc);
    int calculateMarketValuePerCapita(String zip, IAverageComparator avc);
}
