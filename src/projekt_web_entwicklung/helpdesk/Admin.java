package projekt_web_entwicklung.helpdesk;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class Admin implements Serializable{
	private static final long serialVersionUID = 1L;
	
	DbStatment statment = new DbStatment();
	Util util = new Util();

	private String vorname;
	private String nachname;
	private String abteilung;
	private String user;
	private String pwd;
	private int adminID;
	
	private int invNr;
	private String rUsername;
	private String rName;
	private String ram;
	private String cpu;
	private String drive;
	private String driveSpace;
	private String os;
	private String software;
	
	public void rechnerAnlegen() {
		try {
			statment.connect();
			boolean check = statment.insert_rechner(  invNr, rUsername, rName, ram, cpu, drive, driveSpace, os, software );
			statment.disconnect();
			
			if (check) {
				setInvNr(0);
				setrUsername("");
				setrName("");
				setRam("");
				setCpu("");
				setDrive("");
				setDriveSpace("");
				setOs("");
				setSoftware("");
				
				FacesContext.getCurrentInstance().addMessage( null,
						 new FacesMessage(FacesMessage.SEVERITY_INFO,"Erfolgreich.","Der Rechner wurde angelegt."));
			}
		}catch(Exception e) {
			FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Kein Benutzer angelegt."));
		}	
	}
	
	
	
	public void userAnlegen() {

		pwd = util.cryptpw(user, pwd);
		
		try {
			statment.connect();
			boolean check = statment.insert_user(vorname, nachname, abteilung, user, pwd, adminID );
			statment.disconnect();
			
			if(check) {
				FacesContext.getCurrentInstance().addMessage( null,
						 new FacesMessage(FacesMessage.SEVERITY_INFO,"Erfolgreich.","Der Benutzer wurde angelegt."));
				
				 setVorname("");
				 setNachname("");
				 setAbteilung("");
				 setUser("");
				 setPwd("");
				 setAdminID(0);
			}else {
				FacesContext.getCurrentInstance().addMessage( null,
						 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Kein Benutzer angelegt."));
			}
				
			
			
		}catch(Exception e) {
			FacesContext.getCurrentInstance().addMessage( null,
					 new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fehler.","Kein Benutzer angelegt."));
		}	
		
		
	}
	
	
	public int getInvNr() {
		return invNr;
	}

	public void setInvNr(int invNr) {
		this.invNr = invNr;
	}

	public String getrUsername() {
		return rUsername;
	}

	public void setrUsername(String rUsername) {
		this.rUsername = rUsername;
	}

	public String getrName() {
		return rName;
	}

	public void setrName(String rName) {
		this.rName = rName;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getDrive() {
		return drive;
	}

	public void setDrive(String drive) {
		this.drive = drive;
	}

	public String getDriveSpace() {
		return driveSpace;
	}

	public void setDriveSpace(String driveSpace) {
		this.driveSpace = driveSpace;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getSoftware() {
		return software;
	}

	public void setSoftware(String software) {
		this.software = software;
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
	public Admin(){
		
	}



	public int getAdminID() {
		return adminID;
	}



	public void setAdminID(int adminID) {
		this.adminID = adminID;
	}
}
