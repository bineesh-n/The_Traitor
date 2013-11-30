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

public class scrollBar {
    
    private int height, markHeight, width = 3, Target, scrollInterval = 20;
    private int x, y, my, parentTarget;
    private float ratio;
    private long Timout = 10L;
    private boolean TargetSet = false;
    public boolean visible = true;
    public Window parent;
    
    public static final int NORMAL_RUN = -1;
    public static final int NEXT_PAGE = -2;
    public static final int PREV_PAGE = -3;
    public static final int TARGET = -4;
    
    public int moveTo = NORMAL_RUN;
    
    public void setParams(int AllowedHeight, int ActualHeight) {
     //   System.out.println(AllowedHeight+","+ActualHeight);
        height = AllowedHeight;
        markHeight = (AllowedHeight*AllowedHeight)/ActualHeight;
        ratio = (float)ActualHeight/(float)AllowedHeight;
       // System.out.println(ratio);
    }
    
    public void setCoOrdinates(int x, int y) {
        this.x = x;
        this.y = y;
        my = y;
    }
    
    public void paint(Graphics g) {
        
        if(visible) {
            Theme t = new Theme();
            t.setGraphics(g);
        
            t.drawScrollBar(x, y, width, height, x, my, markHeight);
        }
    }
    
    public long run() {
        
        if(moveTo != NORMAL_RUN) {
            Timout = 1L;
        }
        
        synchronized(this) {
            try {
                wait(Timout);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(moveTo == NEXT_PAGE) {
            
            if(!visible) {
                visible = true;
            }
            
            if(!TargetSet) {
                setNextPageTarget();
            }
            else {
                
                if(my < Target) {
                    scrollUp();  
                    return 1L;
                }
                else {
                    endScrolling();
  //                  System.out.println("At last ty:"+parent.TranselateY);
                    Component ParentFocused = parent.getComponentFocused();
                    if(ParentFocused != null) {
            
                        if(!parent.isShown(ParentFocused)) {
                            ParentFocused.goInactive();
                        }
                    }
                }
            }
        }
        else if(moveTo == PREV_PAGE) {
            
            if(!visible) {
                visible = true;
            }
            
            if(!TargetSet) {
                setPrevPageTarget();
            }
            else {
                
                if(my > Target) {
                    
                    scrollDown();
                    
                    return 1L;
                }
                else {
                    endScrolling();
                    Component ParentFocused = parent.getComponentFocused();
                    if(ParentFocused != null) {
            
                        if(!parent.isShown(ParentFocused)) {
                            ParentFocused.goInactive();
                        }
                    }
                }
            }
        }
        else if(moveTo == TARGET) {
            
            if(!visible) {
                visible = true;
            }
            
            if(Target > my) {
                scrollUp();
                return 1L;
            }
            else if (Target < my) {
                scrollDown();
                return 1L;
            }
            else {
                endScrolling();
//                System.out.println("At last ty:"+parent.TranselateY);
            }
        }
        
        if(visible) {
            visible = false;
            Timout = 10L;
        }
        
        return 10L;
    } 
    
    private void setNextPageTarget() {
        
        Theme t = new Theme();
        int target = my + markHeight;
        int ptarget = parent.TranselateY - (Theme.HEIGHT - 2*t.getFHeight());
        
        int maxptarget = -(parent.WindowHeight - (Theme.HEIGHT - t.getFHeight()));
        if(ptarget < maxptarget) {
            Target = y + height - markHeight;
            parentTarget = maxptarget;
        }
        else {
            Target = target;
            parentTarget = ptarget;
        }
                
        TargetSet = true;
        int ptg = (parent.TranselateY - parentTarget)/5;
        scrollInterval = (ptg > 0)? ptg : 1;        
    }
    
    private void scrollUp() {
        
        int ty = parent.TranselateY - (int)(ratio*scrollInterval);
        int tmy = my + scrollInterval;
        
        if(ty <= parentTarget) {
            parent.TranselateY = parentTarget;
        }
        else {
            parent.TranselateY = ty;
        }
        
        if(tmy >= Target) {
            my = Target;
            parent.TranselateY = parentTarget;
        }
        else {
            my += scrollInterval;
        }
        
    }
    
    private void setPrevPageTarget() {
        
        Theme t = new Theme();
        int target = my - markHeight;
        int ptg = parent.TranselateY + (Theme.HEIGHT - 2 * t.getFHeight());
        
        if(ptg > 0) {
            Target = y;
            parentTarget = 0;
        }
        else {
            Target = target;
            parentTarget = ptg;
        }
                
        TargetSet = true;
        scrollInterval = 20;
                
    }
    
    private void scrollDown() {
        
        int ty = parent.TranselateY + (int)(ratio*scrollInterval);
        int tmy = my - scrollInterval;
        
        if(ty >= parentTarget) {
            parent.TranselateY = parentTarget;
        }
        else {
            parent.TranselateY = ty;
        }
        
        if(tmy <= Target) {
            my = Target;
            parent.TranselateY = parentTarget;
        }
        else {
            my -= scrollInterval;
        }
    }
    
    private void endScrolling() {
        moveTo = NORMAL_RUN;
        TargetSet = false;
        Timout = 10L;
    }
    
    public void makeVisible(Component com) {
        
        if(TargetSet) {
            return;
        }
        
        int ht = com.getHeight(), vy = com.y;
        int WindowTransition = parent.TranselateY;
        int ActualPosition = WindowTransition + vy;
        
        if(ActualPosition + ht > y+height) {
            
            if(ActualPosition < y) {
                return;
            }
            
            parentTarget = y + height - (ht + vy);
            int target = y - (int)((parentTarget)/ratio);
            
          //  System.out.println("\nTargetY:"+parentTarget);
            Target = target;
            moveTo = TARGET;
            TargetSet = true;
            
            int interval = (Target - my)/3;
            scrollInterval = (interval > 0) ? interval : 1;
            
        }
        else if(ActualPosition < y) {
            
            if(com.getHeight() > height) {
                return;
            }
            
            parentTarget = y - vy;
            Target = y - (int)(parentTarget/ratio);
            moveTo = TARGET;
            TargetSet = true;
         //   System.out.println("TargetY:"+parentTarget);
            int interval = (my - Target)/3;
            scrollInterval = (interval > 0) ? interval : 1;
        }
        
    }
}
