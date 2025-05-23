import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphPanel extends JPanel {

    private List<String> fullPath = new ArrayList<>();
    private List<String> animatedPath = new ArrayList<>();
    private int animationIndex = 0;
    private javax.swing.Timer animationTimer;

    private Graph graph;//cities stored

    public GraphPanel(Graph graph) {
        this.graph = graph;
    }

    public void setAnimatedPath(List<String> path) {
        this.fullPath = path;
        this.animatedPath.clear();
        this.repaint();// trigger to redraw 
    }

    public void animatePath(List<String> path) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        fullPath = path;
        animatedPath = new ArrayList<>();
        animationIndex = 0;

        animationTimer = new javax.swing.Timer(300, e -> {
            if (animationIndex < fullPath.size()) {
                animatedPath.add(fullPath.get(animationIndex));
                animationIndex++;
                repaint();
            } else {
                ((javax.swing.Timer) e.getSource()).stop();
            }
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw all edges
        g2.setColor(Color.LIGHT_GRAY);
        for (String from : graph.getGraph().keySet()) {
            for (Edge edge : graph.getGraph().get(from)) {
                Point p1 = graph.getCityCoordinates().get(from);
                Point p2 = graph.getCityCoordinates().get(edge.destination);
                if (p1 != null && p2 != null) {
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        // Highlight blocked cities in red
        for (String blockedCity : graph.getBlockedCities()) {
            Point p = graph.getCityCoordinates().get(blockedCity);
            if (p != null) {
                g2.setColor(Color.RED);
                g2.fillOval(p.x - 10, p.y - 10, 20, 20);
      
                g2.setColor(Color.BLACK);
                g2.drawString(blockedCity, p.x - 15, p.y - 15);
            }
        }

        // Highlight animated path
        g2.setColor(Color.GREEN);
        for (int i = 0; i < animatedPath.size() - 1; i++) {
            Point p1 = graph.getCityCoordinates().get(animatedPath.get(i));
            Point p2 = graph.getCityCoordinates().get(animatedPath.get(i + 1));
            if (p1 != null && p2 != null) {
                g2.setStroke(new BasicStroke(4));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Draw city nodes
        g2.setColor(Color.BLACK);
        for (String city : graph.getCityCoordinates().keySet()) {
            Point p = graph.getCityCoordinates().get(city);
            if (p != null) {
                g2.fillOval(p.x - 5, p.y - 5, 10, 10);
                g2.drawString(city, p.x + 5, p.y);
            }
        }
    }
}
