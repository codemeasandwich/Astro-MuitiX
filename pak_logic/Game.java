package pak_logic;

import java.net.InetAddress;
import java.util.Random;

import pak_Core.Core;
import pak_Display.Defender;
import pak_Display.DefenderShot;
import pak_Display.Level;
import pak_Net.NetworkInterface;
import processing.core.PApplet;
import processing.core.PFont;

public class Game
{
	private Core Perent;
	private PApplet Display;
	private Level myLevel;
	private Defender myDefender;
	private Random generator;
	private int score;
	private PFont fontA,fontB,fontC,fontD;
	private byte[] netIn;
	private byte[] netOut;
	private short txtFade;
	private int crazyLandCount;
	private float[] crazyRotate;
	private String message;
	
	public Game(Core inputPerent, PApplet inputDisplay)
	{
		System.out.println("Game..");
		Perent = inputPerent;
		Display = inputDisplay;
		generator = new Random();
		score = 100;
		txtFade = 255;
		netIn = new byte[50];
		netOut = new byte[50];
		crazyRotate = new float[]{100,100,100};
		crazyLandCount = 0;
		message =Perent.version();
		
		fontA = Display.loadFont("Silkscreen-16.vlw");
		fontB = inputDisplay.loadFont("BlueHighwayBold-18.vlw");
		fontC = inputDisplay.loadFont("Swinkydad-42.vlw");
		fontD = inputDisplay.loadFont("InterplanetaryCrap-48.vlw");
		Display.textFont(fontA, 18);
		
		myDefender = new Defender(this, Display,Perent.getLocalAddress());
		myLevel = new Level(this, Display);
		
		addDefender(myDefender,false);
		System.out.println("Game..Done");
	}
	
	public void draw()
	{
		//crazyLand();
		Display.lights();
		myLevel.draw();
		
		drawNet();
		drawMessage();
		myName();
		//if the score is around 5 give a shot ever 5 sec
		if(5>score && (Display.frameCount %(2*Display.frameRate))< 1)
		{		score++;		}
	}
	
	public int[] spaceReset_Int(int[] XY)
	{
		return myLevel.spaceReset_Int(XY);
	}
	
	public float[] spaceReset_Float(float[] XY)
	{
		return myLevel.spaceReset_Float(XY);
	}

