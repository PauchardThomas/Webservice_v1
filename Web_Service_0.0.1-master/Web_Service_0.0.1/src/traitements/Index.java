package traitements;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.sun.xml.internal.ws.wsdl.writer.document.Service;

import entites.CodePostaux;
import entites.ListeZoneChalandise;
import entites.ListeZoneSecondaire;
import entites.ListeZones;
import entites.Ville;
import entites.ZoneChalandise;
import entites.ZoneSecondaire;
import entites.Zones;
import fonctions.ServiceDAO;
import fonctions.VerifInjectionXSS;
import fonctions.ZoneChalandiseDAO;
import fonctions.ZoneSecondaireDAO;

/**
 * 
 * @author tpauchard
 * @
 */

public class Index {


	static Document document;
	static Element racine;

		/**
		 * Connection à la base de donnée
		 */
	
		private  Connexion mysqlConnect = Connexion.getDbCon();
		/**
		 * Lorsqu’on tape un début de code postal (exemple 53…) -> Renvoi tous les codes postaux commençant par ce début de code postal (53000, 53001, 53002)
			+ nom de la ville
			+ les zones de chalandises
			+ les zones secondaires
		 * @param CP_recherche
		 * @return liste
		 * @throws Exception
		 */
		public ArrayList<CodePostaux> lister(String CP_recherche) throws Exception { 
			
			/*********************************VERIF INJECTION XSS************************************************************/
			
			VerifInjectionXSS verif = new VerifInjectionXSS().verif(CP_recherche);;
			/****************************************************************************************************************/
			
			Connection cnx = null;	// Initialisation de la connexion 	
			cnx = Connexion.getDbCon().getCnx();// Récupération de la connexion
			ArrayList<CodePostaux> liste = new ArrayList<>(); // Initialisation de la liste
			ResultSet results = null;
			
			String select = "SELECT code_postaux.ID,CP,VILLE FROM CODE_POSTAUX INNER JOIN jointure ON jointure.CODE_POSTAUX_ID = code_postaux.ID WHERE CP LIKE ? ORDER BY VILLE" ; // Initialisation de la requete
			

			try {
				PreparedStatement prep1 = cnx.prepareStatement(select); // preparation de la requete
				prep1.setString(1,CP_recherche + "%"); // ajout d'un parametre a la requete
				System.out.println("Requete de base : "+select);
				System.out.println("Prep1 : "+prep1);
				results = prep1.executeQuery(); // execution de la requète

			} catch (Exception e) {
				e.printStackTrace(); // Si on y arrive pas on affiche l'erreur

			}

			while (results.next()) { // Tant qu'on trouve une reponse
				String cp = results.getString("CP"); // On récupère le CP renvoyé par la BDD, dans une varaible .
				String ville = results.getString("VILLE"); // On récupere la ville renvoyé par la BDD, dans une varaible 
				int id = results.getInt("ID"); // On récuêre la ville renvoyé par la BDD, dans une varaible
				
				ZoneChalandise zonechalandise = new ZoneChalandiseDAO().listerzonechalandise(id); // On appel la fonction listerzonechalandise en lui passant en paramètre l'ID de chaque CP.
				ZoneSecondaire zonesecondaire = new ZoneSecondaireDAO().listerzonesecondaire(id); // On appel la fonction listerzonesecondaire en lui passant en paramètre l"ID de chaque CP.				
				ArrayList<entites.Service> service = new ServiceDAO().listerservices(id);
				
				CodePostaux codePostaux = new CodePostaux(cp,ville,zonechalandise,zonesecondaire,id,service);// On créé notre objet codepostal avec le cp , la ville , la zone de chalandise , la zone secondaire et l'id qui lui sont associé
				
				
				liste.add(codePostaux); // On ajoute a la liste le codepostal créé
			}

			return liste; // On renvoie la liste de codePostaux

		}

	
	
	/**
	 *  Lorsqu’on tape un début de nom de ville (exemple La…) -> Renvoi toutes les villes commençant par ce début de nom (Laval, Lafat...)
		+ les zones de chalandises
		+ les zones secondaires
	 * @param Ville_recherche
	 * @return liste
	 * @throws Exception
	 */
		
		public ArrayList<Ville> listerVille(String Ville_recherche) throws Exception {
			
			/*********************************VERIF INJECTION XSS************************************************************/
			
			VerifInjectionXSS verif = new VerifInjectionXSS().verif(Ville_recherche);;
			/*******************************************************************************************************/
			
			Connection cnx = null; // Initialisation de la connexion			
			cnx = Connexion.getDbCon().getCnx(); // Récupération de la connexion
			ArrayList<Ville> liste = new ArrayList<>(); // On initialise la liste de Ville
			ResultSet results = null;

			String select = "SELECT code_postaux.ID,CP,VILLE FROM CODE_POSTAUX INNER JOIN jointure ON jointure.CODE_POSTAUX_ID = code_postaux.ID WHERE VILLE LIKE ? ORDER BY VILLE" ; // On initialise la requète

			try {
				//results = mysqlConnect.query(select); // On essaie d'envoyer la requète a la BDD
				PreparedStatement prep1 = cnx.prepareStatement(select); // préparation de la requète
				prep1.setString(1,Ville_recherche + "%"); // ajout d'un parametre a la requete
				prep1.execute();
				System.out.println("Prep1 : "+prep1);
				System.out.println("select : "+select);
				results = prep1.executeQuery(); // execution de la requete

			} catch (Exception e) {
				e.printStackTrace(); // Si on y arrive pas on affiche l'erreur
			}

			while (results.next()) { // Tant qu'on trouve une réponse
				String nom = results.getString("VILLE"); // On récupère la ville renvoyée par BDD, dans une variable
				int id = results.getInt("ID"); // On récupère l'ID renvoyé par la BDD, dans une variable
				
				ZoneChalandise zonechalandise = new ZoneChalandiseDAO().listerzonechalandise(id); // On appel la fonction listerzonechalandise en lui passant en paramètre l'ID de chaque ville.				
				ZoneSecondaire zonesecondaire = new ZoneSecondaireDAO().listerzonesecondaire(id); // On appel la fonction listerzonesecondaire en lui passant en paramètre l'ID de la chaque ville.
				ArrayList<entites.Service> service = new ServiceDAO().listerservices(id);
				
				
				Ville ville = new Ville(nom,zonechalandise,zonesecondaire,service); // on créé notre objet ville avec son nom , sa zone de chalandise et sa zone secondaire.
				
				
				liste.add(ville); // on ajoute notre objet a la liste
			}

			return liste; // on renvoie la liste de ville(s).

		}
		
