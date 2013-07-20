package pak_Net;

import java.net.DatagramPacket;

import pak_Core.Core;
import pak_Display.Defender;

public class ListenBroadcastsThread extends Thread
{
	private NetworkInterface net;
	private Core Perent;
	private Defender myDefender;
	
	public ListenBroadcastsThread(NetworkInterface inputNet, Core inputPerent, Defender inputDefender)
	{
		Perent = inputPerent;
		net = inputNet;
		myDefender = inputDefender;
	}
	
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
            	net.broadcastSocket.receive(incomingPacket);
            	incomingData = new String(incomingPacket.getData());
            	incomingData = incomingData.trim();
            	
            	if(false == incomingPacket.getAddress().equals(Perent.getLocalAddress()))
            	{
            		Perent.updateNet(true);
            		
            		if(incomingData.startsWith(NetworkInterface.SendDefenderLocation_ID))
            		{
            			//incomingData ship: x : y : heading
            			String[] inputLocation = incomingData.split(":");
            			inputLocation[0] = incomingPacket.getAddress().toString();
            			//inputLocation [0]=ID [1]=X  [2]=Y  [3]=heading
            			Perent.ReceiveDefenderLocation(inputLocation);
            		}
            		else if(incomingData.startsWith(NetworkInterface.SendShotLocation_ID))
            		{
            			String[] inputLocation = incomingData.split(":");
            			inputLocation[0] = incomingPacket.getAddress().toString();
            			Perent.ReceiveShotLocation(inputLocation);
            		}
            		else if(incomingData.equals(Perent.version()))
            		{
            			net.addIP(incomingPacket.getAddress());
            			
            			System.out.println(Perent.version()+" : "+incomingPacket.getAddress().toString());

            			net.Send2NetWap(
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
    }
}
