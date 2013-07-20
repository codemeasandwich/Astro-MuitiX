package pak_logic;

import processing.core.PApplet;
import processing.core.PConstants;
import pak_Core.Core;
import pak_Display.Defender;

public class KeyboardInput
{
	private Core perent;
	private PApplet Display;
	
	public KeyboardInput(Core inputPerent, PApplet inputDisplay)
	{
		perent = inputPerent;
		Display = inputDisplay;
	}
	
	public boolean test()
	{
		//System.out.println("KeyboardInput.test() "+Display.keyCode);
		
		if(PConstants.RIGHT == Display.keyCode)
		{
			perent.moveDefender(Defender.RIGHT);
		}
		else if(PConstants.LEFT == Display.keyCode)
		{
			perent.moveDefender(Defender.LEFT);
		}
		else if(PConstants.UP == Display.keyCode)
		{
			perent.moveDefender(Defender.FORWARE);
		}/*
		else if(PApplet.DOWN == Display.keyCode)
		{
			perent.moveDefender(Defender.BACK);
		}*/
		else if(PConstants.CONTROL == Display.keyCode || 17 == Display.keyCode)//space
		{
			perent.fireDefender();
		}
		//Display.keyCode = -1;
		else if(90 == Display.keyCode)//z
		{
			perent.killDefender();
		}
		else if(81 == Display.keyCode)//q
		{
			perent.zoneInDefender();
		}
		Display.keyPressed = false;
		return false;
		
	}
	
	
}