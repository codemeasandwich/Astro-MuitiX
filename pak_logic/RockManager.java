package pak_logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import pak_Display.DefenderShot;
import pak_Display.Level;
import pak_Display.Rock;

import processing.core.PApplet;

public class RockManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	private ArrayList<Rock> arrayRocks;
	private transient PApplet Display;
	private int deadRocksNum;
	private transient Level Perent;
	
	public RockManager(Level inputPerent,PApplet inputDisplay)
	{
		System.out.print("RockManager");
		Perent = inputPerent;
		Display = inputDisplay;
		deadRocksNum = 0;
		arrayRocks = new ArrayList<Rock>();
		creatRocks();
		System.out.println("Done");
	}
	
	public void reBuild(Level inputPerent, PApplet inputDisplay)
	{
		Perent = inputPerent;
		Display = inputDisplay;
		
		for(Rock rock: arrayRocks)
		{
			rock.reBuild(this,Display);
		}
	}
	
	public void creatRocks()
	{
		synchronized(arrayRocks)
		{
			arrayRocks.clear();
			deadRocksNum = 0;
			Random generator = new Random();
			int rn = generator.nextInt(5)+5;
			//rn = 0;
			for(int count =0; count < rn; count++)
			{
				arrayRocks.add(new Rock(this,Display));
			}
		}
	}
	
	public void draw()
	{
		synchronized(arrayRocks)
		{
			for(Rock rock: arrayRocks)//ConcurrentModificationException Need to fix
			{
				rock.draw();
			}
		}
	}
	public boolean hitTest(float[] inputXY, boolean takeAction)
	{
		return hitTest(new DefenderShot(0,(int)inputXY[0],(int)inputXY[1],""),takeAction);
	}
	
	public boolean hitTest(DefenderShot fire, boolean takeAction)
	{
		float[] rockXY;
		float[] XY = fire.getXY();
		boolean boolHit = false;
		
		for(Rock rock: arrayRocks)
		{
			if(rock.getSize() != Rock.DELETE)
			{
				rockXY = rock.getXY();
				if(
					rockXY[0]+rock.getSize()*6 > XY[0] && 
					rockXY[0]-rock.getSize()*6 < XY[0] &&	
					rockXY[1]+rock.getSize()*6 > XY[1] && 
					rockXY[1]-rock.getSize()*6 < XY[1])
				{
					Perent.playSound(Sound.ATROHIT);
					
					fire.kill();
					rock.downSize();
					boolHit = true;
					
					if(rock.getSize() != Rock.DELETE)
					{
						Rock myRock = new Rock(this,Display);
						myRock.setSize(rock.getSize());
						myRock.setXY(rock.getXY());
						
						synchronized(arrayRocks)
						{
							arrayRocks.add(myRock);
						}
					}
					else if(arrayRocks.size()-1 == deadRocksNum)
					{
						creatRocks();
					}
					
					else//if(rock.delete == ture)
					{
						deadRocksNum++;
					}
					
					synchronized(arrayRocks)
					{
						Perent.SendRockManager();
					}

					
					break;
				}
			}
		}
		return boolHit;
	}

	
	public float[] spaceReset_Float(float[] XY)
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
	
	public void setRockHit(Rock[] twoRocks)
	{
		twoRocks[0].reBuild(this,Display);
		twoRocks[1].reBuild(this,Display);
		
		for(Rock rock: arrayRocks)
		{
			if(rock.getID() == twoRocks[0].getID())
			{
				synchronized(arrayRocks)
				{
					arrayRocks.remove(rock);
					arrayRocks.add(twoRocks[0]);
					arrayRocks.add(twoRocks[1]);
				}
				Rock.RockCount++;
				break;
			}		
		}
	}
}