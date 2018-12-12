package isebase.cognito.tourpilot_apk.Data.AdditionalEmployment;

public class AdditionalEmployment {
	
	private int id;
	private String name;

	public int getID() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public AdditionalEmployment(String strElement) {
		this.id = Integer.parseInt(strElement.split("@")[1]);
		this.name = strElement.split("@")[0];
	}
	
	public AdditionalEmployment(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
