import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;

public class Labirynt extends JFrame {
    int wiersze = 0;
    int kolumny = 0;
    File file;
    char zawartosc[][];

    public void doPamieci() {
        try {
            FileReader reader = new FileReader(file);
            this.zawartosc = new char[wiersze][kolumny];
            int znak;
            for (int i = 0; i < this.wiersze; i++) {
                for (int j = 0; j < this.kolumny; j++) {
                    znak = reader.read();
                    if (znak == '\n') {
                        znak = reader.read();
                    }
                    zawartosc[i][j] = (char) znak;

                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Błąd podczas czytania pliku: " + e.getMessage());
        }
    }

    public void liczWielkosc() {
        try {
            int pomKol = 0;
            FileReader reader = new FileReader(file);
            int znak;
            while ((znak = reader.read()) != -1) {
                if (znak == '\n') {
                    pomKol++;
                    this.wiersze++;
                }
                if (pomKol == 0) {
                    this.kolumny++;
                }
            }
            reader.close();
            this.wiersze++;
        } catch (IOException e) {
            System.err.println("Błąd podczas czytania pliku: " + e.getMessage());
        }
    }

    public void wyswietlLabirynt() { // row wiersze columny
        setTitle("Maze");
        setSize(10 * this.kolumny, 10 * this.wiersze);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(this.wiersze, this.kolumny));

        for (int i = 0; i < this.wiersze; i++) {
            for (int j = 0; j < this.kolumny; j++) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                switch (this.zawartosc[i][j]) {
                    case 'X':
                        label.setBackground(Color.BLACK);
                        break;
                    case ' ':
                        label.setBackground(Color.WHITE);
                        break;
                    case 'P':
                        label.setBackground(Color.GREEN);
                        break;
                    case 'K':
                        label.setBackground(Color.RED);
                        break;
                    default:
                        label.setBackground(Color.WHITE);
                        break;
                }
                panel.add(label);
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);
        // add(panel);
        setVisible(true);
    }
}
