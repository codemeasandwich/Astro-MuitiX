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
	private PFont fontA,fontB,fontC;
	private byte[] netIn;
	private byte[] netOut;
	private boolean Menushow;
	private Menu mainMenu;
	private int crazyLandCount;
	private float[] crazyRotate;
	
	public Game(Core inputPerent, PApplet inputDisplay)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		rn = new Random();
		score = 100;
		netIn = new byte[50];
		netOut = new byte[50];
		crazyRotate = new float[]{100,100,100};
		Menushow = true;
		crazyLandCount = 0;
		
		fontA = Display.loadFont("Silkscreen-16.vlw");
		fontB = inputDisplay.loadFont("BlueHighwayBold-18.vlw");
		fontC = inputDisplay.loadFont("Swinkydad-42.vlw");
		Display.textFont(fontA, 18);
		
		mainMenu = new Menu(this, Display);
		myDefender = new Defender(this, Display,Perent.getLocalAddress());
		myLevel = new Level(this, Display);
		

		addDefender(myDefender,false);
	}
	
	public void draw()
	{
		if(Menushow)
		{Display.pushMatrix();
			crazyLand();}
		
		myLevel.draw();
			
		if(Menushow)
		{Display.popMatrix();}
		
		drawNet();
		
		//if the score is around 5 give a shot ever 5 sec
		if(5>score && (Display.frameCount %(2*Display.frameRate))< 1)
		{		score++;		}
		
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
	public void setCommandkeyInput(boolean val)
	{
		Perent.setCommandkeyInput(val);
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
		{	updateNetIncoming();	}
		else
		{	updateNetOutgoing();	}
	}
	
	public boolean showMenuToggle()
	{
		return setMenuVisible(!Menushow);
	}
	
	private boolean setMenuVisible(boolean val)
	{
		if(val)
		{
			crazyLandCount = 0;
			crazyRotate = new float[]{
					(getRandom(500)-250),
					(getRandom(500)-250),
					(getRandom(500)-250)};
		}
		
		Menushow = val;
		mainMenu.setVisible(val);
		
		return Menushow;
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
}
