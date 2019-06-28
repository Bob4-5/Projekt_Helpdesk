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
	final String SQL_ticket = "Select TicketNr, PersNr_FK, Rechner_FK, Status_FK, Bemerkung, Kategorie, Problem, Anfrage,StartDate,EndDate from ticket";

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

	public List<String> selectUser(String username) {
		List<String> bkennung = new ArrayList<String>();
		String statment = "select username, passwort from bearbeiter where username = " + username;
		selectStatment(statment);

		try {
			if (rs.first()) {
				if (rs.isLast())
					return null;
				bkennung.add(rs.getNString(1));
				bkennung.add(rs.getString(2));
				return bkennung;
			}
		} catch (SQLException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
			out.println("Error:  " + ex);
			ex.printStackTrace();
		}
		return null;
	}

	// allgemeines Select Statment. es bekommt �ber SQL_SELECT das ausf�hrbare
	// Statment �bermittelt
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
	 * Hilfstabellen mit Daten f�llen
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

	/*
	 * Ticketdatensatz normalisiert einf�gen
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
			"PersNr_FK  =  ?,  Rechner_FK  =  ?,  Status_FK  =  ?,  Bemerkung  =  ?, Kategorie= ?, Problem= ?, Anfrage= ?,StartDate= ?" +
			"WHERE   TicketNr =  ?"  ); 
		
			ps.setInt(1, user);
			ps.setInt(2, rechner);
			ps.setInt(3, status);
			ps.setString(4, bemerkung);
			ps.setInt(5, kategorie);
			ps.setString(6, grund);
			ps.setInt(7, anfrage);
			ps.setDate(8, endDate);
			ps.setInt	(9, tNr);
			
			int  n  =  ps.executeUpdate(); 
			if( n == 1 ) {
				out.println(  "O.K.,	Datensatz  ge�ndert.");
				FacesContext.getCurrentInstance().addMessage(  null,  new  FacesMessage(
						FacesMessage.SEVERITY_INFO,  "O.  K.", "Datensatz  wurde  erfolgreich  ge�ndert."  ));
			}else  if(  n  ==  0  )  {
				out.println(  "Keine  �nderung!!");
				FacesContext.getCurrentInstance().addMessage(  null,  new  FacesMessage(
				FacesMessage.SEVERITY_WARN,  "Datensatz  nicht  ge�ndert!",
				"PK-�nderung  nicht  erlaubt."  ));
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
		// String vorbereiten und ausf�hren lassen
		
		String sqlStatment = SQL_ticket + " where TicketNr = " + ticketNr ;
		selectStatment(sqlStatment);
		// f�lle die das ResultSet in die Liste als String --> konvertierung passiert im Ticket
		try {
			if(rs.next()) {
				if (!rs.isLast()) return null;
				
				data.add(1, rs.getString(1));
				data.add(2,rs.getString(2));
				data.add(3, rs.getString(1));
				data.add(4, rs.getString(3));
				data.add(5, rs.getString(4));
				data.add(6,rs.getString(5));
				data.add(7, rs.getString(6));
				data.add(8,rs.getString(7));
				data.add(9,rs.getString(8));
				data.add(10, rs.getString(9));
				data.add(10, rs.getString(10));
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
	// Baue eine Liste von Tickets auf, damit eine Tabelle aufgebaut werden kann.
	
	public List<Ticket> select_all_ticket(int userFK){
		List<Ticket> daten = new ArrayList<Ticket>();
		System.out.println(userFK);
		String sqlStatment = SQL_ticket + " where PersNr_FK = " + userFK;
		selectStatment(sqlStatment);
		try {
			while (rs.next()) {
				Ticket ticket = new Ticket();
				System.out.println("Ticket Nr: "+ rs.getInt("TicketNr"));
				ticket.settNr(rs.getInt("TicketNr"));
				ticket.setUserID(rs.getInt("PersNr_FK"));
				ticket.setRechner(rs.getInt("Rechner_FK"));
				ticket.setStatusID(rs.getInt("Status_FK"));
				ticket.setBemerkung(rs.getString("Bemerkung"));
				ticket.setKategorieID(rs.getInt("Kategorie"));
				ticket.setAnfrageID(rs.getInt("Anfrage"));
				ticket.setGrund(rs.getString("Problem"));
				daten.add(ticket);
			}
			rs.close();
		}catch(SQLException ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
			out.println("Error:  " + ex);
			ex.printStackTrace();
		}
		return daten;
	}
}
