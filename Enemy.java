import java.awt.*;
class Enemy extends Gameplay
{
    
    double x;
    double y;
    double velocityX;
    double velocityY;
    int diameter;  
    Image image;
    
    public Enemy(int targetX, int targetY)
    {        
        diameter = (int)(Math.random()*40+20);
        int startPosition = (int)(Math.random()*4+1);
        x=0;y=0;
        switch (startPosition)
        {
            case 1: //enter screen from top
                    x = (int)(Math.random()*PANEL_WIDTH);
                    y = 0;
                    break;
            case 2: //enter screen from right
                    x = PANEL_WIDTH;
                    y = (int)(Math.random()*PANEL_HEIGHT);
                    break;
            case 3: //enter screen from bottom
                    x = (int)(Math.random()*PANEL_WIDTH);
                    y = PANEL_HEIGHT;
                    break;
            case 4: //enter screen from left
                    x = 0;
                    y = (int)(Math.random()*PANEL_HEIGHT);
                    break;
        }
        
        
        image = enemyImage.getScaledInstance(diameter,diameter,0);
        
        double theta = Math.atan2(targetY-y, targetX-x);
        velocityX = (Math.cos(theta));
        velocityY = (Math.sin(theta));
    }
    
    public void update()
    {
        x += velocityX;
        y += velocityY;        
    }
    
    public void draw(Graphics g)
    {        
        g.drawImage(image, (int)Math.floor(x)-diameter/2, (int)Math.floor(y)-diameter/2, this);
    }
}