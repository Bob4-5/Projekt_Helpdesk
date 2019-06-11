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

@Named("Helpdesk")
@SessionScoped
public class Helpdesk implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private  DbStatment  statment  =  new  DbStatment();
	
	private int tNr;
	private int status;
	private int anfrage;
	private int rechner;
	private int kategorie;
	private int user;
	private String grund; 
	private String bemerkung;
	private Date startdate;
	private Date enddate;
	private List<SelectItem> options = new ArrayList<SelectItem>();
	
	public Helpdesk() {
        System.out.println( "MyBean.<init>..."  );
        System.out.println( (new Date()).toString() ); 
        options = statment.select_Anfrage();
    }
	
	public List getOptions() {
		return options;
	}
	
    public int gettNr() {
		return tNr;
	}
    
	public void settNr(int tNr) {
		this.tNr = tNr;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getAnfrage() {
		return anfrage;
	}
	
	public void setAnfrage(int anfrage) {
		this.anfrage = anfrage;
	}
	
	public int getRechner() {
		return rechner;
	}
	
	public void setRechner(int rechner) {
		this.rechner = rechner;
	}
	
	public int getKategorie() {
		return kategorie;
	}
	
	public void setKategorie(int kategorie) {
		this.kategorie = kategorie;
	}
	
	public int getUser() {
		return user;
	}
	
	public void setUser(int user) {
		this.user = user;
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
			statment.insert_ticket(tNr, status, anfrage, rechner, kategorie, user, grund, bemerkung, startdate, enddate);
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Die Es wurde die Methode abgebrochen."));
		}
		FacesContext.getCurrentInstance().addMessage( null,
   			 new FacesMessage(FacesMessage.SEVERITY_INFO,"O. K.","Die Daten werden übernommen"));
    }
    
    public void updateTicket(ActionEvent ae) {
    	FacesContext.getCurrentInstance().addMessage( null,
   			 new FacesMessage(FacesMessage.SEVERITY_INFO,"O. K.","Die Daten werden gupdatet"));
    }
}
