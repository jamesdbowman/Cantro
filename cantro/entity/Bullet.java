package cantro.entity;

public class Bullet
{
	public Player shooter;
	public double rotation;
	public double x;
	public double y;
	public Bullet(Player s, double rot)
	{
		shooter = s;
		rotation = rot;
		x = s.x+s.xShootOff;
		y = s.y+s.yShootOff;
	}
	public void travel()
	{
		x+=20*Math.cos(rotation);
		y+=20*Math.sin(rotation);
	}
    public boolean isStillOnScreen()
    {
    	boolean bounds = x > 0 && x < shooter.owner.gameScreen.width && y > 0 && y < shooter.owner.gameScreen.height;
    	boolean pink = false;
    	if(bounds)
    	pink = shooter.owner.gameScreen.getRGBofSkeletonWithoutBoundCheck((int)x,(int)y) == -65281;
    	return bounds && !pink;
    }
}
