package projekt_web_entwicklung.helpdesk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
        System.out.println( "MyBean.<init>..."  );
        System.out.println( (new Date()).toString() ); 
	}
	
	private String user = "";
	private String pwd = "";
	private int userID = 1;
	
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
	
	public String checkLogin() {
		if (checkPwd()) {
			if (user == "admin") return "admin";
			else return user;
			
		}else {
			FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Der Benutzer oder das Passwort sind ungültig."));
		}
		return null;
	}
	
	private boolean checkPwd() {
		List<String> daten = new ArrayList<String>();
		daten= statment.selectUser(user);
		
		if (daten.isEmpty()) return false;
		if (util.cryptpw(user, pwd) == daten.get(1))return true;
		return false;
	}
	
	private void sessionSetzen() {
		/* 
		 * ToDo: hier muss die Session gesetzt werden
		 */
	}
}
