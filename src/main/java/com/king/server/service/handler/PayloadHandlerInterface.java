package com.king.server.service.handler;

/**
 * Handler components to handle the given payload
 * 
 * @param payload
 */
public interface PayloadHandlerInterface<T> {

	/**
	 * Handle the given payload
	 * 
	 * @param payload
	 */
	public void handle(T payload);
}
