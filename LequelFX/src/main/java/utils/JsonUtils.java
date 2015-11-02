package utils;

import java.io.IOException;

import org.jongo.marshall.jackson.JacksonMapper;

import javafx.collections.ObservableList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class JsonUtils {
	
	final private static ObjectMapper mapper = new ObjectMapper();
	
	static JsonNode n ;
	

	public static void loadList(String file, ObservableList<CellFields> resArray) {
		
//		System.out.println("file : " + file);
//		System.out.println("file.fichier : " + ((BasicDBObject)JSON.parse(file)).get("fichier"));
		
		//if ((Boolean) ((BasicDBObject)JSON.parse(file)).get("fichier")){

			try {
				JsonNode root = mapper.readTree(file);
				resArray.add(new CellFields(root));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	
	}    


}
