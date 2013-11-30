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
public interface FocusableObject {
    /**
     * 
     * @return Menu Which holds the left soft key 
     */
    public Menu getLSMenu();
    /**
     * 
     * @return Menu Which holds the right soft key
     */
    public Menu getRSMenu();
    /**
     * 
     * @return Menu Which holds the fire key
     */
    public Menu getFireMenu();
    
    /**
     * To perform the down key action
     */
    public void next();
    /**
     * To perform the up key action
     */
    public void previous();
    /**
     * To perform the left key action
     */
    public void left();
    /**
     * To perform the right key action
     */
    public void right();
    
    /**
     * Paints the Focusable Object
     * @param g Graphics object
     */
    public void paint(Graphics g);
    
    /**
     * Runs the required animations
     * @return the waiting time to main thread
     */
    public long run();
}
