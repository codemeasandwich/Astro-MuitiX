package pak_Net;

import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import pak_Core.Core;

public class Send2NetWap extends Thread
{
	private NetworkInterface net;
	private Core Perent;
	
	public Send2NetWap(NetworkInterface inputNet, Core inputPerent)
	{
		Perent = inputPerent;
		net = inputNet;
	} 
	
	public void Send(//like client
			final InetAddress Address,
			final Object objToSend,
			final byte Type,
			final boolean returnThis)
	{
		Thread Thread4Send2NetWap
		 = new Thread(new Runnable()
			{
				public void run()
	            {
					try
					{
						if(Perent.getLocalAddress() == Address)//if send to me.. then send to every one BUT me
						{
                			for(InetAddress address: net.aLiveAddresses)
                			{
                				Send(address,objToSend,Type,returnThis);
                			}
						}
						else
						{
							Perent.updateNet(false);
							System.out.println("OUT NetWap:"+net.typeConveter(Type)+" "+Address.toString());
							
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
            			for(int count = 0; count > net.aLiveAddresses.size(); count++)
            			{
            				if(net.aLiveAddresses.get(count).equals(Address))
            				{
            					net.aLiveAddresses.remove(count);
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
}
