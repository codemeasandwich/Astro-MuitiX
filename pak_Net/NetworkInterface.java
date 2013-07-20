package pak_Net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import pak_Display.Defender;
import pak_Display.Rock;
import pak_logic.RockManager;
//import pak_Display.DefenderShot;

import pak_Core.Core;

public class NetworkInterface
{
	private MulticastSocket broadcastSocket;
	private Core Perent;
	private Thread ListenThread;
	private InetAddress netGroup;
	private StringBuilder Location;
	private String SendDefenderLocation_ID;
	private String SendShotLocation_ID;
	private String SendShotRemove_ID;
	private String lastDefenderLocation;
	private boolean error;
	private Thread Thread4Return2NetWap;
	private Thread Thread4Send2NetWap;
	private ServerSocket ServerSocket4ReturnShip;
	private Socket incomingListen2User;
	private ArrayList<InetAddress> aLiveAddresses;
	private boolean[] aLiveBool;
	private Defender myDefender;
	
	public static final byte STRING = -1;
	public static final byte HIT_TEST = 0;
	public static final byte HIT = 1;
	public static final byte SHIP = 2;
	public static final byte SHIPKILLED = 3;
	public static final byte SHIPALIVE = 4;
	public static final byte ROCKSRequest = 5;
	public static final byte ROCKSResponse = 6;
	public static final byte ROCKHIT = 7;
	
	private String typeConveter(byte val)
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
	
	public NetworkInterface(Core inputPerent)
	{
		System.out.print("NetworkInterface:");
		Perent = inputPerent;
		error = false;
		aLiveAddresses = new ArrayList<InetAddress>();
		
		 try
		 {
			SendDefenderLocation_ID = "DP:";
			SendShotLocation_ID = "SL:";
			SendShotRemove_ID = "SR:";
			lastDefenderLocation = "";
			netGroup = InetAddress.getByName(Core.GroupIP);
			broadcastSocket = new MulticastSocket(Core.GroupPort);//send & recive on this
			broadcastSocket.joinGroup(netGroup);
			ServerSocket4ReturnShip = new ServerSocket(Core.socketPort);

		 }
		 catch (IOException e) 
		 {
			 Perent.setError("NetworkInterface():"+e.toString());
			 System.out.println("NetworkInterface():"+e.toString());
			 error = true;
		 }
		 
		 Listen4broadcasts();
		 Listen4NetWrap();
		 sent(Perent.version());
		 System.out.println("Done");
	}
	/*
	public void aLiveTest()
	{
		 
	}
	*/
	
