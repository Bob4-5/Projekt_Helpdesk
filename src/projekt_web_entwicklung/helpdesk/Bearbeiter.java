package projekt_web_entwicklung.helpdesk;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import projekt_web_entwicklung.helpdesk.Util;

@Named
@SessionScoped
public class Bearbeiter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DbStatment statment = new DbStatment();
	
	//private List<Ticket> alTicket = new ArrayList<Ticket>();
	//private TicketDataModel tdm = null;
	private Util util = new Util();
	private Helpdesk hd = (Helpdesk) util.getBean("helpdesk");
	private List<Ticket> tickets;
	
	//public Bearbeiter() {
	//		System.out.println("Anlegen von Bearbeiter");
			
	//}
	
	@PostConstruct
    public void init() {
    	statment.connect();
		tickets = statment.select_all_ticket(hd.getUserID()); //
		statment.disconnect();;
    }
     
    public List<Ticket> getAll() {
        return tickets;
    }
	
	/*
	public TicketDataModel getAll() {
		
		System.out.println("Bearbeiter.getAll()");
			
		alTicket.clear();
			
			statment.connect();
			alTicket = statment.select_all_ticket(hd.getUserID()); //
			statment.disconnect();
			
			System.out.println("Die Liste hat "+ alTicket.size()+" Elemente");
			for (int n=0; n<= alTicket.size()-1;n++) {
				System.out.println("Das Ticket hat folgende Eigenschaften: "+ alTicket.get(n).toString());
			}
			tdm = new TicketDataModel(alTicket);
			System.out.println("Im Modell sind folgende Daten " + tdm.getRowCount());
			//if(alTicket == null) return null;
			return tdm;
	}
	
	public void selectedTicket() {
		// hier kommt die weiterleitung rein
	}
	*/
	
}
