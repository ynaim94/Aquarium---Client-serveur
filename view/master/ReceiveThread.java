package aqua;
import java.io.*;
import java.util.regex.Pattern;

public class ReceiveThread extends Thread{
    
    private AquaConnection aquaCon;
    private Aquarium aquarium;
    
    public ReceiveThread(AquaConnection aquaCon){
	this.aquaCon = aquaCon;
    }
    
    public synchronized void run(){
	ClientLog logger = new ClientLog();
	Pattern[] pattern = new Pattern[2];
	pattern[0]= Pattern.compile("^getFishes\\s*");
	pattern[1]= Pattern.compile("^getFishesContinuously\\s*");

	try{
	    
	    while(true){
		
		String response = aquaCon.receive();
		String cmd = aquaCon.getCmd(); // Ne marche pas : Décalage
		//		System.out.println(cmd);
		//		System.out.println(response);
		if (((response.charAt(0) == 'l') ||
		     (response.equals ("NOK: No fishes found") ))&&
		    ((pattern[0].matcher(cmd).matches() == false))) {
		    //System.out.println("if de gfc"+cmd);
		    //System.out.println("gfc: "+ response);
		    aquarium.getFishes(response);
		    logger.info(response);
		    if (pattern[1].matcher(cmd).matches()){
			//			System.out.println("here");
			aquaCon.setCmd("");
			aquaCon.setResponse("getFishesContinuously Launched");
			synchronized(aquaCon){
			    //  System.out.println("Redonne la main");
			    aquaCon.notify();
			}
		    }
		}
		else if (response.charAt(0) == 'p'){
		    //System.out.println("p: "+ response);
		    logger.info(response);
		}
		else  {
		    // System.out.println(response);
		    aquaCon.setResponse(response);
		    aquaCon.setCmd("");
		    synchronized(aquaCon){
			//	System.out.println("Redonne la main2");
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
