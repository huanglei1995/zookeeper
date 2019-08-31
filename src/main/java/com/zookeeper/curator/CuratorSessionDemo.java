package com.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * create by hl on 2019/8/31 13:11
 * descript: curator 包连接zookeeper
 */
public class CuratorSessionDemo {
    private final static String CONNECTIONSTR = "localhost:2181";

    public static CuratorFramework getInstance () {
        // 第一种方式
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECTIONSTR, 10000, 10000,
                new ExponentialBackoffRetry(1000, 3));
        curatorFramework.start(); // 启动连接
        return curatorFramework;
        // 第二种方式
//        CuratorFramework framework = CuratorFrameworkFactory.builder().connectString(CONNECTIONSTR).connectionTimeoutMs(10000).sessionTimeoutMs(10000)
//                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
//        framework.start(); // 启动连接
//        return framework;
    }

    public static void main(String[] args) throws Exception {

        CuratorFramework curatorFramework = getInstance();

        // 创建节点
//        String result = curatorFramework.create().creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT).forPath("/curator/curator1/curator1-1", "123".getBytes());
//        System.out.println(result);

        // 删除节点
        // curatorFramework.delete().deletingChildrenIfNeeded().forPath("/curator");

        // 查询界定啊
//        Stat stat = new Stat();
//        byte[] searchBytes = curatorFramework.getData().storingStatIn(stat).forPath("/curator");
//        System.out.println(new String(searchBytes));

        // 异步操作
        ExecutorService service  = Executors.newFixedThreadPool(1);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
            .inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                    System.out.println(Thread.currentThread().getName() + "->resultCode:" + curatorEvent.getResultCode() + "->" +curatorEvent.getType());
                    countDownLatch.countDown();
                }
            }, service).forPath("/enjoy", "555".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        countDownLatch.await();
        service.shutdown();

        // 事务操作
//        try {
//            Collection<CuratorTransactionResult> resultTransaction = curatorFramework.inTransaction().create().forPath("/demo", "111".getBytes()).and()
//                    .setData().forPath("/demo", "222".getBytes()).and().commit();
//            for (CuratorTransactionResult curatorTransactionResult : resultTransaction) {
//                System.out.println(curatorTransactionResult.getForPath()+"--->" + curatorTransactionResult.getType());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
