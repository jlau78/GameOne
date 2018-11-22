package com.king.model.repository;

import java.util.Map;

public interface MapRepository<K, V> extends Repository {

	/**
	 * Put the key/value pair in the repository
	 * 
	 * @param key
	 * @param value
	 * @return True is success
	 */
	public boolean update(final K key, final V value);

	/**
	 * Find the value for the given key
	 * 
	 * @param key
	 * @return Value of the key
	 */
	public V find(final K key);

	/**
	 * Remove the key from the repository
	 * 
	 * @param key
	 * @return True is success
	 */
	public boolean remove(final K key);

	/**
	 * Get the map
	 */
	public Map<K, V> getAll();

}
