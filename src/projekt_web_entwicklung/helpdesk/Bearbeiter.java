package projekt_web_entwicklung.helpdesk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.ListDataModel;

@Named("Helpdesk")
@SessionScoped
public class Bearbeiter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DbStatment statment = new DbStatment();
	private Helpdesk helpdesk = new Helpdesk();

	TicketDataModel tdm = null;
	
	public Bearbeiter() {
		ArrayList<Ticket> alTicket = new ArrayList<Ticket>();
		statment.connect();
		alTicket = statment.select__all_ticket(helpdesk.getUserID());
		/*List<SelectItem> anfrage = new ArrayList<SelectItem>(statment.select_Hilfstabellen("anfrage"));
		List<SelectItem> kategorie = new ArrayList<SelectItem>(statment.select_Hilfstabellen("kategorie"));
		List<SelectItem> status = new ArrayList<SelectItem>(statment.select_Hilfstabellen("status"));*/
		statment.disconnect();
			TicketDataModel tdm = new TicketDataModel(alTicket);
			
	}
	
	
	public TicketDataModel getTdm() {
		return tdm;
	}
	
	public void selectedTicket() {
		// hier kommt die weiterleitung rein
	}
	
	
}
