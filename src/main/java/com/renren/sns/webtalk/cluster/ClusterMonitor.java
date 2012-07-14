package com.renren.sns.webtalk.cluster;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.renren.sns.webtalk.util.TalkLogUtil;

/**
 * @author jinchao.wen
 * @email jinchao.wen@opi-corp.com
 * @date 2011-4-15
 */
class ClusterMonitor implements Runnable {

    private volatile AtomicBoolean flag = new AtomicBoolean(true);

    @Autowired
    protected IClusterManager clusterManager;

    public ClusterMonitor() {
    }

    public void run() {
        while (flag.get()) {
//            TalkLogUtil.debug("start.");
            try {
                clusterManager.checkServerStatus();
            } catch (Exception e) {
                TalkLogUtil.error(e.getMessage());
            }

//            TalkLogUtil.debug("over.");
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                TalkLogUtil.error(e.getMessage());
            }
        }
    }

    @PostConstruct
    public void start() {
        Thread t = new Thread(this, ClusterMonitor.class.getName());
        t.start();
    }

    public void shutdown() {
        flag.set(false);
    }

    public IClusterManager getClusterManager() {
        return clusterManager;
    }

    public void setClusterManager(IClusterManager clusterManager) {
        this.clusterManager = clusterManager;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext appCtx = new ClassPathXmlApplicationContext(
                new String[] { "classpath:/applicationContext.xml" });
        IClusterManager cm = (IClusterManager) appCtx.getBean("clusterManager");
        ClusterMonitor monitor = (ClusterMonitor) appCtx.getBean("monitor");
        System.out.println(cm);
        System.out.println(monitor);

        System.out.println(monitor.getClusterManager());
        monitor.getClusterManager().checkServerStatus();
        System.exit(0);
    }
}
