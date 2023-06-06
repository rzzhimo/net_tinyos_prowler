package net.tinyos.prowler;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.List;

public class DisplayQueue extends ConcurrentLinkedDeque<TestBroadcastNode> {

	public Display disp;
	private Simulator sim;
	private double maxCoordinate = 0;
	private long simulationTimeInMillisec = 0;
	
	public DisplayQueue()
	{
		super();
	}

//	public void setDisplay(Display display)
//	{
//		this.disp = display;
//	}

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
    	simulationTimeInMillisec = simulator.getSimulationTimeInMillisec();
		TestBroadcastNode tempNode = (TestBroadcastNode)simulator.firstNode;
		while (tempNode != null){
			this.writeNodeState(tempNode);
			tempNode = (TestBroadcastNode)tempNode.nextNode;
		}
    }
    public void setDisplay()
    {
		Display disp = new Display(maxCoordinate);
		disp.show();
		this.disp = disp;
		this.disp.setDisplayQueue(this);
    }
    
    public void flush(Graphics g) {

//		while(this.isEmpty()){}  //判断是否有数据

		/**只取当前生成的数据**/
//		if(!this.isEmpty())
//		{
//			DisplayQueue tempDQ = this;
//		}
//		int now_size = this.size();
//    	while(now_size > 0) {
//    		now_size-=1;

		/**新生成的数据也会进行获取**/
		while (!this.isEmpty()){
//			System.out.println(now_size);
			TestBroadcastNode nowNode =  this.poll();
//			Graphics g = disp.getGraphics();
			int      x = disp.x2ScreenX(nowNode.x);
			int      y = disp.y2ScreenY(nowNode.y);
//			System.out.println("x"+nowNode.x+" y"+nowNode.x);
			if( nowNode.sending ){
				g.setColor( Color.blue );
			}else if( nowNode.receiving ){
				if( nowNode.corrupted )
					g.setColor( Color.red );
				else
					g.setColor( Color.green );
			}else{
				if( nowNode.sent )
					g.setColor( Color.pink );
				else
					g.setColor( Color.black );
			}
			g.fillOval( x-3, y-3, 5, 5 );
			if( nowNode.getParent() != null ){
				g.setColor( Color.black );
				int x1 = disp.x2ScreenX(nowNode.getParent().getX());
				int y1 = disp.y2ScreenY(nowNode.getParent().getY());
				g.drawLine(x,y,x1,y1);
				g.drawLine(x,y,x1,y1);

			}


		}
    }

	public double getNodeState(){

		return maxCoordinate;
	}

	//获取当前网络状态。返回给前端画图
	public JSONArray getNetworkState(int nodecount){

		JSONArray result = new JSONArray();
		int count = 0;
		//只返回前nodeCount个，防止数据过多
		while(!this.isEmpty()&&count < nodecount) {
			count++;
			TestBroadcastNode nowNode =  this.poll();
			JSONObject jo = new JSONObject();
			jo.put("id",nowNode.getId());
			jo.put("x",nowNode.getX());
			jo.put("y",nowNode.getY());
			String nodeColor = "";
			if(nowNode.sending){
				nodeColor = "blue";
			}else if( nowNode.receiving ){
				if( nowNode.corrupted )
					nodeColor = "red";
				else
					nodeColor = "green";
			}else{
				if( nowNode.sent )
					nodeColor = "pink";
				else
					nodeColor = "black";
			}
			jo.put("color",nodeColor);
			if(nowNode.getParent()!= null ){
				double x1 = nowNode.getParent().getX();
				double y1 = nowNode.getParent().getY();
				jo.put("x1",x1);
				jo.put("y1",y1);
			}
			else {
				jo.put("x1",-1);
				jo.put("y1",-1);
			}
			result.add(jo);
		}
		//this.clear();//清除剩余的数据
		return result;
	}

}