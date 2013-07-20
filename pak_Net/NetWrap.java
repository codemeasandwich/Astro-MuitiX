package pak_Net;

public class NetWrap
{
	Object aObject;
	String ClassName;
	boolean toReturn;
	
	public NetWrap(Object inputObject,String inputClassName)
	{
		this(inputObject, inputClassName, false);
	}
	public NetWrap(Object inputObject,String inputClassName, boolean inputToReturn)
	{
		aObject = inputObject;
		ClassName = inputClassName;
		toReturn = inputToReturn;
	}
	
	@Override
	public String toString()
	{
		return "NetWrap";
	}
	
	public String getObjectClassName()
	{
		return ClassName;
	}
	public boolean getToReturn()
	{
		return toReturn;
	}
	public Object getObject()
	{
		return aObject;
	}
}
