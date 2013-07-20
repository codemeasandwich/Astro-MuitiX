//1 degrees = 0.0174532925 radians
//ctrl + 1

package pak_Core;
 
import java.net.InetAddress;
import java.net.UnknownHostException;
//import java.util.Random;
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
	private InetAddress LocalAddress;
	private String Title;
	private String version;
	private String userName;
	private String CompName;
	private Game myGame;
	private String Error;
	
	public Core(PApplet inputPerent)
	{
		System.out.println("Core..");
			//1st
			perent = inputPerent;
			version = "015b";
			Title = "Astro-MultiX";
			userName = System.getProperty("user.name");
			Error = "";
			
			try// throws UnknownHostException
			{
				LocalAddress = InetAddress.getLocalHost();
				CompName = InetAddress.getLocalHost().getHostName();
			}
			catch (UnknownHostException uHE)
			{
				System.out.println(uHE.toString());
			}
			
			//2nd
			myGame = new Game(this,perent);
			net = new NetworkInterface(this);
			myKeyboard = new KeyboardInput(this, inputPerent);
			
			System.out.println("Core..Done");
	}
	
	public InetAddress getLocalAddress()
	{
		return LocalAddress;
	}
	
	public void setError(String inputError)
	{
		Error = inputError;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getCompName()
	{
		return CompName;
	}
	
	public void draw()
	{
		perent.background(0);
		
			if(perent.keyPressed)
			{ perent.keyPressed = !myKeyboard.test(perent.keyCode); }

		//true if key found so set pessed to false i.e. not need any more
		if(!Error.isEmpty())
		{
				perent.fill(255,100,100);
				perent.textAlign(PApplet.CENTER);
				perent.textFont(myGame.getFont('A'), 12);
				perent.text(Error,perent.width/2, perent.height/1.2f);
		}
		
		myGame.draw();
	}

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
	public void killDefender(InetAddress ID)
	{
		myGame.killDefender(ID);
	}
	public void ReSpawnDefender(InetAddress ID)
	{
		myGame.ReSpawnDefender(ID);
	}
	
	public void zoneInDefender()
	{
		myGame.zoneInDefender();
	}
	
	public Defender getDefender()
	{
		return myGame.getDefender();
	}

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
	
	public void SendDefenderLocation(int x, int y, float heading, float drift)
	{
		net.SendDefenderLocation(x, y, heading, drift);
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
	public void sendSocketMessage( 
			InetAddress Address,  Object objToSend,
			byte Type, boolean returnThis)
	{
		net.Send2NetWap(Address,objToSend,Type,returnThis);
	}
	
	public InetAddress HitTest(int[] fireXY)
	{
		return myGame.HitTest(fireXY);
	}
	public void SendShotRemove(String id)
	{
		net.SendShotRemove(id);
	}
	
}
