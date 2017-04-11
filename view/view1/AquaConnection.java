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
    
    public AquaConnection(InetAddress address, int port)
    {
	address=address;
	port=port;

	cmd="";
	response ="NO RESPONSE YET\n\n";
	/* create a scanner so we can read the command-line input*/
	scanner = new Scanner(System.in);
    }
    
    private void init()throws IOException{
   socket=new Socket(address,port);
 }


    public String receive()throws IOException{
	//	System.out.println("SOCKET = " + socket);
	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	String str = "bonjour";
	for (int i = 0; i < 10; i++) {
	    	    out.println(str);
	    str = in.readLine();
	}
	System.out.println("END");
	out.println("END");
	in.close();
	out.close();
	socket.close();
	return str;    
}
    public void  run(){
	
	
    }       
    


}
