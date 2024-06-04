import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class LabirynthDecoder {
    private int wiersze = 0;
    private int kolumny = 0;
    private char zawartosc[][];

    public void liczWielkosc(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            int znak;
            int pomKol = 0;
            while ((znak = inputStream.read()) != -1) {
                if (znak == '\n') {
                    this.wiersze++;
                }
                if (pomKol == 0 && znak != '\n') {
                    this.kolumny++;
                }
                if (znak == '\n') {
                    pomKol = 1;
                }
            }
        }
    }

    public void doPamieci(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
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
        }
    }

    public int getWiersze() {
        return wiersze;
    }

    public int getKolumny() {
        return kolumny;
    }

    public char[][] getZawartosc() {
        return zawartosc;
    }
}
