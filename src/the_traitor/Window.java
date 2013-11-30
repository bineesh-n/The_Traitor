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
public class Window implements FocusableObject {
    
    private Theme t;
    
    public MenuGroup mg;
    
    private Menu markingMenu;
    private Component firstDisplayed, hd, selected;
    private ComponentBuffer cb;
    private String process, ImgUrl = null;
    private scrollBar sb = new scrollBar();
    private boolean scrollBarAttached = false, refreshCB = true, markingOn = false, yaxisAssigned = false;
    
    public int TranselateY = 0, WindowHeight = 0;
    
    public Window(boolean sortingRequired) {
        t = new Theme();
        mg = new MenuGroup();
        mg.opt.parent = this;
        sb.parent = this;
        
        cb = new ComponentBuffer(sortingRequired);
    }
    
    public void setProcess(String process) {
        this.process = process;
    }
    
    public void addMenu(Menu menu) {
        mg.addMenu(menu);
    }
    
    public void removeMenu(Menu menu) {
        mg.removeMenu(menu);
    }
    
    public void addComponent(TextArea ta) {
        
        if(!refreshCB) {
            System.out.println("Failed to add component");
            return;
        }
        
        ta.parent = this;
        cb.add(ta);
    }
    
    public void addComponent(Label label) {
        
        if(!refreshCB) {
            System.out.println("Failed to add component");
            return;
        }
        
        cb.add(label);
    }
    
    public void addComponent(ContactItem citem) {
        
        if(!refreshCB) {
            System.out.println("Failed to add component");
            return;
        }
        
        cb.add(citem);
    }
    
    private void attachScrollBar() {
        
        sb.setParams(Theme.HEIGHT - (2*t.getFHeight()) , WindowHeight);
        sb.setCoOrdinates(Theme.WIDTH-4, t.getFHeight());
    
    }
    
    public long run() {
        
        if(scrollBarAttached) {
            return sb.run();
        }
        return 500L;
    }

    public Menu getLSMenu() {
        return mg.left_soft;
    }

    public Menu getRSMenu() {
        return mg.right_soft;
    }

    public Menu getFireMenu() {
            return mg.fire;
    }

    public void next() {
        
        if(hd == null) {
            return;
        }
        
        if(!selected.focused) {
            selected = firstDisplayed;
        }
        else {
            selected.goInactive();
            selected = selected.next;
        }
        
        if(selected == null) {
            selected = hd;
        }
        
        if(markingOn) {
            if(((ContactItem)selected).isMarked()) {
                markingMenu.setName("Unmark");
            }
            else {
                markingMenu.setName("Mark");
            }
        }
        
        selected.setActive();
    }
    
    public void previous(){
        
        if(hd == null) {
            return;
        }
        
        selected.goInactive();
        selected = selected.prev;
        
        if(markingOn) {
            if(((ContactItem)selected).isMarked()) {
                markingMenu.setName("Unmark");
            }
            else {
                markingMenu.setName("Mark");
            }
        }
        
        selected.setActive();
        
    }

    public void paint(Graphics g) {
        
        t.setGraphics(g);
        if(ImgUrl == null) {
            t.paintBody();
        }
        else {
            t.paintBody(ImgUrl);
        }
        
        maintainScrollBar();
        
        g.translate(0, TranselateY);
        
        if(refreshCB) {
            hd = cb.getComponents();
            refreshCB = false;
        }
        
        Component header = hd;
        
        boolean firstDisplayedSet = false;
        while(header != null) {
            
            assignY(header);
            
            if((!header.focused || !header.isFocusable()) && isShown(header)) {
                
                if(!firstDisplayedSet) {
                    if(selected == null) {
                        selected = header;
                        selected.setActive();
                    }
                    firstDisplayed = header;
                    firstDisplayedSet = true;
                }
                
                header.paint(g);
                
            }
            
            if(header.focused && scrollBarAttached) {
                    sb.makeVisible(header);
            }
            
            header = header.next;
        }
        
        g.translate(0, -TranselateY);
        
        if(Traitor.focused == this && scrollBarAttached) {
            sb.paint(g);
        }
        
        t.showProcess(process);
        
        mg.paintMenu(t);
    }

