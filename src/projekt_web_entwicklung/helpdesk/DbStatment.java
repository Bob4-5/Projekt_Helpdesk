package projekt_web_entwicklung.helpdesk;

import static java.lang.System.out;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import projekt_web_entwicklung.helpdesk.Util;

@Named("DbStatment")
@SessionScoped
public class DbStatment implements  Serializable {
	private  static  final  long  serialVersionUID  =  1L;
	
	/*  Util  ist  eine  Hilfsklasse,  die  u.  a.  den  Verbindungsaufbau  zur  Datenbank
	*  vereinfacht:  */
	
	private  Util  util  =  new  Util();

	private  Connection  con  =  null;
	private  Statement  stm  =  null;
	private  ResultSet  rs  =  null;
	
	/**
	*  Datensatz  einfügen
	*  @param  ae  ActionEvent 
*/
	public  void  insert_ticket( int tNr, int status, int anfrage, int rechner, int kategorie, int user, String grund, String bemerkung )  {

		try {
			//if(  ps  ==  null  ){
			String  sQl  =  "INSERT  INTO  ticket(  "  +
			"TicketNr,PersNr_FK, Rechner_FK, Status_FK, Bemerkung, Kategorie, Problem, Anfrage)  "  +
			"VALUES  (  ?,  ?,  ?,  ?,  ?, ?, ?, ? )";
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
