
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Ramka extends JFrame implements ActionListener {
    JButton b1, b2, b3, b4, b5, b6, b7, b8, b9;
    JLabel t1;
    Labirynt labirynt = new Labirynt();

    public Ramka() {
        setSize(800, 800);
        setTitle("CRAZY MAZE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel background = new JLabel(new ImageIcon("C:\\Users\\czarn\\Desktop\\programowanie\\tlojava.jpg"));
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

        b4 = new JButton("Stworz JPG");
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

        
        ColorPanel colorPanel = new ColorPanel();
        JScrollPane scrollPane = new JScrollPane(colorPanel);
        scrollPane.setBounds((getWidth() - 700) / 2, getHeight() - 600, 700, 600); 
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        background.add(scrollPane);

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
                System.out.println(file);// pod file kryje sie sciezka do pliku ktora podamy do metody
                labirynt.file = file;
                labirynt.liczWielkosc();
                labirynt.doPamieci();
                labirynt.wyswietlLabirynt();
            }

         
        } else if (zrodlo == b3) {
            dispose();
        } else if (zrodlo == b4) {
            t1.setText("Tworze obrazek...");

        } else if (zrodlo == b5) {
            t1.setText("Wyszukuje najkrotsza sciezke...");
        } else if (zrodlo == b6) {
            t1.setText("Ustaw poczatek labiryntu!");
        } else if (zrodlo == b7) {
            t1.setText("Ustaw koniec labiryntu!");
        } else if (zrodlo == b8) {
            t1.setText("RESETUJE LABIRYNT...");
        } else if (zrodlo == b9) {
            t1.setText("Obecny komunikat:");
        }

    }
}
