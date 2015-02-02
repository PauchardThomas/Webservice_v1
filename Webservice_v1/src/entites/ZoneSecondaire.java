package entites;

public class ZoneSecondaire {

	String intitule;
	int id;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public ZoneSecondaire(String intitule, int id) {
		super();
		this.intitule = intitule;
		this.id = id;
	}
	
	
	
}
