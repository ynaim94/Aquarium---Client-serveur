import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.net.InetAddress;
import java.lang.Integer;

    
public class Config {

    private  InetAddress ipAdress;
    private  String id;
    private  int tcpPort;
    private  int displayTimeoutValue;
    private  String visualRepertory;

    public Config (String configFile){
	setProperties(configFile);
    }
    
    private void setProperties(String configFile){

	Properties prop = new Properties();
	InputStream input = null;

	try {

	    input = new FileInputStream(configFile);

	    // load a properties file
	    prop.load(input);

	    // get the property value
	    this.ipAdress = InetAddress.getByName(prop.getProperty("controller-address"));
	    this.id = prop.getProperty("id");
	    this.tcpPort = Integer.parseInt(prop.getProperty("controller-port"));
	    this.displayTimeoutValue = Integer.parseInt(prop.getProperty("display-timeout-value"));
	    this.visualRepertory = prop.getProperty("resources");

	} catch (IOException ex) {
	    ex.printStackTrace();
	} finally {
	    if (input != null) {
		try {
		    input.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}

    }

    public InetAddress getIpAdress (){
	return ipAdress;
    }

    public String getId(){
	return id;
    }

    public int getTcpPort(){
	return tcpPort;
    }

    public int getDtv(){
	return displayTimeoutValue;
    }

    public String getVisualRepertory(){
	return visualRepertory;
    }
    
}