		/**
		 * Récupérer la liste des codes postaux et ville  par zone de chalandises NOM
		 * @param ZoneChalandise
		 * @return listeZoneChalandise
		 * @throws Exception
		 */
		
		public ArrayList<ListeZoneChalandise> listerCpEtVilleParZoneChalandise(String ZoneChalandise) throws Exception {
			
			/*********************************VERIF INJECTION XSS************************************************************/
			
			VerifInjectionXSS verif = new VerifInjectionXSS().verif(ZoneChalandise);;
			
			/*******************************************************************************************************************/
			
			Connection cnx = null;			
			cnx = Connexion.getDbCon().getCnx();
			 ArrayList<ListeZoneChalandise> listeZoneChalandise = new ArrayList<>(); // Initialisation de la liste de zone de chalandise	
			 ResultSet results = null;
			 
			 String select = "SELECT CODE_POSTAUX.ID AS ID , CODE_POSTAUX.VILLE AS VILLE , CODE_POSTAUX.CP AS CP, ZONE_CHALANDISE.INTITULE AS Zone_de_chalandise FROM JOINTURE INNER JOIN CODE_POSTAUX ON CODE_POSTAUX.ID = JOINTURE.CODE_POSTAUX_ID INNER JOIN ZONE_CHALANDISE ON ZONE_CHALANDISE.ID = JOINTURE.ZONE_CHALANDISE_MS WHERE ZONE_CHALANDISE.INTITULE = ? ORDER BY CODE_POSTAUX.VILLE ASC"; // Initialisation de la requète.
			 //System.out.println(select);
			 
			 try {
				// results = mysqlConnect.query(select); // On essaie d'envoyer la requète a la  BDD
					PreparedStatement prep1 = cnx.prepareStatement(select);
					prep1.setString(1,ZoneChalandise);
					prep1.execute();
					System.out.println("Prep1 : "+prep1);
					System.out.println("select : "+select);
					results = prep1.executeQuery();
				 
			 }catch(Exception e) {
				 e.printStackTrace(); // Si on y arrive pas on renvoie l'erreur
			 }
			 
			 while (results.next()) { // Tant qu'on trouve une réponse
				
				 int Id = results.getInt("CODE_POSTAUX.ID");
				 String Ville = results.getString("CODE_POSTAUX.VILLE"); // On récupère la ville renvoyée par la BDD, dans une variable
				 String CP = results.getString("CODE_POSTAUX.CP"); // On récupère le CP renvoyé par la BDD, dans une variable
				 String ZoneChalandise2 = results.getString("Zone_de_chalandise"); // On récupère la zone de chalandise renvoyée par la BDD, dans une variable.
				 ArrayList<entites.Service> service = new ServiceDAO().listerservices(Id);
				 ListeZoneChalandise zonedechalandise = new ListeZoneChalandise(Id,CP, Ville, ZoneChalandise2,service); // On créé notre objet zone de chalandise avec un CP une ville et une zone de chalandise
				 
				 listeZoneChalandise.add(zonedechalandise); // On ajoute notre objet a la liste
				 
			 }
			 
			 
			 return listeZoneChalandise; // On renvoie la liste des zones de chalandise
			
		}
		
		/**
		 * Récupérer la liste des codes postaux et ville  par zone de chalandises ID
		 * @param ZoneChalandise
		 * @return listeZoneChalandise
		 * @throws Exception
		 */
		
		public ArrayList<ListeZoneChalandise> listerCpEtVilleParZoneChalandiseID(int ZoneChalandise) throws Exception {
			
			
			Connection cnx = null;			
			cnx = Connexion.getDbCon().getCnx();
			 ArrayList<ListeZoneChalandise> listeZoneChalandise = new ArrayList<>(); // Initialisation de la liste de zone de chalandise	
			 ResultSet results = null;
			 
			 String select = "SELECT CODE_POSTAUX.ID AS ID , CODE_POSTAUX.VILLE AS VILLE , CODE_POSTAUX.CP AS CP, ZONE_CHALANDISE.INTITULE AS Zone_de_chalandise FROM JOINTURE INNER JOIN CODE_POSTAUX ON CODE_POSTAUX.ID = JOINTURE.CODE_POSTAUX_ID INNER JOIN ZONE_CHALANDISE ON ZONE_CHALANDISE.ID = JOINTURE.ZONE_CHALANDISE_MS WHERE ZONE_CHALANDISE.ID = ? ORDER BY CODE_POSTAUX.VILLE ASC"; // Initialisation de la requète.
			 //System.out.println(select);
			 
			 try {
				// results = mysqlConnect.query(select); // On essaie d'envoyer la requète a la  BDD
					PreparedStatement prep1 = cnx.prepareStatement(select);
					prep1.setInt(1,ZoneChalandise);
					prep1.execute();
					System.out.println("Prep1 : "+prep1);
					System.out.println("select : "+select);
					results = prep1.executeQuery();
				 
			 }catch(Exception e) {
				 e.printStackTrace(); // Si on y arrive pas on renvoie l'erreur
			 }
			 
			 while (results.next()) { // Tant qu'on trouve une réponse
				
				 int Id = results.getInt("CODE_POSTAUX.ID");
				 String Ville = results.getString("CODE_POSTAUX.VILLE"); // On récupère la ville renvoyée par la BDD, dans une variable
				 String CP = results.getString("CODE_POSTAUX.CP"); // On récupère le CP renvoyé par la BDD, dans une variable
				 String ZoneChalandise2 = results.getString("Zone_de_chalandise"); // On récupère la zone de chalandise renvoyée par la BDD, dans une variable.
				 ArrayList<entites.Service> service = new ServiceDAO().listerservices(Id);
				 ListeZoneChalandise zonedechalandise = new ListeZoneChalandise(Id,CP, Ville, ZoneChalandise2,service); // On créé notre objet zone de chalandise avec un CP une ville et une zone de chalandise
				 
				 listeZoneChalandise.add(zonedechalandise); // On ajoute notre objet a la liste
				 
			 }
			 
			 
			 return listeZoneChalandise; // On renvoie la liste des zones de chalandise
			
		}
		
