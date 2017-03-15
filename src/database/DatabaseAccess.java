package database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseAccess {
	Connection connection;
	
	public DatabaseAccess() {
		connection = DatabaseConnection.connector();
		if(connection == null)
		{
			System.out.println("No database connection1");
		}
	}
	
	public boolean isDbConnected()
	{
		try
		{
			return !connection.isClosed();
		}
		catch(SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}

}
