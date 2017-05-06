package aqua;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class AquaConnection {
    Scanner scanner;
    private static String cmd,response;
    static private Socket socket;
    private ArrayList<Fish> fishes;
    boolean flag;
    
    /** @brief  getFishes
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     *
     * @return     ArrayList<Fish> fishes
     *
     * @details    Getter of fishes attribute
     *
     */ 
    ArrayList<Fish> getFishes(){
	return fishes;
    }
    public AquaConnection(){
	flag = false;
	cmd="";
	response ="NO RESPONSE YET\n\n";
	/* create a scanner so we can read the command-line input*/
	scanner = new Scanner(System.in);	
    }

    /** @brief  send
     * @param[in]  String cmd
     *
     * @param[out] NONE
     *
     *
     * @return    NONE
     *
     * @details    Sends a string in the socket
     *
     */

    public void send(String cmd) throws IOException{
	PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	out.println(cmd);
    }

    /** @brief  receive
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     *
     * @return    String
     *
     * @details    Reads a line from the socket
     *
     */

    public String receive() throws IOException{
	BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	return is.readLine();
    }

    
    /** @brief  openConnection
     * @param[in]  InetAddress address
     * @param[in]  int port
     *
     * @param[out] Socket socket
     *
     *
     * @return     NONE
     *
     * @details    Opens the connection of the socket
     *
     */

    public void openConnection(InetAddress address, int port) throws IOException{
	socket=new Socket(address,port);
    }


    /** @brief  closeConnection
     * @param[in]  NONE
     *
     * @param[out] Socket socket
     *
     *
     * @return     NONE
     *
     * @details    Closes the connection of the socket
     *
     */

    public void closeConnection()throws IOException{
	socket.close();
    }


    
    /** @brief  setResponse
     * @param[in]  String response
     *
     * @param[out] String response
     *
     *
     * @return     NONE
     *
     * @details    Puts a string to the response field of AquaConnection
     *
     */

    public void setResponse(String response){
	this.response = response;
    }

    /** @brief  getResponse
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     *
     * @return     String
     *
     * @details    Getter of response field
     *
     */

    public String getResponse(){
	return this.response;
    }

    /** @brief  setCmd
     * @param[in]  String cmd
     *
     * @param[out] String cmd
     *
     *
     * @return     NONE
     *
     * @details    Puts a string to the cmd field of AquaConnection
     *
     */

    public void setCmd(String cmd){
	this.cmd = cmd;
    }


    /** @brief  getCmd
     * @param[in]  NONE
     *
     * @param[out] NONE
     *
     *
     * @return     String
     *
     * @details    Getter of cmd field
     *
     */

    public String getCmd(){
	return this.cmd;
    }

    
    


}
