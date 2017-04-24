package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Scheduler
{
	public static ArrayList<String> allocation = new ArrayList<String>();
	public static ArrayList<String> allocation2 = new ArrayList<String>();
	public static double total1;
	public static double total2;
	public static ArrayList<String> extractedRequests = new ArrayList<String>();
	
	public void createAllocation(String string)
	{
		allocation.add(string);
	}
	
	public void createAllocation2(String string)
	{
		allocation2.add(string);
	}
	
	public ArrayList<String> getAllocation()
	{	
		if(total1 < total2)
		{
			return allocation;
		}
		
		else if(total2 < total1)
		{
			return allocation2;
		}
		
		else
		{
			return allocation2;
		}
	}
	
	public void extractRequests()
	{
		for(int i=0; i<allocation.size(); i++)
		{
			String allocation = Scheduler.allocation.get(i);
			String[] bundles = allocation.split("-");
			String[] requests = bundles[1].split(", ");
			for(int j=0; j<requests.length; j++)
			{
				extractedRequests.add(requests[j]);
			}
		}
	}
	
	
	
	
}
