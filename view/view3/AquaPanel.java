package aqua;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.*;


class AquaPanel extends JPanel implements ActionListener{

    private int x=0,y=0,z=0,w=0;
    private static int height,width,scaleX,scaleY,firstX,firstY,destX,destY;  
    private static double decX, decY, timeUnity;

    private BufferedImage[]images = new BufferedImage[8];
    /*an array with the names of the fish images*/
    private String[]fishImages={"background","sneakingFish","smilingFish","bubbleFish","oldFish","happyFish","madFish","lostFish"};
    ArrayList<Fish> Fishes=new ArrayList<Fish>();
    /*timer to repaint fishes in their new positions each 100 ms*/
    private Timer timer = new Timer(100, (ActionListener) this);
  

        
    void setFishes(ArrayList<Fish> newFishes){
	/* initialisation ? */
	/*/!\ traiter tous les cas avec les positions additionnelles */
	fishesToString(this.Fishes);
	for(Fish newItem : newFishes){
	    if (this.Fishes.contains(newItem)){
		Fish item=this.Fishes.get(this.Fishes.indexOf(newItem));
		item.initPosition.addAll(newItem.initPosition);
	    }
	    else {
		this.Fishes.add(newItem);
	    }
	}
    }
    
    public void setStartFish(String cmd){
	String tokens[] = cmd.split("[ ]+");
	for(Fish fish : Fishes){
	    if (fish.name.equals(tokens[1])){
		fish.start();
	    }
	}
    }

    
    public void setAddFish(String[] items){
	ArrayList<Integer> initPosition = new ArrayList<Integer>();
	String name = new String(items[1]);
	int dimensions[] = new int[2];
	int mobilityTime = 5; // par defaut
	int started;
	Fish tmpFish ;
	initPosition.add(Integer.parseInt(items[3]));
	initPosition.add(Integer.parseInt(items[4]));
	dimensions[0] = Integer.parseInt(items[5]);
	dimensions[1] = Integer.parseInt(items[6]);
	started = 0;
	tmpFish= new Fish(name, initPosition, mobilityTime, dimensions, started);
	ArrayList<Fish> newFishes = new ArrayList<Fish>();
	newFishes.add(tmpFish);
	setFishes(newFishes);
    }

    public void setGetFishes(String[][] items){
	ArrayList<Fish> newFishes = new ArrayList<Fish>();
	int added = 0;
	for (String[] s: items){
	    ArrayList<Integer> initPosition = new ArrayList<Integer>();
	    String name = new String(s[0]);
	    int dimensions[] = new int[2];
	    int mobilityTime; // par defaut
	    Fish tmpFish ;
	    int started = 1;
	    /*to keep the ancient positions*/
	    for(Fish poiss : Fishes){
		if (poiss.name.equals(name)){
		    initPosition.addAll(poiss.initPosition);
		    break;
		}	    
	    }
	    initPosition.add(Integer.parseInt(s[2]));
	    initPosition.add(Integer.parseInt(s[3]));
	    dimensions[0] = Integer.parseInt(s[4]);
	    dimensions[1] = Integer.parseInt(s[5]);
	    mobilityTime = Integer.parseInt(s[6]);
	    tmpFish= new Fish(name, initPosition, mobilityTime, dimensions,started);
	    newFishes.add(tmpFish);
	}

	for(Fish fish : Fishes){
	    if (!(newFishes.contains(fish))){
		if (fish.started == 0){
		    newFishes.add (fish);
		}
	    }
	}
	this.Fishes = newFishes;

    }




