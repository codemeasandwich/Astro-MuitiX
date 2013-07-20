package pak_Display;

import java.awt.Color;
import java.io.Serializable;
import java.util.Random;
import processing.core.PApplet;

public class DefenderModel implements Serializable
{

	private static final long serialVersionUID = 1L;
	private transient PApplet Display;
	private boolean drawDeadCells;
	private boolean drawLines;
	
	private boolean good2ReSpawn;
	private Defender perent;
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
		drawDeadCells = false;
		good2ReSpawn = false;
	}
	
	public void addPerent(Defender inputPerent)
	{
		perent = inputPerent;
	}
	
	public void reBuild(PApplet inputDisplay)
	{
		Display = inputDisplay;
	}
	
	public void ReSpawn()
	{
		shipColour = new Color(
				shipColour.getRed(),
				shipColour.getGreen(),
				shipColour.getBlue(),
				255);
		explosionCount = 0;
		drawLines = true;
		good2ReSpawn = false;
		drawDeadCells = false;
		perent.setMessage("");
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

		if(drawDeadCells)
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
				
				good2ReSpawn = true;
				drawDeadCells = false;
				perent.setMessage("Hit fire to Respawn!");
			}
			explosionCount+=2;
		}
		if(!good2ReSpawn)
		{
			for(int count1 = 0; count1<shipCells.length; count1++)
			{
				if(drawDeadCells)
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
				
				if(drawDeadCells)
				{	Display.popMatrix();  }
			}
		}
	}
	
	public void drawDrift(final float inputNum, final float maxVal)
	{
		if(!drawDeadCells && !good2ReSpawn)
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
		drawDeadCells = true;
		drawLines = false;
		Random rn = new Random();//(Display.frameCount);
		
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
	
	public boolean KeyBoardSpawn()
	{
		return good2ReSpawn;
	}
		
	public boolean isDead()//cant respawn
	{
		if(drawDeadCells || good2ReSpawn)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}