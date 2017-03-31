package aqua;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;

public class Aquarium {

    private AquaPanel contentPane;
    private AquaConnection aquaCon;
    static int port;
    static InetAddress address;
    private void displayGUI(String ImagesPath)
    {
	
        JFrame frame = new JFrame("Aquarium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane = new AquaPanel(ImagesPath);        
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        //frame.setSize(1200,600);
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
	    /*TODO recieve respnse from server*/
	cmd=cmd.intern();

	return cmd;
    }
	static private void promptOut(String response)throws IOException
    {	
	System.out.print("<"+response); //+server's response

    }

    public static void main(String[] args) throws Exception
    {

	/*Get Configuration data*/  
	Config conf = new Config(args[0]);
	port = conf.getTcpPort();
	final String ImagesPath = conf.getVisualRepertory();
	address = conf.getIpAddress();

	/*Prompt*/
	System.out.print(">>>>>>>Enter your command please <<<<<<<\n");
	String response ="NO RESPONSE YET\n\n",cmd="";
	while(cmd==""){  
	    cmd=promptIn();
	    	if (cmd=="hello")//&&response =greeting 
	    /*Display the aquarium*/
	    SwingUtilities.invokeLater(new Runnable(){    
		    @Override
		    public void run() {
			new Aquarium().displayGUI(ImagesPath);
			
		    }
		});
	    
		promptOut(response);
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
