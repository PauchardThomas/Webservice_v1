package fonctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import traitements.Connexion;
import entites.ZoneChalandise;

public class ZoneChalandiseDAO {
	

	public ZoneChalandise listerzonechalandise(int id) throws Exception {

		ZoneChalandise zonechalandise = null; // Initialisation de la zone de chalandise

		ResultSet results = null;
		Connection cnx = null;	// Initialisation de la connexion 	
		cnx = Connexion.getDbCon().getCnx();// Récupération de la connexion
		String select = "SELECT zone_chalandise.id , zone_chalandise.intitule FROM jointure INNER JOIN zone_chalandise ON zone_chalandise.ID = jointure.ZONE_CHALANDISE_MS WHERE jointure.CODE_POSTAUX_ID = ? "; // Initialisation de la requète
		try {
			//results = mysqlConnect.query(select); // On essaie d'envoyer la requète a la BDD
			
			PreparedStatement prep1 = cnx.prepareStatement(select); // preparation de la requete
			prep1.setInt(1,id); // ajout d'un parametre a la requete
			System.out.println("Requete de base : "+select);
			System.out.println("Prep1 : "+prep1);
			results = prep1.executeQuery(); // execution de la requète
		} catch (Exception e) {
			e.printStackTrace(); // Sinon on envoie un message d'erreur
		}

		while (results.next()) { // Tant qu'on a un résultat
			String intitule = results.getString("zone_chalandise.intitule"); // On stock dans une variable l'intitule de la zone de chalandise 
			int id2 = results.getInt("zone_chalandise.id");
			
			 zonechalandise = new ZoneChalandise(intitule,id2); // on créé notre objet zonechalandise avec son intititule

		}
		return zonechalandise; // On renvoie lo'bjet zonechalandise.
	}

}

