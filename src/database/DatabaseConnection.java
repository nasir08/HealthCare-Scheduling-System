package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

		public static Connection connector()
		{
			try
			{
				Class.forName("org.sqlite.JDBC");
				Connection conn = DriverManager.getConnection("jdbc:sqlite:HealthcareDb.sqlite");
				return conn;
			}
			catch(Exception e)
			{
				System.out.println(e);
				return null;
			}
		}
}
