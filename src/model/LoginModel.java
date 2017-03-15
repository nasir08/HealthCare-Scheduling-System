package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.Agent;
import database.DatabaseAccess;
import database.DatabaseConnection;

public class LoginModel {
	
	private DatabaseAccess dbAccess = new DatabaseAccess();
	Connection connection;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private Agent agent = new Agent();
	
	public boolean isDbConnected()
	{
		return dbAccess.isDbConnected();
	}
	
	public boolean login(String user, String pass) throws SQLException {
		connection = DatabaseConnection.connector();
		String query = "SELECT * FROM Agents WHERE uName = ? AND pass = ?";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, user);
		preparedStatement.setString(2, pass);
		resultSet = preparedStatement.executeQuery();
		int count = 0;
		while(resultSet.next()) 
		{
			agent.setAgentName(resultSet.getString("fname"));
			count++;
		}
		if(count == 0){ return false; }
		else { return true; }
	}
}
