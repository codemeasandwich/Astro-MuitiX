package pak_Display;

import java.io.Serializable;
import java.util.Random;

import processing.core.PApplet;

public class RockBang implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int [][][] astrido;
	private int moveCount;
	private int[]xy;
	private transient PApplet Display;
	private boolean use;
	private float explosionNums[][];//explosion random vals
	
	public RockBang(PApplet inputDisplay)
	{
		Display = inputDisplay;
		Random generator = new Random();
		moveCount = 0;
		astrido = new int[5+generator.nextInt(5)][4][3];//10 to 20 cells with 4 point & xyz
		explosionNums = new float[astrido.length][5];
		
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
	public void reBuild(PApplet inputDisplay)
	{
		Display = inputDisplay;
	}
	
	public void setXY(float[]inputXY)
	{
		xy = new int[]{(int)inputXY[0],(int)inputXY[1]};
		use = true;
		Random rn = new Random();
		
		//num of cells
		for(int count1 = 0; count1<explosionNums.length; count1++)
		{
			float[] view3 = new float[5];
			//translate 3 & rotate 2
			for (int count2 = 0; count2<view3.length; count2++)
			{
				explosionNums[count1][count2] = (float)(rn.nextInt(25)/100.0);
				if(0 == rn.nextInt(2))
				{
					explosionNums[count1][count2] = -explosionNums[count1][count2];
				}
			}
		}
	}
	
	public void draw()
	{
		Display.pushMatrix();
			Display.translate(xy[0], xy[1]);
			
			Display.noStroke();
			
			for(int countOuter = 0; countOuter<astrido.length; countOuter++)
			{
				Display.pushMatrix();//move each shape
				Display.translate(
						(explosionNums[countOuter][0]*moveCount),
						(explosionNums[countOuter][1]*moveCount),
						((explosionNums[countOuter][2]*5)*moveCount));
				Display.rotateY((explosionNums[countOuter][3]*moveCount)/5);
				Display.rotateX((explosionNums[countOuter][4]*moveCount)/5);
					Display.beginShape();
						Display.fill(astrido[countOuter][0][2]*25,255-(moveCount*4));
						
						for(int countInner = 0; countInner<astrido[countOuter].length; countInner++)
						{
							Display.vertex(
									astrido[countOuter][countInner][0], 
									astrido[countOuter][countInner][1],
									astrido[countOuter][countInner][2]);
						}
					Display.endShape();
				Display.popMatrix();
			}
			
			
		Display.popMatrix();
		moveCount++;
		
		if(moveCount*4>255)
		{
			use = false;
		}
	}
	
	public boolean getUse()
	{
		return use;
	}
}
