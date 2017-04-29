refpackage aqua;
import java.util.regex.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;

public class Aquarium {
    private static Pattern [] pattern;
    private static Matcher matcher;
    private AquaPanel contentPane;
    private static AquaConnection aquaCon;
    private static int port;
    private static InetAddress address;
    private static ClientLog logger;
    private static JFrame frame ;
    /*To be removed after establishing a connection with the server*/
    private static boolean connected=false;
    /* create a scanner so we can read the command-line input*/
    static Scanner	scanner = new Scanner(System.in);
    private void displayGUI(String ImagesPath)
    {
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

    static private String promptIn()throws IOException
    {
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
	System.out.print("<"+response); //+server's response
	logger.info("Client received " + response);


    }

    static private void getFishesContinuously() {
	
    }

    public static void main(String[] args) throws Exception
    {
	int i=0;
	logger = new ClientLog();
	// 	ClientLog logger = new ClientLog();

	/*Get Configuration data*/  
	Config conf = new Config(args[0]);
	port = conf.getTcpPort();
	final String ImagesPath = conf.getVisualRepertory();
	address = conf.getIpAddress();
	aquaCon = new AquaConnection(address, port);
	/*cmd patterns*/
	pattern=new Pattern[10];  

	pattern[0]=Pattern.compile("OK");//,Pattern.CASE_INSENSITIVE);
	pattern[1]= Pattern.compile("hello");
	pattern[2]= Pattern.compile("^greeting \\w+");
	pattern[3]= Pattern.compile("addFish \\w+");
	pattern[4]= Pattern.compile("delFish \\w+");
	pattern[5]= Pattern.compile("startFish \\w+");
	pattern[6]= Pattern.compile("^log out");
	pattern[7]= Pattern.compile("^bye");
	pattern[8]= Pattern.compile("^getFishes");
	pattern[9]= Pattern.compile("^getFishesContinuously");
	/*Prompt*/
	System.out.print(">>>>>>>Enter your command please <<<<<<<\n");
	String response ="",cmd="";
	while(cmd==""){  
	    cmd=promptIn();
	    /*Handling response*/
	    response ="greeting N1";//test
	    if((pattern[2].matcher(response).matches())&&(pattern[1].matcher(cmd).find()))/*Hello & greeting*/{
		if (connected==false){
		    /*Display the aquarium*/
		    SwingUtilities.invokeLater(new Runnable(){    
			    @Override
			    public void run() {
				new Aquarium().displayGUI(ImagesPath);
			    }
			});
		    connected=true;
		}
	    }
	    else if(pattern[8].matcher(cmd).matches()){/*getFishes*/
		System.out.println("calling setFish methode (to update the arrayList Fishes)");
	    }
	    else if(pattern[9].matcher(cmd).matches()){/*getFishesContinuously*/
		System.out.println("listening continuously+ promptout() with each response");
	    }
	    else{response ="OK"; //test
		if(pattern[0].matcher(response).matches()){/*OK*/
		    if(pattern[3].matcher(cmd).matches())  /*addFish*/ 
			System.out.println("calling addFish methode");
		    if(pattern[4].matcher(cmd).matches()) /*delFish*/
			System.out.println("calling delFish methode"); ;
		    if(pattern[5].matcher(cmd).matches()) /*startFish*/
			System.out.println("calling startFish methode"); ;
		}	

		response ="bye"; //test
		if((pattern[6].matcher(cmd).matches())&&(pattern[7].matcher(response).find())){/*log out & bye*/
		    if(frame.isDisplayable())
			frame.setVisible(false);
		    connected=false;

		}
		
	    }
	    if(i==0) i++;
	    else
		response=aquaCon.receive();
	    promptOut(response);
	    System.out.println(String.format("\nYour command is %s ; Your response is %s",cmd, response));
	    /*  TO be Used in treating response
	    // compilation de la regex
	    Pattern p = Pattern.compile(":");
	    // séparation en sous-chaînes
	    String[] items = p.split("un:deux:trois");
	    */
		
	    //		System.out.println(pattern[0].matcher(cmd).find());
	    /*   try{
		
		 }
		 catch(IOException e){
		 System.out.println("Sorry.. IOError");	
		 }	*/


	    cmd="";
	}
    }
    
    
}
