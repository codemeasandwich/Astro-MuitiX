package pak_Display;

import java.util.ArrayList;
import pak_Core.Core;
import processing.core.PApplet;

public class Defender
{
	private PApplet Display;
	private Core Perent;
	private String ID;
	private float drift;
	private float heading;
	private float stepsize;
	private float rotate;
	private float shipXY[];
	private float shipScale;
	private ArrayList<DefenderShot> arrayShots;
	private boolean shipScaleGrow;
	
	
	private DefenderModel model;
	
	public static final byte FORWARE = 1;
	public static final byte RIGHT = 2;
	public static final byte LEFT = 3;
	public static final byte BACK = 4;
	
	public Defender (Core inputPerent, PApplet inputDisplay)
	{
		this(inputPerent,inputDisplay, "0");
		
		 /* default values that Java initializes variables to
		 # null for objects
		 # 0 for integer types of all lengths (byte, char, short, int, long)
		 # 0.0 for float types (float and double)
		 # false for booleans
		 */
	}
	
	public Defender (Core inputPerent, PApplet inputDisplay, String inputID)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		ID = inputID;
		shipXY = new float[]
		                   {Perent.getRandom(Display.getWidth()),
							Perent.getRandom(Display.getHeight())};
		//{Display.getWidth()/2,Display.getHeight()/2};
		drift 	 	  = 0.0f;
		heading 	  = 1.599f;
		stepsize 	  = 1.1f;
		rotate 		  = 0.1f;
		shipScale     = 1.5f;
		shipScaleGrow = false;
		arrayShots 	  = new ArrayList<DefenderShot>();
		
		model = new DefenderModel(this, Display);
	}
	
	public void draw()
	{
		drawShots();
		drawShip();
	}

	@Override
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
		Display.noStroke();
		
		if(!arrayShots.isEmpty() && arrayShots.get(0).getTTL()<1)
		{	arrayShots.remove(0);  }
		
		for/*each*/ (DefenderShot fire: arrayShots)
		{
				fire.setXY(Perent.spaceReset_Int(fire.getXY()));
				Display.ellipse(fire.getXY()[0], fire.getXY()[1], DefenderShot.SIZE, DefenderShot.SIZE);
				fire.move();
		}
	}
	
	private void drawShip()
	{
		//Display.pointLight(255,255,255, (float)(.9*shipXY[0]),(float)(.9*shipXY[1]), 90);
		//Display.pointLight(150,150,150, (float)(1.1*shipXY[0]),(float)(1.1*shipXY[1]), 90);
		
		//Display.ellipse(20,20,20,20);
		
		Display.pushMatrix();
		
			Display.translate(shipXY[0],shipXY[1]);
			//Display.rotateX(Display.frameCount /100.0f);
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
			Display.rotateZ(heading-1.6f);
			Display.translate(-10,-20);
			
			// ========= draw ship
			model.draw();
			/*
			if(0.1<drift && !model.getkilled())
			{	model.drawDrift(drift);  }
			*/
		Display.popMatrix();

		// ========= if the ship is moving why stop it?
		if(!Display.keyPressed && 0.1<drift)
		{ drift(); }
		
	}
	
	private void drift()
	{
		shipXY = Perent.spaceReset_Float(shipXY);
		
		shipXY[0] -= PApplet.cos(heading)*(stepsize*drift);
		shipXY[1] -= PApplet.sin(heading)*(stepsize*drift);
		drift = drift *0.985f;
		Perent.SendDefenderLocation((int)shipXY[0], (int)shipXY[1], heading);
	}
	
	public void moveDefender(byte inputMove)
	{
		shipXY = Perent.spaceReset_Float(shipXY);
		
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
		if(Perent.getScore()>0)
		{
			Perent.addScore(-1);
			arrayShots.add(new DefenderShot(heading,(int)shipXY[0],(int)shipXY[1]));
			Perent.SendShotLocation((int)shipXY[0], (int)shipXY[1], heading);
		}
	}
	
	public void killDefender()
	{
		model.setKilled();
	}
	
	public boolean getkilled()
	{
		return model.getkilled();
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
	public void setHeading(float inputHeading)
	{
		heading = inputHeading;
	}
}