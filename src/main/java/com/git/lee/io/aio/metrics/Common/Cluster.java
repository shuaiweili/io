package com.git.lee.io.aio.metrics.Common;


import java.util.ArrayList;
import java.util.List;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 14:38
 */
public class Cluster {

    private List<Node> nodes;

    public Cluster(List<Node> nodes) {
        this.nodes = nodes;
    }

    public static Cluster bootstrap(String[] servers) {
        List<Node> nodes = new ArrayList<>();
        int nodeId = 1;
        for (String server : servers) {
            String[] hostPort = server.split(":");
            nodes.add(new Node(nodeId++, hostPort[0], Integer.parseInt(hostPort[1])));
        }
        return new Cluster(nodes);
    }

    public void close() {
        if (nodes != null) {
            for (Node node : nodes) {
                node.close();
            }
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
