package net.tinyos.prowler;

public class Monitor {
    private DisplayQueue displayQueue;
    private Display display;
    private Simulator simulator;
    private LogQueue logQueue;

    public Monitor(Simulator sim, DisplayQueue displayQueue, LogQueue logQueue)
    {

        this.displayQueue = displayQueue;
        this.display = displayQueue.disp;
        this.simulator = sim;
        this.logQueue = logQueue;
    }
    public void updateView()
    {
        this.display.update();    //可视化显示
        this.logQueue.flush();    //导出为文件
    }


}
