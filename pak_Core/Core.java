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
	private String LocalAddress;
	private final String Title;
	private final String version;
	private boolean keyPressed;
	
	public Core(PApplet inputPerent)
	{
			perent = inputPerent;
			rn = new Random();
			keyPressed = false;
			GroupIP = "224.0.41.0";
			//http://www.iana.org/assignments/multicast-addresses
			GroupPort = 9574;
			
			version = "003";
			Title = "Astro-MultiX";
				
			net = new NetworkInterface(this);
			
			myLevel = new Level(this, inputPerent);
			myDefender = new Defender(this, inputPerent,LocalAddress);
			myKeyboard = new KeyboardInput(this, inputPerent);
			
			addDefender(myDefender);
	}
	
	public void draw()
	{
		keyPressed = false;
		
		if(perent.keyPressed && !myDefender.getkilled())
		{
			myKeyboard.test(); 
			perent.keyPressed = false;
			keyPressed = true;
		}
		
		myLevel.draw();
	}
	
	public int getRandom(int range)
	{
		return rn.nextInt(range);
	}
	public void addDefender(Defender newDefender)
	{
		myLevel.addDefender(myDefender);
	}
	public void addDefender(String ID)
	{
		myLevel.addDefender(new Defender(this,perent,ID));
	}
	public void moveDefender(byte inputMove)
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
	public int[] spaceReset_Int(int[] XY)
	{
		return myLevel.spaceReset_Int(XY);
	}
	public float[] spaceReset_Float(float[] XY)
	{
		return myLevel.spaceReset_Float(XY);
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
	public void setLocalAddress(String inputString)
	{
		LocalAddress = inputString;
	}
	public String getLocalAddress()
	{
		return LocalAddress;
	}
	public String version()
	{
		return Title + " " + version;
	}
	public void setRotateX(boolean more)
	{
		myLevel.setRotateX(more);
	}
	public void SendDefenderLocation(int x, int y, float heading)
	{
		net.SendDefenderLocation(x, y, heading);
	}
	public void ReceiveDefenderLocation(String[] inputLocation)
	{
		myLevel.updateDefender(inputLocation);
	}
	public void SendShotLocation(int x, int y, float heading)
	{
		net.SendShotLocation(x, y, heading);
	}
	public void ReceiveShotLocation(String[] inputLocation)
	{
		myLevel.addShot(inputLocation);
	}
	public boolean getKeyPressed()
	{
		return keyPressed;
	}
}
