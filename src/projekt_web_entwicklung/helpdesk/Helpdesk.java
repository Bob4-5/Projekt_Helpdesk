package projekt_web_entwicklung.helpdesk;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.model.SelectItem;

import projekt_web_entwicklung.helpdesk.DbStatment;

@Named("Helpdesk")
@SessionScoped
public class Helpdesk implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private  DbStatment  statment  =  new  DbStatment();
	
	/* Klassenbeschreibung
	 * diese Klasse dient dem Login in die Anwendung.
	 * Sie enthält die Login Routine und ist außerdem der Vermittler für den User
	 */
	public Helpdesk() {
        System.out.println( "MyBean.<init>..."  );
        System.out.println( (new Date()).toString() ); 
	}
	
	
}
