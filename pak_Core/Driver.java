package pak_Core;
  
import processing.core.PApplet;

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
		size(500,400, P3D);
		frameRate(24);
		Sys = new Core(this);
	}
	
	@Override
	public void draw()
	{
		Sys.draw(); 
	}
}
