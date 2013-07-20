package pak_Gui;

import java.awt.Color;

public class Button
{
	  private int x, y, w, h;	// the size of the button
	  private String txt;		// TXT ON THE BUTTON
	  private int[] colour;		// colour of the button
	  private boolean rollover;	// will we use a rollover fx
	  private boolean couserBool;	// true if the cursor is a HAND & false is arrow
	  private int alphOver;		// temp transparencie value
	  private int alph, alphTemp;	// transparencie values

//	=============================================================================== constructor
	  
	  public Button(int x_input, int y_input, int width_input, int height_input, String text_input, Color color_input, boolean rollover_input)
	  {

			//================================== assign values

	    x = x_input;
	    y = y_input;
	    w = width_input;
	    h = height_input;
	    txt = text_input;
	    colour = new int[]{(int)(color_input.getRGB() >> 16 & 0xFF),(int)(color_input.getRGB() >> 8 & 0xFF),(int)(color_input.getRGB() & 0xFF)};//greater speed
	    couserBool = false;
	    alph = color_input.getAlpha();
	    alphTemp = alph;
	    rollover = rollover_input;

			//================================== test value

	    if(0>=alph || 255<=alph)
	    {
	      alph = 125;
	    }
	    
	    if((alph*2)<255)
	    {
	      alphOver = (int)alph*2;
	    }
	    else
	    {
	      alphOver = 250;
	    }

	  }

//	===============================================================================
//	=============================================================================== Draw

	  void Display()
	  {
	    if(rollover)			//======================== Do this if the rollover is enabled
	    {
	      if(test(mouseX,mouseY))		// is the couser over the button
	      {
	        fill(colour[0],colour[1],colour[2],alphOver);// show high lighted colour

	        alphTemp = alphOver;		// get ready to return colour
	      }
	      else
	      {
	        if(alphTemp>alph)		// if fade is still high
	        {
	          alphTemp -= 3;		//lower the val
	        }
	        fill(colour[0],colour[1],colour[2],alphTemp);// apply the colour
	      }
	    }
	    else				//======================== Else
	    {
	      fill(colour[0],colour[1],colour[2],alph);// no rollover colour
	    }


	    stroke(255);			//button border color
	    rect(x,y,w,h);			//draw button			
	    fill(255);				// text colour
	    textFont(fontA,16);			// font type and size
	    textAlign(CENTER);			// center the text on the button
	    text(txt,x+w*.5,y+h*.75);		// Add text to the button
	  }

//	===============================================================================
//	===============================================================================
	  
	  boolean test()			//asume the buttons have not been Translated
	  {
	    return test(mouseX,mouseY);
	  }

//	===============================================================================
	  
	  boolean test(int x_input, int y_input)//user if the button has been Translated
	  {
	    if((x_input>x && x_input<x+w) && (y_input>y && y_input<y+h))//test input agent this button
	    {
	        if(!couserBool)			//if the test is true than show a hand set hit to true
	        {
	          cursor(HAND);
	          couserBool = true;
	        }
	    }
	    else				//test failed
	    {
	      if(couserBool)			//if the last test was true then reset
	      {
	        cursor(ARROW);
	        couserBool = false;
	      }
	    }
	    return couserBool;			//return the test result
	  }
	}
