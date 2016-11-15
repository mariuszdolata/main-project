package encore.database.importdata;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa przechowujaca jeden rekord z tabeli.
 * 
 * @author Mariusz Dolata @2016
 *
 */
public class Entity {
	/**
	 * Mapa przechowujaca dane jednego rekordu. Kluczem jest String bedacy nazwa
	 * kolumny, a obiektem wartosc kolumny
	 */
	private Map<String, Object> entity = new HashMap<>();
	private static String pk_name;

	public Object getEntity(String name) {
		return entity.get(name);
	}

	public void putEntity(String name, Object obj) {
		entity.put(name, obj);
	}

	public Entity() {

	}

}
