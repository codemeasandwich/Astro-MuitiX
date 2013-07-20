package pak_Display;

import processing.core.PApplet;

public class DefenderShot
{ 
	private float heading;
	private float[] xy;

	public static final int SIZE = 4;
	public static final float speed = 10;
 
	private byte TTL;//-128 to 128
    private static short count = 0;
	private short myCount;
	private final String IP;
	
	public DefenderShot(float heading, float x1, float y1)
	{
		this(heading, x1, y1, "Address Not Set");
	}
	public DefenderShot(float heading, float x1, float y1, String inputIP)
	{
		count++;
		myCount = count;
		IP = inputIP;
		TTL = 25;
		this.heading = heading;
		xy = new float[]{x1,y1};
	}
	
	public void move()
	{
		xy[0] -= PApplet.cos(heading)*speed;
		xy[1] -= PApplet.sin(heading)*speed;
		TTL--;
	}
	
	public void kill()
	{
		TTL = 0;
	}
	
	public float getHeading()
	{
		return heading;
	}
	
	public float[] getXY()
	{
		return xy;
	}

	public void setXY(float[] inputXY)
	{
		xy = inputXY;
	}

	public float getSpeed()
	{
		return speed;
	}

	public int getTTL()
	{
		return TTL;
	}

	public String getIP()
	{
		return IP;
	}

	public short getID()
	{
		return myCount;
	}
	
	@Override
	public String toString()
	{
		return IP+"-"+myCount;
	}

	public static short getTotalShot()
	{
		return count;
	}
}