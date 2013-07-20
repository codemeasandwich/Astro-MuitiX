package pak_Gui;

import java.awt.Color;

import pak_logic.Game;
import processing.core.PApplet;
import processing.core.PFont;

public class Menu
{
	Button aButton;
	private PApplet Display;
	private Game Perent;
	
	public Menu(Game inputPerent, PApplet inputDisplay)
	{		
		Perent        = inputPerent;
		Display       = inputDisplay;
		aButton = new Button(
				this, 
				Display,
				100,100,50,25,"Helloworld",new Color(255,0,0),true);
	}
	public void draw()
	{
		Display.fill(0,0,0,125);
		Display.rect(Display.width/4, Display.height/10, Display.width/2, Display.height/1.20f);
		aButton.draw();
	}
	public PFont getFont(char type)
	{
		return Perent.getFont(type);
	}
}
