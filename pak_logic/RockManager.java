package pak_logic;

import java.util.ArrayList;
import java.util.Random;

import pak_Display.DefenderShot;
import pak_Display.Level;
import pak_Display.Rock;
import processing.core.PApplet;

public class RockManager
{
	private ArrayList<Rock> arrayRocks;
	private Level Perent;
	private PApplet Display;
	private int deadRocksNum;
	
	public RockManager(Level inputPerent, PApplet inputDisplay)
	{
		
		Perent = inputPerent;
		Display = inputDisplay;
		deadRocksNum = 0;
		arrayRocks = new ArrayList<Rock>();
		creatRocks();

	}
	public void creatRocks()
	{
		arrayRocks.clear();
		Random generator = new Random();
		int rn = generator.nextInt(10)+5;//5 to 15
		
		for(int count =0; count < rn; count++)
		{
			arrayRocks.add(new Rock(this,Display));
		}
	}
	
	public void draw()
	{
		for(Rock rock: arrayRocks)
		{
			rock.draw();
		}
	}
	
	public boolean hitTest(DefenderShot fire, boolean takeAction)
	{
		float[] rockXY;
		int[] XY = fire.getXY();
		boolean boolHit = false;
		
		for(Rock rock: arrayRocks)
		{
			if(rock.getSize() != Rock.DELETE)
			{
				rockXY = rock.getXY();
				if(
					rockXY[0]+15 > XY[0] && 
					rockXY[0]-15 < XY[0] &&	
					rockXY[1]+15 > XY[1] && 
					rockXY[1]-15 < XY[1])
				{
					fire.kill();
					rock.downSize();
					
					if(rock.getSize() != Rock.DELETE)
					{
						Rock myRock = new Rock(this,Display);
						myRock.setSize(rock.getSize());
						myRock.setXY(rock.getXY());
						arrayRocks.add(myRock);
						boolHit = true;
						break;
					}
					else if(arrayRocks.size() == deadRocksNum)// && rock.delete is ture
					{
						creatRocks();
					}
					else//if(rock.delete == ture)
					{
						deadRocksNum++;
					}
				}
			}
		}
		
		return boolHit;
	}

	public Random getRandom()
	{
		return Perent.getRandom();
	}
	
	public float[] spaceReset_Float(float[] XY)
	{
		return Perent.spaceReset_Float(XY);
	}
}
