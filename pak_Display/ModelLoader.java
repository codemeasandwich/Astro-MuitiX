package pak_Display;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import processing.core.PApplet;

public class ModelLoader
{
	private PApplet Display;
	
	public ModelLoader(PApplet inputDisplay)
	{
		Display = inputDisplay;
	}
	
	private DefenderModel DefaultModelFile()
	{
		System.out.print(" -DefaultModel- ");
		DefenderModel aDefender = new DefenderModel(Display);

		byte[][][] shipCells = new byte[4][3][3];
		
		shipCells[0][0] = new byte[]{10,5,4};
		shipCells[0][1] = new byte[]{3,25,4};
		shipCells[0][2] = new byte[]{10,20,8};

		shipCells[1][0] = new byte[]{10,5,4};
		shipCells[1][1] = new byte[]{17,25,4};
		shipCells[1][2] = new byte[]{10,20,8};

		shipCells[2][0] = new byte[]{3,25,4};
		shipCells[2][1] = new byte[]{10,20,8};
		shipCells[2][2] = new byte[]{10,20,4};

		shipCells[3][0] = new byte[]{10,20,8};
		shipCells[3][1] = new byte[]{10,20,4};
		shipCells[3][2] = new byte[]{17,25,4};
		
		aDefender.set3DModel(shipCells);
		aDefender.setColorShip(new Color(255,0,0,255));
		
		//[num of Jets][num of points][num of states][xyz]
		float[][][][] shipJets_int = new float[1][4][2][3];
		
		shipJets_int[0][0][0] = new float[]{10,20,4};//Go
		shipJets_int[0][0][1] = new float[]{10,20,4};//Stop
		
		shipJets_int[0][1][0] = new float[]{7,22,4};//Go
		shipJets_int[0][1][1] = new float[]{7,22,4};//Stop
		
		shipJets_int[0][2][0] = new float[]{10,30,4};//Go
		shipJets_int[0][2][1] = new float[]{10,20,4};//Stop
		
		shipJets_int[0][3][0] = new float[]{14,22,4};//Go
		shipJets_int[0][3][1] = new float[]{14,22,4};//Stop
		
		aDefender.set3DJets(shipJets_int);
		aDefender.setColorJet(new Color(255,255,255,255));
		
		
		
		return aDefender;
	}
	
