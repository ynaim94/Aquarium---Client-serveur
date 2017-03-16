
public class TestConfig {
    public static void  main(String[] args){

	Config conf =  new Config ("./affichage.cfg");
	System.out.println(conf.getIpAddress());
	System.out.println(conf.getId());
	System.out.println(conf.getTcpPort());
	System.out.println(conf.getDtv());
	System.out.println(conf.getVisualRepertory());

	
    }

}
