/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import the_traitor.supporters.ContactsImporter;
import the_traitor.supporters.SMSSender;
import the_traitor.supporters.RMSManager;
/**
 *
 * @author Bineesh
 */
public class Traitor extends Canvas implements Runnable {
    /**
     * The main window.
     */
    private final Window main;
    /**
     * Contacts displayed in this window.
     */
    private final Window ContactsWindow;
    /**
     * about screen.
     */
    private final Window aboutWindow;
    /**
     * help screen
     */
    private final Window helpWindow;
    /**
     * Contacts view window.
     */
    private Window viewWindow;
    /**
     * splash screen.
     */
    private final Window loader;
    /**
     * Sending messages here.
     */
    private final Window sender;
    /**
     * Syncing contacts.
     */
    private final Window SyncWindow;
    /**
     * Handles the legal part.
     */
    private final Window LawWindow;
    /**
     * The core part of the application. One and only object of FocusableObject
     */
    public static FocusableObject focused;
    
    private ContactsImporter ci;
    
    private Menu exit;
    private Menu send;
    private Menu about;
    private Menu contacts;
    private Menu back;
    private Menu view;
    private Menu commit;
    private Menu mark;
    private Menu sendtoMarked;
    private Menu delete;
    private Menu sync;
    private Menu accept;
    private Menu decline;
    private Menu help;
    
    private TextArea msg, tname, tno, tnn;
    private Prompt exitPrompt;
    public boolean running = false;
    private boolean SendWindow = false, acceptedPolicy, declinedPolicy;
    private final SMSSender ss;
    private final Midlet midlet;
    
    private static final int KEY_LEFT_SOFT = -6;
    private static final int KEY_RIGHT_SOFT = -7;
    private static final int KEY_FIRE = -5;
    private static final int KEY_DOWN = -2;
    private static final int KEY_UP = -1;
    private static final int KEY_LEFT = -3;
    private static final int KEY_RIGHT = -4;
    
    public Traitor(Midlet m) {
        
        this.midlet = m;
        ss = new SMSSender();
        
        main = getHomeScreen();
        ContactsWindow = getContactsScreen();
        
        ci = new ContactsImporter(ContactsWindow);
        
        aboutWindow = getAboutScreen();
        loader = getPreloaderScreen();
        sender = getSenderScreen();
        SyncWindow = getSyncingScreen();
        LawWindow = getPrivacyScreen();
        helpWindow = getHelpScreen();
        
        focused = loader;
    
    }
    
    protected void paint(Graphics g) {
        Theme.HEIGHT = getHeight();
        Theme.WIDTH = getWidth();
        
        focused.paint(g);
    }
    
    public void keyPressed(int key) {
        
        Menu menu = null;
        switch(key) {
            case KEY_LEFT_SOFT:
                menu = focused.getLSMenu();
                break;
            case KEY_RIGHT_SOFT:
                menu = focused.getRSMenu();
                break;
            case KEY_FIRE:
                menu = focused.getFireMenu();
                break;
            case KEY_DOWN:
                focused.next();
                break;
            case KEY_UP:
                focused.previous();
                break;
            case KEY_LEFT:
                focused.left();
                if(focused.getRSMenu() == null) {
                    break;
                }
                else if(focused.getRSMenu().type() == Menu.CANCEL && focused.getClass() == OptionsGroup.class) {
                    menu = focused.getRSMenu();
                }
                break;
            case KEY_RIGHT:
                focused.right();
                break;
            default :
                if(focused.equals(msg)) {
                    msg.listenKey(key);
                }
                else if(focused.equals(msg.sym)) {
                    msg.sym.listenKey(key);
                }
                else if(focused == tname) {
                    tname.listenKey(key);
                }
                else if( focused == tno) {
                    tno.listenKey(key);
                }
                else if(focused == tnn) {
                    tnn.listenKey(key);
                }
        }
       
        if(menu != null) {
           doAction(menu);
        }
        
        repaint();
    }
    
