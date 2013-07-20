package pak_logic;

import processing.core.PConstants;
import pak_Display.Defender;

public class KeyboardInput
{
	private Defender myDefender;
	private float[] levelAng;
	private float arg = 0.3f;
	private boolean print = true;
	
	public KeyboardInput(Defender inputDefender,float[] inputLevelAng)
	{
		myDefender = inputDefender;
		levelAng = inputLevelAng;
	}
	
	public boolean test(int keyCode)
	{
		//System.out.println("KeyboardInput.test() "+keyCode);
		
		boolean foundKey = false;
		
		switch(keyCode)
		{
		
		case(PConstants.RIGHT):
			myDefender.moveDefender(Defender.RIGHT);
			foundKey = true;
		break;
		
		case(PConstants.LEFT):
			myDefender.moveDefender(Defender.LEFT);
			foundKey = true;
		break;
		
		case(PConstants.UP):
			myDefender.moveDefender(Defender.FORWARE);
			foundKey = true;
		break;
		
		case(PConstants.CONTROL):
			myDefender.fireDefender();
			foundKey = true;
		break;
		
		case(81):
			myDefender.zoneIn();
			foundKey = true;
		break;
		
		case(112)://F1
			System.exit(1);
		break;
		
		case(113)://F2
			levelAng[0] += arg;
			foundKey = true;
			if(print)
				System.out.println("X+");
		break;
		
		case(114)://F3
			levelAng[0] -= arg;
			foundKey = true;
			if(print)
				System.out.println("X-");
		break;
		
		case(115)://F4
			levelAng[1] += arg;
			foundKey = true;
			if(print)
				System.out.println("Y+");
		break;
		
		case(116)://F5
			levelAng[1] -= arg;
			foundKey = true;
			if(print)
				System.out.println("Y-");
		break;
		
		case(117)://F6
			levelAng[2] += arg;
			foundKey = true;
			if(print)
				System.out.println("Z+");
		break;
		
		case(118)://F7
			levelAng[2] -= arg;
			foundKey = true;
			if(print)
				System.out.println("Z-");
		break;
		
		case(119)://F8
			levelAng[3] += arg/10.0f;
			foundKey = true;
			if(print)
				System.out.println("RX+");
		break;
		
		case(120)://F9
			levelAng[3] -= arg/10.0f;
			foundKey = true;
			if(print)
				System.out.println("RX-");
		break;
		
		case(121)://F10
			levelAng[3] += arg;
			foundKey = true;
		break;
		
		}
		//System.out.println("{"+levelAng[0]+","+levelAng[1]+","+levelAng[2]+","+levelAng[3]+"}");
		return foundKey;
		
	}
	
	public void KeyMap()
	{
		System.out.println("F1: Exit");
		System.out.println("Q:  Find Ship");
	}
	
}
