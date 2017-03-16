import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws Exception {
	
	Config conf = new Config(args[0]);
	int port = conf.getTcpPort();
	InetAddress address = conf.getIpAddress();
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
    }
}
