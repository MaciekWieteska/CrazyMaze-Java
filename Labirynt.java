import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class Labirynt extends JFrame {
    int wiersze = 0;
    int kolumny = 0;
    File file;
    char zawartosc[][];
    int startW;
    int startK;
    int koniecW;
    int koniecK;
    char labiryntDoRysowania[][];
    char znakStart = 'X';
    char znakKoniec = 'X';

    public void doPamieci() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            this.zawartosc = new char[wiersze][kolumny];
            int znak;
            for (int i = 0; i < this.wiersze; i++) {
                for (int j = 0; j < this.kolumny; j++) {
                    znak = reader.read();
                    if (znak == '\n') {
                        znak = reader.read();
                    }
                    zawartosc[i][j] = (char) znak;
                    if (znak == 'P') {
                        this.startK = j;
                        this.startW = i;
                    }
                    if (znak == 'K') {
                        this.koniecK = j;
                        this.koniecW = i;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas czytania pliku: " + e.getMessage());
        }
    }

    public void liczWielkosc() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int znak;
            if (file.getName().toLowerCase().endsWith(".bin")) {
                Dekoder.binToText(file, this);
            } else {
                while ((znak = reader.read()) != -1) {
                    if (znak == '\n') {
                        wiersze++;
                    }
                    if (wiersze == 0) {
                        kolumny++;
                    }
                }
                if (wiersze > 0 && kolumny > 0) {
                    wiersze++; // Dodajemy ostatni wiersz, jeśli nie kończy się znakiem nowej linii
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas czytania pliku: " + e.getMessage());
        }
    }

    public void wyswietlLabirynt(JPanel panel, char[][] labirynt) {
        panel.removeAll();
        panel.setLayout(new GridLayout(this.wiersze, this.kolumny));
        JLabel[][] labels = new JLabel[this.wiersze][this.kolumny];

        for (int i = 0; i < this.wiersze; i++) {
            for (int j = 0; j < this.kolumny; j++) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                label.setBackground(getColor(labirynt[i][j]));
                labels[i][j] = label;
                panel.add(label);
            }
        }

        panel.setPreferredSize(new Dimension(this.kolumny * 10, this.wiersze * 10));
        panel.revalidate();
        panel.repaint();
    }

    private Color getColor(char c) {
        switch (c) {
            case 'X':
                return Color.BLACK;
            case 'P':
                return Color.GREEN;
            case 'K':
                return Color.RED;
            case 'W':
                return Color.BLUE;
            default:
                return Color.WHITE;
        }
    }

    public void kopiujLabirynt(char[][] zrodlo, char[][] cel) {
        if (zrodlo != null && cel != null) {
            for (int i = 0; i < zrodlo.length; i++) {
                System.arraycopy(zrodlo[i], 0, cel[i], 0, zrodlo[i].length);
            }
        }
    }

    public void BFS() {
        char[][] labirynt = new char[wiersze][kolumny];
        kopiujLabirynt(this.zawartosc, labirynt);
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { startW, startK });

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0];
            int y = pos[1];

            if (x == koniecW && y == koniecK) {
                break;
            }

            explore(queue, labirynt, x + 1, y, 'U');
            explore(queue, labirynt, x - 1, y, 'D');
            explore(queue, labirynt, x, y + 1, 'L');
            explore(queue, labirynt, x, y - 1, 'R');
        }

        sciezka(labirynt);
    }

    private void explore(Queue<int[]> queue, char[][] labirynt, int x, int y, char direction) {
        if (x >= 0 && x < wiersze && y >= 0 && y < kolumny) {
            if (labirynt[x][y] == ' ' || labirynt[x][y] == 'K') {
                queue.add(new int[] { x, y });
                labirynt[x][y] = direction;
            }
        }
    }

    public void sciezka(char[][] labirynt) {
        int x = koniecW;
        int y = koniecK;
        this.labiryntDoRysowania = new char[wiersze][kolumny];
        kopiujLabirynt(this.zawartosc, this.labiryntDoRysowania);

        while (labirynt[x][y] != 'P') {
            this.labiryntDoRysowania[x][y] = 'W';
            switch (labirynt[x][y]) {
                case 'U':
                    x--;
                    break;
                case 'D':
                    x++;
                    break;
                case 'L':
                    y--;
                    break;
                case 'R':
                    y++;
                    break;
            }
        }

        // Ensure the starting and ending points are properly marked
        this.labiryntDoRysowania[startW][startK] = 'P';
        this.labiryntDoRysowania[koniecW][koniecK] = 'K';
    }

    public void ustawStart(int wiersze, int kolumny) {
        this.zawartosc[this.startW][this.startK] = this.znakStart;
        this.startW = wiersze;
        this.startK = kolumny;
        this.znakStart = this.zawartosc[wiersze][kolumny];
        this.zawartosc[wiersze][kolumny] = 'P';
    }

    public void ustawKoniec(int wiersze, int kolumny) {
        this.zawartosc[this.koniecW][this.koniecK] = this.znakKoniec;
        this.koniecW = wiersze;
        this.koniecK = kolumny;
        this.znakKoniec = this.zawartosc[wiersze][kolumny];
        this.zawartosc[wiersze][kolumny] = 'K';
    }
}
