package projekt_web_entwicklung.helpdesk;

import static java.lang.System.out;

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class Bearbeiter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DbStatment statment = new DbStatment();
	
	public Bearbeiter() {
			System.out.println("Anlegen der von Bearbeiter");
	}
	
	
	public TicketDataModel getAll() {
		ArrayList<Ticket> alTicket = new ArrayList<Ticket>();
		System.out.println("Bearbeiter.getAll()");
		try{
			statment.connect();
			alTicket = statment.select_all_ticket(1); //helpdesk.getUserID()
			statment.disconnect();
			
			int groesse =  alTicket.size();
			System.out.println("es gibt folgende Tickets" + groesse);
			
			FacesContext.getCurrentInstance().addMessage( null,
		   			 new FacesMessage(FacesMessage.SEVERITY_INFO,"O. K.", ""+ groesse));
			
				TicketDataModel tdm = new TicketDataModel(alTicket);
				return tdm;
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exception", e.getLocalizedMessage()));
			out.println("Error:  " + e);
			e.printStackTrace();
		}
		
		return null;
		
	
	}
	
	public void selectedTicket() {
		// hier kommt die weiterleitung rein
	}
	
	
}
