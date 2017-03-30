package aqua;

import java.util.Scanner;

public class PromptThread extends Thread {
    Scanner scanner;
    /*cmd : store the command written in the prompt*/
    /*response : to store the response  */
    String cmd,response;
    
    public PromptThread()
    {
	
	cmd="";
	response ="NO RESPONSE YET\n\n";
	/* create a scanner so we can read the command-line input*/
	scanner = new Scanner(System.in);
    }
    
    public void  run(){
	
	System.out.print(">>>>>>>Enter your command please <<<<<<<\n");
	while(cmd==""){  
	    /* prompt for the command*/
	    System.out.print(">"); 
	    /*get the input as a String*/
	    cmd = scanner.next(); // We can also use scanner.nextInt() to return an int if needed
	    /*TODO send cmd to server*/
	    /*TODO recieve respnse from server*/
	cmd=cmd.intern();
	System.out.print("<"+response); //+server's response
	System.out.println(String.format("your command is %s, your response is %s", cmd, response));
	cmd="";
	
	}       
	
    }
    
}
