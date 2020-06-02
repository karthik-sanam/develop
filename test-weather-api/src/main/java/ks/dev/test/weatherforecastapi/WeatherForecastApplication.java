package ks.dev.test.weatherforecastapi;

import java.util.Scanner;

public class WeatherForecastApplication 
{
	
    public static void main( String[] args )
    {
    	
    	System.out.println("Starting Weather Forecast Application");    	

    	String coordinates = getInputData();
    	
    	WeatherForecastService wfService = new WeatherForecastService();
    	wfService.getWeatherForecast(coordinates);
    }
    
    public static String getInputData(){
    	
    	boolean inputValid = false;
    	String result ="";
    	while(!inputValid){
	    	Scanner inputCoordinates = new Scanner(System.in);//38.8894,-77.0352
	    	System.out.println("Please Enter the Coordinates(latitude, longitude): ");
	    	String input = inputCoordinates.nextLine();
	    	String[] coordinates = input.split(",");
	    	if(coordinates.length<2 || coordinates.length>2){
	    		System.out.println("Entered input is not in correct format. Please Enter the Coordinates(latitude, longitude): ");
	    		inputValid=false;
	    	}else{
	    		result = input;
	    		inputValid= true;
	    	}
    	}
	    	
		return result;
    }
    
}
