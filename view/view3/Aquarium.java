package aqua;
import java.util.regex.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Aquarium {
    private static Pattern [] pattern;
    private static Matcher matcher;
    private static AquaPanel contentPane;
    private static AquaConnection aquaCon;
    private static int port;
    private static InetAddress address;
    private static ClientLog logger;
    private static JFrame frame ;
    private static boolean connected=false;
    static Scanner scanner = new Scanner(System.in);

    /***Ajouté de view4****/
    private static Parser parser;
    

    private static void startFish(String name){
	contentPane.setStartFish(name);
    }
    
    private static void status(){
	System.out.println(contentPane.fishesToString(contentPane.Fishes));
    }

    private static void addFish(String cmd){
	String[] items =  parser.parseFishPosition(cmd);
	contentPane.setAddFish(items);
	
    }
    
    public static void getFishes(String response){
	String[][] items = parser.parseListFishPosition(response);
	contentPane.setGetFishes(items);
    }
    
    /***Fin Ajout view 4****/

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
	System.out.print("<"+response+"\n"); //+server's response
	logger.info("Client received " + response);
	
	
    }
    
    
    public static void main(String[] args) throws Exception
    {
	logger = new ClientLog();
	/*Get Configuration data*/  
	Config conf = new Config(args[0]);
	port = conf.getTcpPort();
	final String ImagesPath = conf.getVisualRepertory();
	address = conf.getIpAddress();
	try{
	aquaCon = new AquaConnection();
	/*cmd patterns*/
	pattern=new Pattern[11];  
	pattern[0]=Pattern.compile("^OK");//,Pattern.CASE_INSENSITIVE);
	//	pattern[1]= Pattern.compile("hello");
	pattern[2]= Pattern.compile("greeting \\w+");
	pattern[3]= Pattern.compile("^addFish ");
	pattern[4]= Pattern.compile("^delFish ");
	pattern[5]= Pattern.compile("^startFish \\w+");
	pattern[6]= Pattern.compile("^log out");
	pattern[7]= Pattern.compile("^bye");
	pattern[8]= Pattern.compile("^getFishes ");
	pattern[9]= Pattern.compile("^getFishesContinuously");
	pattern[10] = Pattern.compile("^status");
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
		else if (pattern[10].matcher(cmd).matches()){/*status*/
		    status();
		}
		else if(pattern[8].matcher(cmd).matches()){/*getFishes*/
		    //		    System.out.println("calling setFish methode (to update the arrayList Fishes)");
		    getFishes(response);
		}
		else if(pattern[9].matcher(cmd).matches()){/*getFishesContinuously*/
		    //		    System.out.println("getFishesContinuously()");
		    //		    getFishesContinuously();
	    }
		else{
		    if(pattern[0].matcher(response).find()){/*OK*/
		 /*test: addFish smilingFish at 61x52,4x3, RandomWayPoint*/
			System.out.println("This is ok :D");
			if(pattern[3].matcher(cmd).find()){  /*addFish*/
			    addFish(cmd);
			} 
			if(pattern[4].matcher(cmd).find()) /*delFish*/
			    System.out.println("calling delFish methode"); ;
			if(pattern[5].matcher(cmd).matches()) /*startFish*/{
			    startFish(cmd);
			}
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

