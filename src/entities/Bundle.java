package entities;

import java.util.Vector;

public class Bundle {
	
	public double price;
	private Vector<Patient> patients;
	private double saving;
	public int nurseID;
	
	public Bundle(int nurseId, double price, Vector<Patient> patients){
		
		this.price = price;
		this.patients = patients;
		saving = computeSaving(patients);
		this.nurseID = nurseId;
	}
	
	private double computeSaving(Vector<Patient> patient){
		
		int sum = 0;
		
		for(Patient p: patient){
			
			sum += p.PRICE;
			
		}
		
		return sum - price;
		
	}
	
	public boolean overlap(Bundle b){
		
		for (Patient p : patients){
			
			if(b.patients.contains(p)) return true;
		}
		
		return false;
	}
	
	public double getSaving(){
		
		return saving;
	}
	
	public Vector<Patient> getPatients(){
		
		return patients;
	}

}