import java.awt.*;
class Player
{
    static int PANEL_WIDTH;
    static int PANEL_HEIGHT;
    static Image spaceshipImage;
    static Gameplay imageObserver;
    static int diameter = 75;
    
    int x;
    int y;
    int velocityX;
    int velocityY; 
    
    public Player()
    {        
        x = PANEL_WIDTH/2-diameter;
        y = PANEL_HEIGHT/2-diameter;     
        velocityX = 0;
        velocityY = 0;
    }
    
    public void update()
    {
        if (x + velocityX < PANEL_WIDTH - 60 && x + velocityX > 10) x += velocityX;
        if (y + velocityY < PANEL_HEIGHT - 70 && y + velocityY > 10) y += velocityY;
    }
    
    public void draw(Graphics g)
    {
        g.drawImage(spaceshipImage, x-diameter/2, y-diameter/2, imageObserver);
    }
}
