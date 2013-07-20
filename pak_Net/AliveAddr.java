package pak_Net;

import java.net.InetAddress;

public class AliveAddr
{
	private InetAddress address;
	
	private int beatCount;
	private int resets;
	private int totalBeats;
	
	public AliveAddr(InetAddress inputAddress)
	{
		address = inputAddress;
		beatCount = 0;
		totalBeats = 0;
		resets = 0;
	}
	
	public InetAddress getAddress()
	{
		return address;
	}
	
	public void beat()
	{
		beatCount++;
		totalBeats++;
	}
	
	public boolean aLive()
	{
		if(beatCount>0)
		{	return true;	}
		else
		{	return false;	}
	}
	
	public void resetHeartbeat()
	{
		beatCount = 0;
		resets++;
	}
	
	public float getAverage()
	{
		return (float)totalBeats/(float)resets;
	}
}
