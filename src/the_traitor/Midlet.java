/*
 * You should not change this header.
 * All files in the project provided for you for the study purposes.
 * You are not allowed to reproduce the app and distribute it.
 */
package the_traitor;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * Main class which starts main thread.
 * @author Bineesh
 */
public class Midlet extends MIDlet implements CommandListener {
    public Traitor t;
    public Display dis;
    private TextBox tb;
    private Command ok, cancel;
    private TextArea tbs;
    
    public void startApp() {
        
        ok = new Command("Ok", Command.OK, 1);
        cancel = new Command("Cancel", Command.CANCEL, 1);
        
        dis = Display.getDisplay(this);
        t = new Traitor(this);
        Thread tr = new Thread(t);
        tr.start();
        t.setFullScreenMode(true);
        dis.setCurrent(t);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    public void exitMidlet(){
        
        if(dis.getCurrent() == tb) {
            return;
        }
        
        destroyApp(false);
        notifyDestroyed();
    }
    /**
     * Designed to show system text box when T9 pressed.
     * @param ta text area is to be associated with
     */
    public void setTextBox(TextArea ta) {
        
        int lim = ta.limit;
        if(lim < 1) {
            lim = 4096;
        }
        if(ta.Type == TextArea.TEXTAREA_ACTIVE) {
            tb = new TextBox(ta.Title, ta.Content.toString(), lim, 0);
        }
        else {
            tb = new TextBox(ta.Title, ta.Content.toString(), lim, TextField.UNEDITABLE);
        }
        tb.setCommandListener(this);
        tbs = ta;
        
        tb.addCommand(ok);
        tb.addCommand(cancel);
        
        dis.setCurrent(tb);
        
    }

    public void commandAction(Command c, Displayable d) {
        if(c.equals(ok)) {
            tbs.setText(tb.getString());
            dis.setCurrent(t);
            
        }
        else {
           dis.setCurrent(t);
           
        }
    }
}
