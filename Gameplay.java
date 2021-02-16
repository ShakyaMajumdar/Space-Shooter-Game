import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Gameplay extends JPanel implements MouseListener, ActionListener, KeyListener {
    private int score;

    private final Timer timer;

    public final int PANEL_WIDTH;
    public final int PANEL_HEIGHT;

    static Player player;

    static List<Projectile> projectiles = new ArrayList<>();
    static List<Enemy> enemies = new ArrayList<>();

    long timeLastEnemyCreated;

    boolean hasPainted = false;
    boolean willShowInstructions;
    boolean gameOver;
    boolean mouseEnabled;
    boolean keyEnabled;

    Image backgroundImage;
    Image spaceshipSprite;
    Image projectileSprite;
    Image enemySprite;
    Image explosionGif;

    int framesSinceEnd = 0;

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    Gameplay() {
        score = 0;
        gameOver = false;
        willShowInstructions = true;

        timer = new Timer(1, this);
        timer.start();

        timeLastEnemyCreated = System.currentTimeMillis();

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        Dimension screenSize = toolkit.getScreenSize();
        PANEL_WIDTH = (int) screenSize.getWidth();
        PANEL_HEIGHT = (int) screenSize.getHeight();

        backgroundImage = toolkit.getImage("files/background.JPG");
        backgroundImage = backgroundImage.getScaledInstance(PANEL_WIDTH, PANEL_HEIGHT, Image.SCALE_DEFAULT);
        spaceshipSprite = toolkit.getImage("files/spaceship.png");
        projectileSprite = toolkit.getImage("files/projectile.png");
        enemySprite = toolkit.getImage("files/comet.png");
        explosionGif = toolkit.getImage("files/explosion.gif");
        explosionGif = explosionGif.getScaledInstance(150, 150, Image.SCALE_DEFAULT);

        Enemy.PANEL_WIDTH = PANEL_WIDTH;
        Enemy.PANEL_HEIGHT = PANEL_HEIGHT;
        Enemy.enemyImage = enemySprite;
        Enemy.imageObserver = this;

        Player.PANEL_WIDTH = PANEL_WIDTH;
        Player.PANEL_HEIGHT = PANEL_HEIGHT;
        Player.spaceshipImage = spaceshipSprite.getScaledInstance(Player.diameter + 10, Player.diameter + 10, Image.SCALE_DEFAULT);
        Player.imageObserver = this;
        player = new Player();

        Projectile.PANEL_WIDTH = PANEL_WIDTH;
        Projectile.PANEL_HEIGHT = PANEL_HEIGHT;
        Projectile.projectileImage = projectileSprite.getScaledInstance(Projectile.diameter + 5, Projectile.diameter + 5, Image.SCALE_DEFAULT);
        Projectile.imageObserver = this;

        addKeyListener(this);
        keyEnabled = true;

        addMouseListener(this);
        mouseEnabled = true;

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    private void playSound(String name) {
        String filename = "files/" + name;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename).getAbsoluteFile());
            Clip explosionSound = AudioSystem.getClip();
            explosionSound.open(audioInputStream);
            explosionSound.start();
        } catch (Exception ex) {
            System.out.println("Error");
        }
    }

    /**
     * Calculate distance between the points (x1, y1) and (x2, y2)
     */
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }


    /**
     * Updates before each frame
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (gameOver) return;

        if (hasPainted) //ensures it is updated only once per frame
        {

            player.update();

            for (int i = 0; i < projectiles.size(); i++) {

                projectiles.get(i).update();

                if (projectiles.get(i).x < 0 ||
                        projectiles.get(i).x > PANEL_WIDTH ||
                        projectiles.get(i).y < 0 ||
                        projectiles.get(i).y > PANEL_HEIGHT)   //remove projectile if it leaves screen
                    projectiles.remove(i);
                    i--;    // To compensate for decrease in indices by List.remove
            }

            i:
            for (int i = 0; i < enemies.size(); i++) {

                if (enemies.get(i).x < 0 ||
                        enemies.get(i).x > PANEL_WIDTH ||
                        enemies.get(i).y < 0 ||
                        enemies.get(i).y > PANEL_HEIGHT)   //remove enemy if it leaves screen
                {
                    enemies.remove(i);
                    i--;    // To compensate for decrease in indices by List.remove
                    continue;
                }

                for (int j = 0; j < projectiles.size(); j++) //check for collisions with each projectile
                {
                    if (distance(projectiles.get(j).x, projectiles.get(j).y, enemies.get(i).x, enemies.get(i).y)
                            > Projectile.diameter / 2.0 + enemies.get(i).diameter / 2.0)
                        continue;

                    playSound("icebreak.wav");

                    if (enemies.get(i).diameter < 30)         //shrink size if enemy is large
                    {
                        score += 100;
                        enemies.remove(i);
                        i--;
                    } else                                //destroy if enemy is large
                    {
                        score += 50;
                        enemies.get(i).diameter -= 15;
                        enemies.get(i).image = enemySprite.getScaledInstance(enemies.get(i).diameter, enemies.get(i).diameter, Image.SCALE_DEFAULT);
                    }

                    projectiles.remove(j);

                    continue i; //move on to next enemy without any further checks
                }

                if (distance(player.x, player.y, enemies.get(i).x, enemies.get(i).y)
                        < Player.diameter / 2.0 + enemies.get(i).diameter / 2.0)    //set game over if any enemy collides with player
                {
                    enemies.remove(i);
                    gameOver = true;
                    break;
                }

                enemies.get(i).update();

            }

            if (System.currentTimeMillis() - timeLastEnemyCreated > 1500 && enemies.size() < 10) //create new enemy every 1.5 seconds
            {
                enemies.add(new Enemy(player.x, player.y));
                timeLastEnemyCreated = System.currentTimeMillis();
            }

            repaint();
            hasPainted = false;
        }
    }


    /**
     * Change cursor to crosshair
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    /**
     * Create new projectile towards mouse's location
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!mouseEnabled) return;
        int mouseX = e.getX();
        int mouseY = e.getY();
        playSound("laser.wav");
        projectiles.add(new Projectile(player.x, player.y, mouseX, mouseY));
    }

    /**
     * Give player a velocity in a certain direction if WASD / Arrow Keys are pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (!keyEnabled) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) //restart game
            {
                score = 0;
                gameOver = false;
                framesSinceEnd = 0;
                hasPainted = false;
                mouseEnabled = true;
                keyEnabled = true;
                projectiles = new ArrayList<>();
                enemies = new ArrayList<>();
                player = new Player();
                timeLastEnemyCreated = System.currentTimeMillis();
            }
            return;
        }


        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)     //move right
            player.velocityX = 5;
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)      //move left
            player.velocityX = -5;
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)      //move down
            player.velocityY = 5;
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)        //move up
            player.velocityY = -5;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //reset velocities when keys are released
        player.velocityX = 0;
        player.velocityY = 0;
    }

    public void paint(Graphics g) {
        hasPainted = true;

        g.drawImage(backgroundImage, 0, 0, this);

        if (framesSinceEnd > 25)  //25 frames are for explosion gif and sound to complete
        {
            // UI for restart

            g.setColor(Color.lightGray);
            Rectangle container = new Rectangle(PANEL_WIDTH / 2 - 250, PANEL_HEIGHT / 2 - 150, 500, 300);
            g.fillRect(container.x, container.y, container.width, container.height);

            g.setColor(Color.black);
            Font font1 = new Font("TimesRoman", Font.PLAIN, 30);
            Font font2 = new Font("TimesRoman", Font.PLAIN, 64);
            FontMetrics metrics1 = g.getFontMetrics(font1);
            FontMetrics metrics2 = g.getFontMetrics(font2);

            String text1 = "Game Over!";
            int x1 = container.x + (container.width - metrics1.stringWidth(text1)) / 2;
            int y1 = container.y + 50;
            g.setFont(font1);
            g.drawString(text1, x1, y1);

            String text2 = Integer.toString(score);
            int x2 = container.x + (container.width - metrics2.stringWidth(text2)) / 2;
            int y2 = container.y + container.height / 2;
            g.setFont(font2);
            g.drawString(text2, x2, y2);

            String text3 = "Points";
            int x3 = container.x + (container.width - metrics1.stringWidth(text3)) / 2;
            int y3 = y2 + metrics1.getHeight();
            g.setFont(font1);
            g.drawString(text3, x3, y3);

            String text4 = "Press Enter to Play Again";
            int x4 = container.x + (container.width - metrics1.stringWidth(text4)) / 2;
            int y4 = container.y + container.height - 20;
            g.setFont(font1);
            g.drawString(text4, x4, y4);

            return;
        }


        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
        g.drawString("Score: " + score, 10, 30);

        for (Projectile projectile : projectiles) projectile.draw(g);
        for (Enemy enemy : enemies) enemy.draw(g);

        if (!gameOver) player.draw(g);
        else {
            mouseEnabled = false;
            keyEnabled = false;

            g.drawImage(backgroundImage, 0, 0, this);         //clear all projectiles and enemies
            g.drawImage(explosionGif, player.x - 75, player.y - 75, this);
            playSound("explosion.wav");
            framesSinceEnd++;

        }
        g.dispose();
    }
}
