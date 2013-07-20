package pak_Display;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
//import pak_Core.Core;
import pak_Net.NetworkInterface;
import pak_logic.*;
import processing.core.PApplet;

public class Defender implements Serializable
{
	private static final long serialVersionUID = 1L;
	private transient PApplet Display;
	private transient Game perent;
	//private String ID;
	private InetAddress ID;
	private float drift;
	private float heading;
	private float stepsize;
	private float rotate;
	private float shipXY[];
	private float shipScale;
	private int hitArea;
	private transient ArrayList<DefenderShot> arrayShots;
	private boolean shipScaleGrow;
	private boolean isFake;

	private DefenderModel model;
	
	public static final byte FORWARE = 1;
	public static final byte RIGHT = 2;
	public static final byte LEFT = 3;
	public static final byte BACK = 4;
	

		 /* default values that Java initializes variables to
		 # null for objects
		 # 0 for integer types of all lengths (byte, char, short, int, long)
		 # 0.0 for float types (float and double)
		 # false for booleans
		 */
	
	public Defender (Game inputPerent, PApplet inputDisplay, InetAddress inputID)
	{
		System.out.println("Defender..");
		perent = inputPerent;
		Display = inputDisplay;
		ID = inputID;
		isFake = false;
		shipXY = new float[]{perent.getRandom().nextInt(Display.getWidth()),
							 perent.getRandom().nextInt(Display.getHeight())};

		hitArea		  = 15;
		drift 	 	  = 0.0f;
		heading 	  = 1.570796325f;
		stepsize 	  = 1.5f;
		rotate 		  = 0.1f;
		shipScale     = 1.5f;
		shipScaleGrow = false;
		arrayShots 	  = new ArrayList<DefenderShot>();
		
		ModelLoader load3D = new ModelLoader(Display);
		model = load3D.LoadXMLDefender("data/shipModel.xml");
		model.addPerent(this);
		System.out.println("Defender..Done");
	}
	
	public void reBuild(Game inputPerent, PApplet inputDisplay)
	{
		isFake = true;
		perent = inputPerent;
		Display = inputDisplay;
		model.reBuild(Display);
		arrayShots = new ArrayList<DefenderShot>();
	}
	
	public void draw()
	{
		if(!isFake)
		{
			drawShots();
		}
		drawShip();
	}

	@Override
	public String toString()
	{
		return  "Defender";
	}
	
	public void ReSpawnDefender()
	{
		perent.playSound(Sound.ALIVE);
		model.ReSpawn();
	}
	
	public void zoneIn()
	{
		shipScaleGrow = true;
	}
	
	public InetAddress getID()
	{
		return ID;
	}
	
	private void drawShots()
	{
		Display.fill(255);
		Display.noStroke();
		
		if(!arrayShots.isEmpty() && arrayShots.get(0).getTTL()<1)
		{	arrayShots.remove(0);  }
		
		float[] fireXY;
		DefenderShot fire;
		
		for(int count = 0; count<arrayShots.size(); count++)
		{
			fire = arrayShots.get(count);
			if(fire.getTTL() != 0)
			{
				fireXY = fire.getXY();
				fire.setXY(perent.spaceReset_Float(fire.getXY()));
				Display.ellipse(fireXY[0], fireXY[1], DefenderShot.SIZE, DefenderShot.SIZE);
				fire.move();
				
				InetAddress hit = perent.HitTestAllShips(fire);
				
				if(null !=hit && !hit.equals(ID))
				{
					perent.sendSocketMessage(hit,fireXY,NetworkInterface.HIT_TEST,true);
					perent.SendShotRemove(fire.toString());
					arrayShots.remove(count);
				}
			}
		}
	}
	
