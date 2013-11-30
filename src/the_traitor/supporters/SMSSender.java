/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor.supporters;
import javax.microedition.io.Connector;
import the_traitor.ContactItem;
import the_traitor.Label;
import javax.wireless.messaging.*;
import java.io.*;
/**
 *
 * @author Bineesh
 */
public class SMSSender {
    
    private Label Listener;
    private int count, failed;
    public boolean completed;
    
    private static final String NAME_IND = "%nm%";
    private static final String NO_IND = "%no%";
    private static final String NN_IND = "%nn%";
    
    
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
    
    private void sendMessage(ContactItem to, String Message) {
        
        if(!isvalidReceiver(to.ContactNo)){
            System.out.println("Invalid Receiver Provided");
            return;
        }
        
        String dest_address = "sms://" + to.ContactNo ;
        Message = decodeMessage(to, Message);
        System.out.println(Message);
        MessageConnection smsconn = null; 
        
        try {
            smsconn = (MessageConnection)Connector.open(dest_address);
        }
        
        catch(IOException e) {
            notifyFailed();
            System.out.println("Reason 4");
        }
        catch(SecurityException e) {
            notifyFailed();
            System.out.println("Reason 3");
        }
        
        try {
            TextMessage txt = (TextMessage)(smsconn.newMessage(MessageConnection.TEXT_MESSAGE));
            txt.setAddress(dest_address);
            txt.setPayloadText(Message);
            smsconn.send(txt);
            count ++;
            setState("Sent Message(s): "+count+"\n"+failed+" failed");
        }
        
        catch(IOException e) {
            notifyFailed();
            System.out.println("Reason 2");
        }
        catch(SecurityException e) {
            notifyFailed();
            System.out.println("Reason 1");
        }
        catch(NullPointerException e) {
            notifyFailed();
            System.out.println("NULL");
        }
    }
    
    private void notifyFailed() {
        failed ++;
        setState("Sent Message(s): "+count+"\n"+failed+" failed");
    }
    
    private String decodeMessage(ContactItem to, String Message) {
        
        StringBuffer msg = new StringBuffer(Message);
        int previndex = 0;
        for(int i = 0; i < Message.length(); i++) {
            
            int v = Message.indexOf('%', previndex);
            if(v == -1) {
                break;
            }
            else {
                String rds;
                previndex = v+1;
                if(v + 3 < Message.length()) {
                    if(Message.charAt(v+3) == '%') {
                        rds = Message.substring(v,v+4);
                        
                        if(rds.equals(NAME_IND)) {
                            msg = replaceWith(v, to.name, msg);
                            previndex = v + 3;
                        }
                        else if(rds.equals(NO_IND)) {
                            msg = replaceWith(v, to.ContactNo, msg);
                            previndex = v + 3;
                        }
                        else if(rds.equals(NN_IND)) {
                            msg = replaceWith(v, to.NickName, msg);
                            previndex = v + 3;
                        }
                    }
                }
                else {
                    break;
                }
            }
            
        }
        
        return msg.toString();
        
    }
    
    private StringBuffer replaceWith(int index, String str, StringBuffer sb) {
        
        sb.delete(index, index+4);
        sb.insert(index, str);
        
        return sb;
    }
    
    public void run(ContactItem header, String Message) {
    
        count = 0;
        failed = 0;
        
        setState("Sending Messages..");
        
        while(header != null) {
            if(header.isMarked()) {
                sendMessage(header, Message);
            }
            
            header = (ContactItem)header.next;
        }
        completed = true;
    }
    
    public void setListener(Label l) {
        this.Listener = l;
        setState("Waiting for command..");
    }
    
    private void setState(String CurrState) {
        if(this.Listener != null) {
            Listener.editLabel(CurrState);
        }
    }
}
