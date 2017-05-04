package aqua;
import java.io.*;

public class ReceiveThread extends Thread{
    
    private AquaConnection aquaCon;
    private Aquarium aquarium;
    
    public ReceiveThread(AquaConnection aquaCon){
	this.aquaCon = aquaCon;
    }
    
    public synchronized void run(){
	ClientLog logger = new ClientLog();
	/*try{
	  synchronized (aquaCon){
	  aquaCon.wait();
	  }
	  }
	  catch (InterruptedException e) {
	  e.printStackTrace();
	  }*/
	try{
	    
	    while(true){
		
		String response = aquaCon.receive();
		String cmd = aquaCon.getCmd(); // Ne marche pas : Décalage
		//System.out.println(cmd);
		//System.out.println(response);
		if (((response.charAt(0) == 'l') ||
		     (response.equals ("NOK: No fishes found") ))&&
		    ((cmd.equals("getFishes") == false))) {
		    //		    System.out.println("if de gfc"+cmd);
		    if (response.charAt(0) == 'l'){
			aquarium.getFishes(response);
		    }
		    logger.info(response);
		    if (cmd.equals("getFishesContinuously")){
			System.out.println("here");
			aquaCon.setCmd("");
			aquaCon.setResponse("getFishesContinuously Launched");
			synchronized(aquaCon){
			    System.out.println("Redonne la main");
			    aquaCon.notify();
			}
		    }
		}
		else if (response.charAt(0) == 'p'){
		    logger.info(response);
		}
		else  {
		    // System.out.println(response);
		    aquaCon.setResponse(response);
		    aquaCon.setCmd("");
		    synchronized(aquaCon){
			System.out.println("Redonne la main2");
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
