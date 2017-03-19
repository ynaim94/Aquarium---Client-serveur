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

class AquaPanel extends JPanel implements ActionListener{

    private int x=0, y=0;   
    private BufferedImage[]images = new BufferedImage[8];

    Timer timer = new Timer(60, (ActionListener) this);
    
    public AquaPanel(String name) {

        timer.start();
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        setBackground(Color.WHITE);
   
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

