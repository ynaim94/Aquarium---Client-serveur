package aqua;

import java.io.*;
import java.net.*;
import javax.swing.*;

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
           
	Config conf = new Config(args[0]);
	int port = conf.getTcpPort();
	String name = conf.getVisualRepertory();
        System.out.println("name:"+name);
	InetAddress address = conf.getIpAddress();
	SwingUtilities.invokeLater(new Runnable() {
            
           @Override
            public void run() {
            
                new Aquarium().displayGUI(name);

            }
        });
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
