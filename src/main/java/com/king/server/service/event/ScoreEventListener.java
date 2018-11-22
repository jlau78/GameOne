package com.king.server.service.event;

import java.util.EventListener;

/**
 * Listener for ScoreEvents
 *
 */
public interface ScoreEventListener extends EventListener {

	public void scoreUpdate(final ScoreEvent event);

}
