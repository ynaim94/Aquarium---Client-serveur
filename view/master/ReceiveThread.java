package aqua;
import java.io.*;

public class ReceiveThread extends Thread{
    
    private AquaConnection aquaCon;
    private Aquarium aquarium;
    
    public ReceiveThread(AquaConnection aquaCon){
	this.aquaCon = aquaCon;
    }
    
    public  void run(){
	ClientLog logger = new ClientLog();
	try{
	    
	    while(true){
		
		String response = aquaCon.receive();
		String cmd = aquaCon.getCmd(); 

		if (((response.charAt(0) == 'l') ||
		     (response.equals ("NOK: No fishes found") ))&&
		    ((cmd.equals("getFishes") == false))) {
		    
		    if (response.charAt(0) == 'l'){
			aquarium.getFishes(response);
		    }
		    logger.info(response);
		    if (cmd.equals("getFishesContinuously")){
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
