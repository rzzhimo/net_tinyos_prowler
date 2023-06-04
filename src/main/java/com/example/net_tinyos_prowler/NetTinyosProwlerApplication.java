package com.example.net_tinyos_prowler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NetTinyosProwlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetTinyosProwlerApplication.class, args);
    }

    @RestController
    @RequestMapping("/simulation")
    public class SimulationController {

        private boolean isRunning = false;
        private List<Node> nodes = new ArrayList<>();

        @PostMapping("/start")
        public void startSimulation(@RequestParam int nodeCount) {
            isRunning = true;
            nodes.clear();
            for (int i = 0; i < nodeCount; i++) {
                nodes.add(new Node(Math.random() * 500, Math.random() * 500, Math.random() * 2 - 1, Math.random() * 2 - 1));
            }
            draw();
        }

        @PostMapping("/stop")
        public void stopSimulation() {
            isRunning = false;
        }

        @GetMapping("/nodes")
        public List<Node> getNodes() {
            return nodes;
        }

        private void draw() {
            if (!isRunning) {
                return;
            }
            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                node.setX(node.getX() + node.getVx());
                node.setY(node.getY() + node.getVy());
                if (node.getX() < 0 || node.getX() > 500) {
                    node.setVx(node.getVx() * -1);
                }
                if (node.getY() < 0 || node.getY() > 500) {
                    node.setVy(node.getVy() * -1);
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            draw();
        }
    }

    public static class Node {
        private double x;
        private double y;
        private double vx;
        private double vy;

        public Node(double x, double y, double vx, double vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        // getters and setters
    }
}