		/**
		 * 
		 * Récupérer la liste des codes postaux et ville  + par zone secondaire
		 * @param ZoneSecondaire
		 * @return listeZoneSecondaire
		 * @throws Exception
		 */

		public ArrayList<ListeZoneSecondaire> listerCpEtVilleParZoneSecondaire(String ZoneSecondaire) throws Exception {
			
			/*********************************VERIF INJECTION XSS************************************************************/

			VerifInjectionXSS verif = new VerifInjectionXSS().verif(ZoneSecondaire);;
			
			/****************************************************************************************************************/
			
			Connection cnx = null;			
			cnx = Connexion.getDbCon().getCnx();	
			 ArrayList<ListeZoneSecondaire> listeZoneSecondaire = new ArrayList<>(); // On initialise notre liste de zone secondaire	
			 ResultSet results = null;
			 
			 String select = "SELECT CODE_POSTAUX.ID AS ID , CODE_POSTAUX.VILLE AS VILLE , CODE_POSTAUX.CP AS CP, ZONE_SECONDAIRE.INTITULE AS Zone_secondaire FROM JOINTURE INNER JOIN CODE_POSTAUX ON CODE_POSTAUX.ID = JOINTURE.CODE_POSTAUX_ID INNER JOIN ZONE_SECONDAIRE ON ZONE_SECONDAIRE.ID = JOINTURE.ZONE_SECONDAIRE_MS WHERE ZONE_SECONDAIRE.INTITULE = ? ORDER BY CODE_POSTAUX.VILLE ASC"; // Initialise la requète
			// System.out.println(select);
			 
			 try {
				// results = mysqlConnect.query(select); // On essaie d'envoyer la requète a la BDD
					PreparedStatement prep1 = cnx.prepareStatement(select);
					prep1.setString(1,ZoneSecondaire);
					prep1.execute();
					System.out.println("Prep1 : "+prep1);
					System.out.println("select : "+select);
					results = prep1.executeQuery();
			 }catch(Exception e) {
				 e.printStackTrace(); // Sinon on affiche l'erreur
			 }
			 
			 while (results.next()) { // Tant qu'on trouve une réponse
				
				 int Id = results.getInt("CODE_POSTAUX.ID");
				 String Ville = results.getString("CODE_POSTAUX.VILLE"); // On récupère la ville renvoyé par la BDD, dans une variable
				 String CP = results.getString("CODE_POSTAUX.CP"); // On récupère le CP renvoyé par la BDD, dans une varaible
				 String Zonesecondaire2 = results.getString("Zone_secondaire"); // On récupère la zone de chalandise renvoyée par la BDD, dans une variable
				 ArrayList<entites.Service> service = new ServiceDAO().listerservices(Id);
				 ListeZoneSecondaire nouvellezonesecondaire = new ListeZoneSecondaire(Id,CP, Ville, Zonesecondaire2,service); // On créé notre objet nouvelle zone secondaire avec un CP, une ville et une zone secondaire
				 
				 listeZoneSecondaire.add(nouvellezonesecondaire); // on ajoute notre objet a la liste
				 
			 }
			 
			 
			 return listeZoneSecondaire; // on renvoie la liste de zone(s) secondaire.
			
		}
		
		
		/**
		 * 
		 * Récupérer la liste des codes postaux et ville  + par zone secondaire ID
		 * @param ZoneSecondaire
		 * @return listeZoneSecondaire
		 * @throws Exception
		 */

