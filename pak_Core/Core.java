//1 degrees = 0.0174532925 radians
//ctrl + 1

package pak_Core;
 
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import processing.core.*;
import pak_Display.*;
import pak_Net.*;
import pak_logic.*;

public class Core
{
	private PApplet perent;
	public final static String GroupIP = "224.0.41.0";
//	http://www.iana.org/assignments/multicast-addresses
	public final static int GroupPort = 9574;
	public final static int socketPort = 9575;

	private KeyboardInput myKeyboard;

	private NetworkInterface net;
	private String LocalAddress;
	private final String Title;
	private final String version;
	//private PFont fontA;
	private Game myGame;
	
	public Core(PApplet inputPerent)
	{
			//1st
			perent = inputPerent;
			version = "010";
			Title = "Astro-MultiX";

			setLocalAddress();
			
			//2nd
			myGame = new Game(this,perent);
			net = new NetworkInterface(this);
			myKeyboard = new KeyboardInput(this, inputPerent);

			

	}
	
	private void setLocalAddress()// throws UnknownHostException
	{
		try
		{
		LocalAddress = "/"+InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException uHE)
		{
			System.out.println(uHE.toString());
		}
	}
	public String getLocalAddress()
	{
		return LocalAddress;
	}
	
	public void draw()
	{
		if(perent.keyPressed && !myGame.getDefender().getkilled())
		{ myKeyboard.test(); }

		//perent.pushMatrix();
		myGame.draw();
		//perent.popMatrix();
		
		/*
		perent.translate(perent.width - 10, perent.height/2);
		perent.rotateZ(1.570796325f);
		//perent.rotateX(perent.frameCount /100.f);
		//System.out.println(perent.frameCount/100.0f);
		perent.textAlign(PApplet.CENTER);
		perent.textFont(fontA, 16);
		perent.text("Brian Shannon: 07127154 NDS07",10,10);
		*/
	}
	
/*
	public void addDefender(Defender inputDefender,boolean rebuild)
	{
		if(rebuild)
		{	inputDefender.reBuild(this, perent);	}
		myGame.addDefender(inputDefender);
	}*/
	public void moveDefender(byte inputMove)
	{
		myGame.moveDefender(inputMove);
	}
	
	public void fireDefender()
	{
		myGame.fireDefender();
	}
	public void killDefender()
	{
		myGame.killDefender();
	}
	public void zoneInDefender()
	{
		myGame.zoneInDefender();
	}
	public Defender getDefender()
	{
		return myGame.getDefender();
	}
/*
	public void setLocalAddress(String inputString)
	{
		LocalAddress = inputString;
	}*/
	public String version()
	{
		return Title + " " + version;
	}
	public void addScore(int points)
	{
		myGame.addScore(points);
	}
	public int getScore()
	{
		return myGame.getScore();
	}
	public void updateNet(boolean Incoming)
	{
		myGame.updateNet(Incoming);
	}
	public void addDefender(Defender inputDefender,boolean rebuild)
	{
		myGame.addDefender(inputDefender,rebuild);
	}
	public void SendDefenderLocation(int x, int y, float heading)
	{
		net.SendDefenderLocation(x, y, heading);
	}
	public void ReceiveDefenderLocation(String[] inputLocation)
	{
		myGame.ReceiveDefenderLocation(inputLocation);
	}
	public void SendShotLocation(int x, int y, float heading)
	{
		net.SendShotLocation(x, y, heading);
	}
	public void ReceiveShotLocation(String[] inputLocation)
	{
		myGame.ReceiveShotLocation(inputLocation);
	}
}
