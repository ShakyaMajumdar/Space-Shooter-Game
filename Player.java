import java.awt.*;
class Player extends Gameplay
{
    int x;
    int y;
    int velocityX;
    int velocityY;
    public int diameter;  
    
    public Player()
    {        
        diameter = 50;
        x = super.PANEL_WIDTH/2-diameter;
        y = super.PANEL_HEIGHT/2-diameter;     
        super.spaceshipImage = super.spaceshipImage.getScaledInstance(diameter+10,diameter+10,Image.SCALE_DEFAULT);
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
        g.drawImage(super.spaceshipImage, x-diameter/2, y-diameter/2, this);
    }
}