import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    public static void main(String[] args) {
        // Buat JFrame untuk GUI Form
        JFrame formFrame = new JFrame("Flappy Bird Launcher");
        formFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        formFrame.setSize(360, 640);
        formFrame.setLocationRelativeTo(null);
        formFrame.setResizable(false);

        // Buat JPanel untuk menampung komponen-komponen di GUI Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(109,211,234)); // Atur warna background panel

        // Buat GridBagConstraints untuk mengatur posisi tombol
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding antara komponen

        // Buat tombol untuk membuka permainan
        JButton playButton = new JButton("Play Flappy Bird!");
        playButton.setBackground(new Color(255,242,0)); // Atur warna background tombol
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tutup GUI Form
                formFrame.dispose();

                // Buat dan tampilkan JFrame game FlappyBird
                JFrame gameFrame = new JFrame("Flappy Bird");
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setSize(360, 640);
                gameFrame.setLocationRelativeTo(null);
                gameFrame.setResizable(false);

                FlappyBird flappyBird = new FlappyBird();
                gameFrame.add(flappyBird);
                gameFrame.pack();
                flappyBird.requestFocus();
                gameFrame.setVisible(true);
            }
        });

        //Tombol untuk menutup launcher
        JButton closeButton = new JButton("Close Launcher");
        closeButton.setBackground(Color.lightGray);     //warna tombol
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Tutup form
                formFrame.dispose();
            }
        });

        // Tambahkan tombol ke JPanel dengan GridBagConstraints
        formPanel.add(playButton, gbc);
        formPanel.add(closeButton, gbc);

        // Tambahkan JPanel ke JFrame
        formFrame.add(formPanel);

        // Tampilkan GUI Form
        formFrame.setVisible(true);
    }
}
