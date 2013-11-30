/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor;

import javax.microedition.lcdui.Graphics;
import the_traitor.supporters.RMSManager;

/**
 * Storing Contact Details
 * @author Bineesh
 */
public class ContactItem extends Component {
    /** contact number and nick name **/
    public String ContactNo = "100", NickName = "buddy"; 
    private boolean marked = false;
    private int recordId;
    
    public ContactItem(String name) {
        this.name = name;
    } 
    
    public ContactItem(String name, String CNo, String Nn) {
        
        this.name = name;
        this.ContactNo = CNo;
        this.NickName = Nn;
        
    }
    /**
     * 
     * @return height of this component
     * @see component class
     */
    public int getHeight() {
        Theme t = new Theme();
        return 2*t.getFHeight();
    }

    public void paint(Graphics g) {
        
        Theme t = new Theme();
        t.setGraphics(g);
        t.drawContactItem(this);
        
    }
    
    public boolean isFocusable() {
        return false;
    }
    
    /**
     * 
     * @param No new number
     * @param Nn new nick name
     */
    public void commitChanges(String No, String Nn) {
        
        this.NickName = Nn;
        this.ContactNo = No;
        
        RMSManager rm = new RMSManager();
        try {
            rm.updateRecord(this);
        }
        catch(Exception e) {
            System.out.println("Update failed man..!!");
        }
        
    }
    /**
     * Self Destruct
     */
    public void deleteContactItem() {
        
        RMSManager rm = new RMSManager();
        try {
            rm.deleteContact(this);
        }
        catch(Exception e) {
            System.out.println("delete failed man..!!");
        }
        
    }
    /**
     * 
     * @param marking mark or unmark the item in window.
     */
    public void setMarking(boolean marking) {
        this.marked = marking;
    }
    /**
     * 
     * @return is marked
     */
    public boolean isMarked() {
        return this.marked;
    }
    /**
     * Sets record id. (Database requirement)
     * @param val record id
     */
    public void setRecordId(int val) {
        recordId = val;
    }
    /**
     * 
     * @return Record id of contact.
     */
    public int getRecordId() {
        return recordId;
    }
    
}
