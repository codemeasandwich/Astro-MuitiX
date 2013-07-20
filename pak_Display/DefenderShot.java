package pak_Display;

import java.io.Serializable;
import processing.core.PApplet;

public class DefenderShot implements Serializable
{ 
	private float heading;
	private int[] xy;

	public static final int SIZE = 4;
	public static final float speed = 10;
 
	private byte TTL;//-128 to 128
    private static short count = 0;
	private short myCount;
	private final String IP;
	
	public DefenderShot(float heading, int x1, int y1)
	{
		this(heading, x1, y1, "Address Not Set");
	}
	public DefenderShot(float heading, int x1, int y1, String inputIP)
	{
		count++;
		myCount = count;
		IP = inputIP;
		TTL = 25;
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
	/*
	public void setTTL(int inputnum)
	{
		TTL = (byte)inputnum;
	}
	*/
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