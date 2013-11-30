/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor.supporters;

import javax.microedition.rms.*;
import the_traitor.ContactItem;
import the_traitor.Window;
/**
 *
 * @author Bineesh
 */
public class RMSManager {
    private RecordStore rms;
    private Window w;
    
    public void storeContact(ContactItem cit) throws Exception {
        
        try {
            rms = RecordStore.openRecordStore("contacts", true);
        }
        catch(Exception e) {
            throw e;
        }
        
        String name, no, nn;
        name = cit.name;
        no = cit.ContactNo;
        nn = cit.NickName;
        int rid;
        
        byte[] str = name.getBytes();
        
        rid = rms.addRecord(str, 0, str.length);
        cit.setRecordId(rid);
        
        byte[] str1 = no.getBytes();
        
        rms.addRecord(str1, 0, str1.length);
        
        byte[] str2 = nn.getBytes();
        rms.addRecord(str2, 0, str2.length);
              
        rms.closeRecordStore();
    }
    
    public void readContact() {
        
        try {
            rms = RecordStore.openRecordStore("contacts", false);
            System.out.println(rms.getNumRecords());
        }
        catch(Exception e) {
            
        }
        try {
            for(int i = 1; i < rms.getNumRecords(); i ++) {
                
                byte[] str = new byte[5];
                try {
                    if(rms.getRecordSize(i) > str.length) {
                        str = new byte[rms.getRecordSize(i)];
                    }
                }
                catch (Exception e) {
                    i +=2;
                    //System.out.println("Continues ReadFor, i="+i);
                    continue;
                    
                }
                
            //    try {
                    rms.getRecord(i, str, 0);
              /*  }
                catch (Exception e) {
                    i +=2;
                    System.out.println("Continues ReadFor, i="+i);
                    continue;
                    
                }*/
                
                String recordName = new String(str);
                
                byte[] str1 = new byte[rms.getRecordSize(++i)];
                
                rms.getRecord(i, str1, 0);
                
                String recordNo = new String(str1);
                
                byte[] str2 = new byte[rms.getRecordSize(++i)];
                
                rms.getRecord(i, str2, 0);
                
                String recordNn = new String(str2);
                
                ContactItem cit = new ContactItem(recordName, recordNo, recordNn);
                cit.setRecordId(i-2);
                addToWindow(cit);
            }
        }
        catch(Exception e) {
            System.out.println("Exit ReadFor");
        }
        
        try {
            rms.closeRecordStore();
        }
        catch (Exception e) {
            
        }
    }
    /**
     * 
     * @param cit contactItem which is to be commit
     * @throws Exception When update fails 
     */
    public void updateRecord(ContactItem cit) throws Exception {
        
        int recordId = cit.getRecordId();
        
       
        rms = RecordStore.openRecordStore("contacts", false);
        
        
        byte [] str = cit.ContactNo.getBytes();
        
        rms.setRecord(recordId + 1, str, 0, str.length);
        
        
        
        str = cit.NickName.getBytes();
        
        rms.setRecord(recordId + 2, str, 0, str.length);
        
        
        rms.closeRecordStore();
        
    }
    /**
     * 
     * @param cit contact to be deleted
     * @throws Exception when deletion fails
     */
    public void deleteContact(ContactItem cit) throws Exception {
        
        int recordId = cit.getRecordId();
        
       
        rms = RecordStore.openRecordStore("contacts", false);
        
        for (int i = recordId; i < recordId + 3; i++) {
            deleteRecord(i);
        }
        
        rms.closeRecordStore();
    }
    
    /**
     * 
     * @param recordId recordId to be deleted
     * @throws Exception When deletion fails
     */
    private void deleteRecord(int recordId) throws Exception{
        
        if(rms == null) {
            System.out.println("Delete Cheyyan Pattilla");
            return;
        }
        
        rms.deleteRecord(recordId);
        
    }
    
    public void setWindow(Window w) {
        this.w = w;
    }
    
    public void addToWindow(ContactItem cit) {
        w.addComponent(cit);
    }
    
    public boolean isContactsStored() {
        
        rms = null;
        
        try {
            rms = RecordStore.openRecordStore("contacts", false);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        if(rms == null) {
            return false;
        }
        
        try {
            rms.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        
        return true;
    }
    
    public static void destroyRMS() {
        
            try {
                RecordStore.deleteRecordStore("contacts");
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        
    }
    
    public void setPolicyAccepted() throws Exception {
        
            rms = RecordStore.openRecordStore("accept", true);
            
            String val = "1";
            
            byte[] str = val.getBytes();
        
            rms.addRecord(str, 0, str.length);

            rms.closeRecordStore();
    }
    
    public boolean isPolicyAccepted() throws Exception {
        
        rms = RecordStore.openRecordStore("accept", false);
        
        byte[] str = new byte[1];
        
        if(rms.getRecordSize(1) > str.length) {
            str = new byte[rms.getRecordSize(1)];
        }
        
        rms.getRecord(1, str, 0);
        
        rms.closeRecordStore();
        String s = new String(str);
        
        if(s.equals("1")) {
            return true;
        }
        
        return false;
    }
}