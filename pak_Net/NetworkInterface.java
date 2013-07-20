package pak_Net;

import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
//import java.net.Socket;
import java.util.ArrayList;

import pak_Display.Defender;

import pak_Core.Core;

public class NetworkInterface
{
	public MulticastSocket broadcastSocket;
	private Core Perent;
	private InetAddress netGroup;
	private StringBuilder Location;
	public static final String SendDefenderLocation_ID = "DP:";
	public static final String SendShotLocation_ID = "SL:";
	public static final String SendShotRemove_ID = "SR:";
	public static final String HeartbeatFlag = "Alive";
	private String lastDefenderLocation;
	private boolean error;
	public ArrayList<AliveAddr> aLiveAddresses;
	private Defender myDefender;
	private Send2NetWap send2NetWap;
	
	public static final byte STRING = -1;
	public static final byte HIT_TEST = 0;
	public static final byte HIT = 1;
	public static final byte SHIP = 2;
	public static final byte SHIPKILLED = 3;
	public static final byte SHIPALIVE = 4;
	public static final byte ROCKSRequest = 5;
	public static final byte ROCKSResponse = 6;
	public static final byte ROCKHIT = 7;
	
	public NetworkInterface(Core inputPerent,Defender inputDefender)
	{
		System.out.print("NetworkInterface:");
		Perent = inputPerent;
		myDefender = inputDefender;
		error = false;
		aLiveAddresses = new ArrayList<AliveAddr>();
		
		 try
		 {
			lastDefenderLocation = "";
			netGroup = InetAddress.getByName(Core.GroupIP);
			broadcastSocket = new MulticastSocket(Core.GroupPort);//send & recive on this
			broadcastSocket.joinGroup(netGroup);

		 }
		 catch (IOException e) 
		 {
			 Perent.setError("NetworkInterface():"+e.toString());
			 System.out.println("NetworkInterface():"+e.toString());
			 error = true;
		 }

			ListenBroadcastsThread listenThread = new ListenBroadcastsThread(this, Perent,myDefender);
			listenThread.start();
			Listen4NetWrap Thread4Return2NetWap = new Listen4NetWrap(this, Perent,myDefender);
			Thread4Return2NetWap.start();
			send2NetWap = new Send2NetWap(this, Perent);

		 sent(Perent.version());
		 
		 Thread Heartbeat = new Thread(new Runnable()
			{
				public void run()
	            {
					try
					{
						int sleepTime = 500;
						byte count = 0;
						while(true)
						{
							Thread.sleep(sleepTime);
							sent(HeartbeatFlag);
							
							count++;
							if(count >= 3)//test every 1.5 Sec
							{ LiveTest(); }
						}
					}
					catch(Exception ex)
			        {
						Perent.setError("Heartbeat:"+ex.toString());
						System.out.println("Heartbeat:"+ex.toString());
			        }
	            }
			});
		 Heartbeat.start();
		 
		 System.out.println("Done");
	}
	private void LiveTest()
	{
		ArrayList<AliveAddr> delAddr = new ArrayList<AliveAddr>();
		
		for(AliveAddr address: aLiveAddresses)
		{
			if(address.aLive() == false)
			{
				Perent.killDefender(address.getAddress());
				delAddr.add(address);
			}
			else
			{address.resetHeartbeat();}
		}
		for(AliveAddr address: delAddr)
		{
			aLiveAddresses.remove(address);
		}
			

	}
	public void Send2NetWap(//like client
			final InetAddress Address,
			final Object objToSend,
			final byte Type,
			final boolean returnThis)
	{
		send2NetWap.Send(Address, objToSend, Type, returnThis);
	}
	
	public boolean setMyDefender(Defender inputDefender)
	{
		if(null == myDefender)
		{
			myDefender = inputDefender;
			return true;
		}
		else
		{ return false;	}
	}
	
	public void SendDefenderLocation(int x, int y, float heading, float drift)
	{
			Location = new StringBuilder();
			Location.append(SendDefenderLocation_ID);
			Location.append(x);
			Location.append(":");
			Location.append(y);
			Location.append(":");
			
			if(String.valueOf(heading).length()>4)
			{	Location.append((String.valueOf(heading)).substring(0, 4));}
			else
			{	Location.append(String.valueOf(heading));}
			
			Location.append(":");
			
			if(String.valueOf(drift).length()>4)
			{	Location.append((String.valueOf(drift)).substring(0, 4));}
			else
			{	Location.append(String.valueOf(drift));}
			
			//with in the rounding of numbers it may not show any change
			if(false == lastDefenderLocation.equals(Location.toString()))
			{
				lastDefenderLocation = Location.toString();
				sent(lastDefenderLocation);
			}
	}
	
	public void SendShotLocation(int x, int y, float heading)
	{
			Location = new StringBuilder();
			Location.append(SendShotLocation_ID);
			Location.append(x);
			Location.append(":");
			Location.append(y);
			Location.append(":");
			Location.append((String.valueOf(heading)).substring(0, 4));//0.49
			sent(Location.toString());
	}
	
	public void SendShotRemove(String id)
	{
		sent(SendShotRemove_ID+id);
	}
	
	private void sent(String ver)// throws IOException
	{
		if(!error)
		{
			//System.out.println("Send:"+ver);
			try
			{
				 DatagramPacket Message = new DatagramPacket(
						 ver.getBytes(), 
						 ver.length(), 
						 netGroup,
						 Core.GroupPort);
				 broadcastSocket.send(Message);
			}
			catch (IOException e) 
			{
				Perent.setError("Sending broadcast"+e.toString());
				 System.out.println("Sending broadcast"+e.toString());
				 error = true;
			}
		}
	}

	//====================================================
	
	public boolean addIP(InetAddress inputIP)
	{
		boolean found = false;
		
		for/*each*/ (AliveAddr address: aLiveAddresses)
		{
			if(inputIP.equals(address.getAddress()))
			{
				found = true;
				break;//End loop
			}
		}
		
		if(!found)
		{
			aLiveAddresses.add(new AliveAddr(inputIP));
		}
		
		return found;
	}
	
	public boolean IPListIsEmpty()
	{
		return aLiveAddresses.isEmpty();
	}
	
	public String typeConveter(byte val)
	{
		String rString = "?";
		
		switch(val)
		{
			case STRING:
				rString = "String";
			break;
			
			case HIT_TEST:
				rString = "Hit Test";
			break;
			
			case SHIP:
				rString = "Ship";
			break;
			
			case SHIPKILLED:
				rString = "Ship Killed";
			break;
			
			case SHIPALIVE:
				rString = "Ship Alive";
			break;
			
			case ROCKSRequest:
				rString = "Rocks Request";
			break;
			
			case ROCKSResponse:
				rString = "Rocks Response";
			break;
			
			case ROCKHIT:
				rString = "Rocks Hit";
			break;
		}
		return rString;
	}
}