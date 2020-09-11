package com.smj;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

@Slf4j
public class WatchOne {
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
    private ZooKeeper zooKeeper = null;
    //192.168.253.129
    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

    public void createZnode(String path, String nodeValue) throws KeeperException, InterruptedException {
        zooKeeper.create(path, nodeValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
    }

    public String getZKnode(String path) throws KeeperException, InterruptedException {
        String result = null;
        byte[] data = zooKeeper.getData(path, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    trigerValue(PATH);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Stat());
        result = new String(data);
        return result;
    }

    public String trigerValue(String path) throws KeeperException, InterruptedException {
        String result = null;
        byte[] data = zooKeeper.getData(path, false, new Stat());
        result = new String(data);
        log.info("****************watch one" + result);
        return result;
    }

    /**
     * 监听zookeeper节点，获得初值后设置watch，只要发生变化，打印出最新的值，一次性watch
     * @param args
     * @throws KeeperException
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        WatchOne watchOne = new WatchOne();
        watchOne.setZooKeeper(watchOne.startZK());

        if (watchOne.getZooKeeper().exists(PATH,false) == null){
            watchOne.createZnode(PATH,"AAA");
            String retValue = watchOne.getZKnode(PATH);
            log.info("************first retValue:"+retValue);
            Thread.sleep(Long.MAX_VALUE);
        } else{
            log.info("************node ok");

        }
    }
}
