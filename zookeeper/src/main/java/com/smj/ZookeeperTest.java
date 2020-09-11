package com.smj;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

@Slf4j
public class ZookeeperTest {
    private static final String CONNECT_STRING = "192.168.253.129:2181";
    private static final String PATH = "/sumaojin";
    private static final int SESSION_TIMEOUT = 50 * 1000;
    //192.168.253.129
    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

    public void zkStop(ZooKeeper zooKeeper) throws InterruptedException {
        if (null != zooKeeper){
            zooKeeper.close();
        }
    }

    public void createZnode(ZooKeeper zooKeeper, String path, String nodeValue) throws KeeperException, InterruptedException {
        zooKeeper.create(path, nodeValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
    }

    public String getZKnode(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
        String result = null;
        byte[] data = zooKeeper.getData(path, false, new Stat());
        result = new String(data);
        return result;
    }

    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        ZookeeperTest zookeeperTest = new ZookeeperTest();
        ZooKeeper zk = zookeeperTest.startZK();

        if (zk.exists(PATH, false) == null){
            zookeeperTest.createZnode(zk, PATH, "helloZookeeper--v2");
            String retValue = zookeeperTest.getZKnode(zk, PATH);
            log.info("***********retValue:"+retValue);
        }else {
            log.info("***********节点已经存在");
        }

        zookeeperTest.zkStop(zk);
    }
}
