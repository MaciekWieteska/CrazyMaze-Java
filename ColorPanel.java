
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

public class ColorPanel extends JPanel {
    private double scale = 1.0;
    private double translateX = 0;
    private double translateY = 0;
    private Point lastMousePosition;

    public ColorPanel() {
        setPreferredSize(new Dimension(5000, 5000)); 
        addMouseWheelListener(new ScaleHandler());
        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseHandler());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        
        AffineTransform transform = new AffineTransform();
        transform.scale(scale, scale);
        transform.translate(translateX / scale, translateY / scale);
        g2d.setTransform(transform);

        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private class ScaleHandler extends MouseAdapter {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getPreciseWheelRotation() < 0) {
                scale *= 1.1;
            } else {
                scale *= 0.9;
            }
            revalidate();
            repaint();
        }
    }

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                lastMousePosition = e.getPoint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                Point currentMousePosition = e.getPoint();
                translateX += (currentMousePosition.getX() - lastMousePosition.getX());
                translateY += (currentMousePosition.getY() - lastMousePosition.getY());
                lastMousePosition = currentMousePosition;
                revalidate();
                repaint();
            }
        }
    }
}


