package projekt_web_entwicklung.helpdesk;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.model.SelectItem;

import projekt_web_entwicklung.helpdesk.DbStatment;

@Named
@SessionScoped
public class Ticket implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private  DbStatment  statment  =  new  DbStatment();
	
	private int tNr;
	private int statusID;
	private int anfrageID;
	private int rechnerID;
	private int kategorieID;
	private int userID;
	private String userString;
	private String anfrageString;
	private String statusString;
	private String kategorieString;
	private String anzeige;
	private String grund; 
	private String bemerkung;
	private Date startdate;
	private Date enddate;
	private boolean speichernI = true;
	private boolean speichernU = false;
	
	private List<SelectItem> anfrage = new ArrayList<SelectItem>();
	private List<SelectItem> kategorie = new ArrayList<SelectItem>();
	private List<SelectItem> status = new ArrayList<SelectItem>();
	private List<SelectItem> user = new ArrayList<SelectItem>();
	
	public Ticket() {
        System.out.println( "Ticket wird erstellt");
        System.out.println( (new Date()).toString() ); 
        
        if (tNr == 0) setAnzeige("Neues Ticket");
         	
        	/*
        	 * FacesContext.getCurrentInstance().addMessage( null,
        	 * 		new FacesMessage(FacesMessage.SEVERITY_INFO,"Paramter","Der Paramter " + tNr + " wurde übergeben"));
        	 */			 
        
        statment.connect();
        //Hilfstabellen erstellen
        anfrage = statment.select_Hilfstabellen("anfrage");
        kategorie = statment.select_Hilfstabellen("kategorie");
        status = statment.select_Hilfstabellen("status");
        user = statment.select_Hilfstabellen("user");
        statment.disconnect();
	}
	
	public String toPopup() {
		return "helpdesk";
	}
	@Override
	public String toString() {
		return tNr + ", "+ statusID
				+ ", "+ anfrageID
				+ ", "+ rechnerID
				+ ", "+ kategorieID
				+ ", "+ userID
				+ ", "+ grund
				+ ", "+ bemerkung;
				
	}
	
	 public void preRenderAction()  { 
		 System.out.println( "Ticket.preRenderAction"  ); 
	 } 
	
	 public void cbxChangeListener( ValueChangeEvent vce ) {
		    System.out.println( "cbxChangeListener: " + vce.getNewValue() );    
		  }
	
	 public void back(ActionEvent ae) {
		 settNr(0);
	 }
	 
	 
	 
	public void insertTicket(ActionEvent ae) throws ParseException {
		
		//Startdatum wird automatisch ermittelt und in das richtige Format konvertiert
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String startString = format.format( new Date() );
		
		// übermittle die Angaben aus dem Objekt an die Statmentklasse
		try{
			statment.connect();
			boolean st = statment.insert_ticket(tNr, statusID, anfrageID, rechnerID, kategorieID, userID, grund, bemerkung,startString);
			if(st) {
				FacesContext.getCurrentInstance().addMessage( null,
			   			 new FacesMessage(FacesMessage.SEVERITY_INFO,"O. K.","Die Daten werden übernommen"));
			}
			statment.disconnect();
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.",e.getLocalizedMessage()));
					 e.printStackTrace();			 
		}	 
    }
    
    public void updateTicket() {
    	/* hier erfolgt der Tranfer der Daten wieder zurück in die Datenbank
    	 *
    	 */
    	String endString;
    	
    	if(statusID == 3) {
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
    		endString = format.format( new Date() );
    	}else {
    		endString = null;
    	}
    	try {
    		statment.connect();
    		statment.update_ticket(tNr, statusID, anfrageID, rechnerID, kategorieID, userID, grund, bemerkung,endString);
    		statment.disconnect();
    	}catch(Exception e) {
    		FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.",e.getLocalizedMessage()));
					 e.printStackTrace();
    	}
    	settNr(0);
    }
    
    public void setParamter(String tNrString) {
    	System.out.println("Start Methode parmeterAnnahme()");
    	
    	List<String> data = new ArrayList<String>();
   	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   	 	
   	 	int vt = Integer.parseInt(tNrString);
         
         try {
        	 statment.connect();
             data = statment.select_one_ticket(vt);
             statment.disconnect();
             
             if(data == null) {
            	 FacesContext.getCurrentInstance().addMessage( null,
    					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Zu diesem Ticket exestieren keine Daten"));
    				
             }
             
            // if(!data.get(9).equals("null")) {
            //	 System.out.println("Der Wert ist: "+ data.get(9));
            // }
             
             this.tNr = Integer.parseInt(data.get(0));
             this.userID = Integer.parseInt(data.get(1));
             this.rechnerID = Integer.parseInt(data.get(2));
             this.anfrageID = Integer.parseInt(data.get(3));
             this.statusID = Integer.parseInt(data.get(4));
             this.kategorieID = Integer.parseInt(data.get(5));
             this.grund = data.get(6);
             this.bemerkung = data.get(7);
             this.startdate = sdf.parse(data.get(8));
             if(!data.get(9).equals("null")) this.enddate = sdf.parse(data.get(9)); 
             setAnzeige("Ticket Nr: "+ tNr);  
             setSpeichernU(true);
             setSpeichernI(false);
         }catch (Exception e) {
        	 FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"setParamter: ",e.getLocalizedMessage()));
					 e.printStackTrace();
         }
    }

    
    public void auswahl_Rechner(ActionEvent ae) {
    	/*hier muss die Weiterleitung zur Seite Rechner erfolgen 
    	 * außerdem ein Event um den Rechner zu übernehmen.
    	 */
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
	
	public int getRechnerID() {
		return rechnerID;
	}
	
	public void setRechnerID(int rechnerID) {
		this.rechnerID = rechnerID;
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
	public String getAnzeige() {
		return anzeige;
	}
	public void setAnzeige(String anzeige) {
		this.anzeige = anzeige;
	}
	public String getUserString() {
		return userString;
	}
	public void setUserString(String userString) {
		this.userString = userString;
	}
	public String getAnfrageString() {
		return anfrageString;
	}
	public void setAnfrageString(String anfrageString) {
		this.anfrageString = anfrageString;
	}
	public String getStatusString() {
		return statusString;
	}
	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}
	public String getKategorieString() {
		return kategorieString;
	}
	public void setKategorieString(String kategorieString) {
		this.kategorieString = kategorieString;
	}
	
	public boolean isSpeichernU() {
		return speichernU;
	}
	public void setSpeichernU(boolean speichernU) {
		this.speichernU = speichernU;
	}
	public boolean isSpeichernI() {
		return speichernI;
	}
	public void setSpeichernI(boolean speichernI) {
		this.speichernI = speichernI;
	}
}
