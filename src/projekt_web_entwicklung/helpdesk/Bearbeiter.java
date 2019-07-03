package projekt_web_entwicklung.helpdesk;

import static java.lang.System.out;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import projekt_web_entwicklung.helpdesk.Util;

@Named
@SessionScoped
public class Bearbeiter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DbStatment statment = new DbStatment();
	
	private List<Ticket> alTicket = new ArrayList<Ticket>();
	private TicketDataModel tdm = null;
	private Util util = new Util();
	private Helpdesk hd = (Helpdesk) util.getBean("helpdesk");
	
	public Bearbeiter() {
			System.out.println("Anlegen der von Bearbeiter");
	}
	
	
	public TicketDataModel getAll() {
		
		System.out.println("Bearbeiter.getAll()");
		try{
			statment.connect();
			alTicket = statment.select_all_ticket(hd.getUserID()); //
			statment.disconnect();
			
			int groesse =  alTicket.size();
			for(int n=0;n <= alTicket.size()-1;n++) {
				alTicket.get(n).toString();
			}
			System.out.println("es gibt folgende Tickets" + groesse);
			System.out.println("Ticket Nr: hat folgende Nummer:"+ alTicket.get(0).gettNr());
			tdm = new TicketDataModel(alTicket);
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
