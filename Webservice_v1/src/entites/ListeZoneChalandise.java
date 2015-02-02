package entites;

import java.util.ArrayList;

public class ListeZoneChalandise {
	
	private int Id;
	private String CP;
	private String Ville;
	private String ZoneChalandise;
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
	public String getZoneChalandise() {
		return ZoneChalandise;
	}
	public void setZoneChalandise(String zoneChalandise) {
		ZoneChalandise = zoneChalandise;
	}
	public ArrayList<Service> getService() {
		return service;
	}
	public void setService(ArrayList<Service> service) {
		this.service = service;
	}
	public ListeZoneChalandise(int id, String cP, String ville,
			String zoneChalandise, ArrayList<Service> service) {
		super();
		Id = id;
		CP = cP;
		Ville = ville;
		ZoneChalandise = zoneChalandise;
		this.service = service;
	}


	

	
	

}
