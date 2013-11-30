/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor;

import javax.microedition.lcdui.Graphics;

/**
 * Yes or No Answer prompt
 * @author Bineesh
 */
public class Prompt implements FocusableObject {
    
    private Menu yes, no;
    public Window parent;
    private String Dialog;
    
    public Prompt(String Dialog) {
        yes = new Menu("Yes", Menu.NORMAL);
        no = new Menu("No", Menu.CANCEL);
        this.Dialog = Dialog;
    }
    
    public Menu getLSMenu() {
        return yes;
    }

    public Menu getRSMenu() {
        return no;
    }

    public Menu getFireMenu() {
        return yes;
    }

    public void next() {
        
    }

    public void previous() {
        
    }

    public void left() {
        
    }

    public void right() {
        
    }

    public void paint(Graphics g) {
        parent.paint(g);
        
        Theme t = Theme.getInstance();
        
        t.setGraphics(g);
        t.drawPrompt(Dialog);
        t.drawMenu(null, yes, no);
    }

    public long run() {
        return 500L;
    }
    
}
