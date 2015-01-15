package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableViewGenerator {
	
	private TableColumn<CellFields, String> disque;
	private TableColumn<CellFields, String> chemin;
	private TableColumn<CellFields, String> nom;
	private TableColumn<CellFields, Integer> taille;
	private TableColumn<CellFields, String>  date;
	private TableColumn<CellFields, String>  ext;
	
	private int nb;
	private double temps;

	private ObservableList<CellFields> resArray;
	
	public TableViewGenerator(TableView<CellFields> table){
	

		this.disque = new TableColumn<CellFields, String>("Disque source");
		this.chemin = new TableColumn<CellFields, String>("Chemin");
		this.nom = new TableColumn<CellFields, String>("Nom");
		this.taille = new TableColumn<CellFields, Integer>("Taille");
		this.date = new TableColumn<CellFields, String>("Date");
		this.ext = new TableColumn<CellFields, String>("Extension");
		
		this.resArray = FXCollections.observableArrayList(); 
		
		table.getColumns().add(disque);
		table.getColumns().add(chemin);
		table.getColumns().add(nom);
		table.getColumns().add(ext);
		table.getColumns().add(taille);
		table.getColumns().add(date);
	}

	public TableColumn<CellFields, String> getDisque() {
		return disque;
	}

	public TableColumn<CellFields, String> getChemin() {
		return chemin;
	}

	public TableColumn<CellFields, String> getNom() {
		return nom;
	}

	public TableColumn<CellFields, Integer> getTaille() {
		return taille;
	}

	public TableColumn<CellFields, String> getDate() {
		return date;
	}

	public TableColumn<CellFields, String> getExt() {
		return ext;
	}

	public ObservableList<CellFields> getResArray() {
		return resArray;
	}

	public int getNb() {
		return nb;
	}

	public void setNb(int nb) {
		this.nb = nb;
	}

	public double getTemps() {
		return temps;
	}

	public void setTemps(double temps) {
		this.temps = temps;
	}
	

}
