package test.foundation;

import main.foundation.docDB.HSQLDBserver;

import org.junit.Test;
/**
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 *
 */
public class HSQLDBserverTest {

	@Test
	public void testInitShutdown() {
		HSQLDBserver.init();
		HSQLDBserver.shutDown();
	}

}
