package aqua;
import java.util.regex.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;
import java.util.ArrayList;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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
    
    /** @brief  startFish
     * @param[in]  String name
     *
     * @param[out] NONE
     *
     *
     * @return     NONE
     *
     * @details    Calls setStartFish in the AquaPanel
     *
     */
    
    private static void startFish(String name){
	contentPane.setStartFish(name);
    }

    /** @brief  status
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     *
     * @return     NONE
     *
     * @details    Prints the status of the view in the prompt
     *
     */
    
    private static void status(){
	String rep = contentPane.fishesToString(contentPane.Fishes);
        System.out.println(rep);
        logger.info("Client received " + rep.substring(1));
    }

    /** @brief  addFish
     * @param[in]  String cmd
     *
     * @param[out] NONE
     *
     *
     * @return     NONE
     *
     * @details    Stores the fish in a string array and calls setAddFish in AquaPanel
     *
     */
    
    private static void addFish(String cmd){
	String[] items =  parser.parseFishPosition(cmd);
	contentPane.setAddFish(items);
	
    }

    /** @brief  delFish
     * @param[in]  String cmd
     *
     * @param[out] NONE
     *
     *
     * @return     NONE
     *
     * @details    Stores the fish in a string array and calls setDelFish in AquaPanel
     *
     */

    private static void delFish(String cmd){
        String[] items =  parser.parseFishPosition(cmd);
        if (contentPane == null) 
	    System.out.println("Uninitialized!");
        contentPane.setDelFish(items); 
    }

    /** @brief  getFishes
     * @param[in]  String response
     *
     * @param[out] NONE
     *
     *
     * @return     NONE
     *
     * @details    Stores the list of fish in an array of string arrays and calls setGetFishes in AquaPanel
     *
     */

    public static void getFishes(String response){
	String[][] items = parser.parseListFishPosition(response);
	contentPane.setGetFishes(items);
    }

    /*private static void getFishesContinuously(){
	GFCThread p = new GFCThread(aquaCon);
	p.start();
	}*/

    
    /***Fin Ajout view 4****/

    /** @brief  displayGUI
     * @param[in]  String ImagesPath
     *
     * @param[out] JFrame frame
     * @param[out] AquaPanel contentPane
     *
     * @return     NONE
     *
     * @details    Initializes the frame for the aquarium display
     *
     */

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

    /** @brief  promptIn
     * @param[in]  NONE
     *
     * @param[out] NONE
     * 
     *
     * @return     String cmd
     *
     * @details    Scans the prompt entry and sends the string it in the socket.
     *
     */
    
    static private String promptIn()throws IOException{
	System.out.print(">"); 
	/*get the input as a String*/
	String cmd = scanner.nextLine();
	aquaCon.setCmd(cmd);
	logger.info("Client sent " + cmd);
	cmd=cmd.intern();
	if (cmd.equals("status"))
		return cmd;
	try {
	    aquaCon.send(cmd);
	}
	catch(IOException e) {
	    System.out.println("IOEXEption \n");
	}
	return cmd;
    }

    /** @brief  promptOut
     * @param[in]  String response
     *
     * @param[out] NONE
     * 
     *
     * @return     NONE
     *
     * @details    Prints the server response on the prompt
     *
     */
    
    static private void promptOut(String response)throws IOException
    {	
	System.out.print("<"+response+"\n"); //+server's response
	logger.info("Client received " + response);
	
	
    }
    
    
    public static void main(String[] args) throws Exception
    {
	ScheduledExecutorService exec = null;
	ReceiveThread rcvThread = null;
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
	    pattern[2]= Pattern.compile("^greeting \\w+");
	    pattern[3]= Pattern.compile("^addFish ");
	    pattern[4]= Pattern.compile("^delFish ");
	    pattern[5]= Pattern.compile("^startFish \\w+");
	    pattern[6]= Pattern.compile("^log out");
	    pattern[7]= Pattern.compile("^bye");
	    pattern[8]= Pattern.compile("^getFishes\\s*");
	    pattern[9]= Pattern.compile("^getFishesContinuously\\s*");
	    pattern[10] = Pattern.compile("^status");
	    /*Prompt*/
	    //	System.out.print(">>>>>>>Enter your command please <<<<<<<\n");
	    String response ="",cmd="";
	
	    while(cmd==""){  
		if (connected==false){
		    aquaCon.openConnection(address,port);
		    rcvThread = new ReceiveThread(aquaCon);
		    rcvThread.start();
		    exec = Executors.newSingleThreadScheduledExecutor();
		    exec.scheduleAtFixedRate(new Runnable() {
			    @Override
			    public void run() {
				logger.info("ping "+ port);
				try{
				    aquaCon.send("ping " + port);
				}
				catch(IOException e) {
				    System.out.println("IOException \n");
				}
			    }
			}, 0, 5, TimeUnit.SECONDS);
		    
		    connected=true;
		}
		cmd=promptIn();
		//System.out.println(cmd + "a");
		
		if (!(cmd.equals("status"))){
			synchronized (aquaCon){
			    aquaCon.wait();
			}
			response=aquaCon.getResponse();
			promptOut(response);
		}

		else
			response=cmd;

		//	  System.out.println(String.format("\nYour command is %s ; Your response is %s",cmd, response));
		/*Handling response*/
		if(pattern[2].matcher(response).matches())/* greeting*/
		    {	 
			logger.info("Client logging ..");  
			/*Display the aquarium*/
			new Aquarium().displayGUI(ImagesPath);
		    }
		else if (pattern[10].matcher(cmd).matches()){/*status*/
		    status();
		}
		else if(pattern[8].matcher(cmd).matches()){/*getFishes*/
		    //		    System.out.println("calling setFish methode (to update the arrayList Fishes)");
		    getFishes(response);
		}
		else if(pattern[9].matcher(cmd).matches()){/*getFishesContinuously*/
		    //		    System.out.println("listening continuously+ promptout() with each response");
		    //getFishesContinuously();
		}
		else{ if(pattern[0].matcher(response).find()){
			// if(pattern[0].matcher(response).matches()){/*OK*/
			//	System.out.println("This is Ok :D"); 
			/*test: addFish sneakingFish at 61x52,9x10, RandomWayPoint*/
			if(pattern[3].matcher(cmd).find()){  /*addFish*/
			    addFish(cmd);
			} 
			if(pattern[4].matcher(cmd).find()) /*delFish*/
			    //System.out.println("calling delFish methode");
			    delFish(cmd);
			if(pattern[5].matcher(cmd).matches()) /*startFish*/{
			    startFish(cmd);
			}
		    }
		    if((pattern[6].matcher(cmd).find())&&
		       (pattern[7].matcher(response).find())){/*log out & bye*/
			if(frame.isDisplayable())
			    frame.setVisible(false);
			aquaCon.closeConnection();
			connected=false;	
			exec.shutdownNow();
		    }	
		}

		if(connected != false){
		    cmd="";
		}
	    }
	    System.exit(0);
	}catch(IOException e){
	    System.out.println("No Server!!!");}
    }
    

    
}

