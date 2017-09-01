package call_external_api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import my_api.BikeStation;

public class VcubeListBuilder {

	String oResponse=null;

	public String getXMLResponse(String urlToRead) throws MalformedURLException, ProtocolException, IOException
	{
		//Perform an HTTP GET request, to fetch the XML answer, returned as a String
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		oResponse=result.toString();
		return oResponse;
	}

	public BikeStation[] parseXMLResponse(String iXMLString, double iLatitude, double iLongitude) throws ParserConfigurationException, SAXException, IOException, DOMException
	{
		//Parse the String containing the XML response (WFS service).
		//As an output, an array of BikeStations (sorted by distance to the input position) is returned
		BikeStation[] oBikeStations=null;

		//Beginning of the parsing
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream xmlBuff = new StringBufferInputStream(iXMLString);
		Document xml = builder.parse(xmlBuff);
		// Start Browsing the nodes
		Element root = xml.getDocumentElement();
		NodeList stations = root.getElementsByTagName("bm:CI_VCUB_P");
		oBikeStations = new BikeStation[stations.getLength()];
		for (int stationIter=0 ; stationIter<stations.getLength(); stationIter++)
		{
			//For each bike station:
			oBikeStations[stationIter] = new BikeStation();
			NodeList description = stations.item(stationIter).getChildNodes();
			for (int descIter=0 ; descIter<description.getLength();descIter++)
			{
				//The process browse the description elements, and set the useful values (name, number of bikes available...) in the corresponding object
				switch (description.item(descIter).getNodeName() )
				{
				case "bm:NOM":
				{	
					//Fill the name of station
					oBikeStations[stationIter].setName(description.item(descIter).getTextContent());
				}
				break;
				case "bm:NBPLACES":
				{
					//Fill the number of available slots
					try {
						Integer nbPlaces = Integer.valueOf(description.item(descIter).getTextContent());
						oBikeStations[stationIter].setNbPlaces(nbPlaces);			
					}
					catch (NumberFormatException e) {e.printStackTrace();}
				}
				break;
				case "bm:NBVELOS":
				{
					//Fill the number of available bikes
					try {
						Integer nbBikes = Integer.valueOf(description.item(descIter).getTextContent());
						oBikeStations[stationIter].setNbBikes(nbBikes);	
					}
					catch (NumberFormatException e) {e.printStackTrace();}					        
				}
				break;
				case "bm:ETAT":
				{
					//Fill the table
					if (description.item(descIter).getTextContent().equals("CONNECTEE"))
					{
						oBikeStations[stationIter].setConnected(true);						        
					}
					else
					{
						oBikeStations[stationIter].setConnected(false);			
					}
				}
				break;
				case "bm:geometry":
				{
					//Fill the position and distance 
					//TODO: Handle the exceptions
					NodeList points = description.item(descIter).getChildNodes();
					for (int pointIter=0; pointIter<points.getLength();pointIter++)
					{
						if (points.item(pointIter).getNodeName().equals("gml:Point"))
						{
							//Entering the "Point" Node
							NodeList pos = points.item(pointIter).getChildNodes();
							for (int posIter=0; posIter<points.getLength();posIter++)
							{
								if (pos.item(posIter).getNodeName().equals("gml:pos"))
								{
									//Entering the "pos" Node
									Double[] position= {null,null};
									String[] positionInStrings = pos.item(posIter).getTextContent().split("\\s+");
									for (int coordIter=0 ; coordIter<position.length ; coordIter++)
									{
										if (positionInStrings[coordIter]!=null)
										{
											position[coordIter] =  Double.valueOf(positionInStrings[coordIter]);
										}	
									}
									//Here the localisation of the station is known. Let's set it.
									oBikeStations[stationIter].setPosition(position);
								}
							}
							//Distance is computed and set.
							oBikeStations[stationIter].setDistanceToInputPoint(oBikeStations[stationIter].computeDistanceToAPoint(new double[]{iLatitude,iLongitude}));
						}
					}				        
				}
				default:
				{
					//Nothing to be done for the other values so far.
				}
				}	
			}       
		}
		//Finally, this is used to sort the array of stations, based on the comarator defined in the class (by distance to the user input position)
		Arrays.sort(oBikeStations);
		return oBikeStations;
	}
}
