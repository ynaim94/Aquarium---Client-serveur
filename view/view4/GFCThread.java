package aqua;
import java.io.*;

public class GFCThread{

    private AquaConnection aquaCon;
    private Aquarium aquarium;
    
    public GFCThread(AquaConnection aquaCon){
	this.aquaCon = aquaCon;
    }

    public void run(){
	try{
	    aquaCon.send("getFishesContinuously");
	    
	    while(true){
		String response = aquaCon.receive();
		aquarium.getFishes(response);
		System.out.println("> "+response);
	    }
	}
	catch (IOException e){
	    System.out.println("Error send/receive");
	    e.printStackTrace();
	}
    }
}
