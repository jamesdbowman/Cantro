package cantro.entity;
import java.awt.*;
import java.util.*;
import cantro.*;

public class Particle {
	
	public static ArrayList<Particle> particles;
	public static Cantro owner;
	
	public int type;
	public int x;
	public int y;
	public int tarx;
	public int tary;
	public int velx;
	public int vely;
	public int size;
	public int status;
	public boolean hasTarget;
	
	public static final double speed = .1;
	public static final double friction = .2;
	
	public static int maxstatus = 100;
	//0 is a red x
	//1 is smoke
	public Particle(int t, int xx, int yy, int s)
	{
		type = t;
		x = xx+15+(int)(Math.random()*16-8);
		y = yy;
		if(t == 0)y+=50;
		size = s;
		if(t==0)status = maxstatus;
		else if(t==1) status=(int)(Math.random()*100);
		hasTarget = false;
		synchronized(particles){
		particles.add(this);}
	}
	
	public Particle(int t, int xx, int yy, int tx, int ty, int s)
	{
		type = t;
		x = xx+15+(int)(Math.random()*16-8);
		y = yy;
		tarx = tx;
		tary = ty;
		size = s;
		hasTarget = true;
		velx = (int)(Math.random()*40)-20;
		vely = (int)(Math.random()*40)-20;
		if(t==0)status = maxstatus;
		else if(t==1) status=(int)(Math.random()*100);
		else if(hasTarget) status=1;
		synchronized(particles){
		particles.add(this);}
	}
	
	public void update()
	{
		if(!hasTarget)
		{
			if(type == 0)
			{
				status-=2;
				y+=2;
				if(y < 0) y = 0;
			}
			else if(type == 1)
			{
				status--;
				y--;
				if(y < 0) y = 0;
			}
		}
		else
		{
	    	double deltax = tarx-x;
	    	double deltay = tary-y;
	    	double tot = Math.abs(deltax)+Math.abs(deltay);
	    	double xpercent = Math.abs(deltax/tot);
	    	double ypercent = Math.abs(deltay/tot);

	    	x+=velx;
	    	y+=vely;

	    	if(velx > 0) velx-=friction;
	    	else if(velx < 0) velx+=friction;
	    	 x+= deltax*speed*xpercent;

	    	if(vely > 0) vely-=friction;
	    	else if(vely < 0) vely+=friction;
	    	 y+= deltay*speed*ypercent;

	    	if(Math.abs(velx) < 2) velx = 0;
	    	if(Math.abs(vely) < 2) vely = 0;



	    	if(Math.abs(tarx - x) + Math.abs(tary - y) < 10) status = 0;
		}
	}
	
	public static void paintMinusOne(Graphics2D bG, int x, int y, int size, int status)
	{
		Color reset = bG.getColor();
		bG.setColor(new Color(1.0f,0.0f,0.0f,(float)((double)status/(double)maxstatus)));
		bG.drawLine(x-owner.gameScreen.viewedx, y-owner.gameScreen.viewedy, x+size-owner.gameScreen.viewedx, y+size-owner.gameScreen.viewedy);
		bG.drawLine(x+size-owner.gameScreen.viewedx, y-owner.gameScreen.viewedy, x-owner.gameScreen.viewedx, y+size-owner.gameScreen.viewedy);
		bG.setColor(reset);
	}
	public static void paintSmoke(Graphics2D bG, int x, int y, int size, int status)
	{
		Color reset = bG.getColor();
		bG.setColor(new Color(1.0f,1.0f,1.0f,(float)Math.random()));
		bG.fillRect(x-owner.gameScreen.viewedx, y-owner.gameScreen.viewedy, size, size);
		bG.setColor(reset);
	}
	public static void paintCoin(Graphics2D bG, int x, int y, int size, int status)
	{
		Color reset = bG.getColor();
		bG.setColor(new Color(255,120,50));
		bG.fillOval(x-owner.gameScreen.viewedx, y-owner.gameScreen.viewedy, size, size);
		bG.setColor(new Color(255,255,50));
		bG.fillOval(x-owner.gameScreen.viewedx+1, y-owner.gameScreen.viewedy, size, size);
		bG.setColor(reset);
	}
	

}
