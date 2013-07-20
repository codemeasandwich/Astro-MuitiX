package pak_Display;

import processing.core.PApplet;
import processing.core.PFont;
import pak_Core.Core;
import java.util.ArrayList;

public class Level
{
	private PApplet Display;
	private Core Perent;

	private ArrayList<DefenderShot> arrayShots;
	private ArrayList<Defender> arrayDefender;
	private byte[] netIn;
	private byte[] netOut;
	private int score;
	private PFont fontB;
	
	public Level(Core inputPerent, PApplet inputDisplay)
	{
		netIn = new byte[50];
		netOut = new byte[50];
		
		Perent        = inputPerent;
		Display       = inputDisplay;
		score 		  = 100;
		arrayDefender = new ArrayList<Defender>();
		arrayShots    = new ArrayList<DefenderShot>();
		fontB = inputDisplay.loadFont("BlueHighwayBold-18.vlw");
	}
	
	public void draw()
	{	
		
		Display.background(0);
		
		drawNet();
		Display.fill(255);
		Display.textAlign(PApplet.LEFT);
		Display.textFont(fontB, 18);
		Display.text("score: "+score, 20, 20);
		
		Display.rotateX(0.4f);
		Display.translate(0,-80,-150);
		
		//crazyLand();
		
		drawGameBorde();
		
		for (Defender Spaceship: arrayDefender)
		{	Spaceship.draw();}
		
		if(!arrayShots.isEmpty() && arrayShots.get(0).getTTL()<1)
		{	arrayShots.remove(0);}

		Display.fill(255);
		
		synchronized (arrayShots)
		{
			for (DefenderShot fire: arrayShots)
			{
					fire.setXY(Perent.spaceReset_Int(fire.getXY()));
					Display.ellipse(
							fire.getXY()[0], 
							fire.getXY()[1], 
							DefenderShot.SIZE, 
							DefenderShot.SIZE);
					fire.move();
			}
		}
		
		//if the score is around 5 give a shot ever 5 sec
		if(5>score && (Display.frameCount %(2*Display.frameRate))< 1)
		{
			score++;
		}
	}
	
	public void addDefender(Defender inputDefender)
	{
		arrayDefender.add(inputDefender);
	}
	
	public synchronized void addShot(String[] inputLocation)
	{
		arrayShots.add(
				new DefenderShot(
						Float.parseFloat(inputLocation[3]),// Heading
						Integer.parseInt(inputLocation[1]),// X
						Integer.parseInt(inputLocation[2]),// Y
						inputLocation[2]));				   // Address 
	}
	
	public void updateDefender(String[] inputLocation)
	{
		//inputLocation [0]=ID [1]=X  [2]=Y  [3]=heading
		for(Defender Spaceship: arrayDefender)
		{
			if(Spaceship.getID().equals(inputLocation[0]))
			{
				Spaceship.setXY(new int[]{Integer.parseInt(inputLocation[1]),Integer.parseInt(inputLocation[2])});
				Spaceship.setHeading(Float.parseFloat(inputLocation[3]));
			}
		}
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
	
	public int[] spaceReset_Int(int[] XY)
	{
		float[] temp = spaceReset_Float(new float[]{XY[0],XY[1]});
		return new int[]{(int)temp[0],(int)temp[1]};
	}
	
	public float[] spaceReset_Float(float[] XY)
	{
		if(XY[0]<0)
		{	XY[0] = Display.width;	}
		else if(XY[0]>Display.width)
		{	XY[0] = 0; 	}
		
		if(XY[1]<0)
		{	XY[1] = Display.height;	}
		else if(XY[1]>Display.height)
		{	XY[1] = 0;	}
		
		return XY;
	}
	
	public int getScore()
	{
		return score;
	}
	public void addScore(int points)
	{
		score += points;
	}
	
	private void drawNet()
	{
		Display.pushMatrix();
		Display.translate(Display.width - (20 + netOut.length), Display.height * 0.05f);
		/*
		Display.noStroke();
		Display.fill(0,0,255,200);//Blue background
		Display.rect(0, 0, netOut.length, netOut.length / 4);
		Display.fill(255,255,0,200);//yellow background
		Display.rect(0, netOut.length / 4, netOut.length, netOut.length / 4);
		*/
		
		Display.noStroke();
		Display.fill(255,0,0);
		for(byte counter = 1; counter<netOut.length; counter++)
		{
			// rect(x, y, width, height)
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
	
	private void drawGameBorde()
	{
		Display.fill(140, 140, 140,140);
		Display.stroke(0);
		Display.rect(0,0,Display.width,Display.height);
		
		//top
		Display.pushMatrix();
		Display.translate(Display.width/2,-20, 0); 
		//rotateY(0.5);
		Display.box(Display.width, 20, 50);
		Display.popMatrix();
		
		//right
		Display.pushMatrix();
		Display.translate(-20 ,Display.height/2, 0); 
		//rotateY(0.5);
		Display.box(20, Display.height, 50);
		Display.popMatrix();
		
		//left
		Display.pushMatrix();
		Display.translate(20 + Display.width ,Display.height/2, 0); 
		//rotateY(0.5);
		Display.box(20, Display.height, 50);
		Display.popMatrix();
		
		//bottem
		Display.pushMatrix();
		Display.translate(Display.width/2,20 + Display.height, 0); 
		//rotateY(0.5);
		Display.box(Display.width, 20, 50);
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
