package entites;

import java.util.ArrayList;

public class Ville {
	
	private String nom;
	private ZoneChalandise zonechalandise;
	private ZoneSecondaire zonesecondaire;
	private ArrayList<Service> service;
	
	
	
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
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
	public Ville(String nom, ZoneChalandise zonechalandise,
			ZoneSecondaire zonesecondaire, ArrayList<Service> service) {
		super();
		this.nom = nom;
		this.zonechalandise = zonechalandise;
		this.zonesecondaire = zonesecondaire;
		this.service = service;
	}


}
