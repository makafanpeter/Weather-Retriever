package com.Cynical.android.webServices;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The WeatherRetriever class will retrieve current weather conditions from the
 * WeatherBug API. It will store the information in this object.
 * 
 * @author Adam Benjamin
 * 
 */
public class WeatherRetriever {
	private final String APICODE = "CODE";
	
	private double temp;
	private String conditions;
	private int humidity;
	private int windSpeed;
	private String windDir;
	private double barPressure;
	private int dewPoint;
	private double totalRain;
	
	private final String baseUrl = "http://api.wxbug.net/getLiveWeatherRSS.aspx?ACode=" + APICODE;
	private String finalUrl;
	
	private double latitude;
	private double longitude;
	
	/**
	 * Creates a new WeatherRetriever object that will hold weather information.
	 * @param latitude = The current latitude of the location to retrieve weather from.
	 * @param longitude = The current longitude of the location to retrieve weather from.
	 */
	public WeatherRetriever(double latitude, double longitude) {
		this.temp = 0.0;
		this.conditions = "NULL";
		this.humidity = -1;
		this.windSpeed = -1;
		this.windDir = "NULL";
		this.barPressure = -1.0;
		this.dewPoint = 0;
		this.totalRain = -1.0;
		
		this.latitude = latitude;
		this.longitude = longitude;
		
		this.finalUrl = baseUrl +
			"&lat=" + this.latitude +
			"&long=" + this.longitude +
			"&unittype=0";
	}
	
	public void retrieveWeather()
	{
		try {
			URL url = new URL(finalUrl);
			
			/*Get a SAXParser from the SAXParserFactory*/
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			
			/*Get the XMLReader of the SAXParser we created*/
			XMLReader xr = sp.getXMLReader();
			
			//Create a new ContentHandler and apply it to the XMLReader
			WeatherHandler wh = new WeatherHandler(this);
			xr.setContentHandler(wh);
			
			//Parse the xml file at the given URL
			xr.parse(new InputSource(url.openStream()));
			//Parsing has finished
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getWindDir() {
		return windDir;
	}

	public void setWindDir(String windDir) {
		this.windDir = windDir;
	}

	public double getBarPressure() {
		return barPressure;
	}

	public void setBarPressure(double barPressure) {
		this.barPressure = barPressure;
	}

	public int getDewPoint() {
		return dewPoint;
	}

	public void setDewPoint(int dewPoint) {
		this.dewPoint = dewPoint;
	}

	public double getTotalRain() {
		return totalRain;
	}

	public void setTotalRain(double totalRain) {
		this.totalRain = totalRain;
	}
	
	private class WeatherHandler extends DefaultHandler
	{
		private boolean inTemp;
		private boolean inConditions;
		private boolean inHumidity;
		private boolean inWindSpeed;
		private boolean inWindDir;
		private boolean inBarPressure;
		private boolean inDewPoint;
		private boolean inTotalRain;
		
		private WeatherRetriever wr;
		
		private WeatherHandler(WeatherRetriever retriever)
		{
			super();
			wr = retriever;
			inTemp = false;
			inConditions = false;
			inHumidity = false;
			inWindSpeed = false;
			inWindDir = false;
			inBarPressure = false;
			inDewPoint = false;
			inTotalRain = false;
		}
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if(qName.equals("aws:temp"))
			{
				inTemp = true;
			}
			else if(qName.equals("aws:current-condition"))
			{
				inConditions = true;
			}
			else if(qName.equals("aws:humidity"))
			{
				inHumidity = true;
			}
			else if(qName.equals("aws:wind-speed"))
			{
				inWindSpeed = true;
			}
			else if(qName.equals("aws:wind-direction"))
			{
				inWindDir = true;
			}
			else if(qName.equals("aws:pressure"))
			{
				inBarPressure = true;
			}
			else if(qName.equals("aws:dew-point"))
			{
				inDewPoint = true;
			}
			else if(qName.equals("aws:rain-today"))
			{
				inTotalRain = true;
			}
			
			
		}
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			if(inTemp)
			{
				wr.temp = Double.parseDouble(new String(ch, start, length));
				inTemp = false;
			}
			else if(inConditions)
			{
				wr.conditions = new String(ch, start, length);
				inConditions = false;
			}
			else if(inHumidity)
			{
				wr.humidity = Integer.parseInt(new String(ch, start, length));
				inHumidity = false;
			}
			else if(inWindSpeed)
			{
				wr.windSpeed = Integer.parseInt(new String(ch, start, length));
				inWindSpeed = false;
			}
			else if(inWindDir)
			{
				wr.windDir = new String(ch, start, length);
				inWindDir = false;
			}
			else if(inBarPressure)
			{
				wr.barPressure = Double.parseDouble(new String(ch, start, length));
				inBarPressure = false;
			}
			else if(inDewPoint)
			{
				wr.dewPoint = Integer.parseInt(new String(ch, start, length));
				inDewPoint = false;
			}
			else if(inTotalRain)
			{
				wr.totalRain = Double.parseDouble(new String(ch, start, length));
				inTotalRain = false;
			}
		}
		
	}

}
