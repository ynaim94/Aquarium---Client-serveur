package aqua;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;
public class Aquarium {

    private AquaPanel contentPane;

    private void displayGUI(String name) {

        JFrame frame = new JFrame("Aquarium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = new AquaPanel(name);        

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
       // frame.setSize(1200,600);
        frame.setVisible(true);
       // frame.setResizable(false);
            
    }
 
    public static void main(String[] args) throws Exception{
         /*Get Configuration data*/  
	Config conf = new Config(args[0]);
	int port = conf.getTcpPort();
	String name = conf.getVisualRepertory();
	InetAddress address = conf.getIpAddress();

	/*Display the aquarium*/
	SwingUtilities.invokeLater(new Runnable() {
            
           @Override
            public void run() {
            
                new Aquarium().displayGUI(name);

            }
        });


	/*Socket*/
	Socket socket = new Socket(address, port);
	System.out.println("SOCKET = " + socket);
	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	String str = "bonjour";
	for (int i = 0; i < 10; i++) {
	    out.println("bonjour");
	    str = in.readLine();
	}
	System.out.println("END");
	out.println("END");
	in.close();
	out.close();
	socket.close();


	/* create a scanner so we can read the command-line input*/
	Scanner scanner = new Scanner(System.in);
	
	/*cmd : store the command written in the prompt*/
	/*response : to store the response recieved from the server later on to be written in the prompt */
	String cmd="",response ="NO RESPONSE YET\n\n";

	System.out.print(">>>>>>>Enter your command please <<<<<<<\n");
	//while(true){	  
	/* prompt for the command*/
	System.out.print(">"); 

	/*get the input as a String*/
	    cmd = scanner.next(); // We can also use scanner.nextInt() to return an int if needed
	/*TODO send cmd to server*/
	/*TODO recieve respnse from server*/
	System.out.print("<"+response); //+server's response
	System.out.println(String.format("your command is %s, your response is %s", cmd, response));	  
	 //}
	/*TODO The Display of the Aquarium will be moved just after the acceptance of "Hello"*/ 

}
}

