package pak_Core;

import processing.core.PApplet;
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
		//System.out.println(Display.keyCode);
		
		if(PApplet.RIGHT == Display.keyCode)
		{
			perent.moveDefender(Defender.RIGHT);
		}
		else if(PApplet.LEFT == Display.keyCode)
		{
			perent.moveDefender(Defender.LEFT);
		}
		else if(PApplet.UP == Display.keyCode)
		{
			perent.moveDefender(Defender.FORWARE);
		}/*
		else if(PApplet.DOWN == Display.keyCode)
		{
			perent.moveDefender(Defender.BACK);
		}*/
		else if(0 == Display.keyCode)//space
		{
			perent.fireDefender();
		}
		//Display.keyCode = -1;
		
		Display.keyPressed = false;
		return false;
		
	}
	
	
}
