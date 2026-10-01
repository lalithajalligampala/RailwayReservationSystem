package com.railway.adsa;

import java.util.*;

public class RailwayGraph {

    private Map<String, List<String>> graph;

    public RailwayGraph() {
        graph = new HashMap<String, List<String>>();
    }

    public void addStation(String station) {

        if (!graph.containsKey(station)) {
            graph.put(station, new ArrayList<String>());
        }
    }

    public void addConnection(String station1,
                               String station2) {

        addStation(station1);
        addStation(station2);

        graph.get(station1).add(station2);
        graph.get(station2).add(station1);
    }

    public List<String> BFS(String startStation) {

        List<String> visitedOrder =
                new ArrayList<String>();

        if (!graph.containsKey(startStation)) {
            return visitedOrder;
        }

        Queue<String> queue =
                new LinkedList<String>();

        Set<String> visited =
                new HashSet<String>();

        queue.add(startStation);
        visited.add(startStation);

        while (!queue.isEmpty()) {

            String current = queue.remove();

            visitedOrder.add(current);

            for (String neighbour :
                    graph.get(current)) {

                if (!visited.contains(neighbour)) {

                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }

        return visitedOrder;
    }

    public List<String> DFS(String startStation) {

        List<String> visitedOrder =
                new ArrayList<String>();

        Set<String> visited =
                new HashSet<String>();

        dfsRecursive(
                startStation,
                visited,
                visitedOrder
        );

        return visitedOrder;
    }

    public List<String> shortestPath(
            String startStation,
            String destinationStation) {

        List<String> path =
                new ArrayList<String>();

        if (!graph.containsKey(startStation) ||
            !graph.containsKey(destinationStation)) {

            return path;
        }

        Queue<String> queue =
                new LinkedList<String>();

        Map<String, String> parent =
                new HashMap<String, String>();

        Set<String> visited =
                new HashSet<String>();

        queue.add(startStation);

        visited.add(startStation);

        parent.put(startStation, null);

        while (!queue.isEmpty()) {

            String current = queue.remove();

            if (current.equals(destinationStation)) {
                break;
            }

            for (String neighbour :
                    graph.get(current)) {

                if (!visited.contains(neighbour)) {

                    visited.add(neighbour);

                    parent.put(
                            neighbour,
                            current
                    );

                    queue.add(neighbour);
                }
            }
        }

        if (!visited.contains(destinationStation)) {
            return path;
        }

        String current = destinationStation;

        while (current != null) {

            path.add(0, current);

            current = parent.get(current);
        }

        return path;
    }

    private void dfsRecursive(
            String station,
            Set<String> visited,
            List<String> visitedOrder) {

        if (!graph.containsKey(station)) {
            return;
        }

        visited.add(station);

        visitedOrder.add(station);

        for (String neighbour :
                graph.get(station)) {

            if (!visited.contains(neighbour)) {

                dfsRecursive(
                        neighbour,
                        visited,
                        visitedOrder
                );
            }
        }
    }
}