	public DefenderModel LoadXMLDefender(String xmlFileName)
	{
		System.out.print("Load Model:");
		
		File modelFile = new File(xmlFileName);
		
		DefenderModel aDefender = new DefenderModel(Display);
		
		if(modelFile.exists())
		{
		 try
		 {	//http://java.sun.com/developer/technicalArticles/xml/validationxpath/
			 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			 Document doc = docBuilder.parse(new File(xmlFileName));
			 doc.getDocumentElement().normalize();
			 
			//============== Colours
			 
			 Node colorNode = doc.getElementsByTagName("color").item(0);
			 
			 short[] colour = new short[4];
			 colour[0] = (short)Integer.parseInt(colorNode.getAttributes().getNamedItem("R").getNodeValue());
			 colour[1] = (short)Integer.parseInt(colorNode.getAttributes().getNamedItem("G").getNodeValue());
			 colour[2] = (short)Integer.parseInt(colorNode.getAttributes().getNamedItem("B").getNodeValue());
			 colour[3] = 255;

				if(0>colour[0] || 255<colour[0])
				{
					throw new IllegalArgumentException(
							xmlFileName+" Red must be from 0 to 255 not "+colour[0]);
				}
				else if(0>colour[1] || 255<colour[1])
				{
					throw new IllegalArgumentException(
							xmlFileName+" Green must be from 0 to 255 not "+colour[0]);
				}
				else if(0>colour[2] || 255<colour[2])
				{
						throw new IllegalArgumentException(
								xmlFileName+" Blue must be from 0 to 255 not "+colour[0]);
				}
			 
				aDefender.setColorShip(new Color(colour[0],colour[1],colour[2],colour[3]));
				aDefender.setColorJet(new Color(255,255,255,255));
				
				
				
			//============== shapes
				
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
										xmlFileName + pointNode.getAttributes().getNamedItem("xyz")+" = X must be from 0 to 20 not "+StringVals[0]);
							}
							else if(numVals[1]<0 || numVals[1]>30)
							{
								throw new IllegalArgumentException(
										xmlFileName + pointNode.getAttributes().getNamedItem("xyz")+" = Y must be from 0 to 30 not "+StringVals[1]);	
							}
							else if(numVals[2]<0 || numVals[2]>10)
							{
								throw new IllegalArgumentException(
										xmlFileName + pointNode.getAttributes().getNamedItem("xyz")+" = Z must be from 0 to 10 not "+StringVals[2]);
							}
							tempArray.add(numVals);
				    	}//if(point) - END
				    }//Loop points - END
				    shipCells.add(tempArray);
				}//Loop shapes - END
			 
				//============== Arraylist to byte[]..
			 
			 	byte[][][] mobelCells = new byte[shipCells.size()][0][0];
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
				//explosionNums = new float[mobelCells.length][5];
				
				aDefender.set3DModel(mobelCells);
				
				//============== Jets
				
				NodeList listOfJets = doc.getElementsByTagName("Jet");
				
				ArrayList<ArrayList> JetsArrayList = new ArrayList<ArrayList>();
				
				for(int count1=0; listOfJets.getLength()>count1; count1++)//Loop Jets
				{
					Node jetNode = listOfJets.item(count1);
				    Element pointsElement = (Element)jetNode;
				    NodeList listOfpoints = pointsElement.getChildNodes();
				    
				    ArrayList<float[][]> PointsArrayList = new ArrayList<float[][]>();
				    for(int count2 = 0; listOfpoints.getLength()>count2; count2++)//Loop points
				    {	
				    	float startStop[][] = new float[2][3];
				    	Node pointNode = listOfpoints.item(count2);
				    	if(pointNode.getNodeName().equals("point"))
				    	{
				    		String Go = pointNode.getAttributes().getNamedItem("Go").getNodeValue();
				    		String GoArray[] = Go.split(":");
				    		
				    		startStop[0] = new float[]{
				    				Integer.parseInt(GoArray[0]),
				    				Integer.parseInt(GoArray[1]),
				    				Integer.parseInt(GoArray[2])};
				    		
				    		String Stop = pointNode.getAttributes().getNamedItem("Stop").getNodeValue();
				    		String StopArray[] = Stop.split(":");
				    		
				    		startStop[1] = new float[]{
				    				Integer.parseInt(StopArray[0]),
				    				Integer.parseInt(StopArray[1]),
				    				Integer.parseInt(StopArray[2])};
				    		PointsArrayList.add(startStop);
				    	}
				    }
				    JetsArrayList.add(PointsArrayList);
				}
				
				//============== Arraylist to float[]..
				 
				float[][][][] shipJets_int = new float[JetsArrayList.size()][0][2][3];
				//[num of Jets][num of points][num of states][xyz]
				
				for(int count1 = 0; count1<shipJets_int.length; count1++)
				{
					ArrayList points = JetsArrayList.get(count1);
					shipJets_int[count1] = new float[points.size()][2][3];
					
					for(int count2 = 0; count2<shipJets_int[count1].length; count2++)
					{
						shipJets_int[count1][count2] = (float[][])points.get(count2);
					}
				}
				
				aDefender.set3DJets(shipJets_int);
			 
		 }
		 catch (Exception excep)
		 {
			 aDefender = DefaultModelFile();
			 System.out.println(excep.toString());
		 }
		}
		else
		{
			aDefender = DefaultModelFile();
		}
		
		System.out.println("Done");
		
		 return aDefender;
	}
}
