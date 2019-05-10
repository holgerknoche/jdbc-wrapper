package jdbcwrapper.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class NestedMap<K1, K2, V> {
	
	private final Map<K1, Map<K2, V>> outerMap;	 
	
	private final Supplier<Map<K2, V>> innerMapSupplier;
	
	public NestedMap() {
		this(HashMap::new, HashMap::new);
	}
	
	public NestedMap(final Supplier<Map<K1, Map<K2, V>>> outerMapSupplier, final Supplier<Map<K2, V>> innerMapSupplier) {
		this.outerMap = outerMapSupplier.get();
		this.innerMapSupplier = innerMapSupplier;
	}
	
	public void put(final K1 outerKey, final K2 innerKey, final V value) {
		Map<K2, V> innerMap = this.outerMap.get(outerKey);
		
		if (innerMap == null) {
			innerMap = this.innerMapSupplier.get();
			this.outerMap.put(outerKey, innerMap);
		}
		
		innerMap.put(innerKey, value);
	}
	
	public Map<K2, V> get(final K1 outerKey) {
		return this.outerMap.get(outerKey);
	}
	
	public Set<K1> getOuterKeys() {
		return this.outerMap.keySet();
	}
	
}
