package projekt_web_entwicklung.helpdesk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import projekt_web_entwicklung.helpdesk.Util;
import projekt_web_entwicklung.helpdesk.DbStatment;

@Named
@SessionScoped
public class Helpdesk implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private  DbStatment  statment  =  new  DbStatment();
	private  Util		 util	   =  new Util();
	
	/* Klassenbeschreibung
	 * diese Klasse dient dem Login in die Anwendung.
	 * Sie enthält die Login Routine und ist außerdem der Vermittler für den User
	 */
	public Helpdesk() {
        System.out.println( "Anlegen der von Helpdesk" );
        System.out.println( (new Date()).toString() ); 
	}
	
	private String user = "";
	private String pwd = "";
	private int userID;
	private boolean login = false;
	private boolean admin = false;
	
	
	
	public String checkLogin() {
		if (checkPwd()) {
			if (user == "admin") return "admin";
			else {
				setLogin(true);
				
				return "client_bearbeiter.xhtml";
			}
			
		}else {
			FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Der Benutzer oder das Passwort sind ungültig."));
		}
		return null;
	}
	
	private boolean checkPwd() {
		List<String> daten = new ArrayList<String>();
		statment.connect();
		daten= statment.selectUser(user);
		statment.disconnect();
		
		
		
		if (daten == null) {
			FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Der Benutzer exestiert in der Datenbank nicht."));
		}
		
		System.out.println("Der Nutzer mit der ID " + daten.get(0) +" hat sich eingelogt.");
		
		if (daten.isEmpty()) return false;
		if (pwd.equals(daten.get(2))) { //util.cryptpw(user, pwd) == daten.get(2)
			this.user = daten.get(1);
			setUserID(Integer.valueOf(daten.get(0)));
			if(Integer.parseInt(daten.get(3))== 1) this.admin = true;
			else this.admin = false;
			return true;
		}
		return false;
	}
	
	public String logout() {
		setLogin(false);
		System.out.println("User mit der ID " + userID +" hat sich ausgelogt");
		
		setUser("");
		setUserID(0);
		setPwd("");

		return "client_helpdesk.xhtml";
	}
	
	
	public void setUserID(int userID) {
		this.userID= userID;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setUser(String user) {
		this.user= user;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public String getPwd() {
		return pwd;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
