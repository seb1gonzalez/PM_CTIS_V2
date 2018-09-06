package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLTest {
	public static void main(String[] args){
		System.out.println("MySQL Connect Example.");
		 Connection conn = null;
		 String url = "jdbc:mysql://localhost:3306/";
		 String dbName = "judo";
		 String driver = "com.mysql.jdbc.Driver";
		 String userName = "root";
		 String password = "1969";
		 try {
		     Class.forName(driver).newInstance();
		     conn = DriverManager.getConnection(url + dbName + "?autoReconnect=true&useSSL=false", userName, password);

		     System.out.println("Connected to the database");
		     Statement state = conn.createStatement();
		     ResultSet rs = state.executeQuery("SELECT * FROM participants");
		     rs.last();
		     System.out.println(rs.getRow());
		     conn.close();
		     System.out.println("Disconnected from database");
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
	}

}
