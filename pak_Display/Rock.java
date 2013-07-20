package pak_Display;

import pak_logic.RockManager;
import processing.core.PApplet;
import java.util.Random;

public class Rock 
{
	public final static byte BIG = 4;
	public final static byte MEDIUM = 3;
	public final static byte SMALL = 2;
	public final static byte TINY = 1;
	public final static byte DELETE = 0;
	
	private int [][][] astrido;
	private PApplet Display;
	private float[] xy;
	private float rX, rY;
	private int rCount;
	private byte size;
	private float heading;
	private float speed;
	private RockManager perent;
	//Random generator;
	
	public Rock(RockManager inputPerent,PApplet inputDisplay)
	{
		perent = inputPerent;
		Display = inputDisplay;
		Random generator = perent.getRandom();
		xy = new float[2];
		xy[0] = generator.nextInt(Display.width);
		xy[1] = generator.nextInt(Display.height);
		rX = generator.nextFloat()/80;
		rY = generator.nextFloat()/80;
		heading = generator.nextFloat()*6.2831853f;//360 Degree to Radian
		speed = generator.nextFloat()*3;
		rCount = 0;
		size = BIG;
		
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
	}
	
	public void draw()
	{
		if(DELETE != size)
		{
			Display.pushMatrix();
			Display.translate(xy[0], xy[1]);
			move();
			showXYZ();
			Display.scale(size2scale(size));
	
			Display.noStroke();
			
			for(int countOuter = 0; countOuter<astrido.length; countOuter++)
			{
				//Display.pushMatrix();
				Display.rotateX(rX*rCount);
				Display.rotateY(rY*rCount);
				
				Display.beginShape();
				Display.fill(astrido[countOuter][0][2]*25);//255 max ;)
				
				for(int countInner = 0; countInner<astrido[countOuter].length; countInner++)
				{
					//Display.rect(0,0,40,40);
					Display.vertex(
							astrido[countOuter][countInner][0], 
							astrido[countOuter][countInner][1],
							astrido[countOuter][countInner][2]);
				}
				Display.endShape();
				//Display.popMatrix();
			}
			rCount++;
			Display.popMatrix();
		}
		
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
		if(BIG>=inputSize)
			size = inputSize;
		else
			size = BIG;
	}
	
	public float[] getXY()
	{
		//System.out.println("rock:"+heading);
		return xy;
	}
	
	public void setXY(float[] inputXY)
	{
		//System.out.println("myRock:"+heading);
		xy = inputXY;
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
				size = MEDIUM;
			break;
			case(MEDIUM):
				size = SMALL;
			break;
			case(SMALL):
				size = TINY;
			break;
			case(TINY):
				size = DELETE;
			break;
			default: 
				size = DELETE;
			break;
		}
	}
	/*
	public void changeHeading()
	{
		heading = generator.nextFloat()*6.2831853f;//360 Degree to Radian
	}*/
}
