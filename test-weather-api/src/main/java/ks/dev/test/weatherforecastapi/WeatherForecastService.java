package ks.dev.test.weatherforecastapi;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import ks.dev.test.weatherforecastapi.data.WeatherForecastData;

public class WeatherForecastService {

	public String getWeatherForecast(String coordinates){
		
		System.out.println("Invoking Service......");
		
		String result = "";
		try{
			String weatherAPI = "https://api.weather.gov/points/"+coordinates;
			
			URL url = new URL(weatherAPI);
	        HttpURLConnection httpConn =  (HttpURLConnection) url.openConnection();
	
	        httpConn.setRequestProperty("Content-type", "application/json");
	
	        int responseCode = httpConn.getResponseCode();
	        
	        if(responseCode != HttpURLConnection.HTTP_OK){
	        	System.out.println("Error Retrieving the information: Error Code"+responseCode);
	        	result =  "Error Retrieving the information: Error Code"+responseCode;
	        	return result;
	        }
	        
			Scanner sc = new Scanner(url.openStream());
			while(sc.hasNext()){
				result +=sc.nextLine()+"\n";
			}
			sc.close();
			
			JSONObject responeJson  = new JSONObject(result);
			JSONObject propertiesObject = responeJson.getJSONObject("properties");
			String foreCastUrl = propertiesObject.get("forecast").toString();
			System.out.println("Forecast Url Obtained: "+foreCastUrl);
			
			getForecastDetails(foreCastUrl);
	       
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public String getForecastDetails(String forecastUrl){
			
			String result ="";
			try{
				URL url = new URL(forecastUrl);
		        HttpURLConnection httpConn =  (HttpURLConnection) url.openConnection();
	
		        httpConn.setRequestProperty("Content-type", "application/json");
	
		        int responseCode = httpConn.getResponseCode();
		        
		        if(responseCode != HttpURLConnection.HTTP_OK){
		        	System.out.println("Error Retrieving the Forecast information: Error Code"+responseCode);
		        	result =  "Error Retrieving the Forecast information: Error Code"+responseCode;
		        	return result;
		        }
		        
		        Scanner response = new Scanner(url.openStream());
		        while(response.hasNext()){
					result +=response.nextLine()+"\n";
				}
		        response.close();
		        
				JSONObject responeJson  = new JSONObject(result);
				JSONObject propertiesObject = responeJson.getJSONObject("properties");
				String forecastPeriodsData = propertiesObject.get("periods").toString();
				
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
	      		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	      		CollectionType typeRef = TypeFactory.defaultInstance().constructCollectionType(List.class, WeatherForecastData.class);
	      		List<WeatherForecastData> forecastList = mapper.readValue(forecastPeriodsData,typeRef);
				
	      		// Using Java 8 Lambda Expressions
	      		/*forecastList.forEach(fcData ->{
	      			System.out.println(fcData.getName());
	      			System.out.println("From: "+fcData.getStartTime());
	      			System.out.println("To: "+fcData.getStartTime());
	      			System.out.println("Temperature: "+fcData.getTemperature()+" degrees "+fcData.getTemperatureUnit());
	      			System.out.println("Wind Speed: "+fcData.getWindSpeed());
	      			System.out.println("Wind Direction: "+fcData.getWindDirection());
	      			System.out.println("Detailed Forecast: "+ fcData.getDetailedForecast()+"\n\n");
	      		});*/
	      		
	      		for(WeatherForecastData fcData : forecastList){
	      			System.out.println(fcData.getName());
	      			System.out.println("From: "+fcData.getStartTime());
	      			System.out.println("To: "+fcData.getStartTime());
	      			System.out.println("Temperature: "+fcData.getTemperature()+" degrees "+fcData.getTemperatureUnit());
	      			System.out.println("Wind Speed: "+fcData.getWindSpeed());
	      			System.out.println("Wind Direction: "+fcData.getWindDirection());
	      			System.out.println("Detailed Forecast: "+ fcData.getDetailedForecast()+"\n\n");
	      		}
	      		
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Error retrieving forecast information.");
			}
			return result;
	}
}
