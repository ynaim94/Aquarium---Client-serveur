package aqua;
import java.io.*;
import java.util.regex.Pattern;

public class ReceiveThread extends Thread{
    
    private AquaConnection aquaCon;
    private Aquarium aquarium;
    private boolean connected = true;
    
    public ReceiveThread(AquaConnection aquaCon){
	this.aquaCon = aquaCon;
    }

    /** @brief  run
     * @param[in]  NONE
     *
     * @param[out] AquaConnection aquaCon
     * 
     *
     * @return     NONE
     *
     * @details    Receives and treats the server's response.
     *
     */

    public synchronized void run(){
	ClientLog logger = new ClientLog();
	Pattern[] pattern = new Pattern[2];
	pattern[0]= Pattern.compile("^getFishes\\s*");
	pattern[1]= Pattern.compile("^getFishesContinuously\\s*");

	try{
	    
	    while(connected == true){
		
		String response = aquaCon.receive();
		String cmd = aquaCon.getCmd();
 		if (((response.charAt(0) == 'l') ||
		     (response.equals ("NOK: No fishes found") ))&&
		    ((pattern[0].matcher(cmd).matches() == false))) {
		    aquarium.getFishes(response);
		    logger.info(response);
		    if (pattern[1].matcher(cmd).matches()){
			aquaCon.setCmd("");
			aquaCon.setResponse("getFishesContinuously Launched");
			synchronized(aquaCon){
			    aquaCon.notify();
			}
		    }
		}
		else if (response.charAt(0) == 'p'){
		    logger.info(response);
		}
		else  {
		    aquaCon.setResponse(response);
		    aquaCon.setCmd("");
		    if (response.charAt(0) == 'b'){
			connected =  false;			    
		    }
		    synchronized(aquaCon){
			aquaCon.notify();
		    }
		}
	    }
	}
	catch (IOException e){
	    System.out.println("Error send/receive");
	    e.printStackTrace();
	}
    }
    
}
