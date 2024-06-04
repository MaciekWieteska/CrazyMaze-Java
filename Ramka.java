import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Ramka extends JFrame implements ActionListener {
    boolean wczytLab = false;
    boolean settingStart = false;
    boolean settingEnd = false;
    JButton b1, b2, b3, b4, b5, b6, b7, b8, b9;
    JLabel t1;
    Labirynt labirynt = new Labirynt();
    JPanel colorPanel;
    JScrollPane scrollPane;

    public Ramka() {
        setSize(800, 800);
        setTitle("CRAZY MAZE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        ClassLoader classLoader = getClass().getClassLoader();
        java.net.URL imageUrl = classLoader.getResource("tlo1.jpg");
        JLabel background = new JLabel(new ImageIcon(imageUrl));
        background.setBounds(0, 0, 800, 800);
        add(background);
        background.setLayout(null);

        b1 = new JButton("Wczyt. labiryntu");
        b1.setToolTipText("Wczytanie labiryntu");
        b1.setBackground(Color.GREEN);
        b1.setBounds(20, 20, 125, 40);
        background.add(b1);
        b1.addActionListener(this);

        b3 = new JButton("EXIT");
        b3.setToolTipText("Konczy dzialanie programu");
        b3.setBackground(Color.RED);
        b3.setBounds(700, 20, 80, 20);
        background.add(b3);
        b3.addActionListener(this);

        t1 = new JLabel("Obecny komunikat:");
        t1.setBounds(20, 100, 250, 100);
        t1.setForeground(Color.YELLOW);
        t1.setFont(new Font("SansSerif", Font.BOLD, 14));
        background.add(t1);

        b4 = new JButton("Stworz PNG");
        b4.setToolTipText("Tworzy labirynt w postaci graficznej");
        b4.setBackground(Color.GREEN);
        b4.setBounds(150, 20, 125, 40);
        background.add(b4);
        b4.addActionListener(this);

        b5 = new JButton("Najkr. sciezka");
        b5.setToolTipText("Wyswietla najkrotsza sciezke w labiryncie");
        b5.setBackground(Color.GREEN);
        b5.setBounds(150, 80, 125, 40);
        background.add(b5);
        b5.addActionListener(this);

        b6 = new JButton("Ustaw pocz.");
        b6.setToolTipText("Umozliwia ustawienie poczatku labiryntu");
        b6.setBackground(Color.GREEN);
        b6.setBounds(280, 20, 125, 40);
        background.add(b6);
        b6.addActionListener(this);

        b7 = new JButton("Ustaw koniec");
        b7.setToolTipText("Umozliwia ustawienie konca labiryntu");
        b7.setBackground(Color.GREEN);
        b7.setBounds(280, 80, 125, 40);
        background.add(b7);
        b7.addActionListener(this);

        b8 = new JButton("Restart lab.");
        b8.setToolTipText("Resetuje labirynt");
        b8.setBackground(Color.GREEN);
        b8.setBounds(410, 20, 125, 40);
        background.add(b8);
        b8.addActionListener(this);

        b9 = new JButton("CLEAR");
        b9.setToolTipText("Czysci obecny komunikat");
        b9.setBackground(Color.GREEN);
        b9.setBounds(20, 80, 125, 40);
        background.add(b9);
        b9.addActionListener(this);

        colorPanel = new JPanel();
        scrollPane = new JScrollPane(colorPanel);
        scrollPane.setBounds((getWidth() - 700) / 2, getHeight() - 600, 650, 500);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        background.add(scrollPane);

        // Add mouse wheel listener for zooming
        colorPanel.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                Dimension size = colorPanel.getPreferredSize();
                int newWidth = size.width - (notches * 10);
                int newHeight = size.height - (notches * 10);
                if (newWidth > 100 && newHeight > 100) { // Prevents shrinking too much
                    colorPanel.setPreferredSize(new Dimension(newWidth, newHeight));
                    colorPanel.revalidate();
                }
            }
        });

        // Add mouse listener for setting start and end points
        colorPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (settingStart || settingEnd) {
                    Point point = e.getPoint();
                    Dimension panelSize = colorPanel.getSize(); // Current size of the panel
                    Dimension labSize = new Dimension(labirynt.kolumny, labirynt.wiersze); // Size of the labyrinth in
                                                                                           // terms of cells

                    double scaleX = (double) panelSize.width / labSize.width;
                    double scaleY = (double) panelSize.height / labSize.height;

                    // Use Math.floor to ensure the correct cell is selected even if the click is
                    // near the border
                    int col = (int) Math.floor(point.x / scaleX);
                    int row = (int) Math.floor(point.y / scaleY);

                    if (row >= 0 && row < labirynt.wiersze && col >= 0 && col < labirynt.kolumny) {
                        if (settingStart) {
                            labirynt.ustawStart(row, col);
                            t1.setText("Nowy początek: (" + row + ", " + col + ")");
                            settingStart = false;
                        } else if (settingEnd) {
                            labirynt.ustawKoniec(row, col);
                            t1.setText("Nowy koniec: (" + row + ", " + col + ")");
                            settingEnd = false;
                        }
                        labirynt.wyswietlLabirynt(colorPanel, labirynt.zawartosc);
                    }
                }
            }
        });

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object zrodlo = e.getSource();
        if (zrodlo == b1) {
            t1.setText("Wczytuje labirynt...");
            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                if (this.wczytLab == true) {
                    resetLabirynt();
                }
                System.out.println(file);// pod file kryje sie sciezka do pliku ktora podamy do metody
                labirynt.file = file;
                labirynt.liczWielkosc();
                labirynt.doPamieci();
                labirynt.wyswietlLabirynt(colorPanel, labirynt.zawartosc);
                this.wczytLab = true;
            }
        } else if (zrodlo == b3) {
            dispose();
        } else if (zrodlo == b4) {
            t1.setText("Tworze obrazek...");
            makeScreenshot(colorPanel);
        } else if (zrodlo == b5) {
            t1.setText("Wyszukuje najkrotsza sciezke...");
            if (this.wczytLab == true) {
                labirynt.BFS();
                nie_wyswietlaj();
                labirynt.wyswietlLabirynt(colorPanel, labirynt.labiryntDoRysowania);
            }
        } else if (zrodlo == b6) {
            t1.setText("Ustaw poczatek labiryntu!");
            if (this.wczytLab == true) {
                settingStart = true;
                settingEnd = false;
            }
        } else if (zrodlo == b7) {
            t1.setText("Ustaw koniec labiryntu!");
            if (this.wczytLab == true) {
                settingStart = false;
                settingEnd = true;
            }
        } else if (zrodlo == b8) {
            t1.setText("RESETUJE LABIRYNT...");
            resetLabirynt();
            this.wczytLab = false;
        } else if (zrodlo == b9) {
            t1.setText("Obecny komunikat:");
        }
    }

    public static final void makeScreenshot(JPanel colorPanel2) {
        Rectangle rec = colorPanel2.getBounds();
        BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
        colorPanel2.paint(bufferedImage.getGraphics());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Wybierz lokalizację do zapisu");
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                ImageIO.write(bufferedImage, "png", fileToSave);
                JOptionPane.showMessageDialog(null, "Zrzut ekranu zapisany pomyślnie!", "Sukces",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Błąd podczas zapisywania pliku!", "Błąd",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetLabirynt() {
        labirynt = new Labirynt();
        colorPanel.removeAll();
        colorPanel.setBackground(Color.WHITE);
        colorPanel.revalidate();
        colorPanel.repaint();
    }

    private void nie_wyswietlaj() {
        colorPanel.removeAll();
        colorPanel.setBackground(Color.WHITE);
        colorPanel.revalidate();
        colorPanel.repaint();
    }

}