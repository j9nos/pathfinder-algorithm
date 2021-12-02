import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Node {
    int x, y;
    Node parent;
    float g, h, f;
    Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.parent = null;
        this.g = 0;
        this.h = 0;
        this.f = 0;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Node))
            return false;
        Node other = (Node) o;
        return (this.x == other.x && this.y == other.y);
    }
    @Override
    public int hashCode() {
        return 999 * x * y;
    }
}

class Main {

    static ArrayList<Node> convertSetToList(HashSet<Node> openSet) {
        ArrayList<Node> result = new ArrayList<Node>();
        for (Node e : openSet) {
            result.add(e);
        }
        return result;
    }

    static Node findNodeWithLowestF(HashSet<Node> openSet) {
        ArrayList<Node> nodeList = convertSetToList(openSet);
        Collections.sort(nodeList, new Comparator<Node>() {
            @Override
            public int compare(Node nodeL, Node nodeR) {
                return Float.compare(nodeL.f, nodeR.f);
            }
        });
        return nodeList.get(0);
    }

    static ArrayList<Node> reconstructPath(Node currentNode) {
        ArrayList<Node> path = new ArrayList<Node>();
        while (currentNode != null) {
            path.add(new Node(currentNode.x, currentNode.y));
            currentNode = currentNode.parent;
        }
        Collections.reverse(path);
        return path;
    }

    static ArrayList<Node> findPath(Node startNode, Node endNode, int[][] map, int[][] possibleDirections) {
        HashSet<Node> openSet = new HashSet<Node>();
        HashSet<Node> usedSet = new HashSet<Node>();
        openSet.add(startNode);
        while (openSet.size() > 0) {
            Node currentNode = findNodeWithLowestF(openSet);
            if (currentNode.equals(endNode))
                return reconstructPath(currentNode);
            openSet.remove(currentNode);
            usedSet.add(currentNode);
            for (int[] possibleDirection : possibleDirections) {
                Node newNode = new Node(currentNode.x + possibleDirection[0], currentNode.y + possibleDirection[1]);
                newNode.parent = currentNode;
                if (newNode.x > map[0].length - 1 || newNode.y > map.length - 1 || newNode.x < 0 || newNode.y < 0)
                    continue;
                if (map[newNode.x][newNode.y] == 1)
                    continue;
                if (usedSet.contains(newNode))
                    continue;
                newNode.g = currentNode.g + 1;
                newNode.h = (float) (Math.pow(newNode.x - endNode.y, 2) + Math.pow(newNode.y - endNode.y, 2));
                newNode.f = newNode.g + newNode.h;
                if (openSet.contains(newNode))
                    continue;
                openSet.add(newNode);
            }
        }
        return new ArrayList<Node>() {
        };
    }

    public static void main(String[] args){

        int[][] map = {
                { 0, 1, 0, 0, 0, 0, 0 },
                { 0, 1, 0, 1, 1, 1, 0 },
                { 0, 1, 0, 1, 0, 1, 0 },
                { 0, 1, 0, 0, 0, 1, 0 },
                { 0, 1, 0, 0, 0, 1, 0 },
                { 0, 1, 1, 1, 1, 1, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
        };
        int[][] possibleDirections = {
                { 0, -1 },
                { -1, -1 },
                { -1, 0 },
                { -1, 1 },
                { 0, 1 },
                { 1, 1 },
                { 1, 0 },
                { 1, -1 }
        };
        Node startNode = new Node(0, 0);
        startNode.g = startNode.h = startNode.f = 0;

        Node endNode = new Node(4, 4);
        endNode.g = endNode.h = endNode.f = 0;

        ArrayList<Node> path = findPath(startNode, endNode, map, possibleDirections);

        if (path.size() > 0) {
            for (Node foundPath : path) {
                System.out.println(foundPath.x + "." + foundPath.y);
            }
        }else{
            System.out.println("Could not find a path");
        }
    }
}
