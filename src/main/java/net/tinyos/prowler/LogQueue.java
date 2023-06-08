package net.tinyos.prowler;

import java.io.*;

import java.util.concurrent.ConcurrentLinkedDeque;


public class LogQueue extends ConcurrentLinkedDeque<TestBroadcastNode> {


    private Simulator sim;
    private double maxCoordinate = 0;
    private String outputPath = "./output";
    private String outputFile = "data.txt";

    public LogQueue()
    {
        super();
    }

    public void setSim(Simulator sim)
    {
        this.sim = sim;
        this.maxCoordinate = sim.getMaxCoordinate();

    }
    public Simulator getSim()
    {
        return sim;
    }

    public void writeNodeState(TestBroadcastNode node) {
        if (node.getX() > maxCoordinate)
            maxCoordinate = node.getX();
        if (node.getY() > maxCoordinate)
            maxCoordinate = node.getY();
        this.add(node);
    }

    public void writeNetworkState(Simulator simulator) {
        TestBroadcastNode tempNode = (TestBroadcastNode)simulator.firstNode;
        while (tempNode != null){
            this.writeNodeState(tempNode);
            tempNode = (TestBroadcastNode)tempNode.nextNode;
        }
    }
    public void flush()
    {
        //创建文件
        try {

            String fullPath = outputPath + "/" + outputFile;
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
//            while(this.isEmpty()){}  //判断是否有数据
            FileWriter writer = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(writer);

            String data = "";
            data = data + "{";
            out.write(data);
            out.newLine();
            data = "\"maxCoordinate\": " + maxCoordinate + ",\n";
            out.write(data);
            data = "\"length\": " + this.size()+",\n";
            out.write(data);
            data = "\"message\": [" ;
            out.write(data);
            out.newLine();
            while(!this.isEmpty()) {
                TestBroadcastNode nowNode =  this.poll();
                data = "    {";
                out.write(data);
                out.newLine();
                data = "        \"id\": "+nowNode.getId()+",\n";
                data = data + "        \"maxRadioStrength\": "+nowNode.getMaximumRadioStrength()+",\n";
                data = data + "        \"x\": "+nowNode.getX()+",\n";
                data = data + "        \"y\": "+nowNode.getY()+",\n";
                data = data + "        \"z\": "+nowNode.getZ()+",\n";
                data = data + "        \"sending\": "+String.valueOf(nowNode.sending)+",\n";
                data = data + "        \"transmitting\": "+String.valueOf(nowNode.transmitting)+",\n";
                data = data + "        \"receiving\": "+String.valueOf(nowNode.receiving)+",\n";
                data = data + "        \"corrupted\": "+String.valueOf(nowNode.corrupted)+",\n";
//                data = data + "        \"sendingPostponed\": "+String.valueOf(nowNode.sendingPostponed)+",\n";
//                data = data + "        \"sendMinWaitingTime\": "+nowNode.sendMinWaitingTime+",\n";
//                data = data + "        \"sendRandomWaitingTime\": "+nowNode.sendRandomWaitingTime+",\n";
//                data = data + "        \"sendMinBackOffTime\": "+nowNode.sendMinBackOffTime+",\n";
//                data = data + "        \"sendRandomBackOffTime\": "+nowNode.sendRandomBackOffTime+",\n";
//                data = data + "        \"sendTransmissionTime\": "+nowNode.sendTransmissionTime+",\n";
//                data = data + "        \"noiseVariance\": "+nowNode.noiseVariance+",\n";
//                data = data + "        \"maxAllowedNoiseOnSending\": "+nowNode.maxAllowedNoiseOnSending+",\n";
//                data = data + "        \"receivingStartSNR\": "+nowNode.receivingStartSNR+",\n";
//                data = data + "        \"corruptionSNR\": "+nowNode.corruptionSNR+",\n";
                data = data + "        \"msg_seqId\": "+nowNode.msg_seq+",\n";
                data = data + "        \"msg_ttl\": "+nowNode.msg_ttl+",\n";
                data = data + "        \"msg_cache_size\": "+nowNode.msg_cache.size()+",\n";
                if(nowNode.getParent() != null)
                {
                    data = data + "        \"parentNodeId\": "+nowNode.getParent().getId()+",\n";
                }
                data = data + "        \"sent\": "+String.valueOf(nowNode.sent)+"\n";
                out.write(data);
                if (!this.isEmpty()){
                    data = "    },";
                }
                else {
                    data = "    }";
                    out.write(data);
                    out.newLine();
                    break;//退出
                }
                out.write(data);
                out.newLine();
//                out.newLine();
            }
            data = "    ]";
            out.write(data);
            out.newLine();
            data = "}";
            out.write(data);
            out.newLine();
            out.flush(); // 把缓存区内容压入文件
            out.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
