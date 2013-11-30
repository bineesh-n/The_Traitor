/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor;

import javax.microedition.lcdui.Graphics;

/**
 * Menu Group can hold commands
 * @author Bineesh
 */
public class MenuGroup {
    private int lf_count = 0;
    public Menu left_soft, fire, right_soft;
    public OptionsGroup opt;
    public MenuGroup() {
        opt = new OptionsGroup();
    }
    /**
     * Add a menu
     * @param menu new menu
     */
    public void addMenu(Menu menu) {
        
        if(menu.type() == Menu.EXIT || menu.type() == Menu.CANCEL || menu.type() == Menu.CLEAR && right_soft == null) {
            right_soft = menu;
        }
        else if (menu.type() == Menu.OK ) {
            fire = menu;
        }
        else if(lf_count == 0) {
            left_soft = menu;
            lf_count ++;
        }
        else {
            if(left_soft.type() != Menu.OPTIONS) {
                opt.addOption(left_soft);
            }
            lf_count ++;
            opt.addOption(menu);
            left_soft = new Menu("Options", Menu.OPTIONS);
        }
    }
    /**
     * Remove menu from window
     * @param menu menu object
     */
    public void removeMenu(Menu menu) {
        
        if( left_soft == menu) {
            left_soft = null;
        }
        else if(right_soft == menu) {
            right_soft = null;
        }
        else if(fire == menu) {
            fire = null;
        }
        else {
            opt.checkAndRemove(menu);
        }
    }
    /**
     * Paints the menu.
     * @param t 
     */
    public void paintMenu(Theme t) {
  
        t.drawMenu(left_soft, fire, right_soft);
        opt.t = t;
    }
    
}
/**
 * Options group holds supporting 4 menus.
 * @author Bineesh
 */
class OptionsGroup implements FocusableObject {
    
    public Menu fire, right_soft;
    private int count = 0, Selected = 0;
    private Menu[] gr = new Menu[4];
    public Window parent;
    public Theme t;
    
    public OptionsGroup() {
        right_soft = new Menu("Back", Menu.CANCEL);
    }
    
    public void addOption(Menu menu) {
        gr[count++] = menu;
    }
    
    public Menu getLSMenu() {
        return null;
    }

    public Menu getRSMenu() {
        return right_soft;
    }

    public Menu getFireMenu() {
        return fire;
    }
    
    public void next() {
        if(Selected < count-1) {
            Selected++;
        }
        else {
            Selected = 0;
        }
    }
    
    public void previous() {
        if( Selected > 0) {
            Selected--;
        }
        else {
            Selected = count-1;
        }
    }

    public void paint(Graphics g) {
        
        t.setGraphics(g);
        parent.paint(g);
        
        int j = 0;
        String [] menus = new String[count];
        for(int i = 0; i < count; i++) {
            if(i == Selected) {
                fire = gr[i];
            }
            menus[j] = gr[i].name();
            j++;
        }
        
        t.drawOptions(menus, j, Selected);
        t.drawMenu(null, new Menu("Select"), right_soft);
    }
    
    public void checkAndRemove(Menu menu) {
        for(int i = 0; i < count; i ++) {
            if(gr[i] == menu) {
                while(i+1 < count) {
                    gr[i] = gr[i+1];
                    i ++;
                }
                count --;
                Selected = 0;
            }
        }
    }

    public long run() {
        return 500L;
    }
    

    public void left() {
        
    }

    public void right() {
        
    }
}