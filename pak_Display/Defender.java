package pak_Display;

import java.util.ArrayList;

import pak_Core.Core;
import processing.core.PApplet;

public class Defender
{
	private PApplet Display;
	private Core Perent;
	private int ID;
	private int shotLineSize;
	private float shotLineSpeed;
	private float shipX;
	private float shipY;
	private float drift;
	private float heading;
	private float stepsize;
	private ArrayList<Shot> arrayShots;
	
	public static final int FORWARE = 1;
	public static final int RIGHT = 2;
	public static final int LEFT = 3;
	public static final int BACK = 4;
	
	public Defender (Core inputPerent, PApplet inputDisplay)
	{
		this(inputPerent,inputDisplay, 0);
	}
	
	public Defender (Core inputPerent, PApplet inputDisplay, int inputID)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		ID = inputID;
		
		shipX = Display.getWidth()/2;//0.0f;
		shipY =  Display.getHeight()/2;//0.0f;
		drift = 0.0f;
		heading = 1.6f;
		stepsize = 5.2f;
		shotLineSpeed = stepsize *1.2f;
		shotLineSize = 3;
		
		arrayShots = new ArrayList<Shot>();
		
	}
	
	public void draw()
	{
		drawShip();
		drawShots();
	}
	
	public String toString()
	{
		return  String.valueOf(ID);
	}
	
	private void drawShots()
	{
		for/*each*/ (Shot fire: arrayShots)
		{
			Display.pushMatrix();
			//Display.rotateZ(fire.heading -1.6f);
			Display.strokeWeight(shotLineSize);
			Display.line(fire.x1, fire.y1, fire.x1+shotLineSize, fire.y1+shotLineSize);
			fire.move();
			//Spaceship.draw();
			Display.popMatrix();
		}
	}
	
	private void drawShip()
	{
		Display.pushMatrix();
		
		Display.translate(shipX,shipY);//(Display.getWidth()/2, Display.getHeight()/2);
		Display.scale(10);
					//R    G   B   A
		Display.fill(204, 102, 0, 125);
		//count += Display.frameRate/1000;
		Display.rotateZ(heading-1.6f);
		
		//center box
		/*
		Display.beginShape();
			Display.vertex(-0.1f,-0.1f,0);
			Display.vertex(-0.1f,0.1f,0);
			Display.vertex(0.1f,0.1f,0);
			Display.vertex(0.1f,-0.1f,0);
		Display.endShape();
		*/
		//Ship to center
		Display.translate(-1,-2.5f);
		
		
		Display.beginShape();
			Display.vertex(1,0,0);
			Display.vertex(0,4,0);
			Display.vertex(1,3,1);
		Display.endShape();
		
		Display.fill(204, 202, 0, 125);
		Display.beginShape();
			Display.vertex(1,0,0);
			Display.vertex(2,4,0);
			Display.vertex(1,3,1);
		Display.endShape();
		
		Display.fill(104, 102, 0, 125);
		Display.beginShape();
			Display.vertex(1,3,1);
			Display.vertex(0,4,0);
			Display.vertex(1,3,0);
		Display.endShape();
		
		Display.fill(104, 102, 100, 125);
		Display.beginShape();
			Display.vertex(1,3,1);
			Display.vertex(2,4,0);
			Display.vertex(1,3,0);
		Display.endShape();

		//System.out.println("X"+Display.mouseX+" Y"+Display.mouseY);
		
		if(0.1<drift)
		{
			Display.fill(255, 255, 255, 125);
			Display.beginShape();
				Display.vertex(1,3,0);
				Display.vertex(0.5f,3.5f,0);
				Display.vertex(1,3+(2*drift),0);
				Display.vertex(1.5f,3.5f,0);
			Display.endShape();
		}
		
		if(!Display.keyPressed /*PApplet.UP != Display.keyCode*/ && 0.1<drift)
		{
			shipX -= PApplet.cos(heading)*(stepsize*drift);
			shipY -= PApplet.sin(heading)*(stepsize*drift);
			drift = drift *.95f;
			
		   //System.out.println(drift);
		}
		
		Display.popMatrix();
		
	}
	
	public void moveDefender(int inputMove)
	{
		if(RIGHT == inputMove)
		{
			heading += 0.3f;
		}
		else if(LEFT == inputMove)
		{
			heading -= 0.3f;
		}
		else if (FORWARE == inputMove)
		{
			shipX -= PApplet.cos(heading)*stepsize;
			shipY -= PApplet.sin(heading)*stepsize; 

			//a cap on the drift
			if(1.0f > drift)
			{
				drift += 0.2f;
			}

		}/*
		else if(BACK == inputMove)
		{
			shipX += PApplet.cos(heading)*stepsize;
			shipY += PApplet.sin(heading)*stepsize; 
		}*/
			
	}
	
	public void fireDefender()
	{
		arrayShots.add(new Shot(heading,shipX,shipY,shotLineSpeed));
	}
	
	class Shot
	{
		public float heading;
		public float x1,y1;
		public float speed;
		
		public Shot(float heading, float x1, float y1, float speed)
		{
			this.heading = heading;
			this.x1 = x1;
			this.y1 = y1;
			this.speed = speed;
		}
		public void move()
		{
			x1 -= PApplet.cos(heading)*speed;
			y1 -= PApplet.sin(heading)*speed;
		}
	}
}

