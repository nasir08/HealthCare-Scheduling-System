package data;

import java.util.ArrayList;

public class Scheduler
{
	private static ArrayList<String> allocation = new ArrayList<String>();
	public static ArrayList<String> extractedRequests = new ArrayList<String>();
	public static long computationTime;
	
	public void createAllocation(String string)
	{
		allocation.add(string);
	}
	
	public ArrayList<String> getAllocation()
	{
		return allocation;
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
