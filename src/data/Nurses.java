package data;

import java.util.HashMap;

public class Nurses {
	private static HashMap<Integer, String> nurses = new HashMap<Integer, String>();
	
	public void setNursesList(int id, String name) {
		nurses.put(id, name);
	}
	
	public HashMap<Integer,String> getNurseList() {
		return nurses;
	}
}
