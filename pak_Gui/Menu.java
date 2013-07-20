package pak_Gui;

import java.awt.Color;
import java.util.ArrayList;

//import pak_Display.DefenderShot;
import pak_logic.Game;
import processing.core.PApplet;
import processing.core.PFont;

public class Menu
{
	ArrayList<Button> buttonList;
	private PApplet Display;
	private Game Perent;
	private short buttonHeight;
	private short buttonWidth;
	private Button setGame;
	private byte menuStatic;// 0 hide, 1 main, 2 New Game, 3 load
	private String newGameName;
	private String defaultGameName;
	
	public Menu(Game inputPerent, PApplet inputDisplay)
	{		
		Perent       = inputPerent;
		Display      = inputDisplay;
		buttonList 	 = new ArrayList<Button>();
		buttonWidth  = 150;
		buttonHeight = 30;
		menuStatic   = 1;
		defaultGameName = "...";
		newGameName = defaultGameName;
		
		buttonList.add(new Button(
				this, 
				Display,
				(Display.width/2)-(buttonWidth/2),
				(Display.height/4) + buttonHeight,
				buttonWidth,
				buttonHeight,
				"Start Game",
				new Color(255,0,0),
				true));
		
		setGame = new Button(
				this, 
				Display,
				(Display.width/2)-(buttonWidth/2),
				(Display.height/4) + buttonHeight,
				buttonWidth,
				buttonHeight,
				"Start New Game",
				new Color(255,0,0),
				true);
		
		buttonList.add(new Button(
				this, 
				Display,
				(Display.width/2)-(buttonWidth/2),
				(Display.height/4) + 
				(buttonHeight/2) + //half the height for the sapce
				(buttonHeight)*2, // 2nd button
				buttonWidth,
				buttonHeight,
				"Join Game",
				new Color(255,0,0),
				true));
		
		buttonList.add(new Button(
				this, 
				Display,
				(Display.width/2)-(buttonWidth/2),
				(Display.height/4) + 
				(buttonHeight) + //half the height for the sapce
				(buttonHeight)*3, // 2nd button
				buttonWidth,
				buttonHeight,
				"Exit",
				new Color(255,0,0),
				true));
	}
	
	public void draw()
	{
		switch(menuStatic)
		{
			case(0):
			break;
			case(1):
				draw_Base();
				draw_Main();
			break;
			case(2):
				draw_Base();
				draw_New();
			break;
			case(3):
				draw_Base();
				draw_Load();
			break;
		}
	}
	
	private void draw_Base()
	{
		Display.fill(0,0,0,125);
		Display.rect(
				Display.width/2 - (buttonWidth+(buttonWidth/8))/2, 
				Display.height/2 - buttonHeight*3.5f, 
				buttonWidth+(buttonWidth/8), 
				buttonHeight*5);

		Display.pushMatrix();
			Display.fill(255,255,0);
			Display.translate(Display.width - 20, Display.height/2);
			Display.rotateZ(1.570796325f);
			Display.textAlign(PApplet.CENTER);
			Display.textFont(Perent.getFont('C'), 42);
			Display.text("07127154 NDS07",0,0);
		Display.popMatrix();
	}
	
	private void draw_Load()
	{
		
	}
	
	private void draw_New()
	{
		//System.out.println(Display.keyCode);
		
		if(112 == Display.keyCode)//F1 menu key
		{
			Display.keyCode = 0;
			Display.keyPressed = false;
			Perent.setCommandkeyInput(true);
			menuStatic = 1;
		}
		else if(8 == Display.keyCode && (defaultGameName.equals(newGameName) || newGameName.equals("Game name..")))
		{
			newGameName = "";
		}
		else if(8 == Display.keyCode)
		{
			if(newGameName.length()>0)
			{
				newGameName = newGameName.subSequence(0, newGameName.length() - 1).toString();
				Display.keyCode = 0;
				Display.keyPressed = false;
			}
		}
		else if(0 != Display.keyCode && (defaultGameName.equals(newGameName) || newGameName.equals("Game name..")))
		{
			newGameName = String.valueOf((char)Display.keyCode);
		}
		else if(0 != Display.keyCode)
		{
			if(newGameName.length()<13)
			{
				newGameName += (char)Display.keyCode;
			}
			else
			{
				newGameName = newGameName.subSequence(0, newGameName.length() - 1).toString();
				newGameName += (char)Display.keyCode;
			}
			Display.keyCode = 0;
			Display.keyPressed = false;
		}
		
		setGame.draw();
		Display.fill(255);
		Display.textAlign(PApplet.CENTER);
		Display.textFont(Perent.getFont('A'), 16);
		Display.text(newGameName,Display.width/2,(Display.height/2)-10);
		
		if(Display.mousePressed)//mouse click
		{
			if(setGame.test())//find the button
			{
				if(newGameName.trim().equals("") //newGameName.isEmpty()
						|| newGameName.equals(defaultGameName) 
						|| newGameName.equals("Game name.."))
				{
					newGameName = "Game name..";
				}
				else
				{
					//Remove menu
					Display.keyCode = 0;
					Display.keyPressed = false;
					Perent.setCommandkeyInput(true);
					menuStatic = 0;
					Perent.setMenuVisible(false);
					Perent.setGameName(newGameName);
				}
			}
		}
	}
	
	private void draw_Main()
	{
			for (Button aButton: buttonList)
			{
				if(Display.mousePressed)//mouse click
				{
					if(aButton.test())//find the button
					{
						//what you want to do with each button
						if(aButton.toString().equals("Start Game"))
						{
							System.out.println("Clicked Start Game");
							menuStatic = 2;
							Perent.setCommandkeyInput(false);
							Display.keyCode = 0;
							newGameName = defaultGameName;
							Display.mousePressed = false;
						}
						else if(aButton.toString().equals("Join Game"))
						{
							System.out.println("Clicked Join Game");
							menuStatic = 3;
							Display.mousePressed = false;
						}
						else if(aButton.toString().equals("Exit"))
						{
							System.exit(1);
						}
					}
				}
				aButton.draw();
			}
	}
	
	public PFont getFont(char type)
	{
		return Perent.getFont(type);
	}
	
	public void setVisible(boolean val)
	{
		if(val)
		{	menuStatic = 1;	}
		else
		{	menuStatic = 0;	}
	}
}