	public boolean setMyDefender(Defender inputDefender)
	{
		if(null == myDefender)
		{
			myDefender = inputDefender;
			return true;
		}
		else
		{
			return false;
		}
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

	public void Send2NetWap(//like client
			final InetAddress Address,
			final Object objToSend,
			final byte Type,
			final boolean returnThis)
	{
		Thread4Send2NetWap
		 = new Thread(new Runnable()
			{
				public void run()
	            {
					try
					{
						if(Perent.getLocalAddress() == Address)//if send to me.. then send to every one BUT me
						{
                			for(InetAddress address: aLiveAddresses)
                			{
                				Send2NetWap(address,objToSend,Type,returnThis);
                			}
						}
						else
						{
							Perent.updateNet(false);
							System.out.println("OUT NetWap:"+typeConveter(Type)+" "+Address.toString());
							
							Socket socket_SendMyShip = 
								new Socket(Address,Core.socketPort);
							
							ObjectOutputStream outputStream = 
								new ObjectOutputStream(socket_SendMyShip.getOutputStream());
							
							NetWrap wrap = new NetWrap(objToSend,Type,returnThis);
							
							outputStream.writeObject(wrap);
									outputStream.flush();
									outputStream.close();
								socket_SendMyShip.close();
								//System.out.println("Send2NetWap:Sent"+" "+Address.toString());
						}
					}
					catch(ConnectException e)
					{
            			for(int count = 0; count > aLiveAddresses.size(); count++)
            			{
            				if(aLiveAddresses.get(count).equals(Address))
            				{
            					aLiveAddresses.remove(count);
            					break;
            				}
            			}
						Perent.killDefender(Address);
						System.out.println("SendShip("+Address.toString()+") "+ e.toString());
					}
					catch(Exception ex)
			        {
						//Perent.setError("SendShip("+Address.toString()+") "+ ex.toString());
						Perent.killDefender(Address);
						System.out.println("SendShip("+Address.toString()+") "+ ex.toString());
			        }
	            }
			});
		Thread4Send2NetWap.start();
	}
	
	
	//====================================================
	
	private boolean addIP(InetAddress inputIP)
	{
		boolean found = false;
		
		for/*each*/ (InetAddress address: aLiveAddresses)
		{
			if(inputIP.equals(address))
			{
				found = true;
				break;//End loop
			}
		}
		
		if(!found)
		{
			aLiveAddresses.add(inputIP);
		}
		
		return found;
	}
	
	private void Listen4NetWrap()//like server
	{
		Thread4Return2NetWap = new Thread(new Runnable()
		{
			public void run()
		    {
				try 
				{
					while(true)
					{
						//Will with till a clint trys to connect
						incomingListen2User = ServerSocket4ReturnShip.accept();//Livelockb
						ObjectInputStream ois = new ObjectInputStream(
								new DataInputStream(
										incomingListen2User.getInputStream()));
						
						NetWrap incomingWrap = (NetWrap)ois.readObject();
						
						System.out.println("IN NetWap:"+typeConveter(incomingWrap.getType())+" "+incomingListen2User.getInetAddress().toString());
						
						Perent.updateNet(true);
						
						if(incomingWrap.getType() == NetworkInterface.SHIP)
						{
							Perent.addDefender((Defender)incomingWrap.getObject(),true);
							
							if(true == incomingWrap.returnToSender())
							{
		              			Send2NetWap(
	 	              					incomingListen2User.getInetAddress(),
	 	              					myDefender,//Perent.getDefender(),
	                					NetworkInterface.SHIP,
	                					false);
		              		}
							
							if(aLiveAddresses.isEmpty()) //this is the first to talk to me
							{
								Send2NetWap(
										incomingListen2User.getInetAddress(),
										null,NetworkInterface.ROCKSRequest,true);
							}
							
							addIP(incomingListen2User.getInetAddress());
							
						}
						else if(incomingWrap.getType() == NetworkInterface.HIT_TEST)
						{
							/*
							if(Perent.HitTest((int[])incomingWrap.getObject()))
							{
								System.out.println("They hit me :(");
								Perent.killDefender();
							}
							else
							{
								Defender Def = Perent.getDefender();
	 							
								SendDefenderLocation(Def.getXY()[0],Def.getXY()[1],Def.getHeading(),Def.getDrift());
							}*/
							myDefender.killDefender();
							
							Perent.sendSocketMessage(myDefender.getID(),
									myDefender.getID().toString(),
									NetworkInterface.SHIPKILLED,
									false);
							//Perent.killDefender();
							
						}
						else if(incomingWrap.getType() == NetworkInterface.SHIPKILLED)
						{
							Perent.killDefender(incomingListen2User.getInetAddress());
						}
						else if(incomingWrap.getType() == NetworkInterface.SHIPALIVE)
						{
							Perent.ReSpawnDefender(incomingListen2User.getInetAddress());
						}
						else if(incomingWrap.getType() == NetworkInterface.ROCKSRequest)
						{
							//System.out.println("ROCKSRequest <-");
                			Send2NetWap(
                					incomingListen2User.getInetAddress(),
                					Perent.getRockManager(),
                					NetworkInterface.ROCKSResponse,
                					false);
						}
						else if(incomingWrap.getType() == NetworkInterface.ROCKSResponse)
						{
							//System.out.println("ROCKSResponse ->");
							Perent.setRockManager((RockManager)incomingWrap.getObject());
						}
						else if(incomingWrap.getType() == NetworkInterface.ROCKHIT)
						{
							//System.out.println("ROCKSResponse ->");
							Perent.setRockHit((Rock[])incomingWrap.getObject());
						}
                		else
                		{
                			throw new IllegalArgumentException
                					//("NetworkInterface Class: Net Wrap cannot be converted to "+ incomingWrap.getType());
                					("("+incomingWrap.getType()+")NetWrap is in invalid");
                		}
					}
				}
				catch(Exception e2)
				{
					Perent.setError("Listen4connect = new Thread "+e2.toString());
					System.out.println("Listen4connect = new Thread "+e2.toString()); 
					e2.printStackTrace();
				}
		    }
		});
		Thread4Return2NetWap.start();
	}
	
	private void Listen4broadcasts()
	{
		ListenThread = new Thread(new Runnable()
		{
			public void run()
            {
	        	while(true)
	            {
	        		byte[] buf = new byte[32];// will need to find the byte leagn of this???
	        		//Frames with fewer than 64 bytes are padded out to 64 bytes with the Pad field.

	                DatagramPacket incomingPacket = new DatagramPacket(buf, buf.length);
	                String incomingData = "";
	                try
	                {
	                	broadcastSocket.receive(incomingPacket);
	                	incomingData = new String(incomingPacket.getData());
	                	incomingData = incomingData.trim();
	                	
	                	if(false == incomingPacket.getAddress().equals(Perent.getLocalAddress()))
	                	{
	                		Perent.updateNet(true);
	                		
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
	                		}
	                		else if(incomingData.equals(Perent.version()))
	                		{
	                			addIP(incomingPacket.getAddress());
	                			
	                			System.out.println(Perent.version()+" : "+incomingPacket.getAddress().toString());

	                			Send2NetWap(
	                					incomingPacket.getAddress(),
	                					myDefender,//Perent.getDefender(),
	                					NetworkInterface.SHIP,
	                					true);
		            		}
	                	}
	                	else
	                	{
	                		Perent.updateNet(false);
	                	}
	                }
	                catch(Exception ex)
	                {
	                	Perent.setError("Sending Listen for broadcasts: "+ex.toString());
	                	System.out.println("Sending Listen for broadcasts("+incomingData+"): "+ex.toString());
	                } 	
	            }
            }});
		ListenThread.start();
	}
}
