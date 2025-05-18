import java.awt.Point;
import java.util.*;

public class Graph {
    private Map<String, List<Edge>> graph = new HashMap<>();
    private Map<String, Point> cityCoordinates = new HashMap<>();
    private Set<String> blockedCities = new HashSet<>();

    public Map<String, List<Edge>> getGraph() {
        return graph;
    }

    public Map<String, Point> getCityCoordinates() {
        return cityCoordinates;
    }

    public Set<String> getBlockedCities() {
        return blockedCities;
    }

    public void addEdge(String source, String destination, int weight) {
        graph.computeIfAbsent(source, k -> new ArrayList<>()).add(new Edge(destination, weight));
        graph.computeIfAbsent(destination, k -> new ArrayList<>()).add(new Edge(source, weight));
    }

    public void initializeGraph() {
        addEdge("Delhi", "Agra", 233);
        addEdge("Agra", "Kanpur", 280);
        addEdge("Kanpur", "Lucknow", 90);
        addEdge("Lucknow", "Varanasi", 320);
        addEdge("Varanasi", "Patna", 250);
        addEdge("Delhi", "Jaipur", 281);
        addEdge("Jaipur", "Udaipur", 393);
        addEdge("Udaipur", "Ahmedabad", 262);
        addEdge("Ahmedabad", "Mumbai", 531);
        addEdge("Mumbai", "Pune", 149);
        addEdge("Pune", "Hyderabad", 560);
        addEdge("Hyderabad", "Bangalore", 570);
        addEdge("Bangalore", "Chennai", 350);
        addEdge("Chennai", "Kolkata", 1670);
        addEdge("Kolkata", "Guwahati", 970);
        addEdge("Delhi", "Chandigarh", 243);
        addEdge("Chandigarh", "Amritsar", 229);
        addEdge("Amritsar", "Jammu", 217);
        addEdge("Jammu", "Srinagar", 270);
        addEdge("Delhi", "Dehradun", 254);
        addEdge("Dehradun", "Lucknow", 544);
        addEdge("Lucknow", "Bhopal", 585);
        addEdge("Bhopal", "Nagpur", 350);
        addEdge("Nagpur", "Hyderabad", 500);
        addEdge("Nagpur", "Raipur", 290);
        addEdge("Raipur", "Bhubaneswar", 540);
        addEdge("Bhubaneswar", "Kolkata", 440);
        addEdge("Mumbai", "Goa", 590);
        addEdge("Goa", "Mangalore", 360);
        addEdge("Mangalore", "Kochi", 420);
        addEdge("Kochi", "Thiruvananthapuram", 200);
        addEdge("Chennai", "Madurai", 460);
        addEdge("Madurai", "Thiruvananthapuram", 260);
    }

    public void initializeCoordinates() {
        cityCoordinates.put("Delhi", new Point(150, 150));
        cityCoordinates.put("Agra", new Point(200, 200));
        cityCoordinates.put("Kanpur", new Point(250, 250));
        cityCoordinates.put("Lucknow", new Point(300, 250));
        cityCoordinates.put("Varanasi", new Point(350, 300));
        cityCoordinates.put("Patna", new Point(400, 300));
        cityCoordinates.put("Jaipur", new Point(150, 250));
        cityCoordinates.put("Udaipur", new Point(150, 350));
        cityCoordinates.put("Ahmedabad", new Point(150, 450));
        cityCoordinates.put("Mumbai", new Point(150, 550));
        cityCoordinates.put("Pune", new Point(200, 600));
        cityCoordinates.put("Hyderabad", new Point(250, 650));
        cityCoordinates.put("Bangalore", new Point(300, 700));
        cityCoordinates.put("Chennai", new Point(350, 750));
        cityCoordinates.put("Kolkata", new Point(450, 350));
        cityCoordinates.put("Guwahati", new Point(550, 250));
        cityCoordinates.put("Chandigarh", new Point(190, 100));
        cityCoordinates.put("Amritsar", new Point(100, 100));
        cityCoordinates.put("Jammu", new Point(100, 70));
        cityCoordinates.put("Srinagar", new Point(100, 25));
        cityCoordinates.put("Dehradun", new Point(250, 150));
        cityCoordinates.put("Bhopal", new Point(250, 350));
        cityCoordinates.put("Nagpur", new Point(300, 400));
        cityCoordinates.put("Raipur", new Point(350, 450));
        cityCoordinates.put("Bhubaneswar", new Point(400, 500));
        cityCoordinates.put("Goa", new Point(150, 650));
        cityCoordinates.put("Mangalore", new Point(200, 700));
        cityCoordinates.put("Kochi", new Point(250, 750));
        cityCoordinates.put("Thiruvananthapuram", new Point(300, 800));
        cityCoordinates.put("Madurai", new Point(350, 700));
    }

    public List<String> findShortestPath(String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String city : graph.keySet()) {
            distances.put(city, Integer.MAX_VALUE);
            previous.put(city, null);
        }

        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end)) break;

            if (blockedCities.contains(current)) {
                continue; // skip blocked cities
            }

            for (Edge edge : graph.get(current)) {
                if (!blockedCities.contains(edge.destination)) {
                    int newDist = distances.get(current) + edge.weight;
                    if (newDist < distances.get(edge.destination)) {
                        distances.put(edge.destination, newDist);
                        previous.put(edge.destination, current);
                        queue.add(edge.destination);
                    }
                }
            }
        }

        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        if (path.size() == 1 && !path.get(0).equals(start)) {
            // No path found, return empty
            return new ArrayList<>();
        }
        return path;
    }

    public int calculateTotalDistance(List<String> path) {
        int total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            for (Edge edge : graph.get(path.get(i))) {
                if (edge.destination.equals(path.get(i + 1))) {
                    total += edge.weight;
                    break;
                }
            }
        }
        return total;
    }
}
