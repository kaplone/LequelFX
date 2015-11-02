package utils;


import com.fasterxml.jackson.databind.JsonNode;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;


public class CellFields {
	
	StringProperty fieldName = new SimpleStringProperty("");
	StringProperty fieldNameFull = new SimpleStringProperty("");
	StringProperty fieldDiskName = new SimpleStringProperty("");
	StringProperty fieldExt = new SimpleStringProperty("");
	StringProperty fieldType = new SimpleStringProperty("");
	StringProperty fieldDate = new SimpleStringProperty("");
	LongProperty fieldSize = new SimpleLongProperty(0);
	StringProperty fieldPathName = new SimpleStringProperty("");
	StringProperty id  =  new SimpleStringProperty("");
	StringProperty json  =  new SimpleStringProperty("");
	ObjectProperty<Image> fieldImage = new SimpleObjectProperty<Image>();
	ObjectProperty<JsonNode> fieldNode = new SimpleObjectProperty<JsonNode>();
	
	
	Image file = new Image("/images/blank-document_256.jpg");
	Image folder = new Image("/images/file-folder-icon_256.jpg");
	
	public CellFields(){
	}
	
	public CellFields(JsonNode j){
		
		fieldNode.set(j);

		String p = "";

		for (String s : j.get("chemin").asText().split("/")){
			if (! "".equals(s) && ! " ".equals(s)){
			    p += "[" + s + "]  ";
			}
		}
		
		this.fieldPathName.set(p);	
		
		//this.id.set(j.get("id").asText());
		this.json.set(j.asText());
		this.fieldName.set(j.get("nom").asText());
		this.fieldNameFull.set(j.get("fichier").asBoolean() ? 
				               j.get("nom").asText() + "." + j.get("extension").asText() :
				               j.get("nom").asText());
		this.fieldDiskName.set(j.get("chemin").asText().split("/")[1]);
		this.fieldSize.set(j.get("taille").asLong());
		
		
		//this.fieldExt.set(j.get("extension").asText());
		
		
		this.fieldType.set(j.get("fichier").asBoolean() ? "Fichier" : "Dossier");
		this.fieldDate.set(j.get("date").toString().split("\":\"")[1].split("T")[0] + " " +
				           j.get("date").toString().split("\":\"")[1].split("T")[1].split("Z")[0]);
		this.fieldImage.set(j.get("fichier").asBoolean() ? file : folder);

	}

	public String getFieldName() {
		return fieldName.get();
	}
	
	public JsonNode getFieldNode() {
		return fieldNode.get();
	}
	
	public String getId() {
		return id.get();
	}

	public String getFieldDiskName() {
		return fieldDiskName.get();
	}

	public String getFieldExt() {
		return fieldExt.get();
	}
	
	public Image getFieldImage() {
		return fieldImage.get();
	}
	
	public String getFieldNameFull() {
		return fieldNameFull.get();
	}

	public String getFieldType() {
		return fieldType.get();
	}

	public String getFieldDate() {
		return fieldDate.get();
	}

	public Long getFieldSize() {
		return fieldSize.get();
	}

	public String getFieldPathName() {
		return fieldPathName.get();
	}

	public StringProperty fieldNameProperty() {
		return fieldName;
	}
	
	public StringProperty idProperty() {
		return id;
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

	public LongProperty fieldSizeProperty() {
		return fieldSize;
	}

	public ObjectProperty<Image> fieldImageProperty() {
		return fieldImage;
		
	}


}
