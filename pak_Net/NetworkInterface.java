package pak_Net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import pak_Core.Core;

public class NetworkInterface
{
	private static MulticastSocket broadcastSocket;
	private Core Perent;
	private Thread ListenThread;
	private static InetAddress netGroup;
	private StringBuilder Location;
	
	public NetworkInterface(Core inputPerent)
	{
		Perent = inputPerent;
		//Location = new StringBuilder();
		
		 try
		 {
			netGroup = InetAddress.getByName(Perent.getGroupIP());
			broadcastSocket = new MulticastSocket(Perent.getGroupPort());
			broadcastSocket.joinGroup(netGroup);
		 }
		 catch (IOException e) 
		 {
			 System.out.println("NetworkInterface():"+e.toString());
		 }
	}
	
	public void SendDefenderLocation(int x, int y, float heading)
	{
		Location = new StringBuilder();
		Location.append(x);
		Location.append(":");
		Location.append(y);
		Location.append(":");
		//System.out.println(String.valueOf(heading));
		Location.append((String.valueOf(heading)).substring(0, 4));//0.49
		
		sent(Location.toString());
		
	}
	
	
	public void ReciveDefenderLocation()//(int x, int y, float heading)
	{
		//string
	}
	
	public void sent(String ver)// throws IOException
	{
		try
		 {
			 DatagramPacket Message = new DatagramPacket(
					 ver.getBytes(), 
					 ver.length(), 
					 netGroup, 				//my IP
					 Perent.getGroupPort());
			 broadcastSocket.send(Message);
		 
		 }
		 catch (IOException e) 
		 {
			 System.out.println("Sending broadcast"+e.toString());
		 }
		 Listen4broadcasts();
		 
	}
	
	
	//====================================================
	
	
	private void Listen4broadcasts()
	{
		ListenThread = new Thread(new Runnable()
		{
			public void run()
            {
	        	while(true)
	            {								//			  x   y  hading
	        		byte[] buf = new byte[13];//num of chars 001-304-0.49
	                DatagramPacket incomingPacket = new DatagramPacket(buf, buf.length);
	           
	                try
	                {
	                	broadcastSocket.receive(incomingPacket);
	                	String incomingData = new String(incomingPacket.getData());
	                			
	                	//if(incomingPacket.getAddress().equals(crossOverLayer2.getMyinetIP()) == false && incomingData.equals(crossOverLayer2.version()))
	                	//{
	                	if(incomingPacket.getAddress().equals(InetAddress.getLocalHost()))
	                		System.out.println(incomingData.trim() + incomingPacket.getAddress());
	                	
	                	System.out.println(incomingPacket.getAddress()+"  "+InetAddress.getLocalHost());
	                		//crossOverLayer2.userMessager("Connecting to "+incomingPacket.getAddress()+"...");
	                		//connect2User(incomingPacket.getAddress(),true);
	                		
	                	//}
	                }
	                catch(Exception ex)
	                {
	                	System.out.println("Sending Listen for broadcasts"+ex.toString());
	                } 	
	            }
            }});
		ListenThread.start();
	}
	
	//====================================================
}
