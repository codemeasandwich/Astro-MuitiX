package pak_Display;

import processing.core.PApplet;

public class DefenderShot
{
	private float heading;
	private int[] xy;
	public static final int SIZE = 5;
	public static final float speed = 8.0f;
	private byte TTL;//-128 to 128
	
	
	public DefenderShot(float heading, int x1, int y1)
	{
		TTL = 75;
		this.heading = heading;
		xy = new int[]{x1,y1};
	}
	
	public void move()
	{
		xy[0] -= PApplet.cos(heading)*speed;
		xy[1] -= PApplet.sin(heading)*speed;
		TTL--;
	}
	
	public float getHeading()
	{
		return heading;
	}
	
	public int[] getXY()
	{
		return xy;
	}
	public void setXY(int[] inputXY)
	{
		xy = inputXY;
	}
	public float getSpeed()
	{
		return speed;
	}
	
	public void setTTL(int inputnum)
	{
		TTL = (byte)inputnum;
	}
	
	public int getTTL()
	{
		return TTL;
	}
}