package aqua;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class Aquarium {

    private AquaPanel contentPane;
    static private PromptThread pt;

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

    static private void createSocket(InetAddress address, int port)throws IOException
    {
	Socket socket = new Socket(address, port);
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
    }

    public static void main(String[] args) throws Exception
    {
	/*Get Configuration data*/  
	Config conf = new Config(args[0]);
	final int port = conf.getTcpPort();
	final String ImagesPath = conf.getVisualRepertory();
	final InetAddress address = conf.getIpAddress();
	pt =new PromptThread();
	//	if (cmd=="hello")//&&response =greeting 
	/*Display the aquarium*/
		SwingUtilities.invokeLater(new Runnable(){    
			@Override
			public void run() {
			    new Aquarium().displayGUI(ImagesPath);
			 
			}
		    });
  
	pt.start();
   try{
				createSocket(address, port);
			    }
			    catch(IOException e){
				System.out.println("Sorry.. No connection found");	
			    }
			    	
		
		//    }
    }
}
    
    
