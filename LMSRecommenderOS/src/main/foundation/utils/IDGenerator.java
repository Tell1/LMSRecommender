package main.foundation.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class provides the creation of global unique identifiers.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 * 
 */
public class IDGenerator {

	private static final AtomicInteger id = new AtomicInteger();

	private IDGenerator() {
	}

	public static int next() {
		return id.getAndIncrement();
	}
}
