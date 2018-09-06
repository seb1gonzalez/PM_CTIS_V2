package mysql;

public class Coordinate {
	double latitude;
	double longitude;
	Coordinate(String lat, String lng){
		this.latitude = Double.parseDouble(lat);
		this.longitude = Double.parseDouble(lng);
	}
	Coordinate(double lat, double lng){
		this.latitude = lat;
		this.longitude = lng;
	}

}
