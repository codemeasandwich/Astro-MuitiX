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
	private InetAddress netGroup;
	private StringBuilder Location;
	private String LocalAddress;
	private int GroupPort;
	private String SendDefenderLocation_ID;
	private String SendShotLocation_ID;
	private String lastDefenderLocation;
	
	public NetworkInterface(Core inputPerent)
	{
		Perent = inputPerent;
		 try
		 {
			 SendDefenderLocation_ID = "ship:";
			 SendShotLocation_ID = "shot:";
			 lastDefenderLocation = "";
			 
			netGroup = InetAddress.getByName(Perent.getGroupIP());
			LocalAddress = "/"+InetAddress.getLocalHost().getHostAddress();
			Perent.setLocalAddress(LocalAddress);
			GroupPort = Perent.getGroupPort();
			broadcastSocket = new MulticastSocket(GroupPort);
			broadcastSocket.joinGroup(netGroup);

		 }
		 catch (IOException e) 
		 {
			 System.out.println("NetworkInterface():"+e.toString());
		 }
		 
		 Listen4broadcasts();
		 sent(Perent.version());
		 
	}
	
	public void SendDefenderLocation(int x, int y, float heading)
	{
		Location = new StringBuilder();
		Location.append(SendDefenderLocation_ID);
		Location.append(x);
		Location.append(":");
		Location.append(y);
		Location.append(":");
		Location.append((String.valueOf(heading)).substring(0, 4));//0.49
		
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
	
	public void sent(String ver)// throws IOException
	{
		try
		 {
			 DatagramPacket Message = new DatagramPacket(
					 ver.getBytes(), 
					 ver.length(), 
					 netGroup,
					 GroupPort);
			 broadcastSocket.send(Message);
		 
		 }
		 catch (IOException e) 
		 {
			 System.out.println("Sending broadcast"+e.toString());
		 }
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
	        		byte[] buf = new byte[24];//num of chars 001-304-0.49
	                DatagramPacket incomingPacket = new DatagramPacket(buf, buf.length);
	           
	                try
	                {
	                	broadcastSocket.receive(incomingPacket);
	                	String incomingData = new String(incomingPacket.getData());
	                	incomingData = incomingData.trim();
	                	
	                	if(false == incomingPacket.getAddress().toString().equals(LocalAddress))
	                	{
	                		if(incomingData.startsWith(SendDefenderLocation_ID))
	                		{
	                			System.out.print(".");
	                			//incomingData ship: x : y : heading
	                			
	                			String[] inputLocation = incomingData.split(":");
	                			inputLocation[0] = incomingPacket.getAddress().toString();
	                			//inputLocation [0]=ID [1]=X  [2]=Y  [3]=heading
	                			Perent.ReceiveDefenderLocation(inputLocation);
	                		}
	                		else if(incomingData.startsWith(SendShotLocation_ID))
	                		{
	                			System.out.print(",");
	                			String[] inputLocation = incomingData.split(":");
	                			inputLocation[0] = incomingPacket.getAddress().toString();
	                			Perent.ReceiveShotLocation(inputLocation);
	                		}
	                		else if(incomingData.equals(Perent.version()))
	                		{
	                			System.out.print("*");
	                			Perent.addDefender(incomingPacket.getAddress().toString());
	                		}
	                		
	                		
	                		//System.out.println(incomingData);
	                		/*
	                		System.out.println("incomingData:"+incomingData.trim());
	                		System.out.println("incomingPacket.getAddress()"+incomingPacket.getAddress());
	                		System.out.println("LocalAddress"+LocalAddress);*/
	                	}
	                	/*
	                	System.out.println("incomingPacket.getAddress()"+incomingPacket.getAddress());//+"  "+InetAddress.getLocalHost());
	                	System.out.println("InetAddress.getLocalHost()"+InetAddress.getLocalHost());
	                	System.out.println("InetAddress.getLocalHost().getHostAddress()"+InetAddress.getLocalHost().getHostAddress());
	                	
	                	System.out.println("LocalAddress"+LocalAddress);
	                	*/

	                }
	                catch(Exception ex)
	                {
	                	System.out.println("Sending Listen for broadcasts"+ex.toString());
	                } 	
	            }
            }});
		ListenThread.start();
	}
}
