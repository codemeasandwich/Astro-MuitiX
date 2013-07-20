package pak_Display;

import java.awt.Color;
import java.io.Serializable;
import java.util.Random;
import processing.core.PApplet;

public class DefenderModel implements Serializable
{

	private static final long serialVersionUID = 1L;
	private transient PApplet Display;
	private boolean killed;
	private boolean drawLines;
	
	private Color shipColour;
	private Color jetColour;
	
	//Default Ship=============================
	
	private float explosionNums[][];//explosion random vals
	private int explosionCount;//explosion desticion
 
	//New Ship=============================
	private byte[/*num of cells*/][/*num of points*/][/*xyz*/] shipCells;
	private float[/*num of Jets*/][/*num of points*/][/*num of states*/][/*xyz*/] JetsCells;

	//StartUp=============================
	
	public DefenderModel(PApplet inputDisplay)
	{
		Display = inputDisplay;
		
		drawLines = true;
		explosionCount = 0;
		killed = false;
	}
	
	public void reBuild(PApplet inputDisplay)
	{
		Display = inputDisplay;
	}
	
	public void reDraw()
	{
		shipColour = new Color(
				shipColour.getRed(),
				shipColour.getGreen(),
				shipColour.getBlue(),
				255);
	}
	
	//Setup=============================
	
	public void set3DModel(byte[][][] mobelCells)
	{
		shipCells = mobelCells;
		explosionNums = new float[shipCells.length][5];
	}
	public void set3DJets(float[][][][] mobelJetsCells)
	{
		JetsCells = mobelJetsCells;
	}
	public void setColorShip(Color inputColor)
	{
		shipColour = inputColor;
	}
	public void setColorJet(Color inputColor)
	{
		jetColour = inputColor;
	}
	public void setLines(boolean use)
	{
		drawLines = use;
	}
	//Draw=============================
	

	public void draw()
	{
		Display.fill(
				shipColour.getRed(),
				shipColour.getGreen(),
				shipColour.getBlue(),
				shipColour.getAlpha());
		
		if(drawLines)
		{	Display.stroke(0);	}
		else
		{	Display.noStroke();	}

		if(killed)
		{
			if(shipColour.getAlpha()>4)
			{	
				shipColour = new Color(
						shipColour.getRed(),
						shipColour.getGreen(),
						shipColour.getBlue(),
						shipColour.getAlpha() - 4);
			}
			else
			{		
				shipColour = new Color(
					shipColour.getRed(),
					shipColour.getGreen(),
					shipColour.getBlue(),
					0);	
				killed = false;
			}
			explosionCount+=2;
		}
		
		for(int count1 = 0; count1<shipCells.length; count1++)
		{
			if(killed)
			{
				Display.pushMatrix();
				Display.translate(
						(explosionNums[count1][0]*explosionCount),
						(explosionNums[count1][1]*explosionCount),
						((explosionNums[count1][2]*5)*explosionCount));
				Display.rotateY((explosionNums[count1][3]*explosionCount)/5);
				Display.rotateX((explosionNums[count1][4]*explosionCount)/5);
			}
			
			Display.beginShape();
			for(int count2 = 0; count2<shipCells[count1].length; count2++)
			{
				Display.vertex(
						shipCells[count1][count2][0], 
						shipCells[count1][count2][1], 
						shipCells[count1][count2][2]);
			}
			Display.endShape();
			
			if(killed)
			{	Display.popMatrix();  }
		}
	}
	
	public void drawDrift(final float inputNum, final float maxVal)
	{
		if(!killed)
		{
			Display.fill(
					jetColour.getRed(),
					jetColour.getGreen(),
					jetColour.getBlue(),
					jetColour.getAlpha());
			Display.noStroke();
			for(int count1 = 0; JetsCells.length>count1; count1++)
			{
				Display.beginShape();
				for(int count2 = 0; count2<JetsCells[count1].length; count2++)
				{
					
					Display.vertex(
							JetsCells[count1][count2][1][0]+((JetsCells[count1][count2][0][0]-JetsCells[count1][count2][1][0])/100) *((inputNum/maxVal)*100), 
							JetsCells[count1][count2][1][1]+((JetsCells[count1][count2][0][1]-JetsCells[count1][count2][1][1])/100) *((inputNum/maxVal)*100), 
							JetsCells[count1][count2][1][2]+((JetsCells[count1][count2][0][2]-JetsCells[count1][count2][1][2])/100) *((inputNum/maxVal)*100));
				}
				Display.endShape();
			}
		}
	}

	
	//Functions=============================

	
	public void setKilled()
	{
		killed = true;
		drawLines = false;
		
		Random rn = new Random(Display.frameCount);
		
		for(int count1 = 0; count1<explosionNums.length; count1++)
		{
			float[] view3 = new float[5];
			for (int count2 = 0; count2<view3.length; count2++)
			{
				explosionNums[count1][count2] = (float)(rn.nextInt(25)/100.0);
				if(0 == rn.nextInt(2))
				{
					explosionNums[count1][count2] = -explosionNums[count1][count2];
				}
			}
		}
		System.out.println("KILLED");
	}
	
	public boolean getkilled()
	{
		return killed;
	}
}