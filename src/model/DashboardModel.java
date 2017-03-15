package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.Nurses;
import data.Requests;
import database.DatabaseConnection;

public class DashboardModel {
	
	Connection connection;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private Nurses nurses = new Nurses();
	private Requests requests = new Requests();
	
	public void fetchNurses(String query) throws SQLException 
	{
		connection = DatabaseConnection.connector();
		preparedStatement = connection.prepareStatement(query);
		resultSet = preparedStatement.executeQuery();
		while(resultSet.next())
		{
			nurses.setNursesList(resultSet.getInt("nurseID"),resultSet.getString("Name"));
		}
	}

	public void fetchRequests(String query) throws SQLException 
	{
		connection = DatabaseConnection.connector();
		preparedStatement = connection.prepareStatement(query);
		resultSet = preparedStatement.executeQuery();
		while(resultSet.next())
		{
			requests.setRequestList(resultSet.getInt("requestID"),resultSet.getInt("StandardPrice"));
		}
	}
}