    private void doAction(Menu menu) {
        
        if(menu.equals(exit)) {
            focused = exitPrompt;
        }
        else if(menu.equals(contacts)){
            focused = ContactsWindow;
            if(SendWindow) {
                if(ContactsWindow.isMarkingOn()) {
                    ContactsWindow.unmarkAll();
                }
                
                ContactsWindow.removeMenu(mark);
                ContactsWindow.removeMenu(sendtoMarked);
                ContactsWindow.addMenu(view);
                ContactsWindow.setProcess("Manage Contacts");
                SendWindow = false;
            }
        }
        else if(menu.equals(help)) {
            focused = helpWindow;
        }
        else if(menu.equals(about)) {
            focused = aboutWindow;
        }
        else if(menu.equals(view)) {    
            Component com = ContactsWindow.getComponentFocused();
            if(com != null) {
                viewWindow = getViewScreen((ContactItem)com);
                focused = viewWindow;
            }
        }
        else if(menu.equals(commit)) {
            ((ContactItem)ContactsWindow.getComponentFocused()).commitChanges(tno.Content.toString(), tnn.Content.toString());
            focused = ContactsWindow;
        }
        else if(menu.equals(send)) {
            if(!SendWindow) {
                ContactsWindow.removeMenu(view);
                ContactsWindow.addMenu(mark);
                ContactsWindow.addMenu(sendtoMarked);
                SendWindow = true;
                ContactsWindow.setProcess("Select Receivers");
            }
            focused = ContactsWindow;
        }
        else if(menu.equals(mark)) {
            ContactsWindow.markItem(mark);
        }
        else if(menu.equals(sendtoMarked)) {
            focused = sender;
        }
        else if(menu.equals(delete)) {
            ContactsWindow.deleteComponent(ContactsWindow.getComponentFocused());
            focused = ContactsWindow;
        }
        else if(menu.equals(sync)) {
            RMSManager.destroyRMS();
            focused = SyncWindow;
        }
        else if(menu.equals(accept)) {
            PolicyAccepted();
        }
        else if(menu.equals(decline)) {
            PolicyDeclined();
        }
        else if(menu.type() == Menu.OK) {
            if(focused == msg.sym) {
                msg.getFocused();
            }
            else if(focused == msg) {
                msg.goInactive();
            }
            else if(focused == tname.sym) {
                tname.getFocused();
            }
            else if(focused == tname) {
                tname.goInactive();
            }
            else if(focused == tno.sym) {
                tno.getFocused();
            }
            else if( focused == tno) {
                tno.goInactive();
            }
            else if(focused == tnn.sym) {
                tnn.getFocused();
            }
            else if(focused == tnn) {
                tnn.goInactive();
            }
        }
        else if(menu.type() == Menu.OPTIONS) {
            if(focused.equals(main)) {
                focused = main.mg.opt;
            }
            else if(focused.equals(ContactsWindow)) {
                focused = ContactsWindow.mg.opt;
            }
        }
        else if(menu.type() == Menu.CANCEL) {
            if(focused.equals(main.mg.opt)) {
                focused = main;
            }
            else if(focused.equals(ContactsWindow)) {
                focused = main;
            } 
            else if(focused.equals(helpWindow)) {
                focused = main;
            }
            else if(focused.equals(msg.sym)){
                focused = msg;
            }
            else if(focused.equals(ContactsWindow.mg.opt)) {
                focused = ContactsWindow;
            }
            else if(focused.equals(aboutWindow)) {
                focused = main;
            }
            else if(focused.equals(viewWindow)) {
                focused = ContactsWindow;
            }
            else if(focused.equals(exitPrompt)) {
                focused = main;
            }
            else if(focused.equals(SyncWindow)) {
                focused = ContactsWindow;
            }
            else if(focused.equals(tname.sym)){
                focused = tname;
            }
            else if(focused.equals(tno.sym)){
                focused = tno;
            }
            else if(focused.equals(tnn.sym)){
                focused = tnn;
            }
        }
        else if(menu.type() == Menu.CLEAR) {
            if(focused == msg) {
                msg.clear();
            }
            else if(focused == tname) {
                tname.clear();
            }
            else if( focused == tno) {
                tno.clear();
            }
            else if(focused == tnn) {
                tnn.clear();
            }
        }
        else if(menu.type() == Menu.NORMAL) {
            if(focused == exitPrompt) {
                running = false;
            }
            else if(focused == msg) {
                msg.showT9Box(midlet);
            }
            else if(focused == tname) {
                tname.showT9Box(midlet);
            }
            else if(focused == tno) {
                tno.showT9Box(midlet);
            }
            else if(focused == tnn) {
                tnn.showT9Box(midlet);
            }
        }
    }
    
