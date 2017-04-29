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
    private static int height,width,scaleX,scaleY;
    private BufferedImage[]images = new BufferedImage[8];
    private String[]fishImages=new String[8];
    ArrayList<Fish> Fishes=new ArrayList<Fish>();;
    Timer timer = new Timer(100, (ActionListener) this);

    void setFishes(ArrayList<Fish> newFishes){
	/* initialisation ? */
	/*/!\ traiter tous les cas avec les positions additionnelles */
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


    public AquaPanel(String name) {
        timer.start();
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
	/*an array with the names of the fish images*/
	for(int k=0;k<8;k++)
	    fishImages[k]= String.format("fish%d", k);
	
	for(int j=0;j<8;j++){
	    /*the full path to charge an image*/
	    String name1=name+String.format("/%s.png",fishImages[j]);
	    try{
		    images[j] = ImageIO.read(getClass().getResource(name1));		    
	    }
	    catch(IOException ioe) {
		System.out.println("Unable to fetch image.");
		ioe.printStackTrace();
	    }
	}	       
    }
    
    @Override
    public Dimension getPreferredSize(){
	width=images[0].getWidth();
	height= images[0].getHeight();
	return (new Dimension(width,height));
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
	    //System.out.println("fishImages[imageIndex]="+fishImages[Fishes.get(i).imageIndex]+";Fishes.get("+i+").name="+Fishes.get(i).name+";" );
	    /*Actual Position*/
	    x=Fishes.get(i).initPosition.get(0);
	    y=Fishes.get(i).initPosition.get(1);
	    /*if the fish have an other position to move to*/
	    if(Fishes.get(i).initPosition.size()>=4){
		/*destination*/
		z=Fishes.get(i).initPosition.get(2);
		w=Fishes.get(i).initPosition.get(3);
		/*Remove the privious position*/
		Fishes.get(i).initPosition.remove(0);
		Fishes.get(i).initPosition.remove(1);
	    }
	    // System.out.println("Size= "+Fishes.get(i).initPosition.size());
	    /*manage the proportional size*/
	    scaleX=width*Fishes.get(i).dimensions[0]/100;
	    scaleY=height*Fishes.get(i).dimensions[1]/100;
	    /*display the fish*/
	    g.drawImage(images[Fishes.get(i).imageIndex],x,y,scaleX,scaleY,this);
	    
	}
	
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
	/*x += 1;
	  y += 1;*/
	x=z;
	y=w;
	repaint();
    }
}

