# zookeeper
zookeeper学习项目


### 实现zookeeper连接的三种jar包

- 原生jar
```
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.5.5</version>
</dependency>
```

- zkClient
```
<dependency>
    <groupId>com.101tec</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.11</version>
</dependency>
```

- curator(推荐)
```
<!-- https://mvnrepository.com/artifact/org.apache.curator/curator-client -->
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>4.0.0</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.apache.curator/curator-framework -->
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>4.0.0</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.apache.curator/curator-client -->
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-client</artifactId>
    <version>4.0.0</version>
</dependency>
```

### 三种jar的优缺点

- 原生jar，写太麻烦
- zkClient,在原生基础上进行了封装，但是
- curator 特性
    + 事务支持
    + 异步支持
