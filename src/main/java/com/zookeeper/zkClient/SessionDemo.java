package com.zookeeper.zkClient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * create by hl on 2019/8/31 9:58
 * descript: zkClient 实现连接zookeeper功能
 */
public class SessionDemo {
    private final static String CONNECTIONSTR = "192.168.0.133:2181";

    public static ZkClient getInstance () {
        return new ZkClient(CONNECTIONSTR, 10000);
    }
    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = getInstance();
        System.out.println(zkClient + "success");

        // zkClient 提供递归创建父节点,第二个参数表示是否级联创建
        // zkClient.createPersistent("/zkClient/zkClient1/zkClient1-1/zkClient1-1-1", true);

        // zkClient 获取字节的
        // List<String> children = zkClient.getChildren("/zkClient");
        // System.out.println(children);

        // zkClient 删除节点，第二个参数表示是否级联删除
        // zkClient.deleteRecursive("/zkClient");

        // zkClient 监听节点值的变化（修改和删除）
//        zkClient.subscribeDataChanges("/zkClient", new IZkDataListener() {
//            @Override
//            public void handleDataChange(String s, Object o) throws Exception {
//                System.out.println("节点名称：" + s + "->节点修改后的值:" + o);
//            }
//            @Override
//            public void handleDataDeleted(String s) throws Exception {
//
//            }
//        });
//
//        zkClient.writeData("/zkClient", "node");
//        TimeUnit.SECONDS.sleep(2);

        // zkClient 监听节点值的变化
        zkClient.subscribeChildChanges("/zkClient/zkClient1", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println("节点名称：" + s + "->节点修改后的值:" + list);
            }
        });
        zkClient.delete("/zkClient/zkClient1/zkClient1-1");
        TimeUnit.SECONDS.sleep(2);

    }
}
