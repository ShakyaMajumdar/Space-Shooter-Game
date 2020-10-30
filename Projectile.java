import java.awt.*;
class Projectile
{
    static int PANEL_WIDTH;
    static int PANEL_HEIGHT;
    static Image projectileImage;
    static Gameplay imageObserver;
    static int diameter = 20;    
    
    double x;
    double y;
    double velocityX;
    double velocityY;
    
    public Projectile(int startX, int startY, int targetX, int targetY)
    {      
        x = startX;
        y = startY;
        double theta = Math.atan2(targetY-startY, targetX-startX);        
        //projectileImage = projectileImage.getScaledInstance(diameter+5,diameter+5,Image.SCALE_FAST);
        velocityX = (4*Math.cos(theta));
        velocityY = (4*Math.sin(theta));
    }
    
    public void update()
    {
        x += velocityX;
        y += velocityY;
    }
    
    public void draw(Graphics g)
    {     
        g.drawImage(projectileImage, (int)Math.floor(x)-diameter/2, (int)Math.floor(y)-diameter/2, imageObserver);
    }
}
