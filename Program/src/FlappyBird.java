import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    //Ukuran frame
    int frameWidth = 360;
    int frameHeight = 640;

    //Image assets
    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    //Player
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;
    int gravity = 1;

    //Pipe
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidht = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;
    Timer pipesCooldown;

    //Game
    Timer gameloop;
    boolean gameOver = false;
    JLabel gameOverLabel;
    double score = 0;
    ScoreLabel scoreLabel;
    JLabel restartLabel;
    public FlappyBird(){
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);

        //Load image
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        //add player / burung
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        //add pipa
        pipes = new ArrayList<Pipe>();

        //timer munculnya pipa
        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        pipesCooldown.start();
        gameloop = new Timer(1000/60, this);
        gameloop.start();

        //munnculkan score
        scoreLabel = new ScoreLabel();
        scoreLabel.setBounds(10, 10, 120, 50);
        add(scoreLabel);
    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);
        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }
    }

    public void move(){
        //grakan player/burung
        player.setVelocityY(player.getVelocityY()+gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        //gerakan pipa
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

            if(!pipe.passed && player.getPosX() > pipe.getPosX() + pipe.getWidth()){
                //jika posisi player melewati pipa
                pipe.passed = true;
                score += 0.5;       //score bertambah
                scoreLabel.updateScore((int) score);
            }
            if(collision(player, pipe)){
                gameOver = true;
            }
        }

        if (player.getPosY() > frameHeight){
            //jika player jatuh hingga keluar frame
            gameOver = true;    //gameover
        }

    }

    public void placePipes(){
        //menampilkan posisi pipa secara random
        int randomPosY = (int)(pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, playerWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY+openingSpace+pipeHeight), playerWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public boolean collision(Player bird, Pipe pipe){
        //posisi player mengenai/menabrak pipa
        return bird.getPosX() < pipe.getPosX() + pipe.getWidth() &&
                bird.getPosX() + bird.getWidth() > pipe.getPosX() &&
                bird.getPosY() < pipe.getPosY() + pipe.getHeight() &&
                bird.getPosY() + bird.getHeight() > pipe.getPosY();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            pipesCooldown.stop();
            gameloop.stop();

            //Update score akhir
            scoreLabel.updateScore((int) score);

            // Atur posisi label skor ke tengah layar
            int labelWidth = scoreLabel.getPreferredSize().width;
            int labelHeight = scoreLabel.getPreferredSize().height;
            int labelX = (frameWidth - labelWidth) / 2; // Posisi X tengah
            int labelY = (frameHeight - labelHeight) / 2; // Posisi Y tengah
            // Tampilkan skor terakhir saat permainan berakhir
            scoreLabel.setBounds(labelX, labelY, labelWidth, labelHeight);

            // Tampilkan label "Game Over" di atas skor
            gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
            gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24));
            gameOverLabel.setForeground(Color.RED);
            gameOverLabel.setBounds(0, labelY-labelHeight-5, frameWidth, 100); // Posisi di atas layar dengan lebar sesuai frameWidth
            add(gameOverLabel); // Menambahkan label "Game Over" ke panel

            // Tampilkan label restart
            restartLabel = new JLabel("Press R : [Restart]", SwingConstants.CENTER);
            restartLabel.setFont(new Font("Arial", Font.BOLD, 16));
            restartLabel.setForeground(Color.WHITE);
            restartLabel.setBounds(0, labelY+70, frameWidth, 50);
            add(restartLabel);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            //jika space ditekan maka buruh seakan akan melompat
            player.setVelocityY(-10);
        }

        if(e.getKeyCode() == KeyEvent.VK_R){
            //ketika r ditekan setelah gameover maka game akan di restart
            if(gameOver){
                player.setPosY(playerStartPosY);
                player.setVelocityY(0);
                pipes.clear();
                score = 0;
                scoreLabel.updateScore((int) score);
                scoreLabel.setBounds(0, 0, 120, 50);
                gameOverLabel.setVisible(false);
                restartLabel.setVisible(false);
                gameOver = false;
                gameloop.start();
                pipesCooldown.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
