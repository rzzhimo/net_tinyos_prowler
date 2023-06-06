package net.tinyos.prowler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@SpringBootApplication
public class Net_Tinyos_ProwlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Net_Tinyos_ProwlerApplication.class, args);
    }

    @RestController
    @CrossOrigin(origins = "*")
    @RequestMapping("/simulation")
    public class SimulationController {

        private boolean isRunning = false;

        Simulator sim = null;

        DisplayQueue dispQ = null;
        LogQueue logQueue = null;

        int nodeCount = 0;

        //用于获取当前的网络状态，里面是点的集合，包括了他的id，x,y坐标，以及他parent的x1,y1坐标，方便前端画图
        @GetMapping("/getNetworkState")
        public JSONArray getNetworkState() {
            return dispQ.getNetworkState(nodeCount);
        }
        //用于获取MaxCoordinate，方便画图
        @GetMapping("/getSimuState")
        public double getSimuState() {
            return  sim.getMaxCoordinate();
        }

        //用于停止模拟，待完善
        @PostMapping("/simustop")
        public JSONObject stopSimulation() {
            isRunning = false;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("stop", "successfully stop！ ");
            return jsonObject;
        }
        //用于跑仿真引擎的线程
        @PostMapping("/simustart")
        public JSONObject postNodeCount(@RequestParam int nodeCount) {

            this.nodeCount = nodeCount;
            System.out.println(nodeCount);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("post", "successfully post！ ");

            try{
                // 在这里运行仿真引擎
                System.out.println("creating nodes...1");
                sim = new Simulator();

                // creating the desired radio model, uncomment the one you need
                GaussianRadioModel radioModel = new GaussianRadioModel(sim);
                //RayleighRadioModel radioModel = new RayleighRadioModel(sim);

                // creating the node in the middle of the field, and adding a broadcast
                // application
                long time0 = System.currentTimeMillis();
                TestBroadcastNode root = (TestBroadcastNode)sim.createNode( TestBroadcastNode.class, radioModel, 1, 15, 15, 0);
                TestBroadcastNode.BroadcastApplication bcApp = root.new BroadcastApplication(root);

                // creating all the other nodes
                Node tempNode = sim.createNodes( TestBroadcastNode.class, radioModel, 2, nodeCount, 300, 5);
                while (tempNode != null){
                    TestBroadcastNode.BroadcastApplication tempBcApp = ((TestBroadcastNode)tempNode).new BroadcastApplication(tempNode);
                    tempNode = tempNode.nextNode;
                }
                // This call is a must, please do not forget to call it whenever the mote
                // field is set up
                radioModel.updateNeighborhoods();

                System.out.println("creation time1: " + (System.currentTimeMillis()-time0) + " millisecs" );
                final long time1 = System.currentTimeMillis();

                System.out.println("start simulation1");

                root.sendMessage( "test message1", bcApp );
                dispQ = new DisplayQueue();    //创建两个队列
                logQueue = new LogQueue();

                sim.runWithDisplayInRealTime(dispQ,logQueue); //获取数据
                /**
                 * 进行输出以及文件导出,不需要
                 */
                isRunning = true;//设一个标记，防止前端取不到数据
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @GetMapping("/hello")
        public JSONObject getHello(){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("hello", "hello,world");
            return jsonObject;
        }
    }
}