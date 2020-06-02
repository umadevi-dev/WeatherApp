import model.Forecast;
import model.Period;
import model.PointsResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Collections;
import java.util.List;


public class WeatherApp {
    private static Logger logger = LoggerFactory.getLogger(WeatherApp.class);
    public static void main(String[] args){

        if(args.length==2){
            try {
                PointsResponse pointsApiResponse = getPoints(args[0],args[1]);
                if(pointsApiResponse.getForecast()!=null){
                    List<Period> periodsList = getForeCastDetails(pointsApiResponse.getForecast());
                    logger.debug(" List size"+ periodsList.size());
                    periodsList.subList(0,10).forEach(day->{
                        System.out.println("name "+day.getName() +" - Number "+day.getNumber()+
                                " DetailedForecast "+day.getDetailedForecast() +" - StartTime "+day.getStartTime()+
                                " EndTime "+day.getEndTime() +" - Icon "+day.getIcon()+
                                " Temperature "+day.getTemperature() +" - Temperature Unit "+day.getTemperatureUnit()+
                                " ShortForeCast "+day.getShortForecast() +" - Detailed Forecast "+day.getDetailedForecast()+
                                " Temperature Trend "+day.getTemperatureTrend() +" - Detailed Forecast "+day.getWindDirection()+
                                " Wind Speed"+day.getWindSpeed()+"\n");
                    });
                }
            } catch (IOException e) {
                logger.error("Error occured"+ e.getCause());
            }
        }
        else{
            logger.error("invalid input");
        }
    }

    private static PointsResponse getPoints(String latitude, String longitude) throws ProtocolException,MalformedURLException, IOException{

        try {
            String urlString = "https://api.weather.gov/points/"+latitude+","+longitude;
            logger.debug(" Constructed url string is "+urlString);
            String response = HttpUtil.getApiResponse(urlString);
            ObjectMapper objectMapper =new ObjectMapper();
            PointsResponse pointsResponse = objectMapper.readValue(response, PointsResponse.class);
            return pointsResponse;
        } catch (Exception e) {
            logger.error("failed to connect"+ e.getMessage());
        }

        return null;
    }

    private static List getForeCastDetails(String forecastUrl) throws ProtocolException,MalformedURLException, IOException{

        try {

            System.out.println(" Constructed url string is "+forecastUrl);
            String response = HttpUtil.getApiResponse(forecastUrl);
            ObjectMapper objectMapper =new ObjectMapper();
            Forecast forecastApiResponse  = objectMapper.readValue(response, Forecast.class);
            return forecastApiResponse.getPeriodList();

        } catch (Exception e) {
            System.out.println("failed to connect"+ e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }


}
