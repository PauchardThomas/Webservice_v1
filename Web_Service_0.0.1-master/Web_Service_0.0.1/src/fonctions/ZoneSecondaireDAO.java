package fonctions;

import java.sql.ResultSet;
import java.sql.SQLException;

import traitements.Connexion;
import entites.ZoneSecondaire;

public class ZoneSecondaireDAO {
	
	private Connexion mysqlConnect = Connexion.getDbCon();

	public ZoneSecondaire listerzonesecondaire(int id) throws Exception {

		ZoneSecondaire zonesecondaire = null;  // Initialisation de la zone secondaire

		ResultSet results = null;
		String select = "SELECT zone_secondaire.id , zone_secondaire.intitule FROM jointure INNER JOIN zone_secondaire ON zone_secondaire.ID = jointure.ZONE_SECONDAIRE_MS WHERE jointure.CODE_POSTAUX_ID = " + id; // Initialisation de la requ�te
		try {
			results = mysqlConnect.query(select); // On essaie d'envoyer la requ�te a la BDD
		} catch (SQLException e) {
			e.printStackTrace(); // Sinon on affiche un message d'erreur
		}

		while (results.next()) { // Tant qu'on a un r�sultat
			String intitule = results.getString("zone_secondaire.intitule"); // On stock dans unez variable l'intitule de la zone secondaire.
			int id2 = results.getInt("zone_secondaire.id");

			 zonesecondaire = new ZoneSecondaire(intitule,id2); // On cr�� notre objet zonesecondaire ave son intitule

		}
		return zonesecondaire; // On renvoie l'objet zonesecondaire.
	}


}
