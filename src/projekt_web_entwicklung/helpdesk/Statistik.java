package projekt_web_entwicklung.helpdesk;

import static java.lang.System.out;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class Statistik implements Serializable {
	private static final long serialVersionUID = 1L;
	DbStatment statment = new DbStatment();

	private int offen;
	private int warten;
	private int geschlossen;
	private int gesamt;
	
	public Statistik() {
		System.out.println("Die Bean: Statistik ist gesartet.");
		getDaten();
	}
	
	 public void preRenderAction()  { 
		 System.out.println( "Ticket.preRenderAction"  ); 
	 } 
	
	
	public void getDaten() {
		System.out.println("Start Methode getDaten()");
		
		List<Integer> data = new ArrayList<Integer>();
		
		try {
			statment.connect();
			data = statment.select_count_Ticket();
			statment.disconnect();
			
			if (data != null && data.size() != 0 ) {
				this.gesamt = data.get(0);
				this.offen = data.get(1);
				this.warten = data.get(2);
				this.geschlossen = data.get(3);
			}

		}catch(Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Aufbau", ex.getLocalizedMessage()));
			out.println("Error:  " + ex);
			ex.printStackTrace();
		}
	}
	public int getOffen() {
		return offen;
	}


	public void setOffen(int offen) {
		this.offen = offen;
	}


	public int getWarten() {
		return warten;
	}


	public void setWarten(int warten) {
		this.warten = warten;
	}


	public int getGeschlossen() {
		return geschlossen;
	}


	public void setGeschlossen(int geschlossen) {
		this.geschlossen = geschlossen;
	}


	public int getGesamt() {
		return gesamt;
	}


	public void setGesamt(int gesamt) {
		this.gesamt = gesamt;
	}


	
}
