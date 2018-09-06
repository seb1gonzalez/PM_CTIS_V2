package mysql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.stream.JsonReader;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class JsonUpload {
	public static void main(String[] args) throws SQLException, IOException{
		/*
		MysqlDataSource ds = new MysqlDataSource();
		ds.setUser("root");
		ds.setPassword("1969");
		ds.setServerName("loclahost");
		ds.setDatabaseName("imsc");
		*/
		Connection conn = null;
		 String url = "jdbc:mysql://localhost:3306/";
		 String dbName = "imsc";
		 String driver = "com.mysql.jdbc.Driver";
		 String userName = "root";
		 String password = "1969";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url + dbName + "?autoReconnect=true&useSSL=false", userName, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//*/
		//Connection conn = ds.getConnection();
		Statement state = conn.createStatement();
		//get file
		System.out.println("Select a file to view");
		File[] files = null;
		JFileChooser chooser;
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("json", "json");
		chooser.setFileFilter(filter);
		//chooser.setMultiSelectionEnabled(true);
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == chooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			JsonReader reader = new JsonReader(new FileReader(file));
			ArrayList<Feature> features = parser(reader);
			for(int i = 0; i < features.size(); i++){
				//get the attributes
				ArrayList<KeyPair> current = features.get(i).attributes;
				//get objectid
				Double tmpObjectId = (Double)(current.get(0).value);
				int objectId = tmpObjectId.intValue();
				String keys = "INSERT INTO FEATURES(";
				String vals = "VALUES(";
				for(int j = 0; j < current.size() - 2; j++){
					KeyPair<?, ?> tmp = current.get(j);
					if(j == 0){
						tmp = new KeyPair<String, Integer>("OBJECTID", objectId, KeyPair.ValClass.INT);
					}
					if(tmp.type == KeyPair.ValClass.STRING){
						vals += "'"+tmp.value + "', ";
					}
					else{
						vals += tmp.value + ", ";
					}
					keys += tmp.key + ", ";
				}
				keys = keys.substring(0, keys.length() -2) + ") ";
				vals = vals.substring(0, vals.length() -2) + ")";
				String sql = keys + vals;
				state.executeUpdate(sql);
				//get the polygon
				Polygon polygon = features.get(i).polygon;
				for(int j = 0; j < polygon.coordinates.size(); j++){
					Coordinate tmp = polygon.coordinates.get(j);
					String sql2 = "INSERT INTO COORDS (OBJECTID, latitude, longitude) VALUES(" + objectId + ", " + tmp.latitude + ", " + tmp.longitude + ")";
					state.executeUpdate(sql2);
				}
			}
			conn.close();
		}
	}
	public static ArrayList<Feature> parser(JsonReader reader) throws IOException{
	    //read the json and return something maybe
		ArrayList<Feature> features = null;
	    reader.beginObject();
	    while(reader.hasNext()){
			String name = reader.nextName();
			if(name.equals("fieldAliases")){
				List<Alias> aliases = readAliases(reader);
		    }
			else if(name.equals("features")){
				features = parseFeatures(reader);
			}
			else{
				reader.skipValue();
			}
	    }
	    reader.endObject();
	    reader.close();
	    return features;
	}
	public static ArrayList<Feature> parseFeatures(JsonReader reader) throws IOException {
		ArrayList<Feature> features = new ArrayList<Feature>();
		//read the fields
		reader.beginArray();
	    while(reader.hasNext()){
	    	features.add(new Feature(reader));
	    }
	    reader.endArray();
	    return features;
	}
	public static ArrayList<Alias> readAliases(JsonReader reader) throws IOException {
		//read the aliases
		ArrayList<Alias> aliases = new ArrayList<Alias>();
		reader.beginObject();
	    while(reader.hasNext()){
	    	String alias = reader.nextName();
			String name = reader.nextString();
			aliases.add(new Alias(name, alias));
	    }
	    reader.endObject();
	    return aliases;
	}
}
