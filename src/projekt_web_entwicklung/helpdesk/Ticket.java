package projekt_web_entwicklung.helpdesk;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.model.SelectItem;

import projekt_web_entwicklung.helpdesk.DbStatment;

@Named("Ticket")
@SessionScoped
public class Ticket implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private  DbStatment  statment  =  new  DbStatment();
	
	private int tNr;
	private int statusID;
	private int anfrageID;
	private int rechner;
	private int kategorieID;
	private int userID;
	private String grund; 
	private String bemerkung;
	private Date startdate;
	private Date enddate;
	
	private List<SelectItem> anfrage = new ArrayList<SelectItem>();
	private List<SelectItem> kategorie = new ArrayList<SelectItem>();
	private List<SelectItem> status = new ArrayList<SelectItem>();
	private List<SelectItem> user = new ArrayList<SelectItem>();
	
	public Ticket() {
        System.out.println( "MyBean.<init>..."  );
        System.out.println( (new Date()).toString() ); 
        statment.connect();
        anfrage = statment.select_Hilfstabellen("anfrage");
        kategorie = statment.select_Hilfstabellen("kategorie");
        status = statment.select_Hilfstabellen("status");
        user = statment.select_Hilfstabellen("user");
        statment.disconnect();
	}

	public List<SelectItem> getStatus() {
		return status;
	}
	
	public List<SelectItem> getUser() {
		return user;
	}
	
	public List<SelectItem> getAnfrage() {
		return anfrage;
	}
	
	public List<SelectItem> getKategorie() {
		return kategorie;
	}
	
    public int gettNr() {
		return tNr;
	}
    
	public void settNr(int tNr) {
		this.tNr = tNr;
	}
	
	public int getStatusID() {
		return statusID;
	}
	
	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}
	
	public int getAnfrageID() {
		return anfrageID;
	}
	
	public void setAnfrageID(int anfrageID) {
		this.anfrageID = anfrageID;
	}
	
	public int getRechner() {
		return rechner;
	}
	
	public void setRechner(int rechner) {
		this.rechner = rechner;
	}
	
	public int getKategorieID() {
		return kategorieID;
	}
	
	public void setKategorieID(int kategorieID) {
		this.kategorieID = kategorieID;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public String getGrund() {
		return grund;
	}
	
	public void setGrund(String grund) {
		this.grund = grund;
	}
	
	public String getBemerkung() {
		return bemerkung;
	}
	
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	
	public Date getStartDate() {
		return startdate;
	}
    
	public void setStartDate(Date startdate) {
		this.startdate = startdate;
	}
	
	public Date getEndDate() {
		return enddate;
	}
    
	public void setEndDate(Date enddate) {
		this.enddate = enddate;
	}
	
	public void insertTicket(ActionEvent ae) {
		try{
			statment.insert_ticket(tNr, statusID, anfrageID, rechner, kategorieID, userID, grund, bemerkung, startdate, enddate);
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Die Es wurde die Methode abgebrochen."));
		}
		FacesContext.getCurrentInstance().addMessage( null,
   			 new FacesMessage(FacesMessage.SEVERITY_INFO,"O. K.","Die Daten werden �bernommen"));
    }
    
    public void updateTicket(ActionEvent ae) {
    	/* hier erfolgt der Tranfer der Daten wieder zur�ck in die Datenbank
    	 * au�erdem erfolgt die Weiterleitung zur Startseite wieder
    	 */
    	FacesContext.getCurrentInstance().addMessage( null,
   			 new FacesMessage(FacesMessage.SEVERITY_INFO,"O. K.","Die Daten werden gupdatet"));
    }
    
    public void auswahl_Rechner(ActionEvent ae) {
    	/*hier muss die Weiterleitung zur Seite Rechner erfolgen 
    	 * au�erdem ein Event um den Rechner zu �bernehmen.
    	 */
    }
}