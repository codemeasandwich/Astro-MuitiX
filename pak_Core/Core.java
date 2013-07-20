package pak_Core;

import java.util.Random;

import processing.core.*;
import pak_Display.*;
import pak_Net.*;

public class Core
{
	private PApplet perent;
	private final String GroupIP;
	private final int GroupPort;
	private Level myLevel;
	private Defender myDefender;
	private KeyboardInput myKeyboard;
	private Random rn;
	private NetworkInterface net;
	
	public Core(PApplet inputPerent)
	{
		perent = inputPerent;
		rn = new Random();
		
		GroupIP = "224.0.41.0";
		//http://www.iana.org/assignments/multicast-addresses
		GroupPort = 9574;
		
		net = new NetworkInterface(this);
		
		myLevel = new Level(this, inputPerent);
		myDefender = new Defender(this, inputPerent);
		myKeyboard = new KeyboardInput(this, inputPerent);
		
		myLevel.addDefender(myDefender);
	}
	
	public void draw()
	{
		if(perent.keyPressed && !myDefender.getkilled())
		{ myKeyboard.test(); }
		
		myLevel.draw();
	}
	
	public int getRandom(int range)
	{
		return rn.nextInt(range);
	}
	
	public void moveDefender(int inputMove)
	{
		myDefender.moveDefender(inputMove);
	}
	
	public void fireDefender()
	{
		myDefender.fireDefender();
	}
	public void killDefender()
	{
		myDefender.killDefender();
	}
	public float[] spaceReset(float[] XY)
	{
		return myLevel.spaceReset(XY);
	}
	public void zoneInDefender()
	{
		myDefender.zoneIn();
	}
	public String getGroupIP()
	{
		return GroupIP;
	}
	public int getGroupPort()
	{
		return GroupPort;
	}
	public void SendDefenderLocation(int x, int y, float heading)
	{
		net.SendDefenderLocation(x, y, heading);
	}
}
