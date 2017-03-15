package data;

import java.util.HashMap;

public class Requests {
	private static HashMap<Integer, Integer> requests = new HashMap<Integer, Integer>();
	
	public void setRequestList(int id, int price) {
		requests.put(id, price);
	}
	
	public HashMap<Integer,Integer> getRequestList() {
		return requests;
	}
}
