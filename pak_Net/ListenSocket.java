package pak_Net;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import pak_Core.Core;
import pak_Display.Defender;
import pak_Display.Rock;
import pak_logic.RockManager;

public class ListenSocket extends Thread
{
	private ServerSocket ServerSocket4ReturnShip;
	private Socket incomingListen2User;
	private NetworkInterface Perent;
	private Core theCore;
	private Defender myDefender;
	
	public ListenSocket(NetworkInterface inputPerent, Core inputCore, Defender inputDefender)
	{
		Perent = inputPerent;
		theCore = inputCore;
		myDefender = inputDefender;
	}
	
	public void run()
    {
		try 
		{
			ServerSocket4ReturnShip = new ServerSocket(Core.socketPort);
			while(true)
			{
				//Will with till a clint trys to connect
				//ServerSocket ServerSocket4ReturnShip = new ServerSocket(Core.socketPort);
				incomingListen2User = ServerSocket4ReturnShip.accept();//Livelockb
				ObjectInputStream ois = new ObjectInputStream(
						new DataInputStream(
								incomingListen2User.getInputStream()));
				
				NetWrap incomingWrap = (NetWrap)ois.readObject();
				
				System.out.println("IN NetWap:"+Perent.typeConveter(incomingWrap.getType())+" "+incomingListen2User.getInetAddress().toString());
				
				theCore.updateNet(true);
				
				if(incomingWrap.getType() == NetworkInterface.SHIP)
				{
					theCore.addDefender((Defender)incomingWrap.getObject(),true);
					
					if(true == incomingWrap.returnToSender())
					{
						Perent.Send2NetWap(
	              					incomingListen2User.getInetAddress(),
	              					myDefender,//Perent.getDefender(),
            					NetworkInterface.SHIP,
            					false);
              		}
					
					if(Perent.aLiveAddressesisEmpty()) //this is the first to talk to me
					{
						Perent.Send2NetWap(
								incomingListen2User.getInetAddress(),
								null,NetworkInterface.ROCKSRequest,true);
					}
					
					Perent.addIP(incomingListen2User.getInetAddress());
					
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
					
					theCore.sendSocketMessage(myDefender.getID(),
							myDefender.getID().toString(),
							NetworkInterface.SHIPKILLED,
							false);
					//Perent.killDefender();
					
				}
				else if(incomingWrap.getType() == NetworkInterface.SHIPKILLED)
				{
					theCore.killDefender(incomingListen2User.getInetAddress());
				}
				else if(incomingWrap.getType() == NetworkInterface.SHIPALIVE)
				{
					theCore.ReSpawnDefender(incomingListen2User.getInetAddress());
				}
				else if(incomingWrap.getType() == NetworkInterface.ROCKSRequest)
				{
					//System.out.println("ROCKSRequest <-");
					Perent.Send2NetWap(
        					incomingListen2User.getInetAddress(),
        					theCore.getRockManager(),
        					NetworkInterface.ROCKSResponse,
        					false);
				}
				else if(incomingWrap.getType() == NetworkInterface.ROCKSResponse)
				{
					//System.out.println("ROCKSResponse ->");
					theCore.setRockManager((RockManager)incomingWrap.getObject());
				}
				else if(incomingWrap.getType() == NetworkInterface.ROCKHIT)
				{
					//System.out.println("ROCKSResponse ->");
					theCore.setRockHit((Rock[])incomingWrap.getObject());
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
			theCore.setError("Listen4connect = new Thread "+e2.toString());
			System.out.println("Listen4connect = new Thread "+e2.toString()); 
			e2.printStackTrace();
		}
    }
}