		public ArrayList<ListeZoneSecondaire> listerCpEtVilleParZoneSecondaireID(int ZoneSecondaire) throws Exception {
			
			
			Connection cnx = null;			
			cnx = Connexion.getDbCon().getCnx();	
			 ArrayList<ListeZoneSecondaire> listeZoneSecondaire = new ArrayList<>(); // On initialise notre liste de zone secondaire	
			 ResultSet results = null;
			 
			 String select = "SELECT CODE_POSTAUX.ID AS ID , CODE_POSTAUX.VILLE AS VILLE , CODE_POSTAUX.CP AS CP, ZONE_SECONDAIRE.INTITULE AS Zone_secondaire FROM JOINTURE INNER JOIN CODE_POSTAUX ON CODE_POSTAUX.ID = JOINTURE.CODE_POSTAUX_ID INNER JOIN ZONE_SECONDAIRE ON ZONE_SECONDAIRE.ID = JOINTURE.ZONE_SECONDAIRE_MS WHERE ZONE_SECONDAIRE.ID = ? ORDER BY CODE_POSTAUX.VILLE ASC"; // Initialise la requète
			// System.out.println(select);
			 
			 try {
				// results = mysqlConnect.query(select); // On essaie d'envoyer la requète a la BDD
					PreparedStatement prep1 = cnx.prepareStatement(select);
					prep1.setInt(1,ZoneSecondaire);
					prep1.execute();
					System.out.println("Prep1 : "+prep1);
					System.out.println("select : "+select);
					results = prep1.executeQuery();
			 }catch(Exception e) {
				 e.printStackTrace(); // Sinon on affiche l'erreur
			 }
			 
			 while (results.next()) { // Tant qu'on trouve une réponse
				
				 int Id = results.getInt("CODE_POSTAUX.ID");
				 String Ville = results.getString("CODE_POSTAUX.VILLE"); // On récupère la ville renvoyé par la BDD, dans une variable
				 String CP = results.getString("CODE_POSTAUX.CP"); // On récupère le CP renvoyé par la BDD, dans une varaible				
				 String Zonesecondaire2 = results.getString("Zone_secondaire"); // On récupère la zone de chalandise renvoyée par la BDD, dans une variable
				 ArrayList<entites.Service> service = new ServiceDAO().listerservices(Id);
				 ListeZoneSecondaire nouvellezonesecondaire = new ListeZoneSecondaire(Id,CP, Ville, Zonesecondaire2,service); // On créé notre objet nouvelle zone secondaire avec un CP, une ville et une zone secondaire
				 
				 listeZoneSecondaire.add(nouvellezonesecondaire); // on ajoute notre objet a la liste
				 
			 }
			 
			 
			 return listeZoneSecondaire; // on renvoie la liste de zone(s) secondaire.
			
		}
		
		
		/**
		 * En fonction de l’ID de société envoyé -> renvoi la/les zones de chalandises associés à cet ID
		 * @param id_societe
		 * @return zones
		 * @throws Exception 
		 */
	     
		
		public static String  ZoneChalandiseFtDunIDSociete(String id_societe) throws Exception  {

			/*********************************VERIF INJECTION XSS************************************************************/

			VerifInjectionXSS verif = new VerifInjectionXSS().verif(id_societe);;
			
			/****************************************************************************************************************/
			

			SAXBuilder sxb = new SAXBuilder(); //On crée une instance de SAXBuilder
			String filepath = System.getProperty("user.dir"); // On récupère le chemin du futur XML a charger
			System.out.println("yo");
			try
			{
				File file = new File(filepath+"/services-a-domicile.xml"); // On essaie d'ouvrir notre fichier services-a-domicile.xml
				document = sxb.build(file); //On crée un nouveau document JDOM avec en argument le fichier XML
			}
			catch(Exception e){
				System.out.println("Fichier non chargé"); // Sinon on renvoie que le fichier n'a pas été chargé.
				System.out.println(e);
			}
			

			racine = document.getRootElement(); //On initialise un nouvel élément racine avec l'élément racine du document.
			List listSociete = racine.getChildren("societe"); //On crée une List contenant tous les noeuds "societe" de l'Element racine

			Iterator i = listSociete.iterator(); //On crée un Iterator sur notre liste
			Iterator j = listSociete.iterator(); //On crée un Iterator sur notre liste
			String zones = null; //On intiaise une zone 
					System.out.println("zones :" + zones);
			while(i.hasNext()) { // Tant qu'on trouve un element
				Element courant = (Element)i.next(); // On fait avancer notre pointeur
				Element courant2 = (Element)j.next();// On fait avancer notre pointeur.
				
				if(courant2.getAttributeValue("id").equals(id_societe)) { // Si l'ID DE l'élement en cours est égal a celui passé en paramètre
					 zones = courant.getAttributeValue("zone_chalandise_tab"); // On stock dans une varaible les zones de chalandises de cet société.
				//	System.out.println(courant.getAttributeValue("zone_chalandise_tab"));
				}
				
				
			}

			return zones; // On renvoie les zones se chalandises associés à l'id de société recherché.
		}
		
		
		
