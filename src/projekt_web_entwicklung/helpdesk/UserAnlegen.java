package projekt_web_entwicklung.helpdesk;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class UserAnlegen implements Serializable{
	private static final long serialVersionUID = 1L;
	
	DbStatment statment = new DbStatment();
	Util util = new Util();

	String vorname;
	String nachname;
	String abteilung;
	String user;
	String pwd;
	
	
	public String userAnlegen() {
		
		String sOutcome = "anlegen";
		pwd = util.cryptpw(user, pwd);
		
		try {
			statment.connect();
			statment.insert_user(vorname, nachname, abteilung, user, pwd);
			statment.disconnect();
			return sOutcome;
		}catch(Exception e) {
			FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Kein Benutzer angelegt."));
		}
			
		return null;	
	}
	
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getAbteilung() {
		return abteilung;
	}
	public void setAbteilung(String abteilung) {
		this.abteilung = abteilung;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public UserAnlegen(){
		
	}
}
