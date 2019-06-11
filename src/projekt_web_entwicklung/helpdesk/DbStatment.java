package projekt_web_entwicklung.helpdesk;

import static java.lang.System.out;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import projekt_web_entwicklung.helpdesk.Util;

@Named("DbStatment")
@SessionScoped
public class DbStatment implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * Util ist eine Hilfsklasse, die u. a. den Verbindungsaufbau zur Datenbank
	 * vereinfacht:
	 */

	private Util util = new Util();

	private Connection con = null;
	private Statement stm = null;
	private ResultSet rs = null;
	private  boolean  connected  =  false;
	

public  void  connect()  { 
		
		//out.println(  "connect()..."  ); 
		
		if(  util  !=  null  )  con  =  util.getCon();
		if(  con  !=  null  ) connected  =  true;
		else  {
			FacesContext.getCurrentInstance().addMessage(  null,  new  FacesMessage(
					FacesMessage.SEVERITY_ERROR,  "Exception",
					"Keine  Verbindung  zur  Datenbank  (Treiber  nicht  gefunden?)"  ));
			out.println(  "Keine  Verbingung  zur  Datenbank"  );
		}
	} 

	public  void  disconnect()  { 
		
		if(  con  !=  null  )  {
			try {
				if(  rs	!=  null  )  rs.close();
				if(  stm	!=  null  )  stm.close(); 
		
				util.closeConnection(  con  );
				connected  =  false;
			}  catch(  Exception  ex  )  {
				FacesContext.getCurrentInstance().addMessage(  null,  new  FacesMessage(
				FacesMessage.SEVERITY_ERROR,  "Exception",  ex.getLocalizedMessage())
				);
				out.println(  "Error:  "  +  ex  );
				ex.printStackTrace();
			}
		}
	}
	/*
	 * Lese aus der Datenbank die Tabelle Anfrage
	 */
	public  List<SelectItem> select_Anfrage () {
		final  String  SQL_SELECT  =  "select  mAnfrageNr" + 
											  "Name" + 
											  "Beschreibung  "  +
											  "from  anfrage";
		
		List<SelectItem> z = new ArrayList<SelectItem>();
		
		try {
			stm  =  con.createStatement(  ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE  );
			rs  =  stm.executeQuery(  SQL_SELECT  );
			
			while(rs.next()) {
				z.add( new SelectItem( rs.getString(1), rs.getString(2),rs.getString(3)));
			}
			return z;
			
		}catch(  Exception  ex  )  {
			FacesContext.getCurrentInstance().addMessage(  null,  new  FacesMessage(
					FacesMessage.SEVERITY_ERROR,  "SQLException",  ex.getLocalizedMessage())
					);
			out.println(  "Error:  "  +  ex  );
			ex.printStackTrace();
		}
		return null;
	}
	
	/* 
	 * Ticketdatensatz normalisiert einfügen
	 */
	public  void  insert_ticket( int tNr, int status, int anfrage, int rechner, int kategorie, int user, String grund, String bemerkung, Date startDate,Date endDate )  {

		if (connected) {
			try {
				//if(  ps  ==  null  ){
				String  sQl  =  "INSERT  INTO  ticket(  "  +
				"TicketNr,PersNr_FK, Rechner_FK, Status_FK, Bemerkung, Kategorie, Problem, Anfrage,StartDate, EndDate)  "  +
				"VALUES  (  ?,  ?,  ?,  ?,  ?, ?, ?, ?, ?, ? )";
					PreparedStatement  ps  =  con.prepareStatement(  sQl  );
				//}
			
				ps.setInt		(1,  tNr);
				ps.setInt  		(2,  status);
				ps.setInt  		(3,  anfrage);
				ps.setInt		(4,  rechner);
				ps.setInt 		(5,  kategorie); 
				ps.setInt  	  	(6,  user);
				ps.setString  	(7,  grund);
				ps.setString  	(8,  bemerkung);
				ps.setDate		(9, (java.sql.Date) startDate);
				ps.setDate		(10, (java.sql.Date) endDate);
				
			
				int  n  =  ps.executeUpdate(); 
				if( n == 1 ) {
					out.println(  "O.K.,	Datensatz  eingefügt.");
					FacesContext.getCurrentInstance().addMessage(  null,  new  FacesMessage(
					FacesMessage.SEVERITY_INFO,  "O.  K.",
							"Ein  Datensatz  erfolgreich  eingefügt."  )
							);
				}
				ps.close();
		
			}catch(  SQLException  ex  )  {
				FacesContext.getCurrentInstance().addMessage(  null,  new  FacesMessage(
						FacesMessage.SEVERITY_ERROR,  "SQLException",  ex.getLocalizedMessage())
						);
				out.println(  "Error:  "  +  ex  );
				ex.printStackTrace();
			}
		}
	}
}
