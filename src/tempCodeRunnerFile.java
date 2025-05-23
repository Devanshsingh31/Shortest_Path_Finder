import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    private Graph graph;
    private GraphPanel graphPanel;

    private DefaultListModel<String> availableCitiesModel;
    private JList<String> availableCitiesList;

    private DefaultListModel<String> obstacleCitiesModel;
    private JList<String> obstacleCitiesList;

    private JButton addObstacleButton;
    private JButton removeObstacleButton;

    private JComboBox<String> startComboBox;
    private JComboBox<String> endComboBox;

    private JButton findPathButton;
    private JLabel distanceLabel;

    public MainFrame() {
        graph = new Graph();
        graph.initializeGraph();
        graph.initializeCoordinates();

        setTitle("Shortest Path Visualizer");
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Controls at top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        startComboBox = new JComboBox<>(graph.getGraph().keySet().toArray(new String[0]));
        endComboBox = new JComboBox<>(graph.getGraph().keySet().toArray(new String[0]));

        topPanel.add(new JLabel("Start:"));
        topPanel.add(startComboBox);
        topPanel.add(new JLabel("End:"));
        topPanel.add(endComboBox);

        findPathButton = new JButton("Find Shortest Path");
        distanceLabel = new JLabel("Distance: ");

        topPanel.add(findPathButton);
        topPanel.add(distanceLabel);

        add(topPanel, BorderLayout.NORTH);

        // Obstacle lists and buttons panel
        availableCitiesModel = new DefaultListModel<>();
        obstacleCitiesModel = new DefaultListModel<>();

        // Initially, all cities are available and no obstacles
        for (String city : graph.getGraph().keySet()) {
            availableCitiesModel.addElement(city);
        }

        availableCitiesList = new JList<>(availableCitiesModel);
        availableCitiesList.setVisibleRowCount(8);
        availableCitiesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        obstacleCitiesList = new JList<>(obstacleCitiesModel);
        obstacleCitiesList.setVisibleRowCount(8);
        obstacleCitiesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        addObstacleButton = new JButton("Add >>");
        removeObstacleButton = new JButton("<< Remove");

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        JPanel listsContainer = new JPanel(new GridLayout(1, 3, 10, 10));
        listsContainer.add(new JScrollPane(availableCitiesList));

        JPanel buttonsPanel = new JPanel(new GridLayout(2,1,5,5));
        buttonsPanel.add(addObstacleButton);
        buttonsPanel.add(removeObstacleButton);

        listsContainer.add(buttonsPanel);
        listsContainer.add(new JScrollPane(obstacleCitiesList));

        listPanel.add(new JLabel("Available Cities"), BorderLayout.NORTH);
        listPanel.add(listsContainer, BorderLayout.CENTER);

        add(listPanel, BorderLayout.WEST);

        // Graph panel with scroll pane
        graphPanel = new GraphPanel(graph);
        graphPanel.setPreferredSize(new Dimension(700, 800));

        JScrollPane graphScrollPane = new JScrollPane(graphPanel);
        add(graphScrollPane, BorderLayout.CENTER);

        // Button actions

        addObstacleButton.addActionListener(e -> {
            List<String> selected = availableCitiesList.getSelectedValuesList();
            for (String city : selected) {
                if (!graph.getBlockedCities().contains(city)) {
                    graph.getBlockedCities().add(city);
                    obstacleCitiesModel.addElement(city);
                    availableCitiesModel.removeElement(city);
                }
            }
            graphPanel.repaint();
        });

        removeObstacleButton.addActionListener(e -> {
            List<String> selected = obstacleCitiesList.getSelectedValuesList();
            for (String city : selected) {
                graph.getBlockedCities().remove(city);
                obstacleCitiesModel.removeElement(city);
                availableCitiesModel.addElement(city);
            }
            graphPanel.repaint();
        });

        findPathButton.addActionListener(e -> {
            String start = (String) startComboBox.getSelectedItem();
            String end = (String) endComboBox.getSelectedItem();

            List<String> path = graph.findShortestPath(start, end);
            if (path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No path found with current obstacles.");
                graphPanel.setAnimatedPath(path);
                distanceLabel.setText("Distance: N/A");
            } else {
                graphPanel.animatePath(path);
                int totalDistance = graph.calculateTotalDistance(path);
                distanceLabel.setText("Distance: " + totalDistance + " km");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
