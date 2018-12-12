package isebase.cognito.tourpilot_apk.Data.AdditionalTask;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;


public class Catalog {
	
	public enum eCatalogType{
	  btyp_kk, btyp_pk,  btyp_sa, btyp_pr; 
	}
	
	private eCatalogType catalogType;
		
	public eCatalogType getCatalogType() {
		return catalogType;
	}

	public void setCatalogType(eCatalogType catalogType) {
		this.catalogType = catalogType;
	}

	public String getName(){
		return getCatalogName(catalogType);
	}
	
	public Catalog(eCatalogType catalogType){
		setCatalogType(catalogType);
	}	
	
	public static String getCatalogName(eCatalogType catalogType){
		switch (catalogType) {
			case btyp_kk:
				return StaticResources.getBaseContext().getString(R.string.btyp_kk);
			case btyp_pk:
				return StaticResources.getBaseContext().getString(R.string.btyp_pk);
			case btyp_sa:
				return StaticResources.getBaseContext().getString(R.string.btyp_sa);
			case btyp_pr:
				return StaticResources.getBaseContext().getString(R.string.btyp_pr);
			default:
				return "";
		}				
	}	
	
	@Override
	public String toString(){
		return getName();
	}
	
}
