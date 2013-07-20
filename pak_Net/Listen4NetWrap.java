package pak_Net;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import pak_Core.Core;
import pak_Display.Defender;
import pak_Display.Rock;
import pak_logic.RockManager;

public class Listen4NetWrap extends Thread
{
	private NetworkInterface net;
	private Core Perent;
	private Defender myDefender;
	
	public Listen4NetWrap(NetworkInterface inputNet, Core inputPerent, Defender inputDefender)
	{
		Perent = inputPerent;
		net = inputNet;
		myDefender = inputDefender;
		
	} 
	
	public void run()
    {
		try 
		{
			ServerSocket ServerSocket4ReturnShip = new ServerSocket(Core.socketPort);
			
			while(true)
			{
				//Will with till a clint trys to connect
				
				Socket incomingListen2User = ServerSocket4ReturnShip.accept();//Livelockb
				ObjectInputStream ois = new ObjectInputStream(
						new DataInputStream(
								incomingListen2User.getInputStream()));
				
				NetWrap incomingWrap = (NetWrap)ois.readObject();
				
				System.out.println("IN NetWap:"+net.typeConveter(incomingWrap.getType())+" "+incomingListen2User.getInetAddress().toString());
				
				Perent.updateNet(true);
				
				if(incomingWrap.getType() == NetworkInterface.SHIP)
				{
					Perent.addDefender((Defender)incomingWrap.getObject(),true);
					
					if(true == incomingWrap.returnToSender())
					{
						net.Send2NetWap(
	              					incomingListen2User.getInetAddress(),
	              					myDefender,//Perent.getDefender(),
            					NetworkInterface.SHIP,
            					false);
              		}
					
					if(net.IPListIsEmpty()) //this is the first to talk to me
					{
						net.Send2NetWap(
								incomingListen2User.getInetAddress(),
								null,NetworkInterface.ROCKSRequest,true);
					}
					
					net.addIP(incomingListen2User.getInetAddress());
					
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
					net.Send2NetWap(
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
}
