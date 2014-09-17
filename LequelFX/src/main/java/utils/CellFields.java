package utils;


import com.fasterxml.jackson.databind.JsonNode;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class CellFields {
	
	StringProperty fieldName = new SimpleStringProperty();
	StringProperty fieldDiskName = new SimpleStringProperty();
	StringProperty fieldExt = new SimpleStringProperty();
	StringProperty fieldType = new SimpleStringProperty();
	StringProperty fieldDate = new SimpleStringProperty();
	IntegerProperty fieldSize = new SimpleIntegerProperty();
	StringProperty fieldPathName = new SimpleStringProperty();
	
	public CellFields(){
	}
	
	public CellFields(JsonNode j){

		String p = "";

		for (JsonNode s : j.get("parents")){
			p += "[" + s + "]  ";
		}
		
		this.fieldPathName.set(p);		
		
		this.fieldName.set(j.get("nom").asText());
		this.fieldDiskName.set(j.get("disque").asText());
		this.fieldSize.set(j.get("taille").asInt());
		this.fieldExt.set(j.get("extension").asText());
		this.fieldType.set(j.get("fichier").asBoolean() ? "Fichier" : "Dossier");
		this.fieldDate.set(j.get("date").toString().split("\":\"")[1].split("T")[0] + " " +
				           j.get("date").toString().split("\":\"")[1].split("T")[1].split("Z")[0]);

	}

	public String getFieldName() {
		return fieldName.get();
	}

	public String getFieldDiskName() {
		return fieldDiskName.get();
	}

	public String getFieldExt() {
		return fieldExt.get();
	}

	public String getFieldType() {
		return fieldType.get();
	}

	public String getFieldDate() {
		return fieldDate.get();
	}

	public Integer getFieldSize() {
		return fieldSize.get();
	}

	public String getFieldPathName() {
		return fieldPathName.get();
	}

	public StringProperty fieldNameProperty() {
		return fieldName;
	}

	public StringProperty fieldDiskNameProperty() {
		return fieldDiskName;
	}

	public StringProperty fieldExtProperty() {
		return fieldExt;
	}

	public StringProperty fieldTypeProperty() {
		return fieldType;
	}

	public StringProperty fieldDateProperty() {
		return fieldDate;
	}

	public IntegerProperty fieldSizeProperty() {
		return fieldSize;
	}


}
