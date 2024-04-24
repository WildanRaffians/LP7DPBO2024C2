import javax.swing.*;
import java.awt.*;

public class ScoreLabel extends JLabel {
    private int score;

    public ScoreLabel() {
        super("Score: 0", SwingConstants.CENTER); // Menampilkan teks awal "Score: 0" di tengah
        setFont(new Font("Arial", Font.PLAIN, 24)); // Mengatur jenis font dan ukuran
        setForeground(Color.WHITE); // Mengatur warna teks menjadi putih
        setPreferredSize(new Dimension(120, 50)); // Mengatur ukuran label
    }

    public void updateScore(int newScore) {
        this.score = newScore;
        setText("Score: " + score); // Mengupdate teks label dengan skor baru
    }
}
