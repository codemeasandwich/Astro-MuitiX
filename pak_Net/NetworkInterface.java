package pak_Net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import pak_Display.Defender;

import pak_Core.Core;

public class NetworkInterface
{
	private MulticastSocket broadcastSocket;
	private MulticastSocket broadcastGameSocket;
	private Core Perent;
	private Thread ListenThread;
	private InetAddress netGroup;
	private StringBuilder Location;
	private String SendDefenderLocation_ID;
	private String SendShotLocation_ID;
	private String lastDefenderLocation;
	private boolean error;
	
	private Thread Thread4ReturnShip;
	private Thread Thread4SendShip;
	private ServerSocket ServerSocket4ReturnShip;
	private Socket incomingListen2User;
	
	public NetworkInterface(Core inputPerent)
	{
		Perent = inputPerent;
		error = false;
		
		 try
		 {
			 SendDefenderLocation_ID = "ship:";
			 SendShotLocation_ID = "shot:";
			 lastDefenderLocation = "";
			netGroup = InetAddress.getByName(Core.GroupIP);
			broadcastSocket = new MulticastSocket(Core.PublicMulticastPort);//send & recive on this
			broadcastSocket.joinGroup(netGroup);
			ServerSocket4ReturnShip = new ServerSocket(Core.socketPort);

		 }
		 catch (IOException e) 
		 {
			 System.out.println("NetworkInterface():"+e.toString());
			 error = true;
		 }
		 
		 Listen4broadcasts();
		 Listen4Sockets();
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
		if(!error)
		{
			try
			 {
				 DatagramPacket Message = new DatagramPacket(
						 ver.getBytes(), 
						 ver.length(), 
						 netGroup,
						 Perent.getGameMulticastPort());
				 broadcastSocket.send(Message);
			 }
			 catch (IOException e) 
			 {
				 System.out.println("Sending broadcast"+e.toString());
				 error = true;
			 }
		}
	}
	
	//====================================================
	
	private void Listen4Sockets()
	{
		Thread4ReturnShip = new Thread(new Runnable()
		{
			public void run()
		    {
				try 
				{
					while(true)
					{
						System.out.println("Thread4ReturnShip");
						//Will with till a clint trys to connect
						incomingListen2User = ServerSocket4ReturnShip.accept();//Livelockb
						System.out.println("Thread4ReturnShip accept()");
						DataInputStream dis = new DataInputStream(incomingListen2User.getInputStream());
						System.out.println("Thread4ReturnShip DataInputStream");
						ObjectInputStream ois = new ObjectInputStream(dis);
						//Object inObj = (Object)ois.readObject();
						Defender inObj = (Defender)ois.readObject();
						System.out.println("Defender RECIVING: "+inObj.toString());
						
						//if(inObj.toString().startsWith("Defender"))//works
						//{
							Perent.addDefender(inObj,true);
							System.out.println("ADDED");
							if(true == inObj.returnShip)
							{
							SendShip(incomingListen2User.getInetAddress(),false);
							}
						//}
					}
				}
				catch(Exception e2)
				{
					System.out.println("Listen4connect = new Thread "+e2.toString()); 
				}
		    }
		});
		Thread4ReturnShip.start();
	}
	private void Listen4Gamebroadcasts()
	{
		
	}
	
	private void Listen4broadcasts()
	{
		ListenThread = new Thread(new Runnable()
		{
			public void run()
            {
	        	while(true)
	            {								//			  x   y  hading
	        		byte[] buf = new byte[64];// will need to find the byte leagn of this???
	        		//Frames with fewer than 64 bytes are padded out to 64 bytes with the Pad field.

	                DatagramPacket incomingPacket = new DatagramPacket(buf, buf.length);
	           
	                try
	                {
	                	broadcastSocket.receive(incomingPacket);
	                	String incomingData = new String(incomingPacket.getData());
	                	incomingData = incomingData.trim();
	                	
	                	if(false == incomingPacket.getAddress().toString().equals(Perent.getLocalAddress()))
	                	{
	                		Perent.updateNet(true);
	                		/*
	                		if(incomingData.startsWith(SendDefenderLocation_ID))
	                		{
	                			//incomingData ship: x : y : heading
	                			String[] inputLocation = incomingData.split(":");
	                			inputLocation[0] = incomingPacket.getAddress().toString();
	                			//inputLocation [0]=ID [1]=X  [2]=Y  [3]=heading
	                			Perent.ReceiveDefenderLocation(inputLocation);
	                		}
	                		else if(incomingData.startsWith(SendShotLocation_ID))
	                		{
	                			String[] inputLocation = incomingData.split(":");
	                			inputLocation[0] = incomingPacket.getAddress().toString();
	                			Perent.ReceiveShotLocation(inputLocation);
	                		}*/
	                		if(incomingData.startsWith("PCNum"))
	                		{
	                			int port = Integer.parseInt((incomingData.split(":"))[1]);
	                			if(port>=Perent.getPCNum())
	                			{
	                				Perent.setPCNum(port++);
	                			}
	                		}
	                		else if(incomingData.equals(Perent.version()))
	                		{
	                			System.out.println(Perent.version()+" : "+incomingPacket.getAddress().toString());
	                			sent("PCNum:"+Perent.getPCNum());
	                			//SendShip(incomingPacket.getAddress(),true);
		            		}
	                	}
	                	else
	                	{
	                		Perent.updateNet(false);
	                	}
	                }
	                catch(Exception ex)
	                {
	                	System.out.println("Sending Listen for broadcasts: "+ex.toString());
	                } 	
	            }
            }});
		ListenThread.start();
	}
	private void SendShip(final InetAddress Address,final boolean returnShip)
	{
		Thread4SendShip
		 = new Thread(new Runnable()
			{
				public void run()
	            {
					try
					{
						System.out.println("Thread4SendShip");
						Socket socket_SendMyShip = 
							new Socket(Address,Core.socketPort);
						System.out.println("Thread4SendShip socket_SendMyShip");
						ObjectOutputStream oOutputStream = 
							new ObjectOutputStream(socket_SendMyShip.getOutputStream());
						System.out.println("Thread4SendShip ObjectOutputStream");
						Defender myDefender = Perent.getDefender();
						myDefender.returnShip = returnShip;
						oOutputStream.writeObject(myDefender);
								oOutputStream.flush();
								oOutputStream.close();
							socket_SendMyShip.close();
						System.out.println("Defender SENT");
					}
					catch(Exception ex)
			        {
						System.out.println("SendShip("+Address.toString()+") "+ ex.toString());
			        }
	            }
			});
		Thread4SendShip.start();
	}
}