	private void drawShip()
	{		
		if(!isFake && !model.isDead() && perent.ShipSmashTest(shipXY))
		{
			perent.killDefender();
		}
		
		Display.pushMatrix();
		
			Display.translate(shipXY[0],shipXY[1]);
			//showXYZ();
			if(shipScaleGrow)
			{
				if(shipScale<5)
				{shipScale += 0.25f;}
				else if(shipScaleGrow)
				{shipScaleGrow = false;}
			}
			else
			{
				if(shipScale>1.1f)
				{shipScale -= 0.25f;}
			}
			
			Display.scale(shipScale);
			
			// ========= move ship to center
			Display.rotateZ(heading);
			Display.rotateZ(-1.570796325f);
			Display.translate(-10,-20);
			
			// ========= draw ship
			model.draw();
			
			if(0.1<drift && !model.isDead())
			{	model.drawDrift(drift,stepsize+0.15f);  }
			
		Display.popMatrix();

		// ========= if the ship is moving why stop it?

		if(!isFake && !Display.keyPressed && 0.1<drift)
		{ drift(); }
		
	}
	
	private void drift()
	{
		shipXY = perent.spaceReset_Float(shipXY);
		
		shipXY[0] -= PApplet.cos(heading)*(stepsize*drift);
		shipXY[1] -= PApplet.sin(heading)*(stepsize*drift);
		drift = drift *0.99f;
		perent.SendDefenderLocation((int)shipXY[0], (int)shipXY[1], heading,drift);
	}
	
	public boolean HitTest(float[] fireXY)
	{
		if(!model.isDead() &&
			shipXY[0]+hitArea > fireXY[0] && 
			shipXY[0]-hitArea < fireXY[0] &&	
			shipXY[1]+hitArea > fireXY[1] && 
			shipXY[1]-hitArea < fireXY[1])
		{
			return true;
		}
		return false;
	}
	
	public void moveDefender(byte inputMove)
	{
		shipXY = perent.spaceReset_Float(shipXY);
		
		if(!model.isDead())
		{
			switch (inputMove)
			{
				case RIGHT:
					heading += rotate;
				break;
				
				case LEFT:
					heading -= rotate;
				break;
				
				case FORWARE:
					shipXY[0] -= PApplet.cos(heading)*stepsize;
					shipXY[1] -= PApplet.sin(heading)*stepsize; 
	
					//a cap on the drift
					if(stepsize > drift)
					{ drift += 0.2f; }
				break;
			}
			perent.SendDefenderLocation((int)shipXY[0], (int)shipXY[1], heading,drift);
		}
	}
	
	public void fireDefender()
	{
		if(!model.isDead())
		{
			//if(perent.getScore()>0)
			//{
			//	perent.addScore(-1);
			perent.playSound(Sound.SHOOT);
				arrayShots.add(new DefenderShot(heading,(int)shipXY[0],(int)shipXY[1]));
				perent.SendShotLocation((int)shipXY[0], (int)shipXY[1], heading);
		//	}
		}
		else if(model.KeyBoardSpawn())
		{
			shipXY = new float[]{perent.getRandom().nextInt(Display.getWidth()),
					 perent.getRandom().nextInt(Display.getHeight())};
			model.ReSpawn();
			perent.SendDefenderLocation((int)shipXY[0], (int)shipXY[1], heading,drift);
			perent.sendSocketMessage(ID, null, NetworkInterface.SHIPALIVE, false);
		}
	}
	public void setDrift(float val)
	{
		drift = val;
	}
	public void killDefender()
	{
		perent.playSound(Sound.KILLED);
		model.setKilled();
		drift = 0;
		if(!isFake)
		perent.setMessage("Your KILLED!");
	}
	
	public boolean isDead()
	{
		return model.isDead();
	}
	public void setMessage(String mess)
	{
		if(!isFake)
		perent.setMessage(mess);
	}
	
	public int[] getXY()
	{
		return new int[]{(int)shipXY[0],(int)shipXY[1]};
	}
	
	public void setXY(int[] inputXY)
	{
		shipXY = new float[]{inputXY[0],inputXY[1]};
	}
	
	public float getHeading()
	{
		return heading;
	}
	
	public float getDrift()
	{
		return drift;
	}
	
	public void setHeading(float inputHeading)
	{
		heading = inputHeading;
	}
	
	private void showXYZ()
	{
		Display.stroke(255,0,0);
		Display.line(-40, 0, 0, 40, 0, 0); //X is red
		Display.stroke(0,255,0);
		Display.line(0, -40, 0, 0, 40, 0); //Y is green
		Display.stroke(0,0,255);
		Display.line(0, 0, -40, 0, 0, 40); //Z is Blue
		Display.stroke(0);
	}
}