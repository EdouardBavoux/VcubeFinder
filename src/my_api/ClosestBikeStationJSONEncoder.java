package my_api;

import com.google.gson.Gson;

public class ClosestBikeStationJSONEncoder {

	public String encodeJSONResponse(BikeStation[] myBikeStations) {
		// Using Gson library, the array of BikeStation is transformed in JSON.
		Gson gson = new Gson();
		String jsonInString = gson.toJson(myBikeStations);
		return jsonInString;
	}

}
