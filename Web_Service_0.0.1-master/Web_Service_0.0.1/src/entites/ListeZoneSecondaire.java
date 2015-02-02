package entites;

import java.util.ArrayList;

public class ListeZoneSecondaire {

	private int Id;
	private String CP;
	private String Ville;
	private String ZoneSecondaire;
	private ArrayList<Service> service;
	
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getCP() {
		return CP;
	}
	public void setCP(String cP) {
		CP = cP;
	}
	public String getVille() {
		return Ville;
	}
	public void setVille(String ville) {
		Ville = ville;
	}
	public String getZoneSecondaire() {
		return ZoneSecondaire;
	}
	public void setZoneSecondaire(String zoneSecondaire) {
		ZoneSecondaire = zoneSecondaire;
	}
	public ArrayList<Service> getService() {
		return service;
	}
	public void setService(ArrayList<Service> service) {
		this.service = service;
	}
	public ListeZoneSecondaire(int id, String cP, String ville,
			String zoneSecondaire, ArrayList<Service> service) {
		super();
		Id = id;
		CP = cP;
		Ville = ville;
		ZoneSecondaire = zoneSecondaire;
		this.service = service;
	}

	


	
	
	
	
}
