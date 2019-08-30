package com.zookeeper.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * create by hl on 2019/8/30 22:13
 * descript: 创建一个zookeeper会话
 */
public class createSession {

    private final static String CONNECTIONSTR = "192.168.0.133:2181";

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        // 创建zookeeper
        ZooKeeper zooKeeper = new ZooKeeper(CONNECTIONSTR, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
                if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                  // 如果数据发生了变更
                    System.out.println("现在节点发生了变化，变化后的路径"+watchedEvent.getPath() );
                }
            }
        });
        countDownLatch.await();
        System.out.println("============================================");
        System.out.println(zooKeeper.getState());

        // 获得连接之后就可以操作,但是可能会还没有连接上，有延迟，用countDownLatch解决
        // 节点，节点值，ACL权限，节点类型，
        zooKeeper.create("/hl", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 获取数据
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/hl", true, stat);
        System.out.println(new String(data));

        // 修改节点,-1表示不做版本控制
        zooKeeper.setData("/hl", "hello".getBytes(), -1);

        // 删除节点
        zooKeeper.delete("/hl", -1);

        List<String> children = zooKeeper.getChildren("/enjoy", true);
        System.out.println(children);
    }
}
