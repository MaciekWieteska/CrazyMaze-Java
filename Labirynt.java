import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

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
        } catch (IOException e) {
            System.err.println("Błąd podczas czytania pliku: " + e.getMessage());
        }
    }

    public void wyswietlLabirynt(JPanel panel, char[][] labirynt) {
        panel.removeAll();
        panel.setLayout(new GridLayout(this.wiersze, this.kolumny));

        for (int i = 0; i < this.wiersze; i++) {
            for (int j = 0; j < this.kolumny; j++) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                switch (labirynt[i][j]) {
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
                    case 'W':
                        label.setBackground(Color.BLUE);
                        break;
                    default:
                        label.setBackground(Color.WHITE);
                        break;
                }
                panel.add(label);
            }
        }
        panel.setPreferredSize(new Dimension(this.kolumny * 10, this.wiersze * 10));
        panel.revalidate();
        panel.repaint();
    }

    public void kopiujLabirynt(char[][] zrodlo, char[][] cel) {
        if (zrodlo != null && cel != null) {
            int wiersze = zrodlo.length;
            int kolumny = zrodlo[0].length;
            for (int i = 0; i < wiersze; i++) {
                System.arraycopy(zrodlo[i], 0, cel[i], 0, kolumny);
            }
        }
    }

    public void BFS() { // To tu trzeba poprawić
        int i = 0;
        char[][] labirynt = new char[wiersze][kolumny];
        kopiujLabirynt(this.zawartosc, labirynt);
        ArrayList<Integer> kolumnyKol = new ArrayList<>();
        ArrayList<Integer> wierszeKol = new ArrayList<>();
        kolumnyKol.add(startK);
        wierszeKol.add(startW);
        while (true) {// obsługa dla startK i startW = 0 do dodania
            if (wierszeKol.get(i) < this.wiersze - 1) {
                if ((labirynt[wierszeKol.get(i) + 1][kolumnyKol.get(i)] == 'K') == true) {
                    break;
                }
                if ((labirynt[wierszeKol.get(i) + 1][kolumnyKol.get(i)] == ' ') == true) { // dół
                    wierszeKol.add(wierszeKol.get(i) + 1);
                    kolumnyKol.add(kolumnyKol.get(i));
                    labirynt[wierszeKol.get(i) + 1][kolumnyKol.get(i)] = 'D';
                }
            }

            if (kolumnyKol.get(i) < this.kolumny - 1) {
                if ((labirynt[wierszeKol.get(i)][kolumnyKol.get(i) + 1] == 'K') == true) {
                    break;
                }
                if ((labirynt[wierszeKol.get(i)][kolumnyKol.get(i) + 1] == ' ') == true) { // prawo
                    wierszeKol.add(wierszeKol.get(i));
                    kolumnyKol.add(kolumnyKol.get(i) + 1);
                    labirynt[wierszeKol.get(i)][kolumnyKol.get(i) + 1] = 'L';
                }
            }

            if (wierszeKol.get(i) > 0) {
                if ((labirynt[wierszeKol.get(i) - 1][kolumnyKol.get(i)] == 'K') == true) {
                    break;
                }
                if ((labirynt[wierszeKol.get(i) - 1][kolumnyKol.get(i)] == ' ') == true) { // góra
                    wierszeKol.add(wierszeKol.get(i) - 1);
                    kolumnyKol.add(kolumnyKol.get(i));
                    labirynt[wierszeKol.get(i) - 1][kolumnyKol.get(i)] = 'U';
                }
            }

            if (kolumnyKol.get(i) > 0) {
                if ((labirynt[wierszeKol.get(i)][kolumnyKol.get(i) - 1] == 'K') == true) {
                    break;
                }
                if ((labirynt[wierszeKol.get(i)][kolumnyKol.get(i) - 1] == ' ') == true) { // lewo
                    wierszeKol.add(wierszeKol.get(i));
                    kolumnyKol.add(kolumnyKol.get(i) - 1);
                    labirynt[wierszeKol.get(i)][kolumnyKol.get(i) - 1] = 'R';
                }
            }
            i++;
        }
        sciezka(labirynt);
    }

    public void sciezka(char[][] labirynt) { // idziemy od końca i znajdujemy ścieżke. Dodać lokalizacje końca
        char znak;
        int aktW = this.koniecW;
        int aktK = this.koniecK;
        this.labiryntDoRysowania = new char[wiersze][kolumny];
        kopiujLabirynt(this.zawartosc, this.labiryntDoRysowania);
        // badamy otoczenie wokol K
        while (true) {
            // dół
            if (aktW != this.wiersze - 1) {
                znak = labirynt[aktW + 1][aktK];
                if (znak == 'U' || znak == 'D' || znak == 'R' || znak == 'L') {
                    aktW++;
                    break;
                }
            }
            // góra
            if (aktW != 0) {
                znak = labirynt[aktW - 1][aktK];
                if (znak == 'U' || znak == 'D' || znak == 'R' || znak == 'L') {
                    aktW--;
                    break;
                }
            }
            // prawo
            if (aktK != this.kolumny - 1) {
                znak = labirynt[aktW][aktK + 1];
                if (znak == 'U' || znak == 'D' || znak == 'R' || znak == 'L') {
                    aktK++;
                    break;
                }
            }
            // lewo
            if (aktK != 0) {
                znak = labirynt[aktW][aktK - 1];
                if (znak == 'U' || znak == 'D' || znak == 'R' || znak == 'L') {
                    aktK--;
                    break;
                }
            }
        }
        while (true) {
            if ((znak == 'U') == true) {
                this.labiryntDoRysowania[aktW][aktK] = 'W';
                aktW++;
                znak = labirynt[aktW][aktK];
            } else if ((znak == 'D') == true) {
                this.labiryntDoRysowania[aktW][aktK] = 'W';
                aktW--;
                znak = labirynt[aktW][aktK];
            } else if ((znak == 'R') == true) {
                this.labiryntDoRysowania[aktW][aktK] = 'W';
                aktK++;
                znak = labirynt[aktW][aktK];
            } else if ((znak == 'L') == true) {
                this.labiryntDoRysowania[aktW][aktK] = 'W';
                aktK--;
                znak = labirynt[aktW][aktK];
            } else if ((znak == 'P') == true) {
                break;
            }
        }
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
