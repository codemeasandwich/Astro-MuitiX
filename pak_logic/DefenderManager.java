package pak_logic;

import java.util.ArrayList;
import pak_Display.Defender;

public class DefenderManager
{
	private Defender myDefender;
	private ArrayList<Defender> arrayDefender;
	
	public DefenderManager()
	{}
	
	public void draw()
	{
		myDefender.draw();
	}
	
	public boolean setMyDefender(Defender inputDefender)
	{
		if(null == myDefender)
		{
			myDefender = inputDefender;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Defender getMymyDefender()
	{
		return myDefender;
	}
	
	public void addDefender(Defender inputDefender)
	{
		arrayDefender.add(inputDefender);
	}
	
	public void moveFakeDefender()
	{
		
	}
	
}