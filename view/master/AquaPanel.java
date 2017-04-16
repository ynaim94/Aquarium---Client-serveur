package aqua;


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
//import java.util.*;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.lang.Object;

/* problème d'importation de cette biblio */
//import org.apache.commons.lang.ArrayUtils.*;


class AquaPanel extends JPanel implements ActionListener{

    private int x=0, y=0;   
    private BufferedImage[]images = new BufferedImage[8];
    ArrayList<Fish> fishes;
/*liste des noms de nos poissons tels que fishes.size=names.size && fishes.indexOf(PoissonClown)==names.indexOf(PoissonClown.name) (Solution valide si les noms de poissons sont uniques) */  
     ArrayList<String> names;

    void setFishes(ArrayList<Fish> newFishes){
	/* initialisation ? */
	/*/!\ traiter tous les cas avec les positions additionnelles */
	/* On fait le test sur le nom de Fish*/
	  for(Fish newItem : newFishes){
		if((names.contains(newItem.name))&&(!fishes.contains(newItem)))
		   fishes.set(names.indexOf(newItem.name), newItem); 
 /*  for(Fish newItem : newFishes){
	if (this.Fishes.contains(newItem)){ 
	Fish item=this.Fishes.get(this.Fishes.indexOf(newItem));	
	item.initPosition=addAll(Fishes.indexOf(item), newItem.initPosition);
	}*/

	    else {/*Le poison avec ce nom n'existait pas dans notre vue */

		this.fishes.add(newItem); /*On ajoute le Fish dans la liste des fishes*/
		names.add(newItem.name);  /*On ajoute son nom à la liste des noms au même index*/
	    }

	}
    }
    Timer timer = new Timer(60, (ActionListener) this);
    public AquaPanel(String name) {
	
        timer.start();
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        setBackground(Color.WHITE);
	fishes=new ArrayList<Fish>();
 /*TODO: Test de correspondance des noms aus noms de nos images.png et choix d'une image.png par défaut en cas échéant  
for(String fishName : names){
String name1=name+fishName+".png";
*/
       for(int i=0; i<8;i++){
            String name1=name+String.format("/fish%d.png", i);
        try {
	  //images[names.indexOf(fishName)] = ImageIO.read(getClass().getResource(name1));
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

