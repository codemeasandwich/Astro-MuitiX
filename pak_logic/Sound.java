package pak_logic;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Sound
{
	private File sound_Start;
	private File sound_Killed;
	private File sound_Alive;
	private File sound_Shoot;
	private boolean sound;
	
	public static final byte START =0;
	public static final byte KILLED=1;
	public static final byte ALIVE =2;
	public static final byte SHOOT =3;
	
	public Sound()
	{
		System.out.print("Sound:");
			sound_Start  = 	new File("data/2.wav");
			sound_Killed = 	new File("data/risehit.wav");
			sound_Alive  = 	new File("data/swoosh.wav");
			sound_Shoot  = 	new File("data/Lock.wav");
			sound = true;
		System.out.println("Done");
	}
	
	private AudioInputStream stream;
	
	public void setSound(boolean ONorOFF)
	{
		sound = ONorOFF;
	}
	public boolean getSound()
	{
		return sound;
	}
	public void playSound(byte inputSound)//From my 2nd year project ^_^
	{
		if(sound)
		{
			try
			{
				stream = null;

				switch(inputSound)
				{
					case(START):
						stream = AudioSystem.getAudioInputStream(sound_Start);
					break;
					case(KILLED):
						stream = AudioSystem.getAudioInputStream(sound_Killed);
					break;
					case(ALIVE):
						stream = AudioSystem.getAudioInputStream(sound_Alive);
					break;
					case(SHOOT):
						stream = AudioSystem.getAudioInputStream(sound_Shoot);
					break;
					
				}
				
			    // Open a data line to play our type of sampled audio.
			    // Use SourceDataLine for play and TargetDataLine for record.
				DataLine.Info info = null;
				info = new DataLine.Info(Clip.class, stream.getFormat()); 
				
				Clip clip = (Clip) AudioSystem.getLine(info);
				 clip.open(stream);      
				 clip.start();
			}
			catch (IOException e1)
			{
				setSound(false);
				System.out.println("Sound file not found");
				System.out.println(e1.toString());
			}
			catch (Exception e)// Throws IOException or UnsupportedAudioFileException
			{      
				setSound(false);
				System.out.println("Sound System");  
				System.out.println(e.toString());
			} 
		}
	}

}
