		/**
 * @(#)cantro.java
 *
 *
 * @author
 * @version 1.00 2011/1/20
 */
package cantro;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;

import cantro.graphics.*;
import cantro.entity.*;
import cantro.graphics.GameScreen;
import cantro.input.*;
import cantro.threads.*;
import cantro.networking.*;

import java.util.*;

public class Cantro implements KeyListener, MouseListener, MouseMotionListener{
	
	public static JFrame cantroFrame;
	public Player p;
	public GameScreen gameScreen;
	public ChatScreen chatPanel;
	public MouseInput mouseInput;
	public BulletCheckerThread bulletCheckerThread;
	public PositionUpdaterThread posUpdaterThread;
	public GameConnection connection;
	
	public static Cursor blankCursor;
	public static Cursor crosshairCursor;
	
    public Cantro()
    {
    	p = new Player("James",0,false,this);
    	for(int i = 0; i < 20; i++)
    	{
    		Zombie.zombies.add(new Zombie((int)(Math.random()*700+200),(int)(Math.random()*200+200),this));
    	}
    	mouseInput = new MouseInput();

    	
    	bulletCheckerThread = new BulletCheckerThread(this);
    	bulletCheckerThread.setDaemon(true);
    	
    	posUpdaterThread = new PositionUpdaterThread(this);
    	posUpdaterThread.setDaemon(true);
    }

    public void delay(int n) throws InterruptedException
	{
		Thread.sleep(n);
		gameScreen.repaint();
		//chatPanel.repaint();
	}
	public void update()
	{
		if(gameScreen.status == Status.MENU || gameScreen.status == Status.MENUSERVERBROWSER)
		{
			gameScreen.applyInput(mouseInput);
		}
		else if(gameScreen.status == Status.SINGLEPLAYERGAME || gameScreen.status == Status.MULTIPLAYERGAME)
		{
			p.applyInput(mouseInput);
			p.momentum();
			synchronized(Player.players)
			{
				for(Player op : Player.players)
				{
					op.applyInput();
					op.momentum();
				}
			}
			synchronized(Zombie.zombies)
			{
				int lim = Zombie.zombies.size();
				for(int i = 0; i < lim; i++)
				{
					Zombie.zombies.get(i).AICycle();
					if(Zombie.zombies.get(i).health <= 0)
					{
						Zombie z = Zombie.zombies.remove(i);
						i--;lim--;
						for(int c = 0; c < 5; c++)
						{
							synchronized(Particle.particles)
							{
								new Particle(2,z.x,z.y,50,50,7);
							}
						}
					}
				}
			}
			synchronized(Particle.particles)
			{
				int lim = Particle.particles.size();
				for(int i = 0; i < lim; i++)
				{
					Particle.particles.get(i).update();
					if(Particle.particles.get(i).status <= 0)
					{
						Particle.particles.remove(i);
						i--;lim--;
					}
				}
			}
			if(gameScreen.status == Status.MULTIPLAYERGAME)
			if(p.input.change){
				connection.send(p.generateInputUpdateToSend());
				
				p.input.change = false;
			}
		}
		try
		{
			delay(15);
		}
		catch(InterruptedException e){}
	}

    public static void main(String[] args) throws Exception
    {
    	Player.players = new ArrayList<Player>();
    	Zombie.zombies = new ArrayList<Zombie>();
    	Particle.particles = new ArrayList<Particle>();

    	
    	Cantro c = new Cantro();
    	
    	Particle.owner = c;
    	
    	//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    	
    	//c.chatPanel = new ChatScreen(c);
    	c.gameScreen = new GameScreen(c);
    	
    	JFrame cantroFrame = new JFrame("Cantro");
    	
    	byte[]imageByte=new byte[0];  
    	//Create image for cursor using empty array  
    	Image blankCursorImage=Toolkit.getDefaultToolkit().createImage(imageByte);
    	blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImage,new Point(0,0),"cursor");
    	Image crosshairCursorImage = Toolkit.getDefaultToolkit().getImage("cantro/resources/images/crosshair.png");
    	crosshairCursor = Toolkit.getDefaultToolkit().createCustomCursor(crosshairCursorImage,new Point(18,18),"cursor");

    	c.gameScreen.setCursor(crosshairCursor);
    	c.gameScreen.addKeyListener(c);
    	c.gameScreen.addMouseListener(c);
    	c.gameScreen.addMouseMotionListener(c);
    	cantroFrame.getContentPane().setLayout(new BoxLayout(cantroFrame.getContentPane(),BoxLayout.Y_AXIS));
    	cantroFrame.getContentPane().setSize(800,600);
    	cantroFrame.getContentPane().add(c.gameScreen);
    	//j.getContentPane().add(c.chatPanel);
    	cantroFrame.setVisible(true);
    	cantroFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	cantroFrame.pack();
    	
    	c.gameScreen.requestFocusInWindow();
    	
    	c.gameScreen.repaint();
    	//c.chatPanel.repaint();
    	
    }

    public void keyReleased(KeyEvent ke)
    {
        int kc = ke.getKeyCode();
    	if(kc == KeyEvent.VK_RIGHT || kc == 68)
    		p.input.remKey(Input.RIGHT);
    	if(kc == KeyEvent.VK_LEFT || kc == 65)
    		p.input.remKey(Input.LEFT);
    	if(kc == KeyEvent.VK_UP || kc == 87)
    		p.input.remKey(Input.UP);
    	if(kc == KeyEvent.VK_DOWN || kc == 83)
    		p.input.remKey(Input.DOWN);
    }
    public void keyPressed(KeyEvent ke)
    {
        int kc = ke.getKeyCode();
    	if(kc == KeyEvent.VK_RIGHT || kc == 68)
    		p.input.addKey(Input.RIGHT);
    	if(kc == KeyEvent.VK_LEFT || kc == 65)
    		p.input.addKey(Input.LEFT);
    	if(kc == KeyEvent.VK_UP || kc == 87)
    		p.input.addKey(Input.UP);
    	if(kc == KeyEvent.VK_DOWN || kc == 83)
    		p.input.addKey(Input.DOWN);
        System.out.println(kc);
    }
    public void keyTyped(KeyEvent ke)
    {
    	
    }
    public void mouseClicked(MouseEvent me)
    {
    	mouseInput.click(me);
    }
    public void mouseEntered(MouseEvent me)
    {

    }
    public void mouseExited(MouseEvent me)
    {

    }
    public void mousePressed(MouseEvent me)
    {
    	mouseInput.down = true;
    	mouseInput.click(me);
    }
    public void mouseReleased(MouseEvent me)
    {
    	mouseInput.down = false;
    }
    public void mouseDragged(MouseEvent me)
    {
    	mouseInput.move(me);
    	
    }
    public void mouseMoved(MouseEvent me)
    {
    	mouseInput.move(me);
    }


}
