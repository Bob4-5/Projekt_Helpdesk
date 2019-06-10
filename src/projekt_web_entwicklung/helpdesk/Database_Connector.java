package projekt_web_entwicklung.helpdesk;

import java.sql.*;


public class Database_Connector {
	Connection conn = null;
	
	// Verbindung aufbauen
	public void startdatabase() {
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306","root", "Database123");
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void closedatabase() {
		// Verbindung trennen
		if(conn != null) 
		{
			try
			{ 
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void abfragen () {
		// Abfrage
		try
		{
			Statement select = conn.createStatement();
			ResultSet result = select
				.executeQuery("SELECT Vorname, NachnameFROM Partner LIMIT 10;");
			while (result.next())
			{ 
				System.out.print("Vorname = " + result.getString(1));
				System.out.println("  Nachname = " + result.getString(2));
			}
		} catch (Exception e1){
			e1.printStackTrace();
		}	
	}
	
	
	
}
