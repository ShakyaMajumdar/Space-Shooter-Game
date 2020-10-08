import java.awt.*;
class Projectile extends Gameplay
{
    
    double x;
    double y;
    double velocityX;
    double velocityY;
    int diameter;    
    
    public Projectile(int startX, int startY, int targetX, int targetY)
    {      
        diameter = 20;
        x = startX;
        y = startY;
        double theta = Math.atan2(targetY-startY, targetX-startX);        
        super.projectileImage = super.projectileImage.getScaledInstance(diameter,diameter,Image.SCALE_FAST);
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
        g.drawImage(super.projectileImage, (int)Math.floor(x)-diameter/2, (int)Math.floor(y)-diameter/2, this);
    }
}