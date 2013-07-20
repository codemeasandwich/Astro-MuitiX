package pak_Display;

import java.util.Random;
import processing.core.PApplet;

public class DefenderModel
{
	private PApplet Display;
	private Defender Perent;
	private boolean killed; 
	private int colour[];
	private int jetColour[];
	private float explosionNums[];
	private int explosionCount;
	
	public final int MODECELLS = 4;
	
	public DefenderModel(Defender inputPerent, PApplet inputDisplay)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		explosionCount = 0;
		killed = false;
		colour = new int[]{255,0,0,255};
		jetColour = new int[]{0,0,0,125};
		explosionNums = new float[6];
	}
	
	public void setKilled()//(boolean inputVal)
	{
		killed = true;
		
		Random rn = new Random(Display.frameCount);
		for(int count = 0; count<explosionNums.length; count++)
		{
			explosionNums[count] = (float)(rn.nextInt(25)/100.0);//max val of 5
			
			if(0 == rn.nextInt(2))
			{
				explosionNums[count] = -explosionNums[count];
			}
		}
	}
	
	public boolean getkilled()
	{
		return killed;
	}
	
	public void draw()
	{
		Display.noStroke();
		Display.fill(colour[0],colour[1],colour[2],colour[3]);
		
		
		if(killed)
		{
			if(colour[colour.length - 1]>0)
			{
				colour[colour.length - 1] = colour[colour.length - 1] - 4;
			}
			else
			{
				colour[colour.length - 1] = 0;
				//killed = false;
			}
			explosionCount++;
				
			Display.pushMatrix();
			Display.translate(
					(float)(explosionNums[0]*explosionCount),
					(float)(explosionNums[1]*explosionCount),
					(float)((explosionNums[2]*2)*explosionCount));
			Display.rotateY((float)(explosionNums[3]*explosionCount)/5);
			Display.rotateX((float)(explosionNums[4]*explosionCount)/5);
		
		drawCell(0);

			Display.popMatrix();
			Display.pushMatrix();
			Display.translate(
					(float)(explosionNums[1]*explosionCount),
					(float)(explosionNums[2]*explosionCount),
					(float)((explosionNums[3]*2)*explosionCount));
			Display.rotateY((float)(explosionNums[4]*explosionCount)/5);
			Display.rotateX((float)(explosionNums[5]*explosionCount)/5);
		
		drawCell(1);

			Display.popMatrix();
			Display.pushMatrix();
			Display.translate(
					(float)(explosionNums[2]*explosionCount),
					(float)(explosionNums[3]*explosionCount),
					(float)((explosionNums[4]*2)*explosionCount));
			Display.rotateY((float)(explosionNums[5]*explosionCount)/5);
			Display.rotateX((float)(explosionNums[0]*explosionCount)/5);
		
		drawCell(2);

			Display.popMatrix();
			Display.pushMatrix();
			Display.translate(
					(float)(explosionNums[3]*explosionCount),
					(float)(explosionNums[4]*explosionCount),
					(float)((explosionNums[5]*2)*explosionCount));
			Display.rotateY((float)(explosionNums[0]*explosionCount)/5);
			Display.rotateX((float)(explosionNums[1]*explosionCount)/5);

		drawCell(3);

			Display.popMatrix();
		}
		else
		{
			for(int count = 0; count<MODECELLS; count++)
			{
				drawCell(count);
			}
		}
	}
	
	public void drawCell(int inputNum)
	{
		switch (inputNum)
		{
			case 0:
				Display.beginShape();
					Display.vertex(1,0,0);
					Display.vertex(0,4,0);
					Display.vertex(1,3,1);
				Display.endShape();
			break;
			case 1:
				Display.beginShape();
					Display.vertex(1,0,0);
					Display.vertex(2,4,0);
					Display.vertex(1,3,1);
				Display.endShape();
			break;	
			case 2:
				Display.beginShape();
					Display.vertex(1,3,1);
					Display.vertex(0,4,0);
					Display.vertex(1,3,0);
				Display.endShape();
			break;	
			case 3:
				Display.beginShape();
					Display.vertex(1,3,1);
					Display.vertex(2,4,0);
					Display.vertex(1,3,0);
				Display.endShape();
			break;
			
			/* Ships Area
			Display.beginShape();
				Display.vertex(1,0,0);
				Display.vertex(0,4,0);
				Display.vertex(1,3,0);
				Display.vertex(2,4,0);
			Display.endShape();
			*/
			
		}
	}
	
	public void drawDrift(float inputNum)
	{
		if(!killed)
		{
			Display.fill(255);//, 125);
			Display.beginShape();
				Display.vertex(1,3,0);
				Display.vertex(0.5f,3.5f,0);
				Display.vertex(1,3+(2*inputNum),0);
				Display.vertex(1.5f,3.5f,0);
			Display.endShape();

			Display.fill(jetColour[0],jetColour[1],jetColour[2],jetColour[3]);
			Display.beginShape();
				Display.vertex(1,3,0);
				Display.vertex(0.5f,3.5f,0);
				Display.vertex(1,3+(1.5f*inputNum),0);
				Display.vertex(1.5f,3.5f,0);
			Display.endShape();
		
		Display.fill((int)(jetColour[0]*.5),(int)(jetColour[1]*.5),(int)(jetColour[2]*.5),125);
		Display.beginShape();
			Display.vertex(1,3,0);
			Display.vertex(0.5f,3.5f,0);
			Display.vertex(1,3+(0.7f*inputNum),0);
			Display.vertex(1.5f,3.5f,0);
		Display.endShape();
			
		}
	}
}
