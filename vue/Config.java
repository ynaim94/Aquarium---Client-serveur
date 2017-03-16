import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.net.InetAddress;
import java.lang.Integer;

    
public class Config {

    private  InetAddress ipAddress;
    private  String id;
    private  int tcpPort;
    private  int displayTimeoutValue;
    private  String visualRepertory;

    public Config (String configFile){
	setProperties(configFile);
    }

    /** @brief	setProperties
     * @param[in]  configFile 	String
     *
     * @param[out] ipAddress 	InetAddress
     * @param[out] id 	String
     * @param[out] tcpPort 	int
     * @param[out] displayTimeoutValue 	int
     * @param[out] visualRepertory 	String
     *
     *
     * @return     NONE
     *
     * @details    Extract properties from a configuration file
     *
     */
    private void setProperties(String configFile){

	Properties prop = new Properties();
	InputStream input = null;

	try {

	    input = new FileInputStream(configFile);

	    // load a properties file
	    prop.load(input);

	    // get the property value
	    this.ipAddress = InetAddress.getByName(prop.getProperty("controller-address"));
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

    /** @brief	getIpAddress
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     * @return     InetAddress		ipAddress
     *
     * @details    Getter of the IpAdress
     *
     */
    public InetAddress getIpAddress (){
	return ipAddress;
    }

    /** @brief	getId
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     * @return     String		id
     *
     * @details    Getter of the id of the display program
     *
     */
    public String getId(){
	return id;
    }


    /** @brief	getTcpPort()
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     * @return     int		tcpPort
     *
     * @details    Getter of the listening TCP port of the controller node
     *
     */
    public int getTcpPort(){
	return tcpPort;
    }

    /** @brief	getDtv
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     * @return     int		displayTimeoutValue
     *
     * @details    Getter of the default value for the transmission of ping
     *
     */
    public int getDtv(){
	return displayTimeoutValue;
    }

    /** @brief	getVisualRepertory
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     * @return     String		visualRepertory
     *
     * @details    Getter of the repertory of fishes's visual
     *
     */
    public String getVisualRepertory(){
	return visualRepertory;
    }
    
}
