/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor;

/**
 *
 * @author Bineesh
 */
public class Menu {
    /** 6 types of commands **/
    public static final int EXIT = 0;
    public static final int CANCEL = 1;
    public static final int OK = 2;
    public static final int CLEAR = 3;
    public static final int NORMAL = 4;
    public static final int OPTIONS = 5;
    
    private final int menutype;
    private String menuname;
    
    public Menu(String menu) {
        menutype = NORMAL;
        menuname = menu;
    }
    
    public Menu(String menu, int Menutype) {
        menutype = Menutype;
        menuname = menu;
    }
    
    public int type() {
        return menutype;
    }
    
    public String name() {
        return menuname;
    }
    
    public void setName(String Name) {
        this.menuname = Name;
    }
    
}