    private Window getHomeScreen() {
        Window Main = new Window(false);
        
        msg = new TextArea("Message", 100, false);
        Main.addComponent(msg);
        
        Main.setProcess("Send Message");
        
        exit = new Menu("Exit", Menu.EXIT);
        Main.addMenu(exit);
        
        send = new Menu("Send", Menu.OK);
        Main.addMenu(send);
        
        exitPrompt = new Prompt("Are you sure you want to exit ?");
        Main.attachPrompt(exitPrompt);
        
        about = new Menu("About");
        contacts = new Menu("Contacts");
        help = new Menu("Help");
        
        Main.addMenu(contacts);
        Main.addMenu(help);
        Main.addMenu(about);
        
        return Main;
    }
    
    private Window getHelpScreen() {
        
        Window help = new Window(false);
        help.setProcess("Help");
        
        back = new Menu("Back", Menu.CANCEL);
        help.addMenu(back);
        
        Label l3 = new Label("The Application helps you to send multiple messages at a time personally. You can "
                + "mark and send multiple messages. Application decodes your message \n%nm% - replaces with name, "
                + "\n%no% - replaces with number and \n%nn% - replaces with nickname\n of each contact.\nYou can edit nickname inside the app.", (float)1.5);
        l3.setXAxis(5);
        l3.setColor(0x000000);
        help.addComponent(l3);
        
        Label l = new Label(" ",1);
        help.addComponent(l);
        
        return help;
        
    }
    
    private Window getContactsScreen(){
        
        Window ContactWindow = new Window(true);
        
        ContactWindow.setProcess("Manage Contacts");
        
        back = new Menu("Back", Menu.CANCEL);
        ContactWindow.addMenu(back);
        
        sync = new Menu("Sync");
        ContactWindow.addMenu(sync);
        
        delete = new Menu("Delete");
        ContactWindow.addMenu(delete);
        
        mark = new Menu("Mark", Menu.OK);
        sendtoMarked = new Menu("Send");
        
        view = new Menu("View", Menu.OK);
        ContactWindow.addMenu(view);
          
        return ContactWindow;
        
    }
    
    private Window getAboutScreen() {
        
        Window about = new Window(false);
        about.setProcess("About");
        
        back = new Menu("Back", Menu.CANCEL);
        about.addMenu(back);
        
        Label l = new Label(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM), "The Traitor  ", (float)0.5);
        l.setAlign(Label.ALIGN_CENTRE);
        about.addComponent(l);
        
        Label ver = new Label("v 0.4.12", (float)0.2);
        ver.setColor(0x330000);
        ver.setAlign(Label.ALIGN_CENTRE);
        about.addComponent(ver);
        
        Label l1 = new Label("Developed By", (float)0.20);
        l1.setColor(0x000000);
        l1.setAlign(Label.ALIGN_CENTRE);
        about.addComponent(l1);
        
        Label aut = new Label("Bineesh N\nwww.bineesh.tk", (float)0.3);
        aut.setAlign(Label.ALIGN_CENTRE);
        about.addComponent(aut);
        
        Label l31 = new Label("\nRead our privacy policy at:",(float)0.2);
        l31.setColor(0x252525);
        l31.setAlign(Label.ALIGN_CENTRE);
        about.addComponent(l31);
        
        Label l32 = new Label("http://bineesh.tk/traitor/privacy/index.php",(float)0.3);
        l32.setColor(0x000099);
        l32.setAlign(Label.ALIGN_CENTRE);
        about.addComponent(l32);
        
        Label l4 = new Label("",0);
        about.addComponent(l4);
        
