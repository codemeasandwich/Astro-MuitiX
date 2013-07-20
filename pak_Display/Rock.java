package pak_Display;

import processing.core.PApplet;
//import processing.core.PFont;

import java.io.Serializable;
import java.util.Random;

public class Rock implements Serializable
{
	private static final long serialVersionUID = 1L;
	public final static byte BIG = 4;
	public final static byte MEDIUM = 3;
	public final static byte SMALL = 2;
	public final static byte TINY = 1;
	public final static byte DELETE = 0;
	
	public static int RockCount = 0; 
	private int [][][] astrido;
	private transient PApplet Display;
	private float[] xy;
	private float rX, rY;
	private int rCount;
	private byte size;
	private float heading;
	private float speed;
	private RockManager perent;
	private int ID;
	//private static PFont fontA;
	RockBang myBang;
	
	public Rock(RockManager inputPerent,PApplet inputDisplay)
	{
		
		RockCount++;
		ID = RockCount;
		perent = inputPerent;
		Display = inputDisplay;
		Random generator = new Random();//perent.getRandom();
		xy = new float[2];
		xy[0] = generator.nextInt(Display.width);
		xy[1] = generator.nextInt(Display.height);
		rX = generator.nextFloat()/80;
		rY = generator.nextFloat()/80;
		heading = generator.nextFloat()*6.2831853f;//360 Degree to Radian
		speed = generator.nextFloat()*3;
		rCount = 0;
		size = MEDIUM;
		
		astrido = new int[10+generator.nextInt(10)][4][3];//10 to 20 cells with 4 point & xyz
		
		for(int countOuter = 0; countOuter<astrido.length; countOuter++)
		{
			for(int countInner = 0; countInner<astrido[countOuter].length; countInner++)
			{
				astrido[countOuter][countInner] = new int[]
				{
						generator.nextInt(20)-10,
						generator.nextInt(20)-10,
						generator.nextInt(10)
				};
			}
		}
		myBang = new RockBang(Display);
		System.out.print(".");
	}
						//RockManager had a different id
	public void reBuild(RockManager inputPerent, PApplet inputDisplay)
	{
		perent = inputPerent;
		Display = inputDisplay;
		myBang.reBuild(Display);
	}
	
	public void draw()
	{
		if(myBang.getUse())
		{
			myBang.draw();
		}
		
		if(DELETE != size)
		{
			Display.pushMatrix();
			
				Display.translate(xy[0], xy[1]);
				Display.scale(size2scale(size));
				Display.noStroke();
				
				for(int countOuter = 0; countOuter<astrido.length; countOuter++)
				{
					Display.rotateX(rX*rCount);
					Display.rotateY(rY*rCount);
					
					Display.beginShape();
					Display.fill(astrido[countOuter][0][2]*25);
					//Display.noFill();
					//Display.stroke(255,255,0);
					for(int countInner = 0; countInner<astrido[countOuter].length; countInner++)
					{
						Display.vertex(
								astrido[countOuter][countInner][0], 
								astrido[countOuter][countInner][1],
								astrido[countOuter][countInner][2]);
					}
					Display.endShape();
				}
				move();
				rCount++;
				
			Display.popMatrix();
		}
	}

	private float size2scale(byte inputSize)
	{
		float scale = 1;
		
		switch(inputSize)
		{
			case(BIG):
				scale = 2.0f;
			break;
			case(MEDIUM):
				scale = 1.8f;
			break;
			case(SMALL):
				scale = 1.5f;
			break;
			case(TINY):
				scale = 1.2f;
			break;
			default: 
				scale = 1.0f;
				size = DELETE;
			break;
		}
		return scale;
	}
	
	public byte getSize()
	{
		return size;
	}
	
	public void setSize(byte inputSize)
	{
		Random generator = new Random();
		heading = generator.nextFloat()*6.2f;
		
		if(BIG>=inputSize)
			size = inputSize;
		else
			size = BIG;
	}
	
	public float[] getXY()
	{
		return xy;
	}
	
	public void setXY(float[] inputXY)
	{
		//F**KING TWO DAYS TO FIND THIS BUG!!! //xy = inputXY;
		xy = new float[]{inputXY[0],inputXY[1]};
	}
	
	public void move()
	{
		xy[0] -= PApplet.cos(heading)*speed;
		xy[1] -= PApplet.sin(heading)*speed;
		
		xy = perent.spaceReset_Float(xy);
	}
	
	public void downSize()
	{
		switch(size)
		{
			case(BIG):
				setSize(MEDIUM);
			break;
			case(MEDIUM):
				setSize(SMALL);
			break;
			case(SMALL):
				setSize(TINY);
			break;
			case(TINY):
				size = DELETE;
			break;
			default: 
				size = DELETE;
			break;
		}
		
		myBang.setXY(xy);
	}
	
	public int getID()
	{
		return ID;
	}
}
