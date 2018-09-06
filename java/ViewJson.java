package jsonTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import javax.naming.Context;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FileChooserUI;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class ViewJson {
	public static void main (String [] args) throws IOException{
		System.out.println("Select a file to view");
		File[] files = null;
		JFileChooser chooser;
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("json", "json");
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(true);
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			files = chooser.getSelectedFiles();
			FileOutputStream fOut = null;
			OutputStreamWriter myOutWriter = null;
			File myFile = null;
			for(int i = 0; i < files.length; i++){
				JsonReader reader = new JsonReader(new FileReader(files[i]));
				String jsonstr = new String(Files.readAllBytes(files[i].toPath()));
				jsonstr = parse(jsonstr);
				//System.out.println(jsonstr);
				/*****
				GsonBuilder builder = new GsonBuilder();
				builder.serializeNulls();
		        Gson gson = builder.create();
		        String s = gson.toJson(elements);
		        *****/
		        
				myFile = new File(files[i].getParent() + "/reduced/" + files[i].getName() + ".json");
				//System.out.println(files[i].getParent() + "/reduced/" + files[i].getName() + ".json");
		        myFile.createNewFile();
		        fOut = new FileOutputStream(myFile);
		        myOutWriter =new OutputStreamWriter(fOut);
		        myOutWriter.append(jsonstr);
		        myOutWriter.close();
		        fOut.close();
			}
			
	        
	        System.out.println("Succesfully reduced");
	        
		}
		
	}
	public static String parse(String jsonLine) {
		ArrayList<String> list = null;
	    JsonElement jelement = new JsonParser().parse(jsonLine);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    JsonArray jarrayOriginal = jobject.getAsJsonArray("features");
	    System.out.println(jarrayOriginal.size());
	    for(int j = 0; j < jarrayOriginal.size(); j++){
	    	System.out.println(jarrayOriginal.get(j));
	    	jobject = jarrayOriginal.get(j).getAsJsonObject();
		    jobject = jobject.getAsJsonObject("geometry");
		    JsonArray jarray = jobject.getAsJsonArray("paths");
		    jarray = (JsonArray) jarray.get(0);
		    int length = jarray.size();
		    list = new ArrayList<String>();
		    for(int i = 0; i < length; i++){
		    	String curr = jarray.get(i).getAsJsonArray().toString();
		    	curr = curr.substring(1, curr.length() -1);
		    	String[] coord = curr.split(",");
		    	list.add(coord[1] + "," + coord[0]);
		    }
	    }
	    
	    Gson gson = new Gson();
	    JsonElement element = gson.toJsonTree(list, new TypeToken<ArrayList<JsonArray>>() {}.getType());
	    JsonArray jsonArray = element.getAsJsonArray();
	    JsonObject jObject = new JsonObject();
	    jObject.add("paths", jsonArray);
	    String result = jObject.toString();
	    return result;
	}

}