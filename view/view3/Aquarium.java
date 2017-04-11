package aqua;
import java.util.regex.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;

public class Aquarium {
    private static Pattern [] pattern;
    private static Matcher matcher;
    private AquaPanel contentPane;
    private AquaConnection aquaCon;
    static int port;
    static InetAddress address;
    static ClientLog logger;
    /*Boolean var to detect connection state*/
    static   private	boolean connected=false;
    private void displayGUI(String ImagesPath)
    {
	
        JFrame frame = new JFrame("Aquarium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane = new AquaPanel(ImagesPath);        
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        //frame.setSize(1200,600);
	if(connected==false)
	    frame.setVisible(false);
	else
	    frame.setVisible(true);
	// frame.setResizable(false);
    }

    static private String promptIn()throws IOException
    { 

       

	/* create a scanner so we can read the command-line input*/
	Scanner	scanner = new Scanner(System.in);
	
	/* prompt for the command*/
	    System.out.print(">"); 
	    /*get the input as a String*/
	    String cmd = scanner.next(); // We can also use scanner.nextInt() to return an int if needed
	    /*TODO send cmd to server*/
		logger.info("Client sent " + cmd);
	    /*TODO recieve respnse from server*/
	cmd=cmd.intern();

	return cmd;
    }
	static private void promptOut(String response)throws IOException
    {	
	System.out.print("<"+response); //+server's response
	logger.info("Client received " + response);


    }

    public static void main(String[] args) throws Exception
    { 
	logger = new ClientLog();
	// 	ClientLog logger = new ClientLog();

	/*Get Configuration data*/  
	Config conf = new Config(args[0]);
	port = conf.getTcpPort();
	final String ImagesPath = conf.getVisualRepertory();
	address = conf.getIpAddress();

	/*cmd patterns*/
	pattern=new Pattern[10];  

	pattern[0]=Pattern.compile("OK");//,Pattern.CASE_INSENSITIVE);
	pattern[1]= Pattern.compile("hello");
	pattern[2]= Pattern.compile("^greeting \\w+");
	pattern[3]= Pattern.compile("addFish");
	pattern[4]= Pattern.compile("delFish");
	pattern[5]= Pattern.compile("startFish");
	pattern[6]= Pattern.compile("^logout");
	pattern[7]= Pattern.compile("^bye");
	pattern[8]= Pattern.compile("^getFishes");
	/*Prompt*/
	System.out.print(">>>>>>>Enter your command please <<<<<<<\n");
	String response ="OK\n",cmd="";
	while(cmd==""){  
	    cmd=promptIn();
	    	if (cmd=="hello")//&&response =greeting 

	   
	    
		promptOut(response);
		/*Handling response*/
response ="greeting N1";
		if((pattern[2].matcher(response).matches())&&(pattern[1].matcher(cmd).find())){ 
		     connected=true;
		    /*Display the aquarium*/
		    SwingUtilities.invokeLater(new Runnable(){    
			    @Override
			    public void run() {
			
				    new Aquarium().displayGUI(ImagesPath);
				
			    }
			});
		}
		else
		    {
			if(pattern[0].matcher(response).matches()){
			    if(pattern[3].matcher(cmd).find())  /*calling a addFish methode*/ ;
			    if(pattern[4].matcher(cmd).find()) /*calling a delFish methode*/  ;
			    if(pattern[5].matcher(cmd).find()) /*calling a startFish methode*/ ;
			}
			
			else{
			    response ="bye";
			    if((pattern[6].matcher(cmd).matches())&&(pattern[7].matcher(response).matches())){
				connected =false;
				//	System.out.println(connected);

			    }
			}		
		    }
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
	    	System.out.println(String.format("your command is %s, your response is %s", cmd, response));
	
		cmd="";
	}
    }
    
    
}
