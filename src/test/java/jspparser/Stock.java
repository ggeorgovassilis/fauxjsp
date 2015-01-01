package jspparser;

public class Stock {

	protected String shortName;
	protected String longName;
	protected int changePercent;
	protected int priceInCent;
	
	public Stock(String shortName, String longName, int changePercent, int priceInCent) {
		this.shortName = shortName;
		this.longName = longName;
		this.changePercent = changePercent;
		this.priceInCent = priceInCent;
	}
	public String getShortName() {
		return shortName;
	}
	public String getLongName() {
		return longName;
	}
	public int getChangePercent() {
		return changePercent;
	}
	public int getPriceInCent() {
		return priceInCent;
	}
}
