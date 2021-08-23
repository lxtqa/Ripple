# Ripple

## 1. 编译
在根目录中执行mvn install。

## 2. 系统部署
大规模容器集群配置系统分为服务器模块和客户端模块两部分，需要内嵌部署至用户程序中以实现数据分发功能。建议用户程序基于Apache Maven构建从而方便添加对模块的依赖。

### 2.1 服务器端部署
请遵循如下步骤完成服务器端部署：
1. 将服务器端模块ripple-server-1.0.0.jar和ripple-server-1.0.0.pom复制到本地Maven仓库，或将其添加至classpath。

2. 在用户程序的pom.xml文件中添加如下依赖项：
```
<dependency>
    <groupId>ripple</groupId>
    <artifactId>ripple-server</artifactId>
    <version>1.0.0</version>
</dependency>
```

3. 在用户程序中通过如下代码创建服务器端：
```
int serverId = 1;
String storageLocation = "path-to-storage-database";
RippleServer rippleServer = RippleServer
    .starProtocol(serverId, storageLocation);
```

其中，serverId为服务器的唯一编号，storageLocation为服务器本地存储的路径，即用于存储所有配置信息的路径。

此外，可以在创建服务器端时同时指定端口号：

```
int serverId = 1;
String storageLocation = "path-to-storage-database";
int port = 8888;
RippleServer rippleServer = RippleServer.starProtocol(serverId, storageLocation, port);
```

其中port为端口号。

值得注意的是，如果在创建服务器端时未指定端口号，可在如下第5步启动操作执行后通过rippleServer. getPort()方法获得端口号。

此外，用户可选择使用不同的数据推送协议以适用于不同的场景：
* 星型通知：调用RippleServer.starProtocol方法创建。
* 完全N叉树：调用RippleServer.treeProtocol方法创建。需要同时传入树的分叉数branch。

4.	在用户程序中通过如下代码配置服务器集群：
```
List<NodeMetadata> nodeList = new ArrayList<>();
NodeMetadata nodeOne = new NodeMetadata(idOne,addressOne,portOne);
NodeMetadata nodeTwo = new NodeMetadata(idTwo,addressTwo,portTwo)
nodeList.add(nodeOne);
nodeList.add(nodeTwo);
rippleServer.setNodeList(nodeList);
```

其中，NodeMetadata定义了服务器的ID、IP地址和端口号。

配置服务器集群的操作可以在如下第5步启动服务器端的操作后进行，但需要保证在此期间没有客户端发起配置更新操作。

5. 在用户程序中通过如下代码启动服务器端：
```
rippleServer.start();
```
6. 在用户程序中通过如下代码关闭服务器端：
```
rippleServer.stop();
```

