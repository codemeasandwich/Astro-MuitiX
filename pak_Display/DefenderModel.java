package pak_Display;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import processing.core.PApplet;
import java.io.File;
import java.util.ArrayList;

public class DefenderModel implements Serializable
{

	private static final long serialVersionUID = 1L;
	private transient PApplet Display;
	//private Defender Perent;
	private final String modelFileName;
	private boolean killed;
	private String comment;
	private boolean drawLines;
	
	private short colour[];
	private int jetColour[];
	
	//Default Ship=============================
	private float explosionNums[][];//explosion random vals
	private int explosionCount;//explosion desticion
 
	//New Ship=============================
	private byte[/*num of cells*/][/*num of points*/][/*xyz*/] shipCells_int;
	//private int[/*num of Jets*/][/*num of points*/][/*xyz*/] shipJets_int;
	
	public void reBuild(PApplet inputDisplay)
	{
		Display = inputDisplay;
	}
	
	public DefenderModel(PApplet inputDisplay)
	{
		//Perent = inputPerent;
		Display = inputDisplay;
		
		comment = "~";
		drawLines = true;
		shipCells_int = new byte[0][0][0];
		//shipJets_int = new int[0][0][0];
		colour = new short[4];
		modelFileName = new String("ship.data");
		
		explosionCount = 0;
		killed = false;
		
		loadModelFile();
	}
	
	private void DefaultModelFile()
	{
		colour = new short[]{255,0,0,255};
		shipCells_int = new byte[4][3][3];
		
		shipCells_int[0][0] = new byte[]{10,5,4};
		shipCells_int[0][1] = new byte[]{3,25,4};
		shipCells_int[0][2] = new byte[]{10,20,8};

		shipCells_int[1][0] = new byte[]{10,5,4};
		shipCells_int[1][1] = new byte[]{17,25,4};
		shipCells_int[1][2] = new byte[]{10,20,8};

		shipCells_int[2][0] = new byte[]{3,25,4};
		shipCells_int[2][1] = new byte[]{10,20,8};
		shipCells_int[2][2] = new byte[]{10,20,4};

		shipCells_int[3][0] = new byte[]{10,20,8};
		shipCells_int[3][1] = new byte[]{10,20,4};
		shipCells_int[3][2] = new byte[]{17,25,4};
		
		explosionNums = new float[4][5];
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
							colour[count] =	new Short(
									Short.parseShort(colourArray[1+count]));	
							if(colour[count]<0 || colour[count]>255)
							{
								colour[count] = 125;
							}
						}
						colour[3] = 255;
					}
					else if(lineVal.startsWith("#points_start"))
					{
						do
						{
						lineVal = Buffer.readLine().trim();//get next line
						}while(lineVal.startsWith(comment));
							lineVal = lineVal.split(comment)[0];
							
							while(lineVal.equals("#points_end") == false)
							{
								ArrayList<byte[]> tempArray = new ArrayList<byte[]>(3);
								
								while(lineVal.equals("#next") == false) //|| lineVal.equals("#points_end") == false)
								{
									String[] StringVals = lineVal.split(":");
									byte[] numVals = new byte[]{
										(byte) Integer.parseInt(StringVals[0]),
										(byte) Integer.parseInt(StringVals[1]),
										(byte) Integer.parseInt(StringVals[2])};
									tempArray.add(numVals);
									do
									{
									lineVal = Buffer.readLine().trim();//get next line
									}while(lineVal.startsWith(comment));
									lineVal = lineVal.split(comment)[0];
								}
								shipCells.add(tempArray);
								do
								{
								lineVal = Buffer.readLine().trim();//get next line
								}while(lineVal.startsWith(comment));
								lineVal = lineVal.split(comment)[0];
						}
						//arraylist to int[] ...	
							
							shipCells_int = new byte[shipCells.size()][0][0];
							
							//for (ArrayList cell: shipCells)
							for(int count1 = 0; count1<shipCells.size(); count1++)
							{
								ArrayList cell = shipCells.get(count1);
								shipCells_int[count1] = new byte[cell.size()][0];
								
								for(int count2 = 0; count2<cell.size();count2++)
								{
									byte[] Shape = (byte[])cell.get(count2);
									
									shipCells_int[count1][count2] = Shape;
								}
							}
							explosionNums = new float[shipCells_int.length][5];
					}
				}
			}
			catch(FileNotFoundException null_ex)//file not found
			{
				System.out.println(null_ex.toString());
				DefaultModelFile();
			}
			catch (IOException e3)
			{
				System.out.print(e3.toString());
				DefaultModelFile();
			}
		}
		else
		{
			DefaultModelFile();
		}
	}
	
	public void setKilled()//(boolean inputVal)
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
	
	public void draw()
	{
		Display.fill(colour[0],colour[1],colour[2],colour[3]);
		
		if(drawLines)
		{	Display.stroke(0);	}
		else
		{	Display.noStroke();	}
		
		if(killed)
		{
			if(colour[colour.length - 1]>0)
			{	colour[colour.length - 1] = (short)(colour[colour.length - 1] - 4);}
			else
			{	colour[colour.length - 1] = 0;	}

			explosionCount++;
		}
		
		for(int count1 = 0; count1<shipCells_int.length; count1++)
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
			
			for(int count2 = 0; count2<shipCells_int[count1].length; count2++)
			{


				Display.vertex(
						shipCells_int[count1][count2][0], 
						shipCells_int[count1][count2][1], 
						shipCells_int[count1][count2][2]);
				
			}
			Display.endShape();
			if(killed)
			{	Display.popMatrix();	}
		}
	}
	
	public void drawDriwft(float inputNum)
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
