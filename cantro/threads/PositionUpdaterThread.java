package cantro.threads;

import java.util.ArrayList;

import cantro.*;
import cantro.entity.*;
import cantro.networking.Commands;
import cantro.util.*;

public class PositionUpdaterThread extends Thread{
	
	Cantro owner;
	
	public PositionUpdaterThread(Cantro c)
	{
		owner = c;
	}
	
	public void run()
	{
		while(true)
		{
			owner.connection.send(StringUtil.CombineWithColons(Commands.POSUPDATE.val,owner.p.UID,owner.p.x,owner.p.y));
			try
			{
				Thread.sleep(30);
			}catch(Exception e){}
		}
	}
}
