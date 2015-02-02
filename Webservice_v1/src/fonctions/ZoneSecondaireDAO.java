package fonctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import traitements.Connexion;
import entites.ZoneSecondaire;

public class ZoneSecondaireDAO {
	

	public ZoneSecondaire listerzonesecondaire(int id) throws Exception {

		ZoneSecondaire zonesecondaire = null;  // Initialisation de la zone secondaire

		ResultSet results = null;
		Connection cnx = null;	// Initialisation de la connexion 	
		cnx = Connexion.getDbCon().getCnx();// R�cup�ration de la connexion
		String select = "SELECT zone_secondaire.id , zone_secondaire.intitule FROM jointure INNER JOIN zone_secondaire ON zone_secondaire.ID = jointure.ZONE_SECONDAIRE_MS WHERE jointure.CODE_POSTAUX_ID = ? "; // Initialisation de la requ�te
		try {
		
			PreparedStatement prep1 = cnx.prepareStatement(select); // preparation de la requete
			prep1.setInt(1,id); // ajout d'un parametre a la requete
			System.out.println("Requete de base : "+select);
			System.out.println("Prep1 : "+prep1);
			results = prep1.executeQuery(); // execution de la requ�te
			
		} catch (Exception e) {
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