### 2.2 客户端部署
请遵循如下步骤完成客户端部署：
1. 将客户端模块ripple-client-1.0.0.jar和ripple-client-1.0.0.pom复制到本地Maven仓库，或将其添加至classpath。
2. 在用户程序的pom.xml文件中添加如下依赖项：
```
<dependency>
    <groupId>ripple</groupId>
    <artifactId>ripple-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

3. 在用户程序中通过如下代码创建客户端：
```
String serverAddress = "ip-of-server";
int serverPort = 1;
String storageLocation = "path-to-storage-database";
RippleClient rippleClient = new RippleClient(serverAddress, serverPort, storageLocation);
```

其中，serverAddress为要连接到的服务器的IP地址，serverPort为服务器端口号，storageLocation为客户端本地存储路径，用于缓存客户端已订阅的配置数据。

4. 在用户程序中通过如下代码启动客户端：
```
rippleClient.start();
```

5. 在用户程序中通过如下代码关闭客户端：
```
rippleClient.stop();
```

## 3. 基本功能
大规模容器集群配置系统的核心概念是“数据”。一个数据条目由如下字段组成：
* 应用名称
* 键
* 值
* 最后更新时间
* 发起最后一次更新的服务器ID

其中，应用名称和键的二元组唯一标识了一个数据条目。

### 3.1 订阅数据
客户端可以通过如下代码订阅一个数据条目：
```
String applicationName="mysql";
String key="user";
rippleClient.subscribe(applicationName,key);
```

其中，application即为应用名称，key即为键。

返回值为Boolean类型，代表操作成功与否。

客户端在订阅了数据之后，后续针对这一数据条目的更新会由服务器推送至客户端。

### 3.2 取消订阅
客户端可以通过如下代码取消订阅一个数据条目：
```
String applicationName="mysql";
String key="user";
rippleClient.unsubscribe(applicationName,key);
```

其中，application即为应用名称，key即为键。

返回值为Boolean类型，代表操作成功与否。

取消订阅之后，后续针对这一数据条目的更新将不会被推送至客户端。

### 3.3 获取数据
客户端可以通过如下代码获取指定数据条目：
```
String applicationName="mysql";
String key="user";
rippleClient.get(applicationName,key);
```

其中，application即为应用名称，key即为键。

返回值为一个Item对象，包含如下属性：

* applicationName：应用名称
* key：键
* value：值
* lastUpdate：最后更新时间
* lastUpdateServerId：发起最后一次更新的服务器ID

需要注意的是，客户端本地会缓存获取的数据，只有在首次发起“获取数据”请求或订阅这一数据条目时会向服务器请求。特别地，如果只使用获取数据而不使用订阅数据功能时，服务器端数据更新时并不会同步到本地。

### 3.4 更新数据
客户端可以通过如下代码更新指定数据条目：
```
String applicationName = "mysql";
String key = "user";
String newValue = "root";
rippleClient.put(applicationName, key, newValue);
```

其中，application即为应用名称，key即为键，newValue为这一数据条目的新值。

返回值为Boolean类型，代表操作成功与否。

### 3.5 删除数据
客户端可以通过如下代码删除一个数据条目：
```
String applicationName="mysql";
String key="user";
rippleClient.delete(applicationName,key);
```

其中，application即为应用名称，key即为键。

返回值为Boolean类型，代表操作成功与否。

删除数据条目时将会通知所有订阅了这一数据条目的客户端，服务器也会同时解除这一订阅关系。

## 4. Web服务
本节介绍大规模容器集群配置系统内部通讯使用的Web服务。在可能的情况下，用户程序可以选择不使用大规模容器集群配置系统提供的内嵌客户端模块，而选择直接基于服务器端API进行适配，从而适用于更广泛的场景。

### 4.1 服务器端API
服务器端提供如下API以支持配置管理功能：
* 订阅配置
   * URL：/Api/Subscribe
   * 方法：HTTP POST
   * 参数：使用HTTP Header传入如下参数
      * x-ripple-application-name：需订阅的配置的应用名称。
      * x-ripple-key：需订阅的配置的键。
      * x-ripple-callback-address：客户端回调IP地址。
      * x-ripple-callback-port：客户端回调端口号。
   * 返回值：一个JSON对象封装的Boolean值，指示操作成功与否。
* 取消订阅
   * URL：/Api/Unsubscribe
   * 方法：HTTP POST
   * 参数：使用HTTP Header传入如下参数
      * x-ripple-application-name：需取消订阅的配置的应用名称。
      * x-ripple-key：需取消订阅的配置的键。
      * x-ripple-callback-address：客户端回调IP地址。
      * x-ripple-callback-port：客户端回调端口号。
   * 返回值：一个JSON对象封装的Boolean值，指示操作成功与否。
* 获取配置
   * URL：/Api/Get
   * 方法：HTTP GET
   * 参数：使用HTTP Header传入如下参数：
      * x-ripple-application-name：配置的应用名称。
      * x-ripple-key：配置的键。
   * 返回值：一个JSON对象，封装了如下信息：
      * applicationName：配置的应用名称
      * key：配置的键
      * value：配置的值
      * lastUpdate：配置的最后更新时间，为Unix Time
      * lastUpdateServerId：最后更新这一配置的服务器ID
* 添加或修改配置
   * URL：/Api/Put
   * 方法：HTTP POST
   * 参数：使用HTTP Header传入如下参数：
      * x-ripple-application-name：配置的应用名称。
      * x-ripple-key：配置的键。
      * x-ripple-value：配置的值。
   * 返回值：一个JSON对象封装的Boolean值，指示操作成功与否。
* 删除配置
   * URL：/Api/Delete
   * 方法：HTTP POST
   * 参数：使用HTTP Header传入如下参数：
      * x-ripple-application-name：配置的应用名称。
      * x-ripple-key：配置的键。
   * 返回值：一个JSON对象封装的Boolean值，指示操作成功与否。

### 4.2 客户端回调API
客户端实现过程中，如果需要使用服务器端提供的订阅和取消订阅的API，需提供如下回调API供服务器推送配置更新：
* URL：/Api/Notify
* 方法：HTTP POST
* 参数：使用HTTP Header传入如下参数：
   * x-ripple-type：通知类型，可选值为“update”和“delete”
   * 当x-ripple-type为“update”时，服务器端会传入如下参数：
      * x-ripple-application-name：配置的应用名称。
      * x-ripple-key：配置的键。
      * x-ripple-value：配置的最新值。
      * x-ripple-last-update：配置的最后更新时间，为Unix Time
      * x-ripple-last-update-server-id：最后更新这一配置的服务器ID
   * 当x-ripple-type为“delete”时，服务器端会传入如下参数：
      * x-ripple-application-name：配置的应用名称。
      * x-ripple-key：配置的键。
   * 返回值：一个JSON对象封装的Boolean值，指示操作成功与否。

回调程序中需要根据type不同更新本地存储中的配置信息或删除对应的配置。

## 5. 二次开发
### 5.1 自定义底层推送拓扑
大规模容器集群配置系统支持用户扩展新的底层推送拓扑，从而适用于不同规模的集群和不同性能、一致性和可靠性要求的场景。用户可以通过实现ripple.server.core.overlay.Overlay接口的方式添加新的底层推送拓扑。

Overlay接口中定义了buildOverlay和calculateNodesToSync两个方法，传入的参数为NodeMetadata对象或其列表。

NodeMetadata定义了服务器端的基本信息，包含如下属性：
* id：服务器端的唯一标识符。
* address：服务器端的IP地址。
* port：服务器端的端口号。

Overlay接口中的两个方法的具体说明如下：
* `void buildOverlay(List<NodeMetadata> nodeList)`：初始化底层推送拓扑，传入的参数为服务器集群信息。配置管理系统会在调用RippleServer类的initCluster方法时调用buildOverlay方法。
* `List<NodeMetadata> calculateNodesToSync(NodeMetadata source, NodeMetadata current)`：计算消息来源节点为source，当前节点为current时，根据底层推送拓扑需要发送的节点列表。例如，对于一个一对多的星型通知拓扑而言，消息来源方同时为自己时需要将消息推送至其他所有的服务器节点，而当消息来源方不为自己时，不需要推送至任何节点。配置管理系统会在更新配置和删除配置时调用这一方法以实现配置更新在服务器集群内的同步。

用户在实现了新的Overlay之后，可以直接调用RippleServer的构造函数以传入自定义的推送拓扑。此外，为了保证配置管理系统的正常运行，服务器集群中的所有节点应当使用相同的底层推送拓扑。
