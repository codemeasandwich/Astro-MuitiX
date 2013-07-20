package pak_Core;

import processing.core.*;
import pak_Display.*;

public class Core
{
	private PApplet perent;
	
	private Level myLevel;
	private Defender myDefender;
	private KeyboardInput myKeyboard;
	
	public Core(PApplet inputPerent)
	{
		perent = inputPerent;
		myLevel = new Level(this, inputPerent);
		myDefender = new Defender(this, inputPerent);
		myKeyboard = new KeyboardInput(this, inputPerent);
		
		myLevel.addDefender(myDefender);
	}
	
	public void draw()
	{
		perent.background(100);
		
		if (perent.keyPressed)
		{ myKeyboard.test(); }
		
		myLevel.draw();
	}
	
	public void moveDefender(int inputMove)
	{
		myDefender.moveDefender(inputMove);
	}
	
	public void fireDefender()
	{
		myDefender.fireDefender();
	}
}