    public AquaPanel(String name) {
        timer.start();
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));	
	for(int j=0;j<8;j++){
	    /*the full path to charge an image*/
	    String fullPath=name+String.format("/%s.png",fishImages[j]);
	    try{
		images[j] = ImageIO.read(getClass().getResource(fullPath));		    
	    }
	    catch(IOException ioe) {
		System.out.println("Unable to fetch image.");
		ioe.printStackTrace();
	    }
	}	       
    }
    
    
    
    @Override
    public Dimension getPreferredSize(){
	width=images[0].getWidth()/100; //8
	height= images[0].getHeight()/100;//6
	return (new Dimension(width*100,height*100));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
	/*display the the background*/
        g.drawImage(images[0], 0, 0, this);
	/*display the fishes*/
	for(int i=0; i<Fishes.size();i++){
	    /* match the fish name with the suitable image */
	    for(int l=1;l<8;l++)
		if (fishImages[l].equals(Fishes.get(i).name)){ /*TODO: accept names with _ */
		    Fishes.get(i).imageIndex=l;
		    break;
		}
		else /*image par défaut*/
		    Fishes.get(i).imageIndex=2;
	    System.out.println("My name is "+Fishes.get(i).name);
	    /*Actual Position*/
	    firstX=width*Fishes.get(i).initPosition.get(0);
	    firstY=height*Fishes.get(i).initPosition.get(1);
	    timeUnity=Fishes.get(i).mobilityTime/100.0; //0.05
	    x=firstX;
	    y=firstY;
	    System.out.println("firstX= "+x+" ; firstY="+firstY);
	    /*if the fish have an other position to move to*/
	    if(Fishes.get(i).initPosition.size()>=4){
		/* TODO: set intermediate positions & manage mobilityTime*/
		/*destination*/
		destX=width*Fishes.get(i).initPosition.get(2);
		destY=height*Fishes.get(i).initPosition.get(3);
		System.out.println("destX="+destX+" ; destY="+destY);
		decX=(destX-firstX)*timeUnity;
		decY=(destY-firstY)*timeUnity;
		/*to avoid the non-movement when the step is <0*/
		if ((decX<1)&&(decX>-1))
		    if(decX<0) 
			decX=-1;
		    else
			decX=1;
		if ((decY<1)&&(decY>-1))
		    if(decY<0)
			decY=-1;
		    else
			decY=1;
		System.out.println("decX="+decX+" ; decY="+decY);
	
		if(Math.abs(z-destX)<=Math.abs(decX)){
		    z=destX;
		    System.out.println("Le zeeed est arrivé");
		}
		else{
	   
		    z=(int)(z+decX);
		    System.out.println(" Le Zeeed is  moving on->z="+z );
		}
		if(Math.abs(w-destY)<=Math.abs(decY)){
		    w=destY;
		    System.out.println("Le Waaay est arrivé");}
		else{
		    w=(int)(w+decY);
		    System.out.println("Le Waaay is  moving on->w="+w );
		}
	       
		if((Math.abs(z-destX)<=Math.abs(decX))&&(Math.abs(w-destY)<=Math.abs(decY))){
		    System.out.println("(|z-destX|<|decX|)&&(|w-destY|<|decY|) ->z="+z+" ; w="+w);
		    System.out.println(" z et w prenne sont  arrivés!!! :D ->z="+z+" ; w="+w);
		    /*Remove the privious position*/
		    Fishes.get(i).initPosition.remove(0);
		    Fishes.get(i).initPosition.remove(1);
		    System.out.println("firstPosition Removed");
		}
        
	    }
	    else{
		System.out.println("We are fixed here");
		z=x;
		w=y;
		System.out.println("We are fixed here z=x:"+z+" ;w=y:"+w);
	    }

	    // System.out.println("Size= "+Fishes.get(i).initPosition.size());
	    /*manage the proportional size*/
	    scaleX=width*Fishes.get(i).dimensions[0];
	    scaleY=height*Fishes.get(i).dimensions[1];
	    g.drawImage(images[Fishes.get(i).imageIndex],x,y,scaleX,scaleY,this);
	    
	}
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
	
	x =z;
	y =w;
 	repaint();
    }
    
    
    public String fishesToString(ArrayList<Fish> Fishes){
	String res = "\n";
	for (Fish fish: Fishes){
	    res = res + "Fish " + fish.name +" at " +
		fish.initPosition.get(0) + "x" + fish.initPosition.get(1)+ "," +
		fish.dimensions[0] +"x"+ fish.dimensions[1] +" :  "+ ((fish.started == 1) ? "started" : "not started") +  "\n" ;
	}
	return res;
    }
    
}