	/**
	 * 	En fonction de l’ID du code postal envoyé -> renvoi le mail de la société associé à cet ID.
	 * @param ID_envoye
	 * @return mail
	 * @throws Exception
	 */
		
		
		@SuppressWarnings("unused")
		public  String MailSocieteEnFtDunCP(int ID_envoye) throws Exception {
			
			
			/*******************************PART 1 : RECUPERATION DE L ID DE LA ZONE DE CHALANDISE OU SECONDAIRE******************************************************* */
			Connection cnx = null;			
			cnx = Connexion.getDbCon().getCnx();
			ResultSet results = null;
			String select = "select ZONE_CHALANDISE_MS , ZONE_SECONDAIRE_MS FROM jointure INNER JOIN code_postaux ON code_postaux.id = jointure.CODE_POSTAUX_ID WHERE CODE_POSTAUX.ID = ? limit 1" ; // Initialisation de la requète.
			String zone2= null; // ID de zone
			String zone_chalandise = null; // zone de chalandise récupérée par la requete sql
			String zone_secondaire =null; // zone secondairez récupérée par la requète sql
			String zonechalandiseOK= null; // stockage de la variable zone_chalandise sous certaines conditions
			String zonesecondaireOK = null; // stockage de la variable zone_secondaire sous certaines conditions.
			String aucuneAgence = "Aucune agence"; // Chaine renvoyé si on ne trouve aucune agence pour l'ID CP recherché
			int SavoirSiOnAUnResults = 0; // Savoir si la réponse de la requète sql nous renvoie quelque chose.

			try {
				PreparedStatement prep1 = cnx.prepareStatement(select); // On prépare la requète
				prep1.setInt(1,ID_envoye); // on envoye un parametre a la requète
				System.out.println("Prep1 : "+prep1);
				System.out.println("select : "+select);
				results = prep1.executeQuery(); // execution de la requète.

			} catch (Exception e) {
				e.printStackTrace(); // Sinon on affiche le message d'erreur

			}
			
			

			 while (results.next()) { // Tant qu'on trouve un résultat
				 
				 SavoirSiOnAUnResults = 1;
			 zone_chalandise = results.getString("ZONE_CHALANDISE_MS"); // On stock dans une vaiable l'id de la zone de chalandise récupéré en BDD 
			 zone_secondaire = results.getString("ZONE_SECONDAIRE_MS"); // On stock dans une varaible l'id de la zone secondaire récupéré en BDD

			 
			  if(zone_secondaire == null) { // Si l'ID du CP n'a pas de zone_secondaire
				
				if(zone_chalandise != null) {// Si l'ID du CP a une zone de chalandise	
				
					zonechalandiseOK = zone_chalandise; // On conserve l'id de la zone de chalandise
					System.out.println("Zone de chalandise : "+zonechalandiseOK);

				} else { // Sinon
					return "Aucune agence !"; //  on renvoie que l'ID du CP n'a ni zone de chalandise ni zone secondaire.
				}
				

			}else { // Sinonl'ID DU cp a une zone secondaire
				zonesecondaireOK = zone_secondaire; // On conserve l'id de la zone secondaire
				System.out.println("Zone secondaire : "+zonesecondaireOK);
			}


			
			 }
			 
			 if(SavoirSiOnAUnResults == 0) { // Si la requète ne nous renvoie rien.
				 return "Aucune agence !"; // Cela signie que l'ID CP n'a ni zone de chalandise ni zone secondaire.
			 }
			 
			 
		
			/*******************************PART 2 : RECUPERATION DU MAIL******************************************************* */	

			SAXBuilder sxb = new SAXBuilder(); //On crée une instance de SAXBuilder
			String filepath = System.getProperty("user.dir"); // On récupère le chemin du futur XML a charger
			System.out.println("Userdir : "+filepath);
			
			try
			{
				File file = new File(filepath+"/services-a-domicile.xml"); // On essaie d'ouvrir notre fichier services-a-domicile.xml
				System.out.println("Chemin : "+filepath+"/services-a-domicile.xml");
				document = sxb.build(file); //On crée un nouveau document JDOM avec en argument le fichier XML
			}
			catch(Exception e){
				System.out.println("Fichier non chargé");  // Sinon on renvoie que le fichier n'a pas été chargé.
				System.out.println(e);
			}
			
			racine = document.getRootElement(); //On initialise un nouvel élément racine avec l'élément racine du document.
			List listSociete = racine.getChildren("societe"); //On crée une List contenant tous les noeuds "societe" de l'Element racine
			List listContact = racine.getChildren("contact"); //On crée une List contenant tous les noeuds "contact" de l'Element racine

			Iterator i = listSociete.iterator(); //On crée un Iterator sur notre liste
			Iterator j = listContact.iterator(); //On crée un Iterator sur notre liste
			String zones = null; // chaine de caractere du fichier service-a-domicile.xml
			String valeurcontact = null; // valeur du mail du contact
			String mail= ""; // mail a renvoyé
			String temp2 = ""; // stockage du mail en attendant de le trouver sous certaines conditons.
			String temp3 =""; // stockage du mail en attendant de le trouver sous d'autres certaines conditions.
			

			
			boolean sortir = false; // On initialise une sortir a faut
			while(i.hasNext() && !sortir) { // tant qu'on a un résultat et qu'on ne doit sortir

				Element courant = (Element)i.next(); // On fait avancer notre pointeur

				String[] temp;			// initialisation d'un tableau pou couper notre chaine "zones" 
				String delimiter = " "; // Initilisation d'un delimiter qui va servir a découper notre chaine zones

				valeurcontact = courant.getChild("contact").getAttributeValue("mail"); // On récupère le mail du contact

				if(courant.getAttributeValue("zone_chalandise_tab") != null){ // Si l'attribut zone_chalandise_tab existe
					
				
				zones = courant.getAttributeValue("zone_chalandise_tab");	// On associe a la varaibles zones, les différentes zones de chalandises
				
				}else {
					zones = courant.getAttributeValue("zone_secondaire_tab"); // Sinon on associe a la variable zonesz, les différentes zones secondaire.
				}
				temp = zones.split(delimiter); // On découpe notre chaine de zones secondaires ou chalandises de la forme (" 11	2 ") par le delimiter.

				for(int f =0; f<temp.length;f++) { // Pour chaque case du tableau temp 
					
					System.out.println("Zone n°" + temp[f]);
					
					if(zonechalandiseOK != null && zonesecondaireOK == null) { // Si l'ID CP n'a pas de zone secondaire mais a une zone de chalandise
						if(temp[f].equals(zonechalandiseOK) == true) { // Si la zone de chalandise de l'ID CP est attribué a une societe
							mail = valeurcontact; // On récupère le mail.
							System.out.println("Le mail : "+mail);
							sortir = true; // on a récupéré le mail on sort donc de notre boucle.
						}
					 else { // Sinon si la zone de chalanhdise de l'ID cp n'est pas attribué a une societe
						mail = aucuneAgence; // L'ID CP n'a donc ni zone de chalandise ni zone secondaire et par conséquent pas de mail.
						System.out.println("Mail aucune agence :"+mail);
					}
						
					}
					
					
					if(zonechalandiseOK == null && zonesecondaireOK == null) { // si l'ID CP n'a ni zone de chalandise ni zone secondaire 
						mail = aucuneAgence; // l'ID CP n'a donc ni zone de chalandise ni zone secondaire et par conséquent pas de mail
						
					}				
					
					
					if(zonesecondaireOK != null) { // Si l'ID CP a une zone secondaire.
						System.out.println("Zone secondaire rataché a une société ? : "+temp[f].equals(zonesecondaireOK));
						if(temp[f].equals(zonesecondaireOK) == false) { // Si la zone secondaire de l'ID CP n'est pas attribué a une societe
							zonechalandiseOK = zone_chalandise; // on récupère la zone de chalandise.
							System.out.println("Mail initialise :"+mail);
							System.out.println("Zone de chalandise : "+zonechalandiseOK);
							System.out.println("Zone de chalandise associée a une société ? : "+temp[f].equals(zonechalandiseOK));
							if(temp[f].equals(zonechalandiseOK)) { // Si la zone de chalandise de l'ID CP est attribué a une societe
								System.out.println("Mail d'avant : "+mail);
								temp2 = valeurcontact; // On récupère le mail dans une varaible temporaire au cas ou qu'on trouve l'association d'une zone secondaire a une société sur un contact ultérieur
								System.out.println("Le mail  : "+temp2);
							}else { // Sinon si ni la zone de chalandise n'est pas attribué a une société
								System.out.println("yo3");												
								temp3 = aucuneAgence; // l'ID CP n'a donc pas de société associé a une zone de chalandise ou secondaire sur le contact actuel.
							}
						
						
						} else { // Sinon si la zone seoncdaire de l'ID CP est attribué a une société
							mail = valeurcontact; // On récupère le mail
							System.out.println("Le mail yo2 : "+mail);
							sortir = true; // On a récupéré le mail on sort donc de notre boucle.
						}
					
					}				

				}
				
			}
			
			if(mail == "") { // si le mail n'a pas été trouvé 
				if(temp2 != "") { // si le mail est stocké dans la varaible temporaire 
					mail = temp2; // On récupère alors ce mail
				} else { // sinon
					mail = temp3; // On récupère le mail de la secondaire varaible temporaire. 
				}
				
			}

			return mail; // On renvoie le mail.
			
		
		// IT'S WORK ! :-)
		

	
		}
		
		
public ArrayList<ZoneSecondaire> listerZoneSecondaireEnFtZoneChalandise(int ZoneChalandise) throws Exception {
			
			
			Connection cnx = null;			
			cnx = Connexion.getDbCon().getCnx();	
			 ArrayList<ZoneSecondaire> ZoneSecondaire = new ArrayList<>(); // On initialise notre liste de zone secondaire	
			 ResultSet results = null;
			 
			 String select = "select zone_secondaire.ID AS ID ,zone_secondaire.intitule AS ZoneSecondaire FROM zone_secondaire WHERE zone_secondaire.ID_ZONE_CHALANDISE = ? ;"; // Initialise la requète
			// System.out.println(select);
			 
			 try {
				// results = mysqlConnect.query(select); // On essaie d'envoyer la requète a la BDD
					PreparedStatement prep1 = cnx.prepareStatement(select);
					prep1.setInt(1,ZoneChalandise);
					prep1.execute();
					System.out.println("Prep1 : "+prep1);
					System.out.println("select : "+select);
					results = prep1.executeQuery();
			 }catch(Exception e) {
				 e.printStackTrace(); // Sinon on affiche l'erreur
			 }
			 
			 while (results.next()) { // Tant qu'on trouve une réponse
				
				 int Id = results.getInt("zone_secondaire.ID");
				 String intitule = results.getString("ZoneSecondaire"); // On récupère la zone de chalandise renvoyée par la BDD, dans une variable
				 ZoneSecondaire zonesecondaire = new ZoneSecondaire(intitule, Id); // On créé notre objet nouvelle zone secondaire avec un CP, une ville et une zone secondaire
				 
				 ZoneSecondaire.add(zonesecondaire); // on ajoute notre objet a la liste
				 
			 }
			 
			 
			 return ZoneSecondaire; // on renvoie la liste de zone(s) secondaire.
			
		}


/**
 * 
 * Fonction permettant de récupérer les zones de chalandises ( + leurs zones secondaires s'ils elles en ont ) ou les zones secondaires , associés a une société
 * 
 * @param ID_societe
 * @return liste
 * @throws Exception
 */

public ArrayList<ListeZones> listerZonesEnFtIdSociete(int ID_societe) throws Exception {

	ArrayList<ListeZones> liste = new ArrayList<>();
		
		
	SAXBuilder sxb = new SAXBuilder(); //On crée une instance de SAXBuilder
	String filepath = System.getProperty("user.dir"); // On récupère le chemin du futur XML a charger
	try
	{
		File file = new File(filepath+"/services-a-domicile.xml"); // On essaie d'ouvrir notre fichier services-a-domicile.xml
		document = sxb.build(file); //On crée un nouveau document JDOM avec en argument le fichier XML
	}
	catch(Exception e){
		System.out.println("Fichier non chargé"); // Sinon on renvoie que le fichier n'a pas été chargé.
		System.out.println(e);
	}
	

	racine = document.getRootElement(); //On initialise un nouvel élément racine avec l'élément racine du document.
	List listSociete = racine.getChildren("societe"); //On crée une List contenant tous les noeuds "societe" de l'Element racine

	Iterator i = listSociete.iterator(); //On crée un Iterator sur notre liste
	Iterator j = listSociete.iterator(); //On crée un Iterator sur notre liste
	String zones = null; //On intiaise une zone 
	String zonechal = null; // On initialise une zone de chalandise
	String zonesecond = null; // On initialise une zone secondaire
	int verification=0; // On initialise une vérification de passage dans certaines boucles.
	
	String[] temp;			// initialisation d'un tableau pou couper notre chaine "zones" 
	String delimiter = " "; // Initilisation d'un delimiter qui va servir a découper notre chaine zones
	
	Connection cnx = null;			// Initialisation de la connexion
	cnx = Connexion.getDbCon().getCnx();
	ResultSet results = null;
	String zone_secondaire_intitule = null; // Initialisations des parametres de results
	int zone_chalandise_ID = 0;	
	String zone_chalandise_intitule = null;
	int  zone_secondaire_ID = 0;
	
			System.out.println("zones :" + zones);
	while(i.hasNext()) { // Tant qu'on trouve un element
		
		
		Element courant = (Element)i.next(); // On fait avancer notre pointeur
		Element courant2 = (Element)j.next();// On fait avancer notre pointeur.
		String idCourant2 = courant2.getAttributeValue("id");
		int test = Integer.parseInt(idCourant2);
		
		
		System.out.println("String to int : "+Integer.parseInt(idCourant2));
		System.out.println("ID societe : "+ ID_societe);
	//	System.out.println("Egalité : "+courant2.getAttributeValue("id").equals(ID_societe));
		
		
		if(test == (ID_societe)) { // Si l'ID DE l'élement en cours est égal a celui passé en paramètre

			
			if(courant.getAttribute("zone_chalandise_tab") != null) { 

				zones = courant.getAttributeValue("zone_chalandise_tab"); // On stock dans une varaible les zones de chalandises de cette société.
				temp = zones.split(delimiter);
				
				for (int k = 0; k < temp.length; k++) {		// Pour chaque valeur du tableau => chaque zone de chalandise extraite de la chaine zones
					
					System.out.println(temp[k]);
					temp[k].replace(delimiter,""); // on enleves les espaces s'il en reste.				
					zonechal = temp[k];
					System.out.println(temp[k]);
					
					if(!zonechal.equals("")) { // Si la société possède des zones de chalandises
						
					verification = 0;
					zone_secondaire_intitule = null;
					zone_secondaire_ID = 0;
					//ancienne
					//String select = "select zone_chalandise.ID , zone_chalandise.intitule , zone_secondaire.ID , zone_secondaire.intitule from zone_chalandise INNER JOIN zone_secondaire ON zone_secondaire.ID_ZONE_CHALANDISE = zone_chalandise.ID where zone_chalandise.ID = ?" ; // Initialisation de la requète.
					// nouvelle : test
					 String select = "select ID, intitule FROM zone_chalandise WHERE ID = ? " ; // Initialisation de la requète.
					
					
					System.out.println(select);
					 try {
								PreparedStatement prep1 = cnx.prepareStatement(select); // Préparation de la requète
								prep1.setString(1,zonechal); // On passe un parametre a notre requète
								System.out.println("Prep1 : "+prep1);
								System.out.println("select : "+select);
								results = prep1.executeQuery(); // On execute notre requète
							 
						 }catch(Exception e) {
							 e.printStackTrace(); // Si on y arrive pas on renvoie l'erreur
						 }
						 
						 while (results.next()) { // Tant qu'on trouve une réponse
							System.out.println("Il y a un résultat !");
							  zone_chalandise_ID = results.getInt("ID"); // On récupère l'ID de la zone de chalandise
							  zone_chalandise_intitule = results.getString("intitule"); // On récupère l'intitule de la zone de chalandise
						 
							 	
							 		/* ON VA MAINTENANT RECHERCHER LA/LES ZONE(S) SECONDAIRE(S) ASSOCIÉES A LA ZONE DE CHALANDISE  */
							 		String select2 = "select ID , intitule from zone_secondaire where id_zone_chalandise = ?"; // Initialisation de la requète
							 		
							 		 try {
									
												PreparedStatement prep2 = cnx.prepareStatement(select2); // Préparation de la requète
												prep2.setInt(1,zone_chalandise_ID); // On passe en paramètre l'ID de la zone de chalandise.
												System.out.println("Prep2 : "+prep2);
												System.out.println("select2 : "+select2);
												results = prep2.executeQuery(); // On execute la requète
											 
										 }catch(Exception e) {
											 e.printStackTrace(); // Si on y arrive pas on renvoie l'erreur
										 }
							 		
							 		
							 		 while (results.next()) { // Tant qu'on trouve une réponse
							 			  zone_secondaire_intitule = results.getString("zone_secondaire.intitule"); // On récupère l'intitule de la zone secondaire
							 			 zone_secondaire_ID = results.getInt("ID"); // on récupère l'ID de la zone secondaire
							 			 
							 			 
							 			ZoneChalandise zonechalandise = new ZoneChalandise(zone_chalandise_intitule, zone_chalandise_ID); // on créer un objet zonechalandise avec les params l'intitule et l'ID de la zone de chalandise
							 			 ZoneSecondaire zonesecondaire = new ZoneSecondaire(zone_secondaire_intitule,zone_secondaire_ID); // on créer une objet zonesecondaire avec les params l'intitule et l'ID de la zone secondaire
										 ListeZones zones2  = new ListeZones(zonechalandise, zonesecondaire); // On créé une zone en params la zone de chalandise et zonesecondaire associé
										 
										 liste.add(zones2); // On l'ajoute a la liste.
										 verification++; // On incrémente la vérification.
							 		 }
							 		 

	
							 	
							 	if(verification == 0) { // Si la vérification n'a pas été incrémenté, et donc q'aucune secondaire n'a été trouvé a la zone de chalandise 
							 
								ZoneChalandise zonechalandise = new ZoneChalandise(zone_chalandise_intitule, zone_chalandise_ID); // on créer un objet zonechalandise avec les params l'intitule et l'ID de la zone de chalandise
								 ZoneSecondaire zonesecondaire = new ZoneSecondaire(zone_secondaire_intitule, zone_secondaire_ID);	// on créer une objet zonesecondaire avec les params l'intitule et l'ID de la zone secondaire					 
								 ListeZones zones2  = new ListeZones(zonechalandise, zonesecondaire);	// On créé une zone en params la zone de chalandise et zonesecondaire associé						 
								 liste.add(zones2); // On l'ajoute a la liste.
							 	}

							// ListeZoneChalandise zonedechalandise = new ListeZoneChalandise(Id,CP, Ville, ZoneChalandise2,service); // On créé notre objet zone de chalandise avec un CP une ville et une zone de chalandise
							 
							// listeZoneChalandise.add(zonedechalandise); // On ajoute notre objet a la liste
							 
						 }
					}
					
				}

				
			} else {
				System.out.println("Zones secondaires : "+zonesecond);
				System.out.println(courant.getAttributeValue("zone_secondaire_tab"));
				zonesecond = courant.getAttributeValue("zone_secondaire_tab"); // On stock dans une variable les zones secondaire de cette société		
				
				temp= zonesecond.split(delimiter); // On split notre chaine
				System.out.println("yo1");
				for (int k = 0; k < temp.length; k++) {	
					
					System.out.println("Avant retrait des espaces : |"+ temp[k]+"|");
					
					zonesecond = temp[k].replace(delimiter,""); // On retire les espaces s'il en existe encore.
					System.out.println("Apres retrait des espaces : |"+ zonesecond+"|");
					
					if(!zonesecond.equals("")) { // Si la zone secondaire est vide
						
						String select = "select ID , intitule FROM zone_secondaire WHERE ID = ?" ; // Initialisation de la requète.
						System.out.println(select);
						
						 try {
									PreparedStatement prep1 = cnx.prepareStatement(select); // Préparation de la requète
									prep1.setString(1,zonesecond); // Ajout de l'id de la zone secondaire a la requète
									System.out.println("Prep1 : "+prep1);
									System.out.println("select : "+select);
									results = prep1.executeQuery();// Execution de la requète
								 
							 }catch(Exception e) {
								 e.printStackTrace(); // Si on y arrive pas on renvoie l'erreur
							 }
							 
							 while (results.next()) { // Tant qu'on trouve une réponse
								
								 int id = results.getInt("ID"); // on récupère l'ID de la zone secondaire
								 String intitule = results.getString("intitule"); // on récupère l'intitulé de la zone secondaire.
								 
								 ZoneSecondaire zonesecondaire = new ZoneSecondaire(intitule, id); // On créer un objet zone secondaire avec un params l'intitule et l'id														 
								 ListeZones zones2  = new ListeZones(zonesecondaire); 
								 
								 liste.add(zones2);		// On ajoute notre zone secondaire a la liste.						 
							 }
						
					}
					
					
					
				}
				
				
				
			}
		}
		
		
	}

	return liste; // On renvoie les zones se chalandises (+ zones secondaire s'il y a lieu ) ou les zones secondaires associés à l'id de société recherché.
		
		
}



public ArrayList<ZoneChalandise> listerZoneChalandisePourvues() throws Exception {
	
	ArrayList<ZoneChalandise> liste = new ArrayList<>();
	Connection cnx = null;			
	cnx = Connexion.getDbCon().getCnx();	
	ResultSet results = null;
	
	
	String select = "select id , intitule from zone_chalandise where etat = 1 order by intitule asc"; // Initialise la requète
	// System.out.println(select);
	 
	 try {
		// results = mysqlConnect.query(select); // On essaie d'envoyer la requète a la BDD
			PreparedStatement prep1 = cnx.prepareStatement(select);
			System.out.println("Prep1 : "+prep1);
			System.out.println("select : "+select);
			results = prep1.executeQuery();
	 }catch(Exception e) {
		 e.printStackTrace(); // Sinon on affiche l'erreur
	 }
	 
	 while (results.next()) { // Tant qu'on trouve une réponse
		
		 int Id = results.getInt("id");
		 String intitule = results.getString("intitule"); // On récupère la zone de chalandise renvoyée par la BDD, dans une variable
		 ZoneChalandise zonechalandise = new ZoneChalandise(intitule, Id); // On créé notre objet nouvelle zone secondaire avec un CP, une ville et une zone secondaire
		 
		 liste.add(zonechalandise); // on ajoute notre objet a la liste
		 
	 }
	 
	 
	 return liste; // on renvoie la liste de zone(s) secondaire.

	
}

public ArrayList<ZoneChalandise> listerZoneChalandiseNonPourvues() throws Exception {
	
	ArrayList<ZoneChalandise> liste = new ArrayList<>();
	Connection cnx = null;			
	cnx = Connexion.getDbCon().getCnx();	
	ResultSet results = null;
	
	
	String select = "select id , intitule from zone_chalandise where etat = 0 order by intitule asc"; // Initialise la requète
	// System.out.println(select);
	 
	 try {
		// results = mysqlConnect.query(select); // On essaie d'envoyer la requète a la BDD
			PreparedStatement prep1 = cnx.prepareStatement(select);
			System.out.println("Prep1 : "+prep1);
			System.out.println("select : "+select);
			results = prep1.executeQuery();
	 }catch(Exception e) {
		 e.printStackTrace(); // Sinon on affiche l'erreur
	 }
	 
	 while (results.next()) { // Tant qu'on trouve une réponse
		
		 int Id = results.getInt("id");
		 String intitule = results.getString("intitule"); // On récupère la zone de chalandise renvoyée par la BDD, dans une variable
		 ZoneChalandise zonechalandise = new ZoneChalandise(intitule, Id); // On créé notre objet nouvelle zone secondaire avec un CP, une ville et une zone secondaire
		 
		 liste.add(zonechalandise); // on ajoute notre objet a la liste
		 
	 }
	 
	 
	 return liste; // on renvoie la liste de zone(s) secondaire.

	
}










}