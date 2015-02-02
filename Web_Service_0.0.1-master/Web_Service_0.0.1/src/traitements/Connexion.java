package traitements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Connexion {
	
	public  Connection cnx = null;
	private Statement statement;
	public static Connexion db;

	
	
	public Connection getCnx() {
		return cnx;
	}

	public void setCnx(Connection cnx) {
		this.cnx = cnx;
	}

	private  Connexion() {
		String url = "jdbc:mysql://localhost/";
		String dbName = "mydb";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "";
		try {
			System.out.println("Chargement du Driver...");
			Class.forName(driver);
			System.out.println("Driver chargé.");
			cnx = DriverManager.getConnection(url + dbName, userName, password);
			System.out.println("Connexion :" + cnx);
			System.out.println("Connexion réussie !");
		} catch (Exception ex) {
			System.err.println("Erreur lors du chargement  du Driver.");
		}
	
		

	}

	public ResultSet query(String query) throws Exception {
		statement = db.cnx.createStatement();
		ResultSet res = statement.executeQuery(query);
		return res;
	}

	public static synchronized Connexion getDbCon() {
		if (db == null) {
			db = new Connexion();
		}
		return db;
	}

}
