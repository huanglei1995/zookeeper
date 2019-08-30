package com.zookeeper.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * create by hl on 2019/8/30 23:07
 * descript: zookeeper权限实现
 */
public class AuthControlDemo implements Watcher {

    private final static String CONNECTIONSTR = "192.168.0.133:2181";

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static CountDownLatch countDownLatch2 = new CountDownLatch(1);

    private static ZooKeeper zooKeeper;
    private static Stat stat;

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, KeeperException {
        zooKeeper = new ZooKeeper(CONNECTIONSTR, 5000, new AuthControlDemo());
        countDownLatch.await();
        ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest("root:root")));
        ACL acl2 = new ACL(ZooDefs.Perms.CREATE, new Id("ip", "192.168.56.1"));

        List<ACL> acls = new ArrayList<>();
        acls.add(acl);
        acls.add(acl2);

        zooKeeper.create("/auth1", "123".getBytes(), acls, CreateMode.PERSISTENT);
        zooKeeper.addAuthInfo("digest", "root:root".getBytes());

        zooKeeper.create("/auth1/auth1-1", "123".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        ZooKeeper zooKeeper1 = new ZooKeeper(CONNECTIONSTR, 5000, new AuthControlDemo());
        // countDownLatch.await();
        zooKeeper1.addAuthInfo("digest", "root:root".getBytes());
        // 会报错，因为该会话下没有临时节点
        zooKeeper1.delete("/auth1/auth1-1", -1);

    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
            if (event.getType() == Event.EventType.None && null == event.getPath()) {
                // 如果数据发生了变更
                System.out.println(event.getState()+ "-->" + event.getPath() );
            }
        }

    }
}
