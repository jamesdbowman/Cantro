/**
 * @(#)MouseInput.java
 *
 *
 * @author 
 * @version 1.00 2010/9/16
 */
package cantro.input;

import java.awt.event.*;

public class MouseInput {
	
	public int hoverx;
	public int hovery;
	public int clickx;
	public int clicky;
	public boolean down;

    public MouseInput() {
    	down = false;
    }
    
    public void click(MouseEvent me)
    {
    	clickx = me.getX();
    	clicky = me.getY();
    }
    
    public void move(MouseEvent me)
    {
    	hoverx = me.getX();
    	hovery = me.getY();
    }
    
    
}