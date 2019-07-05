package projekt_web_entwicklung.helpdesk;

import java.util.List;

import javax.annotation.PostConstruct;

public class Suche {
	private DbStatment statment = new DbStatment();
	private List<Ticket> tickets;
	
	//public Bearbeiter() {
	//		System.out.println("Anlegen von Bearbeiter");
			
	//}
	
	@PostConstruct
    public void init() {
    	statment.connect();
		tickets = statment.select_all_ticket(3); 
		statment.disconnect();;
    }
     
    public List<Ticket> getAll() {
        return tickets;
    }
}