	public Random getRandom()
	{
		return generator;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void addScore(int points)
	{
		score += points;
	}
	
	public void addDefender(Defender inputDefender,boolean rebuild)
	{
		System.out.println("Defender~"+inputDefender.getID().toString()+":ADDED");
		if(rebuild)
		{	inputDefender.reBuild(this, Display);	}
		myLevel.addDefender(inputDefender);
	}
	
	public void SendDefenderLocation(int x, int y, float heading, float drift)
	{
		Perent.SendDefenderLocation(x, y, heading ,drift);
	}
	public void ReceiveDefenderLocation(String[] inputLocation)
	{
		myLevel.updateDefender(inputLocation);
	}
	
	public void SendShotLocation(int x, int y, float heading)
	{
		Perent.SendShotLocation(x, y, heading);
	}
	
	public void ReceiveShotLocation(String[] inputLocation)
	{
		myLevel.addShot(inputLocation);
	}
	public void moveDefender(byte inputMove)
	{
		myDefender.moveDefender(inputMove);
	}
	
	public void fireDefender()
	{
		myDefender.fireDefender();
	}
	public void ReSpawnDefender(InetAddress ID)
	{
		myLevel.ReSpawnDefender(ID);
	}
	public void killDefender(InetAddress ID)
	{
		myLevel.killDefender(ID);
	}
	
	public void killDefender()
	{
		Perent.sendSocketMessage(myDefender.getID(),
				myDefender.getID().toString(),
				NetworkInterface.SHIPKILLED,
				false);
		
		myDefender.killDefender();
	}
	
	public void zoneInDefender()
	{
		myDefender.zoneIn();
	}
	
	public Defender getDefender()
	{
		return myDefender;
	}
	
	public void updateNet(boolean Incoming)
	{
		if(Incoming)
		{	updateNetIncoming();	}
		else
		{	updateNetOutgoing();	}
	}
	
	public PFont getFont(char type)
	{
		type = Character.toUpperCase(type);
		PFont toReturn;
		
		switch(type)
		{
			case 'A':
				toReturn = fontA;
			break;
			
			case 'B':
				toReturn = fontB;
			break;
			
			case 'C':
				toReturn = fontC;
			break;
			case 'D':
				toReturn = fontD;
			break;
			
			default:
				toReturn = fontA;
			break;
		}
		return toReturn;
	}
	
	public void updateNetOutgoing()
	{
		if((netOut.length/4)>netOut[netOut.length - 1])
		{		netOut[netOut.length - 1]++;		}
	}
	
	public void updateNetIncoming()
	{
		if((netIn.length/4)>netIn[netIn.length - 1])
		{		netIn[netIn.length - 1]++;			}
	}
	
	private void drawNet()
	{
		Display.pushMatrix();
		Display.translate(Display.width - (20 + netOut.length), Display.height * 0.05f);

		Display.noStroke();
		Display.fill(255,0,0);
		for(byte counter = 1; counter<netOut.length; counter++)
		{
			Display.rect(counter,netOut.length/4,1,-netOut[counter]);
			
			if((Display.frameCount %2) == 0)
			{
				if(counter  + 1 !=netOut.length)
				{	netOut[counter] = netOut[1+counter];}
				else
				{	netOut[counter] = 0;		}
			}
		}
		
		Display.fill(255,255,0);
		for(byte counter = 1; counter<netIn.length; counter++)
		{
			Display.rect(counter,netIn.length/4,1,netIn[counter]);
			
			if((Display.frameCount %2) == 0)
			{
				if(counter  + 1 !=netIn.length)
				{	netIn[counter] = netIn[1+counter];	}
				else
				{	netIn[counter] = 0;		}
			}
		}
		Display.stroke(255);
		Display.noFill();
		Display.rect(0, 0, netOut.length, netOut.length / 2);
		
		Display.popMatrix();
	}
	
	private void crazyLand()
	{
		crazyLandCount++;
		Display.translate(Display.width/2,Display.height/2);
		Display.rotateZ(crazyLandCount/crazyRotate[0]);
		Display.rotateY(crazyLandCount/crazyRotate[1]);
		Display.rotateX(crazyLandCount/crazyRotate[2]);
		Display.translate(-Display.width/2,-Display.height/2);
	}
	
	private void myName()
	{
		Display.pushMatrix();
			Display.fill(255,255,0,125);
			Display.translate(Display.width - 20, Display.height/2);
			Display.rotateZ(1.570796325f);
			Display.textAlign(PApplet.CENTER);
			Display.textFont(getFont('C'), 42);
			Display.text("07127154 NDS07",0,0);
		Display.popMatrix();
	}
	
	private void drawMessage()
	{
		if(txtFade>0)
		{
		Display.pushMatrix();
			Display.fill(255,255,255,txtFade);
			Display.translate(Display.width/2, Display.height/2);
			Display.textAlign(PApplet.CENTER);
			Display.textFont(getFont('D'), 42);
			Display.text(message,0,0);
		Display.popMatrix();
		txtFade -= 2;
		}
	}
	
	public void setMessage(String mess)
	{
		if(mess.isEmpty())
		{ txtFade = 0;   }
		else
		{ txtFade = 255; }	
		
		message = mess;
	}
	
	public InetAddress HitTest(DefenderShot fire)
	{
		return myLevel.HitTest(fire);
	}
	
	public void sendSocketMessage( 
			InetAddress Address,  Object objToSend,
			byte Type, boolean returnThis)
	{
		Perent.sendSocketMessage(Address,objToSend,Type,returnThis);
	}
	public void SendShotRemove(String id)
	{
		Perent.SendShotRemove(id);
	}
}
