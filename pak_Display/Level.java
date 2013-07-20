package pak_Display;

import processing.core.PApplet;
//import processing.core.PFont;
//import pak_Core.Core;
import java.util.ArrayList;
import pak_logic.*;

public class Level
{
	private PApplet Display;
	private Game Perent;

	private ArrayList<DefenderShot> arrayShots;
	private ArrayList<Defender> arrayDefender;
	//private PFont fontB;
	
	public Level(Game inputPerent, PApplet inputDisplay)
	{		
		Perent        = inputPerent;
		Display       = inputDisplay;
		arrayDefender = new ArrayList<Defender>();
		arrayShots    = new ArrayList<DefenderShot>();
		//fontB = inputDisplay.loadFont("BlueHighwayBold-18.vlw");
	}
	
	public String HitTest(int[] fireXY)
	{
		for (Defender Spaceship: arrayDefender)
		{	
			int[] XY = Spaceship.getXY();
			
			if( XY[0]+13 > fireXY[0] && 
				XY[0]-13 < fireXY[0] &&	
				XY[1]+13 > fireXY[1] && 
				XY[1]-13 < fireXY[1])
			{
				return Spaceship.getID();
			}
		}
		return "";
	}
	
	public void draw()
	{	
		Display.pushMatrix();
		Display.background(0);
		
		Display.fill(255);
		Display.textAlign(PApplet.LEFT);
		Display.textFont(Perent.getFont('B'), 18);
		Display.text("score: "+Perent.getScore(), 20, 20);
		
		Display.rotateX(0.4f);
		Display.translate(0,-80,-150);
		
		drawGameBorde();
		
		for (Defender Spaceship: arrayDefender)
		{	Spaceship.draw();}
		
		if(!arrayShots.isEmpty() && arrayShots.get(0).getTTL()<1)
		{	arrayShots.remove(0);}

		Display.fill(255);
		
		synchronized (arrayShots)
		{
			int[] fireXY;
			for (DefenderShot fire: arrayShots)
			{
				fireXY = fire.getXY();
					fire.setXY(Perent.spaceReset_Int(fire.getXY()));
					Display.ellipse(
							fireXY[0], 
							fireXY[1], 
							DefenderShot.SIZE, 
							DefenderShot.SIZE);
					fire.move();
					/*
					for (Defender Spaceship: arrayDefender)
					{	
						int[] XY = Spaceship.getXY();
						if(XY[0] == fireXY[0] && XY[1] == fireXY[1])
						{
							System.out.println("Hit:"+Spaceship.getID());
						}
					}*/
			}
		}
		Display.popMatrix();
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
				Spaceship.setDrift(Float.parseFloat(inputLocation[4]));
			}
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
	
}
