package pak_logic;

import processing.core.PApplet;
import processing.core.PConstants;
import pak_Core.Core;
import pak_Display.Defender;

public class KeyboardInput
{
	private Core perent;
	
	public KeyboardInput(Core inputPerent, PApplet inputDisplay)
	{
		perent = inputPerent;
	}
	
	public boolean test(int keyCode)
	{
		//System.out.println("KeyboardInput.test() "+keyCode);
		
		boolean foundKey = false;
		
		switch(keyCode)
		{
		case(PConstants.RIGHT):
			perent.moveDefender(Defender.RIGHT);
			foundKey = true;
		break;
		
		case(PConstants.LEFT):
			perent.moveDefender(Defender.LEFT);
			foundKey = true;
		break;
		
		case(PConstants.UP):
			perent.moveDefender(Defender.FORWARE);
			foundKey = true;
		break;
		
		//case(17)://space
		case(PConstants.CONTROL):
			perent.fireDefender();
			foundKey = true;
		break;
		
		case(90):
			perent.killDefender();
			foundKey = true;
		break;
		
		case(81):
			perent.zoneInDefender();
			foundKey = true;
		break;
		
		case(112)://F1
			perent.showMenuToggle();
			foundKey = true;
		break;
		
		}
		return foundKey;
		
	}
	
	
}
