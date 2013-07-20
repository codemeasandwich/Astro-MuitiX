package pak_Display;

import java.util.ArrayList;
import pak_Core.Core;
import processing.core.PApplet;

public class Defender
{
	private PApplet Display;
	private Core Perent;
	private int ID;
	private float shotLineSpeed;
	private float drift;
	private float heading;
	private float stepsize;
	private float rotate;
	private float shipXY[];
	private float shipScale;
	private ArrayList<Shot> arrayShots;
	private boolean shipScaleGrow;
	
	
	private DefenderModel model;
	
	public static final int FORWARE = 1;
	public static final int RIGHT = 2;
	public static final int LEFT = 3;
	public static final int BACK = 4;
	
	public Defender (Core inputPerent, PApplet inputDisplay)
	{
		this(inputPerent,inputDisplay, 0);
		
		 /* default values that Java initializes variables to
		 # null for objects
		 # 0 for integer types of all lengths (byte, char, short, int, long)
		 # 0.0 for float types (float and double)
		 # false for booleans
		 */
	}
	
	public Defender (Core inputPerent, PApplet inputDisplay, int inputID)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		ID = inputID;
		shipXY = new float[]
		                   {Perent.getRandom(Display.getWidth()),
							Perent.getRandom(Display.getHeight())};
		//{Display.getWidth()/2,Display.getHeight()/2};
		drift = 0.0f;
		heading = 1.599f;
		stepsize = 3.2f;
		rotate = 0.1f;
		shipScale = 7.5f;
		shotLineSpeed = stepsize *2.0f;
		shipScaleGrow = false;
		arrayShots = new ArrayList<Shot>();
		
		model = new DefenderModel(this, Display);
	}
	
	public void draw()
	{
		drawShots();
		drawShip();
	}

	public String toString()
	{
		return  String.valueOf(ID);
	}
	
	public void zoneIn()
	{
		shipScaleGrow = true;
	}
	
	private void drawShots()
	{
		Display.fill(255);
		
		if(!arrayShots.isEmpty() && arrayShots.get(0).getTTL()<1)
		{	arrayShots.remove(0);  }
		
		//System.out.println("arrayShots"+arrayShots.size());
		for/*each*/ (Shot fire: arrayShots)
		{
				fire.setXY(Perent.spaceReset(fire.getXY()));
				Display.ellipse(fire.getXY()[0], fire.getXY()[1], Shot.SIZE, Shot.SIZE);
				fire.move();
		}
	}
	
	private void drawShip()
	{
		Display.pointLight(255,255,255, (float)(.9*shipXY[0]),(float)(.9*shipXY[1]), 90);
		Display.pointLight(150,150,150, (float)(1.1*shipXY[0]),(float)(1.1*shipXY[1]), 90);
		
		//Display.ellipse(20,20,20,20);
		
		Display.pushMatrix();
		
			Display.translate(shipXY[0],shipXY[1]);
			
			if(shipScaleGrow)
			{
				if(shipScale<16)
				{shipScale += 0.25f;}
				else
				{shipScaleGrow = false;}
			}
			else
			{
				if(shipScale>7.5)
				{shipScale -= 0.25f;}
			}
			
			Display.scale(shipScale);
			
			// ========= move ship to center
			Display.rotateZ(heading-1.6f);
			Display.translate(-1,-2.5f);
			
			// ========= draw ship
			model.draw();
			
			if(0.1<drift && !model.getkilled())
			{	model.drawDrift(drift);  
			
			}

		Display.popMatrix();

		// ========= if the ship is moving why stop it?
		if(!Display.keyPressed && 0.1<drift)
		{ drift(); }
		
	}
	
	private void drift()
	{
		shipXY = Perent.spaceReset(shipXY);
		
		shipXY[0] -= PApplet.cos(heading)*(stepsize*drift);
		shipXY[1] -= PApplet.sin(heading)*(stepsize*drift);
		drift = drift *0.975f;
		Perent.SendDefenderLocation((int)shipXY[0], (int)shipXY[1], heading);
	}
	
	public void moveDefender(int inputMove)
	{
		shipXY = Perent.spaceReset(shipXY);
		
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
				if(1.0f > drift)
				{ drift += 0.2f; }
			break;
			
			case BACK:
				shipXY[0] += PApplet.cos(heading)*stepsize;
				shipXY[1] += PApplet.sin(heading)*stepsize;
			break;	
		}
		Perent.SendDefenderLocation((int)shipXY[0], (int)shipXY[1], heading);
		//System.out.println("heading"+heading);
	}
	
	public void fireDefender()
	{
		arrayShots.add(new Shot(heading,shipXY[0],shipXY[1],shotLineSpeed));
	}
	
	public void killDefender()
	{
		model.setKilled();
	}
	
	public boolean getkilled()
	{
		return model.getkilled();
	}
}
class Shot
{
	private float heading;
	private float[] xy;
	private float speed;
	public static final int SIZE = 5;
	private int TTL;
	
	public Shot(float heading, float x1, float y1, float speed)
	{
		TTL = 75;
		this.heading = heading;
		xy = new float[]{x1,y1};
		this.speed = speed;
	}
	
	public void move()
	{
		xy[0] -= PApplet.cos(heading)*speed;
		xy[1] -= PApplet.sin(heading)*speed;
		TTL--;
	}
	
	public float getHeading()
	{
		return heading;
	}
	
	public float[] getXY()
	{
		return xy;
	}
	public void setXY(float[] inputXY)
	{
		xy = inputXY;
	}
	public float getSpeed()
	{
		return speed;
	}
	
	public void setTTL(int inputnum)
	{
		TTL = inputnum;
	}
	
	public int getTTL()
	{
		return TTL;
	}
	
}

