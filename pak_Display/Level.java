package pak_Display;

import processing.core.PApplet;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

import pak_Net.NetworkInterface;
import pak_logic.*;

public class Level
{
	private PApplet Display;
	private Game Perent;

	private ArrayList<DefenderShot> arrayShots;
	private ArrayList<Defender> arrayDefender;
	private RockManager myRockManager;
	
	public Level(Game inputPerent, PApplet inputDisplay)
	{		
		System.out.println("Level..");
		
		Perent        = inputPerent;
		Display       = inputDisplay;
		arrayDefender = new ArrayList<Defender>();
		arrayShots    = new ArrayList<DefenderShot>();
		myRockManager = new RockManager(this,Display);
		System.out.println("Level..Done");
	}
	
	public InetAddress HitTest(DefenderShot fire)//Im not synchroning this because there is no change made to Array
	{
		InetAddress Addr = null;
		for (Defender Spaceship: arrayDefender)
		{	
			if(Spaceship.HitTest(fire.getXY()))
			{
				Addr = Spaceship.getID();
			}
		}
		
		myRockManager.hitTest(fire, true);
		
		return Addr;
	}
	
	public boolean ShipSmashTest(float[] inputXY)
	{
		boolean hit = false;
		
		for (Defender Spaceship: arrayDefender)
		{	
			if(Spaceship.HitTest(inputXY) && !Spaceship.getID().equals(Perent.getDefender().getID()))
			{
				hit = true;
				Perent.sendSocketMessage(Spaceship.getID(),inputXY,NetworkInterface.HIT_TEST,true);
				break;
			}
		}
		if(!hit)
		{
			if(myRockManager.hitTest(inputXY, true))
			{
				hit = true;
			}
		}
		return hit;
	}
	
	public void draw()
	{	
		Display.pushMatrix();
		
		//Display.fill(255);
		//Display.textAlign(PApplet.LEFT);
		//Display.textFont(Perent.getFont('B'), 18);
		//Display.text("score: "+Perent.getScore(), 20, 20);
		
		Display.rotateX(0.4f);
		Display.translate(0,-80,-150);
		
		drawGameBorde();
		
		synchronized (arrayDefender)
		{
			for (Defender Spaceship: arrayDefender)
			{	Spaceship.draw();}
		}
		
		//for (Rock asteroid: arrayRock)
		//{	asteroid.draw();}
		
		myRockManager.draw();
		
		synchronized (arrayShots)
		{
			
			if(!arrayShots.isEmpty() && arrayShots.get(0).getTTL()<1)
			{	arrayShots.remove(0);}
	
			Display.fill(255);
		

			float[] fireXY;
			for (DefenderShot fire: arrayShots)
			{
				fireXY = fire.getXY();
					fire.setXY(Perent.spaceReset_Float(fire.getXY()));
					Display.ellipse(
							fireXY[0], 
							fireXY[1], 
							DefenderShot.SIZE, 
							DefenderShot.SIZE);
					fire.move();
			}
		}
		Display.popMatrix();
	}
	
	public Random getRandom()
	{
		return Perent.getRandom();
	}
	
	public void addDefender(Defender inputDefender)
	{
		synchronized (arrayDefender)
		{
			inputDefender.zoneIn();
			for(Defender Spaceship: arrayDefender)
			{
				if(Spaceship.getID().equals(inputDefender.getID()))
				{
					arrayDefender.remove(Spaceship);
					break;
				}
			}
			arrayDefender.add(inputDefender);
		}
	}
	
	public void killDefender(InetAddress ID)
	{
		for(Defender Spaceship: arrayDefender)
		{
			if(Spaceship.getID().equals(ID))
			{
				Spaceship.killDefender();
				break;
			}
		}
	}
	
	public void SendRockManager()
	{
		Perent.SendRockManager(myRockManager);
	}
	
	public void SendRockHit(Rock[] twoRocks)
	{
		Perent.SendRockHit(twoRocks);
	}
	
	public RockManager getRockManager()
	{
		return myRockManager;
	}
	public void setRockManager(RockManager inputRockManager)
	{
		myRockManager = inputRockManager;
		myRockManager.reBuild(this,Display);
	}
	public void setRockHit(Rock[] twoRocks)
	{
		myRockManager.setRockHit(twoRocks);
	}
	 
	public void ReSpawnDefender(InetAddress ID)
	{
		for(Defender Spaceship: arrayDefender)
		{
			if(Spaceship.getID().equals(ID))
			{
				Spaceship.ReSpawnDefender();
				//Spaceship.zoneIn();
				break;
			}
		}
	}

	public void addShot(String[] inputLocation)
	{
		synchronized (arrayShots)
		{
		arrayShots.add(
				new DefenderShot(
						Float.parseFloat(inputLocation[3]),// Heading
						Integer.parseInt(inputLocation[1]),// X
						Integer.parseInt(inputLocation[2]),// Y
						inputLocation[2]));				   // Address 
		Perent.playSound(Sound.SHOOT);
		}
	}
	
	public void updateDefender(String[] inputLocation)
	{
		//inputLocation [0]=ID [1]=X  [2]=Y  [3]=heading
		for(Defender Spaceship: arrayDefender)
		{
			//String s = Spaceship.getID().toString();
			if(Spaceship.getID().toString().endsWith(inputLocation[0]))
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
	
	public void setMessage(String mess)
	{
		Perent.setMessage(mess);
	}
	
	private void drawGameBorde()
	{

		Display.fill(140,90);
		Display.stroke(0);
		Display.rect(0,0,Display.width,Display.height);
		drawGameWalls();
		
	}
	
	private void drawGameWalls()
	{
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
	public void playSound(byte inputSound)
	{
		Perent.playSound(inputSound);
	}
	
}
