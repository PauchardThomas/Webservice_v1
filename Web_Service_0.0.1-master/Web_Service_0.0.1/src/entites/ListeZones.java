package entites;

public class ListeZones {

	private ZoneChalandise zonechalandise;
	private ZoneSecondaire zonesecondaire;
	
	public ListeZones(ZoneSecondaire zonesecondaire) {
		super();
		this.zonesecondaire = zonesecondaire;
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
	public ListeZones(ZoneChalandise zonechalandise,
			ZoneSecondaire zonesecondaire) {
		super();
		this.zonechalandise = zonechalandise;
		this.zonesecondaire = zonesecondaire;
	}
	
	
	
	
}
