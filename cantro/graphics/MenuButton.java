package cantro.graphics;

public class MenuButton {
	
	public int x;
	public int y;
	public int width;
	public int height;
	public String text;
	public int fontSize;
    
    public MenuButton(int xx, int yy, int w, int h, String t, int fs)
    {
    	x = xx;
    	y = yy;
    	width = w;
    	height = h;
    	text = t;
    	fontSize = fs;
    }
    
    public boolean clickInside(int cx, int cy) //Is a point clicked inside this button?
    {
    	return (cx >= x && cx <= (x+width)) && (cy >= y && cy <= (y+height));
    }
    
    
}