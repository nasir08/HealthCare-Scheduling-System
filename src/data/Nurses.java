package data;

import java.util.ArrayList;
import java.util.HashMap;

public class Nurses {
	private static HashMap<Integer, String> nurses = new HashMap<Integer, String>();
	private static ArrayList<Integer> noOfBundles = new ArrayList<Integer>();
	private static ArrayList<String> pool = new ArrayList<String>();
	
	public void setNursesList(int id, String name) {
		nurses.put(id, name);
	}
	
	public HashMap<Integer,String> getNurseList() {
		return nurses;
	}

	public void createPool(String string) 
	{
		pool.add(string);
	}
	
	public String getPoolItem(int num) 
	{
		return pool.get(num);
	}

	public void setNoOfBundles(int num) 
	{
		noOfBundles.add(num);
	}
	
	public int getNoOfBundles(int num)
	{
		return noOfBundles.get(num);
	}
}
