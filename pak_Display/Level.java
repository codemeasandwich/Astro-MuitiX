package pak_Display;

import processing.core.PApplet;
import pak_Core.Core;
import java.util.ArrayList;
import java.util.Random;

public class Level
{
	private PApplet Display;
	private Core Perent;
	private star[] starArray;

	private ArrayList<Defender> arrayDefender;
	
	public Level(Core inputPerent, PApplet inputDisplay)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		arrayDefender = new ArrayList<Defender>();
		/*
		Random rn = new Random();
		starArray = new star[30+rn.nextInt(20)];
		
		for(int count = 0; count<starArray.length; count++)
		{
			starArray[count] = new star();
			starArray[count].x = rn.nextInt(Display.width);
			starArray[count].y = rn.nextInt(Display.height);
			starArray[count].rotateSpeed = (1 + rn.nextInt(500))/10000.0f;
			starArray[count].shade = 125 + rn.nextInt(125);
		}*/
	}
	
	public void draw()
	{
		Display.background(10);
		
		Display.rotateX(0.4f);
		Display.translate(0,-80,-150);

		Display.fill(40, 40, 40, 150);
		Display.rect(0,0,Display.width,Display.height);
		
		//drawStars();
		
		for/*each*/ (Defender Spaceship: arrayDefender)
		{
			Spaceship.draw();
		}
	}
	
	public void addDefender(Defender inputDefender)
	{
		arrayDefender.add(inputDefender);
	}
	public float[] spaceReset(float[] XY)
	{
		if(XY[0]<0)
		{	XY[0] = Display.width;	}
		else if(XY[0]>Display.width)
		{	XY[0] = 0; 	}
		
		if(XY[1]<0)
		{	XY[1] = Display.height;	}
		else if(XY[1]>Display.height)
		{	XY[1] = 0;	}
		
		return XY;
	}
	private void drawStars()
	{
		star tempStar;
		Display.pushMatrix();
		Display.translate(Display.width/2,0,0);

		for(int count = 0; count<starArray.length; count++)
		{
			tempStar = starArray[count];

				Display.rotateZ(tempStar.rotateVal);
				tempStar.rotateVal += tempStar.rotateSpeed;
				Display.stroke(tempStar.shade);
				Display.point(tempStar.x, tempStar.y);
		}
		Display.popMatrix();
	}
	class star
	{
		public int x;
		public int y;
		public float rotateSpeed;
		public float rotateVal;
		public int shade;
	}
}