    public void left() {
        
        if(scrollBarAttached) {
            sb.moveTo = scrollBar.PREV_PAGE;
        }
        
    }

    public void right() {
        
        if(scrollBarAttached) {
            sb.moveTo = scrollBar.NEXT_PAGE;
        }
        
    }
    /**
     * maintains the window scrollbar
     */
    private void maintainScrollBar() {
        
        if(!yaxisAssigned) {
            return;
        }
        
        if(WindowHeight > (Theme.HEIGHT-2*t.getFHeight()) && !scrollBarAttached) {
            attachScrollBar();
            scrollBarAttached = true;
        }
    }
    /**
     * 
     * @param cmp checks its visibility to screen
     * @return true if eligible to paint
     */
    public boolean isShown(Component cmp) {
        
        int cmpy = TranselateY + cmp.y;
        if(cmpy < Theme.HEIGHT - t.getFHeight() && (cmpy + cmp.getHeight()) >= t.getFHeight()) {
            return true;
        }
        
        return false;
    }
    /**
     * 
     * @param com component whose y axis is to be determined
     */
    private void assignY(Component com) {
        
        if(yaxisAssigned) {
            return;
        }
        
        Component ptr = hd;
        int ht = Theme.getInstance().getFHeight(), sum = ht;
        while(ptr != null) {
            
            ptr.y = sum;
            sum += ptr.getHeight();
            
            if(ptr == com) {
                WindowHeight = sum;
                
                if(ptr.next == null) {
                    yaxisAssigned = true;
                }
                
                break;
            }
            
            ptr = ptr.next;
            
        }
        
    }
    /**
     * 
     * @param com component which is to be deleted
     */
    public void deleteComponent(Component com) {
        
        if(com == null) {
            return;
        }
        
        ((ContactItem)com).deleteContactItem();
        Component cm = hd;
        
        while(cm != null) {
            if(cm == com) {
                if(com == hd) {
                    hd = hd.next;
                    if(hd == null) {
                        selected = null;
                        break;
                    }
                    hd.prev = com.prev;
                    break;
                }
                if(cm.prev != null) {
                    cm.prev.next = cm.next;
                }
                if(cm.next != null) {
                    cm.next.prev = cm.prev;
                }
                else {
                    hd.prev = cm.prev;
                }
                break;
            }
            cm = cm.next;
        }
        
        yaxisAssigned = false;
        scrollBarAttached = false;
    }
    /**
     * 
     * @return component focused
     */
    public Component getComponentFocused() {
        
        return selected;
        
    }
    /**
     * 
     * @return Header Component
     */
    public Component getHeaderComponent() {
        return hd;
    }
    /**
     * 
     * @param markingMenu Mark menu to toggle "Mark" and "Unmark"
     */
    public void markItem(Menu markingMenu) {
       
        if(selected != null) {
            ContactItem csel = (ContactItem)selected;
            if(csel.isMarked()) {
                csel.setMarking(false);
                markingMenu.setName("Mark");
            }
            else {
                csel.setMarking(true);
                this.markingMenu = markingMenu;
                markingMenu.setName("Unmark");
                markingOn = true;
            }
        }
    }
    /**
     * unmarks all components
     */
    public void unmarkAll() {
        ContactItem com = (ContactItem)hd;
        while(com != null) {
            com.setMarking(false);
            com = (ContactItem)com.next;
        }
        markingOn = false;
    }
    /**
     * 
     * @return true when marking is on
     */
    public boolean isMarkingOn() {
        return markingOn;
    }
    /**
     * 
     * @param p Prompt is to be attached
     */
    public void attachPrompt(Prompt p) {
        p.parent = this;
    }
    /**
     * 
     * @param url image url
     */
    public void setBGImage(String url) {
        ImgUrl = url;
    }
}