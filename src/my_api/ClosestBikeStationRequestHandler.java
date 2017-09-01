package my_api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import call_external_api.VcubeListBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import javax.ws.rs.core.Context;

@Path("/findClosestBikeStation")
public class ClosestBikeStationRequestHandler {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String decodeClosestBikeStationRequest(@QueryParam("name") String name, @Context UriInfo uriInfo, String content) throws Exception
	{	
		//This is the first method called when receiving a GET request of type findClosestBikeStation
		String oClosestBikeStationsResponse =null;

		//First, let's retrieve the parameters in input of the request, in the URL:
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters(); 
		if (queryParams.getFirst("latitude")!=null && queryParams.getFirst("longitude")!=null)
		{
			try 
			{
				//Conversion of the latitude and longitude from String to Double
				Double iLatitude = Double.valueOf(queryParams.getFirst("latitude"));
				Double iLongitude = Double.valueOf(queryParams.getFirst("longitude"));

				if (iLatitude>=-90 && iLatitude<=90 && iLongitude>=-180 && iLongitude <=180)
				{
					//If inputs are correct: Consume Bordeaux WFS service to fetch the real-time list of bike stations.
					VcubeListBuilder vcubeListBuilder=new VcubeListBuilder();
					BikeStation[] myBikeStations = vcubeListBuilder.parseXMLResponse(vcubeListBuilder.getXMLResponse("http://data.lacub.fr/wfs?key=9Y2RU3FTE8&SERVICE=WFS&VERSION=1.1.0&REQUEST=GetFeature&TYPENAME=CI_VCUB_P&SRSNAME=EPSG:4326"), iLatitude, iLongitude);

					//Encode the JSON for the reply
					ClosestBikeStationJSONEncoder responseEncoder = new ClosestBikeStationJSONEncoder();
					oClosestBikeStationsResponse = responseEncoder.encodeJSONResponse(myBikeStations);
				}
				else
				{
					//If latitude and longitude are not inside a correct range, throw relevant exception
					String message = "The latitude or longitude in input are not in relevant ranges."; 
					throw new IncorrectLocationCodeException(400,4001,message);
				}
			} catch (NumberFormatException e) {
				// This exception is raised if the latitude or longitude provided is not a number.
				e.printStackTrace();
				String message = "The latitude or longitude in input are not correct numbers."; 
				throw new IncorrectLocationCodeException(400,4003,message);
			}	
		}
		else 
		{
			//At least one of the required input is missing 
			String message = "The latitude or longitude in input is missing."; 
			throw new IncorrectLocationCodeException(400,4002,message);
		}
		return oClosestBikeStationsResponse;
	}
}
