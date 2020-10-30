import java.awt.*;
import javax.swing.JFrame;
class Main
{

    /**
     * Create frame which takes up full screen and add in the game to it
     */
    public static void main(String[] args)
    {

        JFrame frame = new JFrame();        
        Gameplay gamePlay = new Gameplay();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();        
        int screenHeight = (int)screenSize.getHeight(); 
        int screenWidth = (int)screenSize.getWidth();         

        frame.setBounds(0, 0, screenWidth, screenHeight);
        frame.setTitle("Game");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePlay);
    }    
}
