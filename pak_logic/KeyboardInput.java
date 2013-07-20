package pak_logic;

import processing.core.PConstants;
import pak_Display.Defender;

public class KeyboardInput
{
	private Defender myDefender;
	
	public KeyboardInput()
	{
	}
	
	public boolean test(int keyCode)
	{
		//System.out.println("KeyboardInput.test() "+keyCode);
		
		boolean foundKey = false;
		
		switch(keyCode)
		{
		
		case(PConstants.RIGHT):
			myDefender.moveDefender(Defender.RIGHT);
			foundKey = true;
		break;
		
		case(PConstants.LEFT):
			myDefender.moveDefender(Defender.LEFT);
			foundKey = true;
		break;
		
		case(PConstants.UP):
			myDefender.moveDefender(Defender.FORWARE);
			foundKey = true;
		break;
		
		case(PConstants.CONTROL):
			myDefender.fireDefender();
			foundKey = true;
		break;
		
		case(81):
			myDefender.zoneIn();
			foundKey = true;
		break;
		
		case(112)://F1
			System.exit(1);
		break;
		
		}
		return foundKey;
		
	}
	public boolean setMyDefender(Defender inputDefender)
	{
		if(null == myDefender)
		{
			myDefender = inputDefender;
			return true;
		}
		else
		{	return false;	}
	}
	
	public void KeyMap()
	{
		System.out.println("F1: Exit");
		System.out.println("Q:  Find Ship");
	}
	
}
