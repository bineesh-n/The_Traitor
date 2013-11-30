/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor.supporters;

import javax.microedition.pim.*;
import java.util.Enumeration;
import the_traitor.Window;
import the_traitor.ContactItem;
import the_traitor.Label;
/**
 *
 * @author Bineesh
 */
public class ContactsImporter {
    
    private Enumeration e;
    private Contact contact = null;
    private PIM pim;
    private PIMList pimlist;
    private Window w;
    public boolean completed = false;
    private RMSManager rms;
    private Label Listener;
    
    public ContactsImporter(Window ContactsWindow) {
        pim = PIM.getInstance();
        w = ContactsWindow;
        rms = new RMSManager();
        rms.setWindow(w);
    }
    
    private void importContacts() {
        
      //  String[] lists = pim.listPIMLists(PIM.CONTACT_LIST);
        
        //for(int i =0; i < lists.length; i++) {
            try {
                pimlist = pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY);
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }
            
            try {
                e = pimlist.items();
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            } 
            
            int count = 0;
            while(e.hasMoreElements()) {
                contact = (Contact) e.nextElement();
                String name = null, no = null;
                
                try {
                    if(pimlist.isSupportedField(Contact.FORMATTED_NAME) && (contact.countValues(Contact.FORMATTED_NAME) > 0)) {
                        name = contact.getString(Contact.FORMATTED_NAME, 0);
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                
                try {
                    if(pimlist.isSupportedField(Contact.TEL) && (contact.countValues(Contact.TEL) > 0)) {
                        no = contact.getString(Contact.TEL, 0);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if(no != null) {
                    
                    if(name == null) {
                        name = "";
                    }
                    
                    if(isvalidReceiver(no)) {
                        count ++;
                        addToWindow(name, no);
                    }
                }
                
                setProgress("Importing Contact "+count);
            }
        //}
    }
    
    public void run() {
        if(!rms.isContactsStored()) {
            setProgress("Importing Contacts..");
            importContacts();
        }
        else {
            setProgress("Reading Contacts..");
            rms.readContact();
        }
        completed = true;
    }
    
    private void addToWindow(String name, String Tel) {
        ContactItem cit = new ContactItem(name, Tel, "buddy");
        
        try {
            rms.storeContact(cit);
        }
        catch(Exception e) {
            System.out.println("Exception caught from rms, reason:");
            e.printStackTrace();
        }
        
        w.addComponent(cit);
    }
    
    private boolean isvalidReceiver(String number) {
        char[] chars = number.toCharArray();

        if (chars.length == 0) {
            return false;
        }

        int startPos = 0;

        // initial '+' is OK
        if (chars[0] == '+') {
            startPos = 1;
        }

        for (int i = startPos; i < chars.length; ++i) {
            if (!Character.isDigit(chars[i])) {
                return false;
            }
        }

        return true;
    }
    
    public void setListenerLabel(Label l) {
        this.Listener = l;
        Listener.editLabel("Loading..");
    }
    
    private void setProgress(String progress) {
        if(Listener != null) {
            Listener.editLabel(progress);
        }
    }
    
    public void setPolicyAccepted() {
        try {
            rms.setPolicyAccepted();
        }
        catch(Exception e) {
            
        }
    }
    
    public boolean isPolicyAccepted() {
        
        try {
            return rms.isPolicyAccepted();
        }
        catch(Exception e) {
            return false;
        }
    }
    
}
