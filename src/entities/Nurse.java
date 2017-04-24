package entities;

import java.util.ArrayList;

public class Nurse {
	
	
	private int ID;
	private String name;
	private ArrayList<Bundle> bundles;
	
	public Nurse(int id, String name, ArrayList<Bundle> bundles){
		
		this.ID = id;
		this.name = name;
		this.bundles = bundles;
	}
	
	public int getID(){
		
		return ID;
	}
	
	public String getName(){
		
		return name;
	}
	
	public ArrayList<Bundle> getBundles(){
		return bundles;
	}
	
	public int getBundleListSize()
	{
		return bundles.size();
	}
	
	public Bundle getLowestCostBundle1()
	{
		return bundles.get(0);
	}
	
	public Bundle getLowestCostBundle2()
	{
		double potentialMin = -1;
		Bundle b = null;
		int counter = 0;
		int id = 0;
		for(Bundle bd: bundles)
		{
			counter++;
			if(potentialMin == -1)
			{
				potentialMin = bd.price;
				b = bd;
				id = counter;
			}
			else
			{
				if(bd.price < potentialMin)
				{
					potentialMin = bd.price;
					b = bd;
					id = counter;
				}
			}
		}
		bundles.remove(id-1);
		return b;
	}

}