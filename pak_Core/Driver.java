package pak_Core;

import processing.core.PApplet;

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
		size(600,450, P3D);
		frameRate(30);
		
		Sys = new Core(this);
	}
	
	public void draw()
	{
		Sys.draw(); 
	}
	
}
