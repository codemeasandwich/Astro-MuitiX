package pak_Display;

import processing.core.PApplet;

public class ModelLoader
{
	private PApplet Display;
	
	public ModelLoader(PApplet inputDisplay)
	{
		Display = inputDisplay;
	}
	
	public void LoadXMLModel(String filePath)
	{
		
	}
	
	public DefenderModel LoadXMLDefender(String filePath)
	{
		return new DefenderModel(Display);
	}
}
