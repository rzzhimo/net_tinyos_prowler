package net.tinyos.prowler;

import java.util.Random;

public class RunSimulation {

    /**
     * Starts up a simulator with a ROOT in the middle of a 300 by 300 meters
     * field with 1000 motes and runs it in real time mode.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        System.out.println("creating nodes...");
        Simulator sim = new Simulator();

        // creating the desired radio model, uncomment the one you need
        GaussianRadioModel radioModel = new GaussianRadioModel(sim);
        //RayleighRadioModel radioModel = new RayleighRadioModel(sim);

        // creating the node in the middle of the field, and adding a broadcast
        // application
        long time0 = System.currentTimeMillis();
        TestBroadcastNode root = (TestBroadcastNode)sim.createNode( TestBroadcastNode.class, radioModel, 1, 15, 15, 0);
        TestBroadcastNode.BroadcastApplication bcApp = root.new BroadcastApplication(root);


        // creating all the other nodes
        Node tempNode = sim.createNodes( TestBroadcastNode.class, radioModel, 2, 1000, 300, 5);
        while (tempNode != null){
            TestBroadcastNode.BroadcastApplication tempBcApp = ((TestBroadcastNode)tempNode).new BroadcastApplication(tempNode);
            tempNode = tempNode.nextNode;
        }
        // This call is a must, please do not forget to call it whenever the mote
        // field is set up
        radioModel.updateNeighborhoods();

        System.out.println("creation time: " + (System.currentTimeMillis()-time0) + " millisecs" );
        final long time1 = System.currentTimeMillis();

        System.out.println("start simulation");

        //因为只会发送一条消息，所以message的seqid自始至终只有一个值。
        Random random = new Random();
        int seqId = random.nextInt(100);
        //改用包装起来的Message类传递信息,这里可以改消息的存活时间ttl
        Message message1 = new Message(seqId,"test message",500);
        root.sendMessage( message1, bcApp );

        DisplayQueue dispQ = new DisplayQueue();    //创建两个队列
        LogQueue logQueue = new LogQueue();

        boolean realTime = true;
        if( realTime )
        {

            sim.runWithDisplayInRealTime(dispQ,logQueue,Boolean.TRUE); //获取数据

            /**
             * 进行输出以及文件导出
             */
            logQueue.setSim(sim);
            dispQ.setSim(sim);
            dispQ.setDisplay();
            Monitor monitor = new Monitor(sim, dispQ, logQueue);
            monitor.updateView();
        }
        else
        {
            // run as fast as possible and measure dump execution time
            Event event = new Event()
            {
                public void execute()
                {
                    System.out.println("execution time: " + (System.currentTimeMillis()-time1) + " millisecs" );
                }
            };
            event.time = Simulator.ONE_SECOND * 1000;
            sim.addEvent(event);
            sim.run(20000);
        }
    }
}
