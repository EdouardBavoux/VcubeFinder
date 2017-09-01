package my_api;

public class BikeStation implements Comparable<BikeStation>{

	private String name;
	private Boolean connected;
	private Integer nbPlaces;
	private Integer nbBikes;
	private Double[] position;
	private Double distanceToInputPoint;

    @Override
    public int compareTo(BikeStation bikeStation) {
    	//Defines a comparator, in order to sort the stations, from the closest to the furthest.
        return distanceToInputPoint.compareTo(bikeStation.distanceToInputPoint);
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getConnected() {
		return connected;
	}
	public void setConnected(Boolean connected) {
		this.connected = connected;
	}
	public Integer getNbPlaces() {
		return nbPlaces;
	}
	public void setNbPlaces(Integer nbPlaces) {
		this.nbPlaces = nbPlaces;
	}
	public Integer getNbBikes() {
		return nbBikes;
	}
	public void setNbBikes(Integer nbBikes) {
		this.nbBikes = nbBikes;
	}
	public Double[] getPosition() {
		return position;
	}
	public void setPosition(Double[] position) {
		if (position.length==2)
		{
			this.position = position;
		}
	}
	public Double getDistanceToInputPoint() {
		return distanceToInputPoint;
	}
	
	public void setDistanceToInputPoint(Double distanceToInputPoint) {
		this.distanceToInputPoint = distanceToInputPoint;
	}
	
	public String getStationDetails() {
		String oString = "Nom: "+this.name+" - Nb of places: "+ this.nbPlaces+" - Nb of Bikes: "+this.nbBikes+" - Connected: "+this.connected+" - Position : "+this.position[0]+" "+this.position[1];
		return oString;
	}
	public double computeDistanceToAPoint(double[] point)
	{
		//Computes the distance between the station's position, and the point in input, in Meters 
		final int R = 6371; // Radius of the earth
		double latDistance = Math.toRadians(this.position[0] - point[0]);
		double lonDistance = Math.toRadians(this.position[1] - point[1]);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(point[0])) * Math.cos(Math.toRadians(this.position[0]))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters
		return distance;
	}
}
