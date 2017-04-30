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

    private int x=0, y=0;   
    private BufferedImage[]images = new BufferedImage[8];
    ArrayList<Fish> Fishes;
  
    
    
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
    
    public void setAddFish(String[] items){
	ArrayList<Integer> initPosition = new ArrayList<Integer>();
	String imageName,name = new String(items[1]);
	int dimensions[] = new int[2];
	int mobilityTime = 5; // par defaut
	Fish tmpFish ;
	    
	initPosition.add(Integer.parseInt(items[3]));
	initPosition.add(Integer.parseInt(items[4]));
	dimensions[0] = Integer.parseInt(items[5]);
	dimensions[1] = Integer.parseInt(items[6]);
	imageName = name ; // TODO
	tmpFish= new Fish(name, initPosition, mobilityTime, dimensions, imageName);
	ArrayList<Fish> newFishes = new ArrayList<Fish>();
	newFishes.add(tmpFish);
	setFishes(newFishes);
    }

    private void setGetFishes(String[][] items){
	ArrayList<Fish> newFishes = new ArrayList<Fish>();
	for (String[] s: items){
	    for(Fish fish :Fishes){
		if (s[0].equals(fish.name)){
		    ArrayList<Integer> initPosition = new ArrayList<Integer>();
		    String imageName,name = new String(s[0]);
		    int dimensions[] = new int[2];
		    int mobilityTime; // par defaut
		    Fish tmpFish ;
			    	    
		    initPosition.add(Integer.parseInt(s[2]));
		    initPosition.add(Integer.parseInt(s[3]));
		    dimensions[0] = Integer.parseInt(s[4]);
		    dimensions[1] = Integer.parseInt(s[5]);
		    imageName = name ; // TODO
		    mobilityTime = Integer.parseInt(s[6]);
		    tmpFish= new Fish(name, initPosition, mobilityTime, dimensions, imageName);
		    newFishes.add(tmpFish);
		}
	    }
	}
	setFishes(newFishes);
    }



    Timer timer = new Timer(60, (ActionListener) this);
    public AquaPanel(String name) {
	
        timer.start();
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        setBackground(Color.WHITE);
	Fishes=new ArrayList<Fish>();
        for(int i=0; i<8;i++){
            String name1=name+String.format("/fish%d.png", i);
	    try {

		images[i] = ImageIO.read(getClass().getResource(name1));

	    } catch(IOException ioe) {

		System.out.println("Unable to fetch image.");
		ioe.printStackTrace();

	    }
	} 
    }

  
    
    @Override
    public Dimension getPreferredSize() {
	return (new Dimension(images[0].getWidth(), images[0].getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(images[0], 0, 0, this);
       
        for(int i=1;i<8;i++){   
	    x=(int)(Math.random()*700);
	    y=(int)(Math.random()*500);
	    g.drawImage(images[i], x ,y, this);
       
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	x += 10;
	y += 10;
	repaint();
    }
}

