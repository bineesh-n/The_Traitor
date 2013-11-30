/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor;

import javax.microedition.lcdui.Graphics;
/**
 *
 * @author Bineesh
 */
 public abstract class Component {
     
     /** stores whether the item is focused or not. **/
    public boolean focused;
    /** its y position **/
    public int y;
    /** next, previous components attachable to window **/
    public Component next, prev;
    public String name;
    
    /**
     * 
     * @return height of this component
     */
    public abstract int getHeight();
    /**
     * makes the component active
     */
    public void setActive() {
        focused = true;
    }
    /**
     * paints the component
     * @param g Graphics object
     */
    public abstract void paint(Graphics g);
    /**
     * Makes the component inactive
     */
    public void goInactive() {
        focused = false;
    }
    /**
     * 
     * @return true if the component is focusable and false if not
     */
    public abstract boolean isFocusable();
}
