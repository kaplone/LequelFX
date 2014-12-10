package LequelFX.LequelFX;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class Test_update {

	public static void main(String[] args) {
		
		connecter();
		inject();
		update();
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
		
		DBCollection coll_orig = db.getCollection("Lequel_V02");
		
		coll.drop();
		
		//coll.insert(coll_orig.find(new BasicDBObject("nom" , new BasicDBObject("$regex", "la"))).toArray());
		coll.insert(coll_orig.find().toArray());
		coll.createIndex(new BasicDBObject("parents", "text"));
		//coll.createIndex(new BasicDBObject("chemin", "text"));
		//coll.createIndex(new BasicDBObject("chemin_pere_ter", "text"));
		//coll.createIndex(new BasicDBObject("id_pere_ter", 1));
		
		//System.out.println(coll.count() + "   " + coll.findOne(new BasicDBObject("fichier" , true)));
	}
	
	@SuppressWarnings("unchecked")
	public static void update(){
		
		int index = 0;
		
		for(DBObject ob : coll.find()){
			
			System.out.println(index);
			index ++;
			
			List<String> outList = new ArrayList<String>();
			BasicDBList liste =  (BasicDBList) ob.get("parents");
			for (Object l : liste){
				outList.add((String) l);
			}
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append("chemin", "/" + String.join("/", outList)));
			coll.update(ob, newDocument , true, false);
			
			outList.remove(outList.size() - 1);
			
			BasicDBObject newDocument2 = new BasicDBObject();
			newDocument2.append("$set", new BasicDBObject().append("chemin_pere", "/" + String.join("/", outList)));
		    coll.update(ob, newDocument2 , true, false);
		    
		    BasicDBObject newDocument3 = new BasicDBObject();
		    newDocument3.append("$set", new BasicDBObject().append("pere", outList.remove(outList.size() - 1)));
		    coll.update(ob, newDocument3 , true, false);
		    
		}
		DBCursor cursor = coll.find();
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
		
		DBObject currentObject;
		index = 0;
		BasicDBObject newDocument4 = new BasicDBObject();;
		DBCursor res;
		DBObject resCur;
		String tmp = "";
		BasicDBObject queryMotif;
		BasicDBObject queryTaille;
		BasicDBObject queryVerif;
		BasicDBObject queryFinal;
		
		
		int taille;
		int tailleString;
		ArrayList<String> parents;
		String chemin;
		String cheminPere;
		
		while(cursor.hasNext()){
			System.out.println("_ " + index);
			index ++;
			currentObject = cursor.next();
//			if (tmp != null  &&  tmp.equals((String)currentObject.get("chemin_pere_ter"))){
//				System.out.println(newDocument4.toString());
//				coll.update(currentObject, newDocument4 , true, false);
//				System.out.println("_");
//			}
//			else{
//				
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
				
				queryVerif = new BasicDBObject("chemin", new BasicDBObject("$size", tailleString) );
				
				//obj.add(queryVerif);
				
				queryFinal = new BasicDBObject("$and", obj);
				
				res = coll.find(queryFinal);
				
				while (res.hasNext()){
					
					resCur = res.next();

					if (resCur != null){
						cheminPere = (String)resCur.get("chemin");
						//System.out.println(tailleString + " = " + cheminPere.length());
						if(cheminPere.length() == tailleString){
							System.out.println(index);
						    newDocument4.append("$set", new BasicDBObject().append("id_pere", (ObjectId)resCur.get("_id")));
						    coll.update(currentObject, newDocument4 , true, false);
						    break;
						}
					}
				//}
			}			
			
			index++;
		}
		//System.out.println(coll.count() + "   " + coll.findOne(new BasicDBObject("$exists" ,"id_pere_ter")));

	    //coll.update(x, {"$set": {"chemin_pere" : "/" + "/".join(x["parents"][:-1])}}, upsert=True)
	    //coll.update(x, {"$set": {"pere" : x["parents"][-2]}}, upsert=True)
	}

}
