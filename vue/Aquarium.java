package aqua;

import javax.swing.*;

public class Aquarium {

    private AquaPanel contentPane;

    private void displayGUI() {

        JFrame frame = new JFrame("Aquarium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = new AquaPanel();        

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
       // frame.setSize(1200,600);
        frame.setVisible(true);
       // frame.setResizable(false);
            
    }
 
    public static void main(String... args) {
       
        SwingUtilities.invokeLater(new Runnable() {
            
           @Override
            public void run() {
            
                new Aquarium().displayGUI();

            }
        });
    }


}
