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
	private boolean connected = false;

	/* Konstante Statments */

	final String SQL_anfrage = "select AnfrageNr,Name, Beschreibung from anfrage";
	final String SQL_kategorie = "select KatNr,Name, Beschreibung from kategorie";
	final String SQL_status = "select AStatusID, Name, Beschreibung from astatus";
	final String SQL_user = "select PersNr, Vorname, Nachname, Abteilung from bearbeiter";

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
		List<String> bkennung = new ArrayList();
		String statment = "select username, passwort from bearbeiter where username = " + username;
		selectStatemnt(statment);
		
		try {
			if (rs.first()) {
				if (rs.isLast()) return null;
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

	private void selectStatemnt(String SQL_SELECT) {
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
		}else {
			FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Keine Datenkverbindung vorhanden."));
		}	
	}

	public ArrayList<String> select_Rechner(){
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
				selectStatemnt(SQL_anfrage);
				break;
			case "kategorie":
				selectStatemnt(SQL_kategorie);
				break;
			case "status":
				selectStatemnt(SQL_status);
				break;
			case "user":
				selectStatemnt(SQL_user);
				break;
			default:
				FacesContext.getCurrentInstance().addMessage( null,
						 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Die Methode abgebrochen. Code: DbStatment"));
			}

			if (tabelle != "user") {
				if (rs != null) {
					while (rs.next()) {
						z.add(new SelectItem(rs.getString(1), rs.getString(2), rs.getString(3)));
					}
				}else {
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
				}else {
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
	 * Ticketdatensatz normalisiert einfügen
	 */
	public void insert_ticket(int tNr, int status, int anfrage, int rechner, int kategorie, int user, String grund,
			String bemerkung, Date startDate) {

		if (connected) {
			try {
				// if( ps == null ){
				String sQl = "INSERT  INTO  ticket(  "
						+ "TicketNr,PersNr_FK, Rechner_FK, Status_FK, Bemerkung, Kategorie, Problem, Anfrage,StartDate)  "
						+ "VALUES  (  ?,  ?,  ?,  ?,  ?, ?, ?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(sQl);
				// }

				ps.setInt(1, tNr);
				ps.setInt(2, status);
				ps.setInt(3, anfrage);
				ps.setInt(4, rechner);
				ps.setInt(5, kategorie);
				ps.setInt(6, user);
				ps.setString(7, grund);
				ps.setString(8, bemerkung);
				ps.setDate(9, (java.sql.Date) startDate);

				int n = ps.executeUpdate();
				if (n == 1) {
					out.println("O.K.,	Datensatz  eingefügt.");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"O.  K.", "Ein  Datensatz  erfolgreich  eingefügt."));
				}
				ps.close();

			} catch (SQLException ex) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
				out.println("Error:  " + ex);
				ex.printStackTrace();
			}
		}
	}
}
