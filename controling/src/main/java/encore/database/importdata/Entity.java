package encore.database.importdata;

import java.util.HashMap;
import java.util.Map;

public class Entity {
	private Map<String,Object> entity=new HashMap<>();
	private static String pk_name;

	public Object getEntity(String name){
		return entity.get(name);
	}
	public void setEntity(String name, Object obj){
		entity.put(name, obj);
	}
}
