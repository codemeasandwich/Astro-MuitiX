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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DefenderModel implements Serializable
{

	private static final long serialVersionUID = 1L;
	private transient PApplet Display;
	//private Defender Perent;
	private final String modelFileName = "shipModel.xml";
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
	private byte[/*num of Jets*/][/*num of states*/][/*num of points*/][/*xyz*/] shipJets_int;
	
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
		shipCells_int = loadModelXML("data/"+modelFileName);
		explosionCount = 0;
		killed = false;
	}
	
	public void reDraw()
	{
		colour[3] = 255;
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
	
	private byte[][][] loadModelXML(String xmlFileName)
	{
		byte[/*num of cells*/][/*num of points*/][/*xyz*/] mobelCells = new byte[0][0][0];
		colour = new short[]{255,0,0,255};
		 try
		 {	//http://java.sun.com/developer/technicalArticles/xml/validationxpath/
			 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			 Document doc = docBuilder.parse(new File(xmlFileName));
			
			 doc.getDocumentElement().normalize ();
			 NodeList listOfshapes = doc.getElementsByTagName("shape");
			 
			 ArrayList<ArrayList> shipCells = new ArrayList<ArrayList>();
			 
			 for(int count1=0; listOfshapes.getLength()>count1; count1++)//Loop shapes
			 {
				    Node shapeNode = listOfshapes.item(count1);
				    Element pointsElement = (Element)shapeNode;
				    
				    NodeList listOfpoints = pointsElement.getChildNodes();
				    ArrayList<byte[]> tempArray = new ArrayList<byte[]>(3);
				    for(int count2 = 0; listOfpoints.getLength()>count2; count2++)//Loop points
				    {				    	
				    	Node pointNode = listOfpoints.item(count2);
				    	if(pointNode.getNodeName().equals("point"))
				    	{
				    		String lineVal = pointNode.getAttributes().getNamedItem("xyz").getNodeValue();
				    		
							String[] StringVals = lineVal.split(":");
							byte[] numVals = new byte[]{
								(byte) Integer.parseInt(StringVals[0]),
								(byte) Integer.parseInt(StringVals[1]),
								(byte) Integer.parseInt(StringVals[2])};
							
							if(numVals[0]<0 || numVals[0]>20)
							{
								throw new IllegalArgumentException(
										modelFileName + pointNode.getAttributes().getNamedItem("xyz")+" = X must be from 0 to 20 not "+StringVals[0]);
							}
							else if(numVals[1]<0 || numVals[1]>30)
							{
								throw new IllegalArgumentException(
										modelFileName + pointNode.getAttributes().getNamedItem("xyz")+" = Y must be from 0 to 30 not "+StringVals[1]);	
							}
							else if(numVals[2]<0 || numVals[2]>10)
							{
								throw new IllegalArgumentException(
										modelFileName + pointNode.getAttributes().getNamedItem("xyz")+" = Z must be from 0 to 10 not "+StringVals[2]);
							}
							tempArray.add(numVals);
				    	}//if(point) - END
				    }//Loop points - END
				    shipCells.add(tempArray);
				}//Loop shapes - END
			 
			 	//Arraylist to byte[]..
			 
			 	mobelCells = new byte[shipCells.size()][0][0];
				for(int count1 = 0; count1<shipCells.size(); count1++)
				{
					ArrayList cell = shipCells.get(count1);
					mobelCells[count1] = new byte[cell.size()][0];
					
					for(int count2 = 0; count2<cell.size();count2++)
					{
						byte[] Shape = (byte[])cell.get(count2);
						
						mobelCells[count1][count2] = Shape;
					}
				}
				explosionNums = new float[mobelCells.length][5];
			 
		 }/*
		 catch (SAXParseException err)
		 {
			 System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
			 System.out.println(" " + err.getMessage ());
	
		 }
		 catch (SAXException e)
		 {
			 Exception x = e.getException ();
			 ((x == null) ? e : x).printStackTrace ();
		 }*/
		 catch (Exception excep)
		 {
			 DefaultModelFile();
			 System.out.println(excep.toString());
			 
		 }/*
		 catch (Throwable t) 
		 {
			 t.printStackTrace();
		 }*/
		 //finally

		 return mobelCells;


	}
	private void loadModelFile()
	{		
		File modelFile = new File("data/"+modelFileName);
		
		if(modelFile.exists())
		{
		    try
		    {
		    	int lineCount = 0;
				String lineVal; 
				BufferedReader Buffer = new BufferedReader(new FileReader(modelFile));//read in the file to a buffer
	
				while ((lineVal = Buffer.readLine()) != null)			//loop true the file while the line being read in is not null
				{lineCount++;
					if(lineVal.startsWith("colour"))
					{
						String[] colourArray = lineVal.split(":");

						for(int count = 0; count<3;count++)
						{
							colour[count] =	new Short(
									Short.parseShort(colourArray[1+count]));	
							if(0>colour[count] || 255<colour[count])
							{
								throw new IllegalArgumentException(
										modelFileName+" Line"+lineCount+"->"+lineVal+" = Must be from 0 to 255 not "+colourArray[1+count]);
							}
						}
						colour[3] = 255;
					}
					else if(lineVal.startsWith("#points_start"))
					{
						ArrayList<ArrayList> shipCells = new ArrayList<ArrayList>();
						do
						{
						lineVal = Buffer.readLine().trim();//get next line
						lineCount++;
						}while(lineVal.startsWith(comment) || lineVal.equals(""));
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
									
									if(numVals[0]<0 || numVals[0]>20)
									{
										throw new IllegalArgumentException(
												modelFileName+" Line"+lineCount+"->"+lineVal+" = X must be from 0 to 20 not "+StringVals[0]);
									}
									else if(numVals[1]<0 || numVals[1]>30)
									{
										throw new IllegalArgumentException(
												modelFileName+" Line"+lineCount+"->"+lineVal+" = Y must be from 0 to 30 not "+StringVals[1]);	
									}
									else if(numVals[2]<0 || numVals[2]>10)
									{
										throw new IllegalArgumentException(
												modelFileName+" Line"+lineCount+"->"+lineVal+" = Z must be from 0 to 10 not "+StringVals[2]);
									}
									
									tempArray.add(numVals);
									do
									{
									lineVal = Buffer.readLine().trim();//get next line
									lineCount++;
									}while(lineVal.startsWith(comment)|| lineVal.equals(""));
									lineVal = lineVal.split(comment)[0];
								}
								shipCells.add(tempArray);
								do
								{
								lineVal = Buffer.readLine().trim();//get next line
								lineCount++;
								}while(lineVal.startsWith(comment)|| lineVal.equals(""));
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
					}/*
					else if(lineVal.startsWith("#Drift_start"))
					{
						ArrayList<ArrayList> shipJets = new ArrayList<ArrayList>();

						do
						{
							do
							{
							lineVal = Buffer.readLine().trim();//get next line
							lineCount++;
							}while(lineVal.startsWith(comment)|| lineVal.equals(""));
							
							if(lineVal.equals("#DS"))
							{
								ArrayList[] shipStates = new ArrayList[3];
								while(false == lineVal.equals("#DS")||false == lineVal.equals("#Drift_end"))
								{
									
									String[] prepostVals = lineVal.split("|");
									shipStates[0] 
									String[] StringVals = lineVal.split(prepostVals[0]);
									byte[] numVals = new byte[]{
										(byte) Integer.parseInt(StringVals[0]),
										(byte) Integer.parseInt(StringVals[1]),
										(byte) Integer.parseInt(StringVals[2])};
									
									StringVals = lineVal.split(prepostVals[1]);
									byte[] numVals2 = new byte[]{
										(byte) Integer.parseInt(StringVals[0]),
										(byte) Integer.parseInt(StringVals[1]),
										(byte) Integer.parseInt(StringVals[2])};
								}
							}
						}
						while(false == lineVal.equals("#Drift_end"));
							
					}*/
				}
			}
			catch (Exception except)
			{
				System.out.println(except.toString());
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
			{	colour[colour.length - 1] = 0;	killed = false;}

			explosionCount+=2;
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
