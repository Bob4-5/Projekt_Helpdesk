package projekt_web_entwicklung.helpdesk;

import java.util.ArrayList;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class TicketDataModel extends ListDataModel<Ticket> implements SelectableDataModel<Ticket> {
	  
	public TicketDataModel(ArrayList<Ticket> alTicket) {
		 super(alTicket);
	}

	@Override
	public Ticket getRowData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRowKey(Ticket arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
