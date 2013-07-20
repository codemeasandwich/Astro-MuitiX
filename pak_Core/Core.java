//1 degrees = 0.0174532925 radians
//ctrl + 1

package pak_Core;
 
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import processing.core.*;
import pak_Display.*;
import pak_Net.*;

public class Core
{
	private PApplet perent;
	public final static String GroupIP = "224.0.41.0";
//	http://www.iana.org/assignments/multicast-addresses
	public final static int GroupPort = 9574;
	public final static int socketPort = 9575;
	private Level myLevel;
	private Defender myDefender;
	private KeyboardInput myKeyboard;
	private Random rn;
	private NetworkInterface net;
	private String LocalAddress;
	private final String Title;
	private final String version;
	private PFont fontA;
	
	public Core(PApplet inputPerent)
	{

			perent = inputPerent;
			rn = new Random();

			version = "008";
			Title = "Astro-MultiX";
			fontA = inputPerent.loadFont("Silkscreen-16.vlw");
			inputPerent.textFont(fontA, 18);
			//inputPerent.textAlign(PApplet.CENTER);
			setLocalAddress();
			
			myLevel = new Level(this, inputPerent);
			myDefender = new Defender(this, inputPerent,LocalAddress);
			net = new NetworkInterface(this);
			myKeyboard = new KeyboardInput(this, inputPerent);

			addDefender(myDefender,false);

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
	
	public void draw()
	{
		if(perent.keyPressed && !myDefender.getkilled())
		{ myKeyboard.test(); }

		perent.pushMatrix();
		myLevel.draw();
		perent.popMatrix();
		
		perent.translate(perent.width - 10, perent.height/2);
		perent.rotateZ(1.570796325f);
		//perent.rotateX(perent.frameCount /100.f);
		//System.out.println(perent.frameCount/100.0f);
		perent.textAlign(PApplet.CENTER);
		perent.textFont(fontA, 16);
		perent.text("Brian Shannon: 07127154 NDS07",10,10);
	}
	
	public int getRandom(int range)
	{
		return rn.nextInt(range);
	}
	public void addDefender(Defender inputDefender,boolean rebuild)
	{
		if(rebuild)
		{	inputDefender.reBuild(this, perent);	}
		myLevel.addDefender(inputDefender);
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
	public Defender getDefender()
	{
		return myDefender;
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
	public void addScore(int points)
	{
		myLevel.addScore(points);
	}
	public int getScore()
	{
		return myLevel.getScore();
	}
	public void updateNet(boolean Incoming)
	{
		if(Incoming)
		{
			myLevel.updateNetIncoming();
		}
		else
		{
			myLevel.updateNetOutgoing();
		}
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
}
