package de.unserewebseite.model;

public class Gaeste {

	private String vorname, nachname, email;

	public Gaeste(String vorname, String nachname, String email) {
		super();
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
