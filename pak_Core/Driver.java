package pak_Core;

import processing.core.PApplet;
//import processing.core.PFont;

public class Driver extends PApplet // hit <ctrl>-1
{
	private static final long serialVersionUID = 1L;
	private static Core Sys;

	
	public static void main(String[] args)
	{
		PApplet.main(args);
	}

	@Override
	public void setup()
	{
		size(640,480, P3D);
		frameRate(24);
		Sys = new Core(this);
		System.out.println("All Done");
		System.out.println();
		System.out.println("======================================");
		System.out.println();
	}
	
	@Override
	public void draw()
	{
		Sys.draw(); 
	}

}
