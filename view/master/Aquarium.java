package aqua;
import java.util.regex.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Aquarium {
    private static ArrayList<Fish> fishes;
    private static Pattern [] pattern;
    private static Matcher matcher;
    private static AquaPanel contentPane;
    private static AquaConnection aquaCon;
    private static int port;
    private static InetAddress address;
    private static ClientLog logger;
    private static JFrame frame ;
    /*To be removed after establishing a connection with the server*/
    private static boolean connected=false;
    /* create a scanner so we can read the command-line input*/
    static Scanner	scanner = new Scanner(System.in);

 
    private void displayGUI(String ImagesPath){
        frame = new JFrame("Aquarium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane = new AquaPanel(ImagesPath);        
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        //frame.setSize(1200,600);
	frame.setVisible(true);
	frame.setResizable(false);
    }
    
    
   private static void addFish(String name){
       System.out.println("calling addFish methode");
     
   }
    
    static private String promptIn()throws IOException{
	System.out.print(">"); 
	/*get the input as a String*/
	String cmd = scanner.nextLine();
	logger.info("Client sent " + cmd);
	cmd=cmd.intern();
	try {
	    aquaCon.send(cmd);
	}
	catch(IOException e) {
	    System.out.println("IOEXEption \n");
	}
	return cmd;
    }
    static private void promptOut(String response)throws IOException
    {	
	System.out.print("<"+response+"!\n"); //+server's response
	logger.info("Client received " + response);
	
	
    }
    
    public static void main(String[] args) throws Exception
    {
	fishes=new ArrayList<Fish>();
	logger = new ClientLog();
	/*Get Configuration data*/  
	Config conf = new Config(args[0]);
	port = conf.getTcpPort();
	final String ImagesPath = conf.getVisualRepertory();
	address = conf.getIpAddress();
	try{
	aquaCon = new AquaConnection();
	/*cmd patterns*/
	pattern=new Pattern[10];  
	pattern[0]=Pattern.compile("^OK");//,Pattern.CASE_INSENSITIVE);
	//	pattern[1]= Pattern.compile("hello");
	pattern[2]= Pattern.compile("greeting \\w+");
	pattern[3]= Pattern.compile("^addFish ");//^(addFish[ ]([[:alnum:]]+)[ ]at[ ][0-9]{1,2}x[0-9]{1,2},[0-9]{1,2}x[0-9]{1,2},[ ]([[:alnum:]]+)){1}
	pattern[4]= Pattern.compile("^delFish ");
	pattern[5]= Pattern.compile("^startFish \\w+");
	pattern[6]= Pattern.compile("^log out");
	pattern[7]= Pattern.compile("^bye");
	pattern[8]= Pattern.compile("^getFishes");
	pattern[9]= Pattern.compile("^getFishesContinuously");
	/*Prompt*/
	//	System.out.print(">>>>>>>Enter your command please <<<<<<<\n");
	String response ="",cmd="";

	while(cmd==""){  
	    if (connected==false){
		aquaCon.openConnection(address,port);
		connected=true;
	    }
	    cmd=promptIn();
	    response=aquaCon.receive();
	    promptOut(response);
	    //	  System.out.println(String.format("\nYour command is %s ; Your response is %s",cmd, response));
	    /*Handling response*/
		if(pattern[2].matcher(response).matches())/* greeting*/
		    {	
			/*Display the aquarium*/
			SwingUtilities.invokeLater(new Runnable(){    
				@Override
				public void run() {
				    new Aquarium().displayGUI(ImagesPath);
				}
			    });
		    }
		else if(pattern[8].matcher(cmd).matches()){/*getFishes*/
		    System.out.println("calling setFish methode (to update the arrayList Fishes)");
		}
		else if(pattern[9].matcher(cmd).matches()){/*getFishesContinuously*/
		    System.out.println("listening continuously+ promptout() with each response");
	    }
		else{
		    if(pattern[0].matcher(response).matches()){/*OK*/
		 /*test: addFish SmilingFish at 61x52,4x3, RandomPathWay*/
			if(pattern[3].matcher(cmd).find()){  /*addFish*/
			   
			    addFish(cmd);
			} 
			if(pattern[4].matcher(cmd).find()) /*delFish*/
			    System.out.println("calling delFish methode"); ;
			if(pattern[5].matcher(cmd).matches()) /*startFish*/
			    System.out.println("calling startFish methode"); ;
		    }
		    if((pattern[6].matcher(cmd).find())&&(pattern[7].matcher(response).find())){/*log out & bye*/
			if(frame.isDisplayable())
			    frame.setVisible(false);
			aquaCon.closeConnection();
			connected=false;	
		    }	
	    }
	    cmd="";
	}
		}catch(IOException e){
	    System.out.println("No Server!!!");}
    }
    
    
}

