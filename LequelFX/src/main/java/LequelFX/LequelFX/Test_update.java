package LequelFX.LequelFX;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import javax.xml.bind.DatatypeConverter;

public class Test_update {
	
	static LocalDateTime dateCurrentDoc;
	static long timeCurrentDoc;
	static long savedTimeDoc;
	static ZonedDateTime zdt;
	
	static LocalDateTime dateResDoc;
	static long timeResDoc;
	static ZonedDateTime zdt_;

	public static void main(String[] args) {
		
		connecter();
		//inject();
		update();
		
		//ajout_root();
	}
	
	static MongoClient mongoclient;
	static DB db;
	static DBCollection coll;
	

	public static void connecter(){

		try {
		mongoclient = new MongoClient( "192.168.0.201" , 27017 );
		}
		catch (UnknownHostException uhe) {
		}
		
	
		db = mongoclient.getDB( "Lequel" );
		coll = db.getCollection("Lequel_V03");
	}
	
	public static void inject(){
		
		coll.createIndex(new BasicDBObject("chemin", "text"));
		coll.createIndex(new BasicDBObject("chemin_pere", 1));
		coll.createIndex(new BasicDBObject("scan", 1));
		coll.createIndex(new BasicDBObject("parents", 1));
		coll.createIndex(new BasicDBObject("id_pere", 1));

	}
	
//	protected static void ajout_root(){
//		
//		coll.insert(new BasicDBObject())
//		
//	}
	
	@SuppressWarnings({ "unchecked", "restriction" })
	public static void update(){
		
		int index = 0;
//		
//		for(DBObject ob : coll.find(new BasicDBObject("fichier" , true))){
//			
//			System.out.println(index);
//			index ++;
//			
//			List<String> outList = new ArrayList<String>();
//			BasicDBList liste =  (BasicDBList) ob.get("parents");
//			for (Object l : liste){
//				outList.add((String) l);
//			}
//			BasicDBObject newDocument = new BasicDBObject();
//			newDocument.append("$set", new BasicDBObject().append("chemin", "/" + String.join("/", outList)));
//			coll.update(ob, newDocument , true, false);
//			
//			outList.remove(outList.size() - 1);
//			
//			BasicDBObject newDocument2 = new BasicDBObject();
//			newDocument2.append("$set", new BasicDBObject().append("chemin_pere", "/" + String.join("/", outList)));
//		    coll.update(ob, newDocument2 , true, false);
//		    
//		    BasicDBObject newDocument3 = new BasicDBObject();
//		    newDocument3.append("$set", new BasicDBObject().append("pere", outList.remove(outList.size() - 1)));
//		    coll.update(ob, newDocument3 , true, false);
//		    
//		}
		DBCursor cursor = coll.find();
		//DBCursor cursor = coll.find(new BasicDBObject("fichier" , true));
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
		
		DBObject currentObject;
//		index = 0;
		BasicDBObject newDocument4 = new BasicDBObject();;
		DBCursor res;
		DBObject resCur;
		String tmp = "";
		BasicDBObject queryMotif;
		BasicDBObject queryTaille;
		BasicDBObject queryFinal;		
		
		int taille;
		int tailleString;
		ArrayList<String> parents;
		String chemin;
		String cheminPere;
		
		while(cursor.hasNext()){
			index ++;
			currentObject = cursor.next();
			
			System.out.println(currentObject.toString());
			
			if (currentObject.get("scan") != null){

				dateCurrentDoc = LocalDateTime.ofInstant(((Date)(currentObject).get("scan")).toInstant(), ZoneId.systemDefault());
				System.out.println(dateCurrentDoc);
				zdt = dateCurrentDoc.atZone(ZoneId.of("Europe/Paris"));
				timeCurrentDoc = zdt.toInstant().toEpochMilli() / 1000;
				System.out.println(timeCurrentDoc);
				
				
				if (tmp != null
				    &&  tmp.equals((String)currentObject.get("chemin_pere"))
				    && !(newDocument4 == null || newDocument4.toString().equals("{ }"))
				    &&  Math.abs(timeCurrentDoc - savedTimeDoc) < 3) {
					System.out.println(newDocument4.toString());
					coll.update(currentObject, newDocument4 , true, false);
					System.out.println("_");
				}
				else{
					
		    		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
					newDocument4 = new BasicDBObject();
					
					tmp = (String)currentObject.get("chemin_pere");
					
					queryMotif = new BasicDBObject("$text", new BasicDBObject("$search", String.format("\"%s\"", tmp ) ) );
					obj.add(queryMotif);
					
					parents = (ArrayList<String>) currentObject.get("parents");
					chemin = (String) currentObject.get("chemin_pere");
					
					taille  = parents.size() - 1;
					tailleString  = chemin.length();
					
					queryTaille  = new BasicDBObject("parents", new BasicDBObject("$size", taille));
	
					obj.add(queryTaille);
					
					queryFinal = new BasicDBObject("$and", obj);
					
					res = coll.find(queryFinal);
					
					while (res.hasNext()){
						
						resCur = res.next();
	
						if (resCur != null){
							
							dateResDoc = LocalDateTime.ofInstant(((Date)(resCur.get("scan"))).toInstant(), ZoneId.systemDefault());
							System.out.println(dateResDoc);
							zdt_ = dateResDoc.atZone(ZoneId.of("Europe/Paris"));
							timeResDoc = zdt_.toInstant().toEpochMilli() / 1000;
							System.out.println(timeResDoc);
							
							cheminPere = (String)resCur.get("chemin");
							if(cheminPere.length() == tailleString && Math.abs(timeCurrentDoc - timeResDoc) < 10 ){
								savedTimeDoc = timeResDoc;
								System.out.println(index);
							    newDocument4.append("$set", new BasicDBObject().append("id_pere", ((ObjectId) resCur.get("_id")).toHexString()));
							    coll.update(currentObject, newDocument4 , true, false);
							    break;
							}
						}
					}
				}
			}			
		} 
	}

}
