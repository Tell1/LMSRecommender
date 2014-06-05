package main.foundation.profileDB;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

import jcolibri.util.FileIO;
import main.foundation.utils.SqlFile;

import org.hsqldb.Server;
/**
 * This class creates a data base server with the tables for the profiles of
 * the e-learning environment using the HSQLDB library.
 * 
 * @author <a href="mailto:mueller_pettenpohl@me.com">Tell
 *         Mueller-Pettenpohl</a>
 * 
 * @version 1.0
 *
 */
public class ProfileHSQLDBserver {

    static boolean initialized = false;

    private static Server server;

    /**
     * Initialize the server
     */
    @SuppressWarnings("rawtypes")
	public static void init()
    {
	if (initialized)
	    return;
        org.apache.commons.logging.LogFactory.getLog(ProfileHSQLDBserver.class).info("Creating data base ...");

	server = new Server();
	server.setDatabaseName(0, "LMS_REC");
	server.setDatabasePath(0, "mem:LMS_REC;sql.enforce_strict_size=true");
	
	server.setLogWriter(null);
	server.setErrWriter(null);
	server.setSilent(true);
	server.start();

	initialized = true;
	try
	{
	    Class.forName("org.hsqldb.jdbcDriver");

	    PrintStream out = new PrintStream(new ByteArrayOutputStream());
	    Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/LMS_REC", "sa", "");
	    SqlFile file = new SqlFile(new
	    File(FileIO.findFile("main/foundation/profileDB/LMS_REC.sql").getFile()),false,new HashMap());
	    file.execute(conn,out,out, true);
	    
	    org.apache.commons.logging.LogFactory.getLog(ProfileHSQLDBserver.class).info("Data base generation finished");
	    
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(ProfileHSQLDBserver.class).error(e);
	}

    }

    /**
     * Shutdown the server
     */
    public static void shutDown()
    {

	if (initialized)
	{
	    server.stop();
	    initialized = false;
	}
    }
}
