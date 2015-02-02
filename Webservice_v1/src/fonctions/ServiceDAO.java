package fonctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import entites.Service;
import traitements.Connexion;

public class ServiceDAO {
	
	public ArrayList<Service> listerservices(int id) throws Exception {
		
		ArrayList<Service> liste = new ArrayList<>(); // Initialisation de la liste
		Service service = null;
		ResultSet results = null;
		Connection cnx = null;	// Initialisation de la connexion 	
		cnx = Connexion.getDbCon().getCnx();// Récupération de la connexion
		String select = "select services.id , services.nom FROM code_postaux INNER JOIN jointure ON jointure.CODE_POSTAUX_ID = code_postaux.ID INNER JOIN jointure_services ON jointure.ID = jointure_services.ID_JOINTURE INNER JOIN services ON services.ID = jointure_services.ID_SERVICE WHERE code_postaux.ID = ?"; // Initialisation de la requète
		
		try {
			PreparedStatement prep1 = cnx.prepareStatement(select); // preparation de la requete
			prep1.setInt(1,id); // ajout d'un parametre a la requete
			System.out.println("Requete de base : "+select);
			System.out.println("Prep1 : "+prep1);
			results = prep1.executeQuery(); // execution de la requète

		} catch (Exception e) {
			e.printStackTrace(); // Sinon on envoie un message d'erreur
		}

		while (results.next()) { // Tant qu'on a un résultat
			String nom = results.getString("services.nom"); // On stock dans une variable l'intitule de la zone de chalandise 
			int id2 = results.getInt("services.id");
			
			 service = new Service(id2,nom); // on créé notre objet zonechalandise avec son intititule
			 liste.add(service);
			 
		}
		return liste; // On renvoie lo'bjet zonechalandise.
		
		

		
	}

}
