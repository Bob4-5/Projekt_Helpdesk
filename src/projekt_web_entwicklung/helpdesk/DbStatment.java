package projekt_web_entwicklung.helpdesk;

import static java.lang.System.out;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import projekt_web_entwicklung.helpdesk.Util;

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
	private boolean connected = false;

	/* Konstante Statments */

	final String SQL_anfrage = "select AnfrageNr,Name, Beschreibung from anfrage";
	final String SQL_kategorie = "select KatNr,Name, Beschreibung from kategorie";
	final String SQL_status = "select AStatusID, Name, Beschreibung from astatus";
	final String SQL_user = "select PersNr, Vorname, Nachname, Abteilung from bearbeiter";
	final String SQL_ticket = "Select TicketNr, PersNr_FK, Rechner_FK,  Anfrage,  Status_FK, Kategorie, Problem, Bemerkung, StartDate,EndDate from ticket";

	public void connect() {

		// out.println( "connect()..." );

		if (util != null)
			con = util.getCon();
		if (con != null)
			connected = true;
		else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Exception", "Keine  Verbindung  zur  Datenbank  (Treiber  nicht  gefunden?)"));
			out.println("Keine  Verbingung  zur  Datenbank");
		}
	}

	public void disconnect() {

		if (con != null) {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close(); 

				util.closeConnection(con);
				connected = false;
			} catch (Exception ex) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exception", ex.getLocalizedMessage()));
				out.println("Error:  " + ex);
				ex.printStackTrace();
			}
		}
	}
	// user wird ermittelt
	public List<String> selectUser(String username) {
		List<String> bkennung = new ArrayList<String>();
		String statment = "select persnr, username, passwort from bearbeiter where username = " + "'"+ username +"'";
		selectStatment(statment);
// !!! Daten noch anpassen!
		try {
			if( !connected) return null;
			if (!rs.next()) return null; 
			if (rs.first()) {
				if (rs.isLast()) {
					bkennung.add(rs.getString(1));// ID
					bkennung.add(rs.getNString(2));// Username
					bkennung.add(rs.getString(3));// Passwort
					return bkennung;	
				}	
			}
		} catch (SQLException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
			out.println("Error:  " + ex);
			ex.printStackTrace();
		}
		return null;
	}

	// allgemeines Select Statment. es bekommt über SQL_SELECT das ausführbare
	// Statment übermittelt
	private void selectStatment(String SQL_SELECT) {
		if (connected) {
			try {
				stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stm.executeQuery(SQL_SELECT);
			} catch (Exception ex) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
				out.println("Error:  " + ex);
				ex.printStackTrace();
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler.", "Keine Datenkverbindung vorhanden."));
		}
	}

	// Konfigurationsdaten der einzelenen Klassen abrufen
	public ArrayList<String> select_Rechner() {
		ArrayList<String> r = new ArrayList<String>();
		return r;
	}

	/*
	 * Hilfstabellen mit Daten füllen
	 */
	public List<SelectItem> select_Hilfstabellen(String tabelle) {
		List<SelectItem> z = new ArrayList<SelectItem>();

		try {
			switch (tabelle) {
			case "anfrage":
				selectStatment(SQL_anfrage);
				break;
			case "kategorie":
				selectStatment(SQL_kategorie);
				break;
			case "status":
				selectStatment(SQL_status);
				break;
			case "user":
				selectStatment(SQL_user);
				break;
			default:
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Fehler.", "Die Methode abgebrochen. Code: DbStatment"));
			}

			if (tabelle != "user") {
				if (rs != null) {
					while (rs.next()) {
						z.add(new SelectItem(rs.getString(1), rs.getString(2), rs.getString(3)));
					}
				} else {
					z.add(new SelectItem("null", "null", "null"));
					return z;
				}

			} else {
				if (rs != null) {
					while (rs.next()) {
						z.add(new SelectItem(rs.getString(1),
								rs.getString(2) + " " + rs.getString(3) + "-" + rs.getString(4),
								"Wer sollte das bearbeiten"));
					}
				} else {
					z.add(new SelectItem("null", "null", "null"));
					return z;
				}
			}
			rs.close();
			return z;

		} catch (SQLException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
			out.println("Error:  " + ex);
			ex.printStackTrace();
		}
		z.add(new SelectItem("null", "null", "null"));
		return z;
	}
	
	 
		public boolean insert_user( String vorname, String nachname, String abteilung, String user, String pwd ) {

			if (connected) {
				try {
					// if( ps == null ){
					String sQl = "INSERT  INTO  ticket(  Vorname, Nachname, Abteilung, Username, Passwort) "
							+ "VALUES  ( ?,  ?,  ?,  ?, ?)";
					PreparedStatement ps = con.prepareStatement(sQl);
					// }

					ps.setString(1, vorname);
					ps.setString(2, nachname);
					ps.setString(3, abteilung);
					ps.setString(4, user);
					ps.setString(5, pwd);
				
					int n = ps.executeUpdate();
					if (n == 1) {
						ps.close();
						return true;
					}

				} catch (SQLException ex) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
					out.println("Error:  " + ex);
					ex.printStackTrace();
				}
			}
			return false;
		}


	/*
	 * Ticketdatensatz normalisiert einfügen
	 */
	public boolean insert_ticket(int tNr, int status, int anfrage, int rechner, int kategorie, int user, String grund,
			String bemerkung, String startDate) {

		if (connected) {
			try {
				// if( ps == null ){
				String sQl = "INSERT  INTO  ticket(  "
						+ "PersNr_FK, Rechner_FK, Status_FK, Bemerkung, Kategorie, Problem, Anfrage,StartDate) "
						+ "VALUES  ( ?,  ?,  ?,  ?, ?, ?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(sQl);
				// }

				ps.setInt(1, user);
				ps.setInt(2, rechner);
				ps.setInt(3, status);
				ps.setString(4, bemerkung);
				ps.setInt(5, kategorie);
				ps.setString(6, grund);
				ps.setInt(7, anfrage);
				ps.setDate(8, Date.valueOf(startDate));

				int n = ps.executeUpdate();
				if (n == 1) {
					ps.close();
					return true;
				}

			} catch (SQLException ex) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
				out.println("Error:  " + ex);
				ex.printStackTrace();
			}
		}
		return false;
	}

	// Update des Ticketdatensatzes
	public void update_ticket(int tNr, int status, int anfrage, int rechner, int kategorie, int user, String grund,
			String bemerkung, String endString) {
		Date endDate = null;
		if(endString != null) endDate= Date.valueOf(endString);
		
		try {
			PreparedStatement  ps  =  con.prepareStatement(  "UPDATE  ticket  SET  "  +
			"PersNr_FK  =  ?,  Rechner_FK  =  ?,  Status_FK  =  ?,  Bemerkung  =  ?, Kategorie= ?, Problem= ?, Anfrage= ?, EndDate= ? "+
			" where TicketNr =  ?"  ); 
		
			ps.setInt(1, user);
			ps.setInt(2, rechner);
			ps.setInt(3, status);
			ps.setString(4, bemerkung);
			ps.setInt(5, kategorie);
			ps.setString(6, grund);
			ps.setInt(7, anfrage);
			ps.setDate(8, endDate);
			ps.setInt(9, tNr);
			
			int  n  =  ps.executeUpdate(); 
			if( n == 1 ) {
				out.println(  "O.K.,	Datensatz  geändert.");
				FacesContext.getCurrentInstance().addMessage(  null,  new  FacesMessage(
						FacesMessage.SEVERITY_INFO,  "O.  K.", "Datensatz  wurde  erfolgreich  geändert."  ));
			}else  if(  n  ==  0  )  {
				out.println(  "Keine  Änderung!!");
				FacesContext.getCurrentInstance().addMessage(  null,  new  FacesMessage(
				FacesMessage.SEVERITY_WARN,  "Datensatz  nicht  geändert!",
				"PK-Änderung  nicht  erlaubt."  ));
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

	// einzelnes Ticket auslesen
	public List<String> select_one_ticket(int ticketNr) {
		List<String> data = new ArrayList<String>();
		// String vorbereiten und ausführen lassen
		
		String sqlStatment = SQL_ticket + " where TicketNr = "  + ticketNr;
		selectStatment(sqlStatment);
		// fülle die das ResultSet in die Liste als String --> konvertierung passiert im Ticket
		try {
			if(rs.next()) {
				if (!rs.isLast()) return null;
				
				data.add(0, rs.getString(1));
				data.add(1,rs.getString(2));
				data.add(2, rs.getString(3));
				data.add(3, rs.getString(4));
				data.add(4, rs.getString(5));
				data.add(5,rs.getString(6));
				data.add(6, rs.getString(7));
				data.add(7,rs.getString(8));
				data.add(8,rs.getString(9));
				if(rs.wasNull()) data.add(9, rs.getString(10));
				else data.add(9,"null");
				rs.close();
				return data;
			}
			
		}catch(SQLException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
			out.println("Error:  " + ex);
			ex.printStackTrace();
		}	
		return null;
	}
	// Liste die bei der Suche zurück kommt.
	public List<Ticket> suche_all_ticket( String suche){
		// anpassen des Tickets --> join einbauen
		List<Ticket> daten = new ArrayList<Ticket>();
		
		if (suche.length() != 0){
			suche= "where " + suche;
		}
		String sqlStatment = "Select ticket.TicketNr, "
						+ "astatus.Name as Status,"
						+ "kategorie.Name as Kategorie, "
						+ "anfrage.Name as Anfrage, "
						+ "bearbeiter.Name, "
						+ "ticket.Problem, "
						+ "ticket.Bemerkung,"
						+ "ticket.StartDate "
						+ "from ticket "
						+ "inner join kategorie "
						+ "on ticket.Kategorie = kategorie.KatNr "
						+ "inner join anfrage "
						+ "on ticket.Anfrage = anfrage.AnfrageNr "
						+ "inner join astatus "
						+ "on ticket.Status_FK = astatus.AStatusID "
						+ "inner join rechner "
						+ "on ticket.rechner_fk = konfiguaration.inventarnummer "
						+ suche;
		
		try {
			if (connected) {
				selectStatment(sqlStatment);
				if(rs != null) {
					while (rs.next()) {
						Ticket ticket = new Ticket();
						ticket.settNr(rs.getInt("TicketNr"));
						ticket.setStatusString(rs.getString("Status"));
						ticket.setKategorieString(rs.getString("Kategorie"));
						ticket.setAnfrageString(rs.getString("Anfrage"));
						ticket.setGrund(rs.getString("Problem"));
						ticket.setBemerkung(rs.getString("Bemerkung"));
						ticket.setStartDate(rs.getDate("StartDate"));
						daten.add(ticket);	
					}
					rs.close();
					return daten;		
				}
			}
			
					}catch(SQLException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
			out.println("Error:  " + ex);
			ex.printStackTrace();
		}
		return null;
	}
	// Baue eine Liste von Tickets auf, damit eine Tabelle aufgebaut werden kann.
	
	public List<Ticket> select_all_ticket(int userFK){
		// anpassen des Tickets --> join einbauen
		List<Ticket> daten = new ArrayList<Ticket>();
		System.out.println(" Es werden Tickets für folgenden User erstellt: "+userFK);
		String sqlStatment = "Select ticket.TicketNr, "
						+ "astatus.Name as Status,"
						+ "kategorie.Name as Kategorie, "
						+ "anfrage.Name as Anfrage, "
						+ "ticket.Problem, "
						+ "ticket.Bemerkung,"
						+ "ticket.StartDate "
						+ "from ticket "
						+ "inner join kategorie "
						+ "on ticket.Kategorie = kategorie.KatNr "
						+ "inner join anfrage "
						+ "on ticket.Anfrage = anfrage.AnfrageNr "
						+ "inner join astatus "
						+ "on ticket.Status_FK = astatus.AStatusID "
						+ " where PersNr_FK = " + userFK;
		
		try {
			selectStatment(sqlStatment);
		if(rs != null) {
			while (rs.next()) {
				Ticket ticket = new Ticket();
				ticket.settNr(rs.getInt("TicketNr"));
				ticket.setStatusString(rs.getString("Status"));
				ticket.setKategorieString(rs.getString("Kategorie"));
				ticket.setAnfrageString(rs.getString("Anfrage"));
				ticket.setGrund(rs.getString("Problem"));
				ticket.setBemerkung(rs.getString("Bemerkung"));
				ticket.setStartDate(rs.getDate("StartDate"));
				daten.add(ticket);
			}
			rs.close();
			return daten;
		}
			
		}catch(SQLException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
			out.println("Error:  " + ex);
			ex.printStackTrace();
		}
		return null;
	}
}
