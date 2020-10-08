import java.awt.*;
import javax.swing.JFrame;
class Main
{
    
    /*
     * Create frame which takes up full screen and add in the game to it
     */
    public static void main(String[] args)
    {
        
        JFrame frame = new JFrame();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();        
        int screenHeight = (int)screenSize.getHeight(); 
        int screenWidth = (int)screenSize.getWidth();         
        
        
        Gameplay gamePlay = new Gameplay();
        frame.setBounds(0, 0, screenWidth, screenHeight);
        frame.setTitle("Game");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(gamePlay);
    }    
    
    /*
     * Close the window
     */
    public void end()
    {
        System.exit(0);
    }
}