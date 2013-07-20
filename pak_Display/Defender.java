package pak_Display;

import java.io.Serializable;
import java.util.ArrayList;
import pak_Core.Core;
import pak_logic.*;
import processing.core.PApplet;

public class Defender implements Serializable
{
	private static final long serialVersionUID = 1L;
	private transient PApplet Display;
	private transient Game Perent;
	private String ID;
	private float drift;
	private float heading;
	private float stepsize;
	private float rotate;
	private float shipXY[];
	private float shipScale;
	private transient ArrayList<DefenderShot> arrayShots;
	private boolean shipScaleGrow;
	public boolean returnShip;

	private DefenderModel model;
	
	public static final byte FORWARE = 1;
	public static final byte RIGHT = 2;
	public static final byte LEFT = 3;
	public static final byte BACK = 4;
	
	public Defender (Game inputPerent, PApplet inputDisplay)
	{
		this(inputPerent,inputDisplay, "0");
		
		 /* default values that Java initializes variables to
		 # null for objects
		 # 0 for integer types of all lengths (byte, char, short, int, long)
		 # 0.0 for float types (float and double)
		 # false for booleans
		 */
	}
	
	public Defender (Game inputPerent, PApplet inputDisplay, String inputID)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		ID = inputID;
		returnShip = false;
		shipXY = new float[]{Perent.getRandom(Display.getWidth()),
							 Perent.getRandom(Display.getHeight())};

		drift 	 	  = 0.0f;
		heading 	  = 1.570796325f;
		stepsize 	  = 1.2f;
		rotate 		  = 0.1f;
		shipScale     = 1.5f;
		shipScaleGrow = false;
		arrayShots 	  = new ArrayList<DefenderShot>();
		
		ModelLoader load3D = new ModelLoader(Display);
		model = load3D.LoadXMLDefender("data/shipModel.xml");
	}
	
	public void reBuild(Game inputPerent, PApplet inputDisplay)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		model.reBuild(Display);
		arrayShots = new ArrayList<DefenderShot>();
	}
	
	public void draw()
	{
		drawShots();
		drawShip();
	}

	@Override
	public String toString()
	{
		return  "Defender";
	}
	
	public void zoneIn()
	{
		shipScaleGrow = true;
	}
	public String getID()
	{
		return ID;
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
		Display.pushMatrix();
		
			Display.translate(shipXY[0],shipXY[1]);
			
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
			
			if(0.1<drift && !model.getkilled())
			{	model.drawDrift(drift,stepsize+0.15f);  }
			
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