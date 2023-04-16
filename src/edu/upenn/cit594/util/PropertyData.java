package edu.upenn.cit594.util;

public class PropertyData {
    
	private String marketValue;
	private String totalLivableArea;
	private String zipCode;

	/**
	 * zero arg constructor
	 */
	public PropertyData() {
		marketValue = "";
		totalLivableArea = "";
		zipCode = "";
	}
	
	public String getMarketValue() {
		return marketValue;
	}
	
	public void setMarketValue(String mVal) {
		marketValue = mVal;
	}
	
	public String getTotalLivableArea() {
		return totalLivableArea;
	}
	
	public void setTotalLivableArea(String area) {
		totalLivableArea = area;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zip) {
		if (zip.length() > 5) {
			zipCode = zip.substring(0, 6);
		} else {
			zipCode = zip;
		}
	}
}
