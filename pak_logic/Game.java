package pak_logic;

import java.util.Random;

import pak_Core.Core;
import pak_Display.Defender;
import pak_Display.Level;
import pak_Gui.Menu;
import processing.core.PApplet;
import processing.core.PFont;

public class Game
{
	private Core Perent;
	private PApplet Display;
	private Level myLevel;
	private Defender myDefender;
	private Random rn;
	private int score;
	private PFont fontA;
	private PFont fontB;
	private byte[] netIn;
	private byte[] netOut;
	private Menu mainMenu;
	
	public Game(Core inputPerent, PApplet inputDisplay)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		rn = new Random();
		score = 100;
		netIn = new byte[50];
		netOut = new byte[50];
		fontA = Display.loadFont("Silkscreen-16.vlw");
		fontB = inputDisplay.loadFont("BlueHighwayBold-18.vlw");
		Display.textFont(fontA, 18);
		
		mainMenu = new Menu(this, Display);
		myDefender = new Defender(this, Display,Perent.getLocalAddress());
		myLevel = new Level(this, Display);
		

		addDefender(myDefender,false);
	}
	
	public void draw()
	{
		Display.pushMatrix();
			crazyLand();
			myLevel.draw();
		Display.popMatrix();
		
		drawNet();
		
		Display.pushMatrix();
		Display.translate(Display.width - 10, Display.height/2);
		Display.rotateZ(1.570796325f);
		Display.textAlign(PApplet.CENTER);
		Display.textFont(fontA, 16);
		Display.text("Brian Shannon: 07127154 NDS07",10,10);
		Display.popMatrix();
		//if the score is around 5 give a shot ever 5 sec
		if(5>score && (Display.frameCount %(2*Display.frameRate))< 1)
		{
			score++;
		}
		
		mainMenu.draw();
		
		
	}
	
	public int[] spaceReset_Int(int[] XY)
	{
		return myLevel.spaceReset_Int(XY);
	}
	
	public float[] spaceReset_Float(float[] XY)
	{
		return myLevel.spaceReset_Float(XY);
	}
	
	public int getRandom(int range)
	{
		return rn.nextInt(range);
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
		if(rebuild)
		{	inputDefender.reBuild(this, Display);	}
		myLevel.addDefender(inputDefender);
	}
	
	public void SendDefenderLocation(int x, int y, float heading)
	{
		Perent.SendDefenderLocation(x, y, heading);
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
	
	public void killDefender()
	{
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
		{
			updateNetIncoming();
		}
		else
		{
			updateNetOutgoing();
		}
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
			
			default:
				toReturn = fontA;
			break;
		}
		return toReturn;
	}
	
	public void updateNetOutgoing()
	{
		if((netOut.length/4)>netOut[netOut.length - 1])
		{
			netOut[netOut.length - 1]++;
		}
	}
	
	public void updateNetIncoming()
	{
		if((netIn.length/4)>netIn[netIn.length - 1])
		{
			netIn[netIn.length - 1]++;
		}
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
				{	netOut[counter] = netOut[1+counter];	}
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
		Display.translate(Display.width/2,Display.height/2);
		Display.rotateZ(Display.frameCount/100.0f);
		Display.rotateY(Display.frameCount/100.0f);
		Display.rotateX(Display.frameCount/100.0f);
		Display.translate(-Display.width/2,-Display.height/2);
	}
}