        return about;
    }
    
    public Window getViewScreen(ContactItem cit) {
        
        Window view = new Window(false);
        view.setProcess("View Contact");
        
        back = new Menu("Back", Menu.CANCEL);
        view.addMenu(back);
        
        commit = new Menu("Commit", Menu.OK);
        view.addMenu(commit);
        
        tname = new TextArea("Name:");
        tname.setText(cit.name);
        tname.Type = TextArea.TEXTAREA_INACTIVE;
        view.addComponent(tname);
        
        tno = new TextArea("No:");
        tno.setText(cit.ContactNo);
        tno.WritingMode = TextArea.NUM_MODE;
        view.addComponent(tno);
        
        tnn = new TextArea("NickName:");
        tnn.setText(cit.NickName);
        view.addComponent(tnn);
        
        return view;
    }
    
    private Window getPreloaderScreen() {
        
        Window pl = new Window(false);
        
        Label l = new Label("", 5);
        l.setColor(0x000000);
        l.setAlign(Label.ALIGN_CENTRE);
        ci.setListenerLabel(l);
        
        pl.addComponent(l);
        pl.setBGImage("/the_traitor.png");
        
        return pl;
    }
    
    private Window getSyncingScreen() {
        
        Window sync = new Window(false);
        sync.setProcess("Syncing Contacts..");
        
        back = new Menu("Back", Menu.CANCEL);
        sync.addMenu(back);
        
        Label l = new Label("Current Records Deleted. Contacts will be synced on next application start", 2);
        l.setColor(0x000000);
        sync.addComponent(l);
        
        return sync;
        
    }
    
    private Window getSenderScreen() {
        Window w = new Window(false);
        w.setProcess("Sending Messages");
        
        back = new Menu("Cancel", Menu.CANCEL);
        w.addMenu(back);
        
        Label l = new Label("", 2);
        l.setColor(0x000000);
        l.setAlign(Label.ALIGN_CENTRE);
        ss.setListener(l);
        
        w.addComponent(l);
        
        return w;
    }
    
    private Window getPrivacyScreen() {
        
        Window w = new Window(false);
        w.setProcess("Privacy Policy");
        
        accept = new Menu("Accept", Menu.OK);
        decline = new Menu("Decline",Menu.CANCEL);
        w.addMenu(accept);
        w.addMenu(decline);
        
        Label Policy = new Label("The Traitor", 1);
        Policy.setColor(0x333333);
        Policy.setAlign(Label.ALIGN_CENTRE);
        
        Label stmt = new Label("By pressing 'accept', you read and agree our"
                + " privacy policy at:",1);
        stmt.setAlign(Label.ALIGN_CENTRE);
        
        Label url = new Label("http://bineesh.tk/traitor/privacy/index.php", 1);
        url.setColor(0x000088);
        
        Label end = new Label("",0);
        
        w.addComponent(Policy);
        w.addComponent(stmt);
        w.addComponent(url);
        w.addComponent(end);
        
        return w;
    }
    /**
     * Main thread.
     * Does pre-loading and running
     */
    public void run() {
        
        if(!ci.isPolicyAccepted()) {
            
            acceptedPolicy = false;
            declinedPolicy = false;
            
            focused = LawWindow;
            
            while(!acceptedPolicy && !declinedPolicy) {
                waitThread(10L);
            }
            
            if(declinedPolicy) {
                midlet.exitMidlet();
            }
            else {
                ci.setPolicyAccepted();
            }
        }
        
        importContacts();
        
        running = true;
        focused = main;
        while(running) {
            
            long WaitTime = 500L;
            
            if(focused == sender) {                
                sendMessages();
                focused = main;
            }
            else if(focused != null) {
                serviceRepaints();
                WaitTime = focused.run();
            }
            
            repaint();
            waitThread(WaitTime);
            System.gc();
        
        }
        
        midlet.exitMidlet();
    }
    
    private void importContacts() {
        focused = loader;
        new Thread(new Runnable() {
            public void run() {
                ci.run();
            }
        }).start();
        
        while(!ci.completed) {
            
            waitThread(50L);
            
            repaint();
        }
        
    }
    
    private void waitThread(long time) {

        synchronized(this) {
                try {
                    wait(time);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        
    }
    
    private void sendMessages() {
        
        ss.completed = false;

        new Thread(new Runnable() {
            public void run() {
                ss.run((ContactItem)ContactsWindow.getHeaderComponent(), msg.Content.toString());
            }
        }).start();

        while(!ss.completed) {

            waitThread(50L);
            repaint();
        }

    }
    
    private void PolicyAccepted() {
        this.acceptedPolicy = true;
    }
    
    private void PolicyDeclined() {
        this.declinedPolicy = true;
    }
}
