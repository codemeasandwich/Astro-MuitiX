package pak_Display;

import processing.core.PApplet;
import pak_Core.Core;
import java.util.ArrayList;

public class Level
{
	private PApplet Display;
	private Core Perent;
	private ArrayList<Defender> arrayDefender;
	
	public Level(Core inputPerent, PApplet inputDisplay)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		
		arrayDefender = new ArrayList<Defender>();
	}
	
	public void draw()
	{
		//Display.directionalLight(126, 126, 126, 0, 0, -500 + Display.frameCount);
		Display.ambientLight(255,255,255);
		
		for/*each*/ (Defender Spaceship: arrayDefender)
		{
			Spaceship.draw();
		}
	}
	
	public void addDefender(Defender inputDefender)
	{
		arrayDefender.add(inputDefender);
	}
}
