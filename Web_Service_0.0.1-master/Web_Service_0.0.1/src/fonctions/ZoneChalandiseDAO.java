package fonctions;

import java.sql.ResultSet;
import java.sql.SQLException;

import traitements.Connexion;
import entites.ZoneChalandise;

public class ZoneChalandiseDAO {
	
	private Connexion mysqlConnect = Connexion.getDbCon();

	public ZoneChalandise listerzonechalandise(int id) throws Exception {

		ZoneChalandise zonechalandise = null; // Initialisation de la zone de chalandise

		ResultSet results = null;
		String select = "SELECT zone_chalandise.id , zone_chalandise.intitule FROM jointure INNER JOIN zone_chalandise ON zone_chalandise.ID = jointure.ZONE_CHALANDISE_MS WHERE jointure.CODE_POSTAUX_ID = " + id; // Initialisation de la requète
		try {
			results = mysqlConnect.query(select); // On essaie d'envoyer la requète a la BDD
		} catch (SQLException e) {
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

