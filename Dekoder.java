import java.io.*;

public class Dekoder {
    private static final char WALL_CHAR = 'X';
    private static final char PATH_CHAR = ' ';
    private static final char ENTRY_CHAR = 'P';
    private static final char EXIT_CHAR = 'K';

    public static void binToText(File file, Labirynt labirynt) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            readHeader(in, labirynt);
            toText(in, labirynt);
        } catch (IOException e) {
            System.err.println("Błąd podczas czytania pliku: " + e.getMessage());
        }
    }

    private static void readHeader(DataInputStream in, Labirynt labirynt) throws IOException {
        in.readInt(); // Skip fileId
        in.read(); // Skip escape
        labirynt.kolumny = Short.reverseBytes(in.readShort());
        labirynt.wiersze = Short.reverseBytes(in.readShort());
        labirynt.startK = Short.reverseBytes(in.readShort());
        labirynt.startW = Short.reverseBytes(in.readShort());
        labirynt.koniecK = Short.reverseBytes(in.readShort());
        labirynt.koniecW = Short.reverseBytes(in.readShort());
        in.readInt(); // Skip reserved1
        in.readInt(); // Skip reserved2
        in.readInt(); // Skip reserved3
        in.readInt(); // Skip counter
        in.readInt(); // Skip solutionOffset
        in.read(); // Skip separator
    }

    private static void toText(DataInputStream in, Labirynt labirynt) throws IOException {
        labirynt.zawartosc = new char[labirynt.wiersze][labirynt.kolumny];
        for (int i = 0; i < labirynt.wiersze; i++) {
            for (int j = 0; j < labirynt.kolumny; j++) {
                in.read(); // Skip sep
                int value = in.read();
                labirynt.zawartosc[i][j] = value == 1 ? WALL_CHAR : PATH_CHAR;
                int count = in.read() + 1;
                in.skipBytes(count - 1); // Skip remaining characters
            }
        }
    }
}
