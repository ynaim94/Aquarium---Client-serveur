package aqua;

import java.io.*;
// import Java.lang.String;
import java.util.logging.*;

public class ClientLog {

    private final static Logger logger = Logger.getLogger(Logger.class.getName());
	private static FileHandler fh = null;	
	
		
	
	// public static void init
	ClientLog(){
		try {
			fh=new FileHandler("ClientLog.log", false);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		logger.setLevel(Level.CONFIG);
	    logger.config("Client logging ..");  

	}
	
	
	
	public static void info(String message) {
		logger.info(message);
	}	
		
}