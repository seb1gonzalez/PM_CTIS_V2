package mysql;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class Feature {
	ArrayList<KeyPair> attributes;
	Polygon polygon;
	Feature(JsonReader reader) throws IOException{
		reader.beginObject();
		while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("attributes")){
				this.attributes = readAttributes(reader);
			}
			else if(name.equals("geometry")){
				reader.beginObject();
				if(reader.nextName().equals("rings")){
					reader.beginArray();
					this.polygon = readRings(reader);
					while(reader.hasNext()){
						reader.skipValue();
					}
					reader.endArray();
				}
				reader.endObject();
			}
		}
		reader.endObject();
		
	}

	private Polygon readRings(JsonReader reader) throws IOException {
		//create the list
		Polygon polygon;
		ArrayList<Coordinate> points = new ArrayList<Coordinate>();
		reader.beginArray();
		while(reader.hasNext()){
			reader.beginArray();
			String lng = reader.nextString();
			String lat = reader.nextString();
			points.add(new Coordinate(lat, lng));
			reader.endArray();
		}
		reader.endArray();
		polygon = new Polygon(points);
		return polygon;
	}

	public ArrayList<KeyPair> readAttributes(JsonReader reader) throws IOException {
		//init array of values
		ArrayList<KeyPair> attributes = new ArrayList<KeyPair>();
		// read the attributes
		reader.beginObject();
		while(reader.hasNext()){
			String key = reader.nextName();
			JsonToken p = reader.peek();
			if(p == p.STRING){
				String value = reader.nextString();
				attributes.add(new KeyPair<String, String>(key, value, KeyPair.ValClass.STRING));				
			}
			else if(p == p.NUMBER){
				double value = reader.nextDouble();
				attributes.add(new KeyPair<String, Double>(key, value, KeyPair.ValClass.DOUBLE));
			}
			else{
				reader.skipValue();
			}
		}
		reader.endObject();
		return attributes;
	}

}
