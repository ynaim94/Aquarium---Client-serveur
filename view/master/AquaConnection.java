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
    //static InetAddress address;
    //static int port;

    static private Socket socket;

    private ArrayList<Fish> fishes;
    
    ArrayList<Fish> getFishes(){
	return fishes;
    }
    public AquaConnection(){
	cmd="";
	response ="NO RESPONSE YET\n\n";
	/* create a scanner so we can read the command-line input*/
	scanner = new Scanner(System.in);	
    }
    
    public void send(String cmd) throws IOException{
	PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	out.println(cmd);
    }
    
    public String receive() throws IOException{
	BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	return is.readLine();
    }
    public void openConnection(InetAddress address, int port) throws IOException{
	socket=new Socket(address,port);
}
    public void closeConnection()throws IOException{
	socket.close();
    }
    
    public void  run(){

	
	
    }       
    


}
