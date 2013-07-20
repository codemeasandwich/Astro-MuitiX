package pak_Core;

import fullscreen.*;
import processing.core.PApplet;
import processing.core.PFont;

public class Driver extends PApplet // hit <ctrl>-1
{
	private static final long serialVersionUID = 1L;
	private static Core Sys;
	private FullScreen fs;
	
	public static void main(String[] args)
	{
		PApplet.main(args);
	}

	@Override
	public void setup()
	{
		size(640,480, P3D);
		frameRate(24);
		  // Create the fullscreen object

		Sys = new Core(this);
		System.out.println("All Done");
		System.out.println();
		System.out.println("======================================");
		System.out.println();
		//  fs = new FullScreen(this); 
		  
		  // enter fullscreen mode
		//  fs.enter();
	}
	
	@Override
	public void draw()
	{
		Sys.draw(); 
	}

}
