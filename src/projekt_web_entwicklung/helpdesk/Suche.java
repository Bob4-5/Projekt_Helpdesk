package projekt_web_entwicklung.helpdesk;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

@Named
@SessionScoped
public class Suche implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private DbStatment statment = new DbStatment();
	private List<Ticket> tickets;
	


	
	private int	   rechnernummer;
	private int tNr;
	private Date zeitraumVon;
	private Date zeitraumBis;
	private String kategorie ="";
	private String status ="";
	private String user ="";
	private String grund ="";
	
	
	
	
	
	public Suche() {
			System.out.println("Anlegen von Bearbeiter");
			
	}
	// Liste erstellen
	@PostConstruct
    public void init() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String where ="";
		if (zeitraumBis == null) zeitraumBis= (Date) format.parse("2099-01-30");
		if (zeitraumVon == null) zeitraumVon= (Date) format.parse("1999-01-30");
		
		
		if(!user.equals("")) {
			where = " and bearbeiter.name = '" + user + "'";
		}else if(!status.equals("")) {
			where = where + " and status.name= '" + status + "'";
		}else if(rechnernummer != 0) {
			where = where +" and rechner.inventarnummer =  "+ rechnernummer;
		}else if(!kategorie.equals("")) {
			where = where +" and kategorie.name =  '"+ kategorie +"'" ;
		}else if(!grund.equals("")) {
			where = where +" and ticket.grund =  '%"+ grund +"%' ";
		}else if(zeitraumVon != null && zeitraumBis != null) {
			where = where +" and  StartEnddate BETWEEN '"+ zeitraumVon + "' and " + zeitraumBis +"' ";
		}
		
		statment.connect();
		tickets = statment.suche_all_ticket(where); 
		statment.disconnect();;
    }
     
    public List<Ticket> getAll() {
        return tickets;
    }
    // Ende Liste erstellen
    
    //Beginn klick envent
    public void selectTicket(ActionEvent ae) throws ParseException {
    	init();
    }

	public DbStatment getStatment() {
		return statment;
	}

	public void setStatment(DbStatment statment) {
		this.statment = statment;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public String getGrund() {
		return grund;
	}

	public void setGrund(String grund) {
		this.grund = grund;
	}

	public int getRechnernummer() {
		return rechnernummer;
	}

	public void setRechnernummer(int rechnernummer) {
		this.rechnernummer = rechnernummer;
	}

	public Date getZeitraumVon() {
		return zeitraumVon;
	}

	public void setZeitraumVon(Date zeitraumVon) {
		this.zeitraumVon = zeitraumVon;
	}

	public Date getZeitraumBis() {
		return zeitraumBis;
	}

	public void setZeitraumBis(Date zeitraumBis) {
		this.zeitraumBis = zeitraumBis;
	}
	public int gettNr() {
		return tNr;
	}
	public void settNr(int tNr) {
		this.tNr = tNr;
	}
    
    
}
