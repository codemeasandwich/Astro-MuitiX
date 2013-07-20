package pak_Core;

import processing.core.PApplet;
import processing.core.PFont;

public class Driver extends PApplet
{
	private static final long serialVersionUID = 1L;
	private static Core Sys;
	
	public static void main(String[] args)
	{
		PApplet.main(args);
	}

	public void setup()
	{
		size(600,600, P3D);
		frameRate(30);
		Sys = new Core(this);
	}
	
	public void draw()
	{
		Sys.draw(); 
	}
	
}
