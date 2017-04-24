package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import database.DatabaseConnection;
import entities.Bundle;
import entities.Nurse;
import entities.Patient;

public class DashboardModel {
	
	Connection connection;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private ArrayList<Nurse> nurses;
	private HashMap<Integer,Patient> patients;
	
	public ArrayList<Nurse> fetchNurses(String query) throws SQLException 
	{
		connection = DatabaseConnection.connector();
		nurses = new ArrayList<Nurse>();
		preparedStatement = connection.prepareStatement(query);
		resultSet = preparedStatement.executeQuery();
		while(resultSet.next())
		{
			String query2 = "SELECT * FROM Bundles WHERE nurseID = "+resultSet.getInt("nurseID")+" ORDER BY Cost ASC";
			ArrayList<Bundle> bundles = fetchBundlesForEachNurse(query2,resultSet.getInt("nurseID"));
			Nurse nurse = new Nurse(resultSet.getInt("nurseID"), resultSet.getString("Name"), bundles);
			nurses.add(nurse);
		}
		return nurses;
	}

	public HashMap<Integer,Patient> fetchPatients(String query) throws SQLException 
	{
		connection = DatabaseConnection.connector();
		patients = new HashMap<Integer,Patient>();
		int key=0;
		preparedStatement = connection.prepareStatement(query);
		resultSet = preparedStatement.executeQuery();
		while(resultSet.next())
		{
			key++;
			Patient p = new Patient(resultSet.getInt("requestID"), resultSet.getDouble("StandardPrice"));
			patients.put(key,p);
		}
		return patients;
	}

	public ArrayList<Bundle> fetchBundlesForEachNurse(String query, int nurse) throws SQLException 
	{
		connection = DatabaseConnection.connector();
		ArrayList<Bundle> bundles = new ArrayList<Bundle>();
		Vector<Vector<Patient>> packages = new Vector<Vector<Patient>>();
		ArrayList<Double> costs = new ArrayList<Double>();
		preparedStatement = connection.prepareStatement(query);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next())
		{
			Vector<Patient> p1 = new Vector<Patient>();
			String[] split = resultSet.getString("VisitBundle").split(",");
			for(String each: split)
			{
				each = each.trim();
				p1.add(patients.get(Integer.parseInt(each)));
			}
			packages.add(p1);
			costs.add(resultSet.getDouble("Cost"));
		}
		
		for(int i=0; i<packages.size(); i++)
		{
			Bundle bundle = new Bundle(nurse, costs.get(i), packages.get(i));
			bundles.add(bundle);
		}
		return bundles;
	}

	public void insertRequests(String query1) throws SQLException {
		connection = DatabaseConnection.connector();
		preparedStatement = connection.prepareStatement(query1);
		preparedStatement.executeUpdate();
	}

	public void reset(String string1, String string2, String string3) throws SQLException {
		connection = DatabaseConnection.connector();
		preparedStatement = connection.prepareStatement(string1);
		preparedStatement.executeUpdate();
		
		preparedStatement = connection.prepareStatement(string2);
		preparedStatement.executeUpdate();
		
		preparedStatement = connection.prepareStatement(string3);
		preparedStatement.executeUpdate();
	}

	public void insertNurses(String query, String name) throws SQLException {
		connection = DatabaseConnection.connector();
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, name);
		preparedStatement.executeUpdate();
	}

	public void insertBundles(String query, String bundle) throws SQLException {
		connection = DatabaseConnection.connector();
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, bundle);
		preparedStatement.executeUpdate();
	}
}
