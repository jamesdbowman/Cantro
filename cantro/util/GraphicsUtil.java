/**
 * @(#)GraphicsUtil.java
 *
 *
 * @author
 * @version 1.00 2010/8/31
 */
package cantro.util;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;

public class GraphicsUtil {

	public GraphicsUtil() {
	}
	//degree input
	public static BufferedImage rotate(BufferedImage img, int angle) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.rotate(Math.toRadians(angle), w/2, h/2);
		g.drawImage(img, null, 0, 0);
		return dimg;
	}
	//radian input
	public static BufferedImage rotate(BufferedImage img, double angle) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.rotate(angle, w/2, h/2);
		g.drawImage(img, null, 0, 0);
		return dimg;
	}

	public static Color ChangeTransparency(Color c, double n)
	{
		return new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(n*255));
	}

	public static Color randBrightColor()
    {
    	return new
    	Color(	(float)Math.random()/2+.5f,
    			(float)Math.random()/2+.5f,
    			(float)Math.random()/2+.5f);
    }

	public static Color randColor()
    {
    	return new
    	Color(	(float)Math.random(),
    			(float)Math.random(),
    			(float)Math.random());
    }

    public static Color inverseColor(Color c)
    {
    	return new
    	Color(	255-c.getRed(),
    			255-c.getGreen(),
    			255-c.getBlue());
    }

    public static int[] centeringText(String s, Font f, int width, int height, Graphics2D g)
	{
		//Interesting centering in a box, using FontMetrics to see how big our font is.
		FontMetrics fm = g.getFontMetrics(f);
		Rectangle2D rect = fm.getStringBounds(s, g);
		int[] ret = new int[2];
		ret[0] = (width  - (int)(rect.getWidth()))  / 2;
		ret[1] = (height - (int)(rect.getHeight())) / 2 + fm.getAscent();
		//ret has [x,y];
		return ret;
	}
}