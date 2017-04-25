package aqua;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class AquaConnection extends Thread{
    Scanner scanner;
    /*cmd : store the command written in the prompt*/
    /*response : to store the response  */
    String cmd,response;
    static InetAddress address;
    static int port;

    // try { 
    static private Socket socket;
    // } catch(IOException e){	System.out.println("Sorry..Error while creating Socket");}

    private ArrayList<Fish> Fishes;
    

    ArrayList<Fish> getFishes(){
	return Fishes;
    }
    
    public AquaConnection(InetAddress address, int port) throws IOException
    {
	address=address;
	port=port;

	cmd="";
	response ="NO RESPONSE YET\n\n";
	/* create a scanner so we can read the command-line input*/
	scanner = new Scanner(System.in);
	socket=new Socket(address,port);
    }

    public void send(String cmd) throws IOException{
	// DataOutputStream os = new DataOutputStream(socket.getOutputStream());

	// os.writeBytes(cmd + "\n");
	PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	out.println(cmd);
    }

    public String receive() throws IOException{
	BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	return is.readLine();
    }
    
    public void  run(){
	
	
    }       
    


}
