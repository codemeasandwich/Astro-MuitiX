package pak_Net;

import java.io.Serializable;

public class NetWrap implements Serializable
{
	private static final long serialVersionUID = 1L;
	Object aObject;
	byte type;
	boolean toReturn;
	
	public NetWrap(Object inputObject, byte inputType)
	{
		this(inputObject, inputType, false);
	}
	
	public NetWrap(Object inputObject, byte inputType, boolean inputToReturn)
	{
		aObject = inputObject;
		type = inputType;
		toReturn = inputToReturn;
	}
	
	@Override
	public String toString()
	{
		return "NetWrap";
	}
	
	public byte getType()
	{
		return type;
	}
	
	public boolean returnToSender()
	{
		return toReturn;
	}
	public Object getObject()
	{
		return aObject;
	}
}
