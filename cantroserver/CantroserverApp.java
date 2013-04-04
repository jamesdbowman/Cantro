/*
 * CantroserverApp.java
 */

package cantroserver;

import cantroserver.view.*;
import cantroserver.networking.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class CantroserverApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    public NetworkController networkController;
    public CantroserverView view;
    public boolean online;
    public String roomName;
    public String roomPass;
    public BufferedImage skeletonImage;


    @Override protected void startup() {
        view = new CantroserverView(this);
        show(view);
        online = false;
        try
    	{
            File ff = new File("cantroserver/resources/GrasslandSkeleton.png");
    		skeletonImage = ImageIO.read(ff);
    	}
    	catch(Exception e){e.printStackTrace();}
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of CantroserverApp
     */
    public static CantroserverApp getApplication() {
        return Application.getInstance(CantroserverApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(CantroserverApp.class, args);
    }

        public int getRGBofSkeleton(int x, int y)
    {
    	if(x < 0) x = 0;
    	if(x >= skeletonImage.getWidth()) x = skeletonImage.getWidth()-1;
    	if(y < 0) y = 0;
    	if(y >= skeletonImage.getHeight()) y = skeletonImage.getHeight()-1;
    	return skeletonImage.getRGB(x,y);//+viewedx, y+viewedy);

    }

    public void getRGBofSkeleton(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize)
    {
    	int yoff = offset;
    	int off;
    	for (int y = startY; y < startY+h; y++, yoff+=scansize) {
    		off = yoff;
    		for (int x = startX; x < startX+w; x++) {
    			int xwithoff = x;
    			int ywithoff = y;
    			if(xwithoff >= 0 && xwithoff < skeletonImage.getWidth() && ywithoff >= 0 && ywithoff < skeletonImage.getHeight())
    				rgbArray[off++] = skeletonImage.getRGB(x,y);
    			else
    				rgbArray[off++] = -65281;
    		}
    	}
    }

    public void LaunchServer()
    {
        if(online == false)
        {
            online = true;
            networkController = new NetworkController(this);
            view.jLabel2.setForeground(Color.GREEN.darker());
            view.jLabel2.setText("Server is online.");
            roomName = view.launchBox.jTextField1.getText();
            roomPass = view.launchBox.getPassword();
            networkController.startThreads();
        }
    }
}
