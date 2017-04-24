package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Allocation {
	private final SimpleIntegerProperty id;
	private final SimpleStringProperty bundle;
	private final SimpleStringProperty cost;
	private final SimpleStringProperty nurseId;
	
	public Allocation(Integer id, String bundle, String cost, String nurseId)
	{
		this.id = new SimpleIntegerProperty(id);
		this.bundle = new SimpleStringProperty(bundle);
		this.cost = new SimpleStringProperty(cost);
		this.nurseId = new SimpleStringProperty(nurseId);
	}
	
	public Integer getId(){
		return id.get();
	}
	
	public String getBundle(){
		return bundle.get();
	}
	
	public String getCost(){
		return cost.get();
	}
	
	public String getNurseId(){
		return nurseId.get();
	}
}
