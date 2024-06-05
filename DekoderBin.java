import java.io.*;

public class DekoderBin {
    private static final char WALL_CHAR = 'X';
    private static final char PATH_CHAR = ' ';
    private static final char ENTRY_CHAR = 'P';
    private static final char EXIT_CHAR = 'K';

    private BinHeader header;

    public void binToText(String inpath, Labirynt labirynt) throws IOException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(inpath))) {
            header = BinHeader.readFrom(in);
            toLabirynt(in, labirynt, header);
        }
    }

    private void toLabirynt(DataInputStream in, Labirynt labirynt, BinHeader header) throws IOException {
        int wiersze = header.getLines();
        int kolumny = header.getColumns();
        char[][] zawartosc = new char[wiersze][kolumny];

        for (int i = 0; i < wiersze; i++) {
            for (int j = 0; j < kolumny; j++) {
                char znak;
                int sep = in.read();
                int value = in.read();

                if (value == header.getWall()) {
                    znak = WALL_CHAR;
                } else if (value == header.getPath()) {
                    znak = PATH_CHAR;
                } else {
                    znak = '?';
                }

                int count = in.read() + 1;

                for (int ile = 0; ile < count; ile++) {
                    int currentColumn = j + ile;
                    if (currentColumn < kolumny) {
                        if (i == header.getEntryY() - 1 && currentColumn == header.getEntryX() - 1) {
                            zawartosc[i][currentColumn] = ENTRY_CHAR;
                        } else if (i == header.getExitY() - 1 && currentColumn == header.getExitX() - 1) {
                            zawartosc[i][currentColumn] = EXIT_CHAR;
                        } else {
                            zawartosc[i][currentColumn] = znak;
                        }
                    } else {
                        throw new IOException("Exceeded column limit");
                    }
                }
                j += count - 1; // Move the column index to account for the run-length encoding
            }
        }

        labirynt.setWiersze(wiersze);
        labirynt.setKolumny(kolumny);
        labirynt.setZawartosc(zawartosc);
        labirynt.setStart(header.getEntryY() - 1, header.getEntryX() - 1);
        labirynt.setKoniec(header.getExitY() - 1, header.getExitX() - 1);
    }

    private static class BinHeader {
        private int fileId;
        private int escape;
        private short columns;
        private short lines;
        private short entryX;
        private short entryY;
        private short exitX;
        private short exitY;
        private int reserved1;
        private int reserved2;
        private int reserved3;
        private int counter;
        private int solutionOffset;
        private int separator;
        private int wall;
        private int path;

        public static BinHeader readFrom(DataInputStream in) throws IOException {
            BinHeader header = new BinHeader();
            header.fileId = Integer.reverseBytes(in.readInt());
            header.escape = in.read();
            header.columns = Short.reverseBytes(in.readShort());
            header.lines = Short.reverseBytes(in.readShort());
            header.entryX = Short.reverseBytes(in.readShort());
            header.entryY = Short.reverseBytes(in.readShort());
            header.exitX = Short.reverseBytes(in.readShort());
            header.exitY = Short.reverseBytes(in.readShort());
            header.reserved1 = Integer.reverseBytes(in.readInt());
            header.reserved2 = Integer.reverseBytes(in.readInt());
            header.reserved3 = Integer.reverseBytes(in.readInt());
            header.counter = Integer.reverseBytes(in.readInt());
            header.solutionOffset = Integer.reverseBytes(in.readInt());
            header.separator = in.read();
            header.wall = in.read();
            header.path = in.read();
            return header;
        }

        public int getColumns() {
            return columns;
        }

        public int getLines() {
            return lines;
        }

        public short getEntryX() {
            return entryX;
        }

        public short getEntryY() {
            return entryY;
        }

        public short getExitX() {
            return exitX;
        }

        public short getExitY() {
            return exitY;
        }

        public int getWall() {
            return wall;
        }

        public int getPath() {
            return path;
        }
    }
}
