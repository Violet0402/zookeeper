package com.smj;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

@Slf4j
public class WatchChildren {
    //常量
    private static final String CONNECT_STRING = "192.168.253.129:2181";
    private static final String PATH = "/sumaojin";
    private static final int SESSION_TIMEOUT = 50 * 1000;

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    //实例变量
    private String oldValue = null;

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    private String newValue = null;
    private ZooKeeper zooKeeper = null;
    //192.168.253.129
    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged && watchedEvent.getPath().equals(PATH)){
                    showChildNode(PATH);
                }else {
                    showChildNode(PATH);
                }
            }
        });
    }

    public void showChildNode(String path){
        List<String> list = null;
        try {
            list = zooKeeper.getChildren(path, true);
            log.info("**************"+list);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        WatchChildren watchChildren = new WatchChildren();
        watchChildren.setZooKeeper(watchChildren.startZK());
        Thread.sleep(Long.MAX_VALUE);
    }
}
