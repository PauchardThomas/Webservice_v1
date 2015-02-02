package entites;

import java.util.ArrayList;

public class CodePostaux {
	
	private String cp;
	private String ville;
	private ZoneChalandise zonechalandise;
	private ZoneSecondaire zonesecondaire;
	private int id;
	private ArrayList<Service> service;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public ZoneChalandise getZonechalandise() {
		return zonechalandise;
	}
	public void setZonechalandise(ZoneChalandise zonechalandise) {
		this.zonechalandise = zonechalandise;
	}
	public ZoneSecondaire getZonesecondaire() {
		return zonesecondaire;
	}
	public void setZonesecondaire(ZoneSecondaire zonesecondaire) {
		this.zonesecondaire = zonesecondaire;
	}
	public ArrayList<Service> getService() {
		return service;
	}
	public void setService(ArrayList<Service> service) {
		this.service = service;
	}
	public CodePostaux(String cp, String ville, ZoneChalandise zonechalandise,
			ZoneSecondaire zonesecondaire, int id, ArrayList<Service> service) {
		super();
		this.cp = cp;
		this.ville = ville;
		this.zonechalandise = zonechalandise;
		this.zonesecondaire = zonesecondaire;
		this.id = id;
		this.service = service;
	}

	

	

	




	
	

}