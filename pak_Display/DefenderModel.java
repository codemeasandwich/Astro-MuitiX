package pak_Display;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import processing.core.PApplet;
import java.io.File;
import java.util.ArrayList;

public class DefenderModel
{
	private PApplet Display;
	private Defender Perent;
	private boolean useDefault;
	private final String modelFileName;
	private boolean killed;
	private String comeaints;

	private int colour[];
	private int jetColour[];
	
	//Default Ship=============================
	private float explosionNums[];//explosion random vals
	private int explosionCount;//explosion desticion
	private final int MODECELLS = 4;//used to loop true all cell if not killed
 
	//New Ship=============================
	private int[/*num of cells*/][/*num of points*/][/*xyz*/] shipCells_int;
	
	public DefenderModel(Defender inputPerent, PApplet inputDisplay)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		
		comeaints = "~";
		shipCells_int = new int[0][0][0];
		colour = new int[]{255,0,0,0};
		modelFileName = new String("ship.data");
		
		explosionCount = 0;
		killed = false;
		jetColour = new int[]{0,0,0,125};
		explosionNums = new float[6];
		useDefault = true;
		
		loadModelFile();
	}
	
	private void setupDefault()
	{
		
	}
	
	private void loadModelFile()
	{		
		File modelFile = new File("data/"+modelFileName);
		ArrayList<ArrayList> shipCells = new ArrayList<ArrayList>();
		
		if(modelFile.exists())
		{
		    try
		    {
				String lineVal; 
				BufferedReader Buffer = new BufferedReader(new FileReader(modelFile));//read in the file to a buffer
	
				while ((lineVal = Buffer.readLine()) != null)			//loop true the file while the line being read in is not null
				{
					if(lineVal.startsWith("colour"))
					{
						String[] colourArray = lineVal.split(":");

						for(int count = 0; count<3;count++)
						{
							colour[count] = Integer.parseInt(colourArray[1+count]);	
							if(colour[count]<0 || colour[count]>255)
							{
								colour[count] = 125;
							}
						}
					}
					else if(lineVal.startsWith("#points_start"))
					{
						do
						{
						lineVal = Buffer.readLine().trim();//get next line
						}while(lineVal.startsWith("~"));
							lineVal = lineVal.split("~")[0];
							
							while(lineVal.equals("#points_end") == false)
							{
								ArrayList<int[]> tempArray = new ArrayList<int[]>(3);
								
								while(lineVal.equals("#next") == false) //|| lineVal.equals("#points_end") == false)
								{
									String[] StringVals = lineVal.split(":");
									int[] numVals = new int[]{
										Integer.parseInt(StringVals[0]),
										Integer.parseInt(StringVals[1]),
										Integer.parseInt(StringVals[2])};
									tempArray.add(numVals);
									do
									{
									lineVal = Buffer.readLine().trim();//get next line
									}while(lineVal.startsWith("~"));
									lineVal = lineVal.split("~")[0];
								}
								shipCells.add(tempArray);
								do
								{
								lineVal = Buffer.readLine().trim();//get next line
								}while(lineVal.startsWith("~"));
								lineVal = lineVal.split("~")[0];
						}
						//arraylist to int[] ...	
							
							shipCells_int = new int[shipCells.size()][0][0];
							
							//for (ArrayList cell: shipCells)
							for(int count1 = 0; count1<shipCells.size(); count1++)
							{
								ArrayList cell = shipCells.get(count1);
								shipCells_int[count1] = new int[cell.size()][0];
								
								for(int count2 = 0; count2<cell.size();count2++)
								{
									int[] Shape = (int[])cell.get(count2);
									
									shipCells_int[count1][count2] = Shape;
								}
							}
					}
					//System.out.println(lineVal);
				}
				useDefault = false;
			}
			catch(FileNotFoundException null_ex)//file not found
			{
				System.out.println(null_ex.toString());
				setupDefault();
			}
			catch (IOException e3)
			{
				System.out.print(e3.toString());
				setupDefault();
			}
		}
		else
		{
			useDefault = true;
		}
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
		Display.fill(colour[0],colour[1],colour[2]);
		Display.stroke(0);
		
		if(useDefault)
		{
			drawDefaultShip();
		}
		else
		{
			drawNewShip();
		}
	}
	
	private void drawNewShip()
	{
		int[] xyzPoint = new int[3];
		
		for(int count1 = 0; count1<shipCells_int.length; count1++)
		{
			//Shape
			Display.beginShape();
			//System.out.println("beginShape()");
			for(int count2 = 0; count2<shipCells_int[count1].length; count2++)
			{
				xyzPoint = shipCells_int[count1][count2];
				
				Display.vertex(
						xyzPoint[0], 
						xyzPoint[1], 
						xyzPoint[2]);
						
				//System.out.println(xyzPoint[0]+":"+	xyzPoint[1]+":"+xyzPoint[2]);
				
			}
			Display.endShape();
			//System.out.println("endShape()");
		}

	}
	private void drawDefaultShip()
	{
		if(killed)
		{
			if(colour[colour.length - 1]>0)
			{
				colour[colour.length - 1] = colour[colour.length - 1] - 4;
			}
			else
			{
				colour[colour.length - 1] = 0;
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
					Display.vertex(10,0,0);
					Display.vertex(0,40,0);
					Display.vertex(10,30,10);
				Display.endShape();
			break;
			case 1:
				Display.beginShape();
					Display.vertex(10,0,0);
					Display.vertex(20,40,0);
					Display.vertex(10,30,10);
				Display.endShape();
			break;	
			case 2:
				Display.beginShape();
					Display.vertex(10,30,10);
					Display.vertex(0,40,0);
					Display.vertex(10,30,0);
				Display.endShape();
			break;	
			case 3:
				Display.beginShape();
					Display.vertex(10,30,10);
					Display.vertex(20,40,0);
					Display.vertex(10,30,0);
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
