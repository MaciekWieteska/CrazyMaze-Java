import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Labirynt {
    int wiersze = 0;
    int kolumny = 0;
    File file;
    char zawartosc[][];

    public void doPamieci(File file) {
        try {
            this.zawartosc = new char[wiersze][kolumny];
            int znak;
            int aktKol = 0;
            FileReader reader = new FileReader(file);
            for (int i = 0; i < this.kolumny; i++) {
                for (int j = 0; j < this.wiersze; j++) {
                    znak = reader.read();
                    if (znak == '\n') {
                        znak = reader.read();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas czytania pliku: " + e.getMessage());
        }
        System.out.println("Wszystko git");
    }

    public void liczWielkosc(File file) {
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
            this.wiersze--;
            this.kolumny--;
        } catch (IOException e) {
            System.err.println("Błąd podczas czytania pliku: " + e.getMessage());
        }
    }
}
