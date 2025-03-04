# Ripple

A flexible pub-sub system for large-scale clusters

- [Ripple](#ripple)
  - [Quick start](#quick-start)
    - [Requirements](#requirements)
    - [Download the package](#download-the-package)
    - [Start the servers](#start-the-servers)
    - [Start the clients](#start-the-clients)
    - [Publish/Subscribe](#publishsubscribe)
  - [Build from the source](#build-from-the-source)
    - [Requirements](#requirements-1)
    - [Build the project](#build-the-project)
  - [Deployment](#deployment)
    - [Deploy the server in standalone mode](#deploy-the-server-in-standalone-mode)
    - [Use the server in embedded mode](#use-the-server-in-embedded-mode)
    - [Deploy the client in standalone mode](#deploy-the-client-in-standalone-mode)
    - [Use the client in embedded mode](#use-the-client-in-embedded-mode)
  - [Benchmarking](#benchmarking)
    - [Preparing the server cluster](#preparing-the-server-cluster)
    - [Creating clients](#creating-clients)
    - [Measuring the latency](#measuring-the-latency)
    - [Analyzing](#analyzing)

## Quick start

### Requirements
JDK >= 1.8.0 is needed to run `ripple-server` and `ripple-client`.

For RHEL/CentOS/Fedora/openEuler
```shell
dnf install java-1.8.0-openjdk-devel
```

For Ubuntu/Debian
```shell
apt-get install openjdk-8-jdk
```

For Windows or Mac, please download the installation files from Oracle website (`https://www.oracle.com/java/technologies/downloads/`).

Confirm with `java -version` command.

### Download the package
Download the package from the [release](https://github.com/ISCAS-SSG/Ripple/releases), and extract files.

For Ripple-Server:
```shell
mkdir -p ripple-server
tar -zxvf ripple-server-v0.1.0.tar.gz -C ripple-server
```

For Ripple-Client
```shell
mkdir -p ripple-client
tar -zxvf ripple-client-v0.1.0.tar.gz -C ripple-client
```

### Start the servers
Run the following command to start a 3-node server cluster in a machine. Please confirm that specific ports are allowed by the firewall.
```shell
cd ripple-server
# Node 1
java -jar -Did=1 -Daddress="127.0.0.1" -DapiPort=3001 -DuiPort=4001 -DstorageLocation=node-1.db -Dnodes="1:127.0.0.1:3001,2:127.0.0.1:3002,3:127.0.0.1:3003" -Dprotocol=hashing ripple-server.jar &
# Node 2
java -jar -Did=2 -Daddress="127.0.0.1" -DapiPort=3002 -DuiPort=4002 -DstorageLocation=node-2.db -Dnodes="1:127.0.0.1:3001,2:127.0.0.1:3002,3:127.0.0.1:3003" -Dprotocol=hashing ripple-server.jar &
# Node 3
java -jar -Did=3 -Daddress="127.0.0.1" -DapiPort=3003 -DuiPort=4003 -DstorageLocation=node-3.db -Dnodes="1:127.0.0.1:3001,2:127.0.0.1:3002,3:127.0.0.1:3003" -Dprotocol=hashing ripple-server.jar &
```

Simply use `kill` command or `Ctrl+C` to stop the server.

The web-based console are located in: `http://127.0.0.1:4001`, `http://127.0.0.1:4002`, `http://127.0.0.1:4003`, which can be used to manage configurations.

### Start the clients
Run the following command to start a client connected to the server cluster above.
```shell
cd ripple-client
java -jar -Daddress="127.0.0.1" -DapiPort=5001 -DuiPort=6001 -DstorageLocation=client-1.db -Dnodes="1:127.0.0.1:3001,2:127.0.0.1:3002,3:127.0.0.1:3003" -DnodeSelector=loadbalance ripple-client.jar
```

Simply use `kill` command or `Ctrl+C` to stop the client.

The web-based console is located in: `http://127.0.0.1:6001`, which can be used to manage configurations.

> Known issues: As Ripple simply binds IP address to `0.0.0.0:port`, please confirm that there is only one active IP address for the node or provide correct IP address in the node list. A simple way is to disable other network connections in OS.

### Publish/Subscribe
Simply use the web-based console of the client to subscribe/unsubscribe specific topics, providing the application name and the key.

After subscribing, you may add/modify configuration from the web-based console of servers or clients.

## Build from the source

### Requirements
1. JDK >= 1.8.0

See [Quick start](#requirements) for instructions.

2. Maven

Download and extract files.
```shell
wget https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz
tar -zxvf apache-maven-3.9.9-bin.tar.gz
mv apache-maven-3.9.9 /usr/local/
```

Add the `bin` directory of the created directory `apache-maven-3.9.9` to the `PATH` environment variable. For example, edit `/etc/profile` and add the following lines.
```shell
export M2_HOME=/usr/local/apache-maven-3.9.9
export PATH=$PATH:$M2_HOME/bin
```

Start a new shell and confirm with `mvn -v` command.

### Build the project
1. Clone project
```shell
git clone https://github.com/ISCAS-SSG/Ripple.git
```

2. Run `mvn install` to compile and install to local maven repository.
```shell
cd Ripple
mvn install
```
Then add the dependency of `ripple-server` or `ripple-client` in `pom.xml` to use in embedded mode.

3. Run `mvn package` in `ripple-server` or `ripple-client` to compile and make archives of the components. For example, to build `ripple-server` and generate the archive:
``` shell
cd Ripple/ripple-server
mvn package
cp target/ripple-server-publish.tar.gz ~/
```

## Deployment

`ripple-server` and `ripple-client` can be run in standalone and embedded modes.

### Deploy the server in standalone mode
1. Execute `mvn package` in `ripple-server` to generate the package, or download from the [release](https://github.com/ISCAS-SSG/Ripple/releases).
2. Extract files from the archive.
3. Use the following command to set arguments and start a Ripple server.
```shell
java -jar -Darg1=value1 -Darg2=value2 ripple-server.jar
```

The arguments supported are:

- (Required) Use the `-Did=[id]` argument to specify the id for different nodes.
- (Required) Use the `-Daddress=[address]` argument to specify the local address to bind.
- (Required) Use the `-Dnodes=[nodes]` argument to specify the node list inside the cluster. The address of a node is given by the format `id:address:port`, and addresses are split by commas.
- (Optional) Use the `-DapiPort=[apiPort]` argument to specify the port used by Ripple.
- (Optional) Use the `-DuiPort=[uiPort]` argument to specify the port of Web console.
- (Optional) Use the `-DstorageLocation=[storageLocation]` argument to specify the location of persistent storage.
- (Optional) Use the `-Dprotocol=[protocol]` argument to specify the protocol.

For example, to start a cluster of 3 nodes in a single machine (127.0.0.1), using consistent hashing based protocol:
```shell
# Node 1
java -jar -Did=1 -DapiPort=3001 -DuiPort=4001 -DstorageLocation=node-1.db -Dnodes="1:127.0.0.1:3001,2:127.0.0.1:3002,3:127.0.0.1:3003" -Dprotocol=hashing ripple-server.jar
# Node 2
java -jar -Did=2 -DapiPort=3002 -DuiPort=4002 -DstorageLocation=node-2.db -Dnodes="1:127.0.0.1:3001,2:127.0.0.1:3002,3:127.0.0.1:3003" -Dprotocol=hashing ripple-server.jar
# Node 3
java -jar -Did=3 -DapiPort=3003 -DuiPort=4003 -DstorageLocation=node-3.db -Dnodes="1:127.0.0.1:3001,2:127.0.0.1:3002,3:127.0.0.1:3003" -Dprotocol=hashing ripple-server.jar
```

A simple Web console is provided via `uiPort` and can be used for publishing/subscribing. Use `Ctrl+C` to stop the server.

### Use the server in embedded mode
1. Add dependency in `pom.xml`
```xml
<dependency>
    <groupId>ripple</groupId>
    <artifactId>ripple-server</artifactId>
    <version>1.0.0</version>
</dependency>
```
2. Creating the instance of `RippleServer`, remember to call `initCluster` method after the node list is changed. Call different static methods of `RippleServer` to use different protocols. For example, to create a local cluster of 3 nodes with random ports and consistent hashing based protocol:
```java
int SERVER_COUNT = 3;
String DATABASE_PATH = "/root/ripple-sever-storage";
Files.createDirectories(Paths.get(DATABASE_PATH));

List<RippleServer> serverList = new ArrayList<>();
List<NodeMetadata> nodeList = new ArrayList<>();

int i = 0;
for (i = 0; i < SERVER_COUNT; i++) {
    int serverId = i + 1;
    String storageLocation = DATABASE_PATH + "/server-" + serverId + ".db";
    RippleServer rippleServer = RippleServer.hashingBasedProtocol(serverId, storageLocation, "127.0.0.1", new ModHashing());
    
    rippleServer.start();
    serverList.add(rippleServer);
    System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
        + "Node " + rippleServer.getId() + ": " + rippleServer.getAddress() + ", API port = " + rippleServer.getApiPort() + ", UI port = " + rippleServer.getUiPort());
    nodeList.add(new NodeMetadata(serverList.get(i).getId(), serverList.get(i).getAddress(), serverList.get(i).getApiPort()));
}

for (i = 0; i < SERVER_COUNT; i++) {
    serverList.get(i).initCluster(nodeList);
}
```
3. Use the `getApiPort` and `getUiPort` methods to get the API port and the UI port if they are set randomly.
4. Call the `stop` method to stop the server.

### Deploy the client in standalone mode
1. Execute `mvn package` in `ripple-client` to generate the package, or download from the [release](https://github.com/ISCAS-SSG/Ripple/releases).
2. Extract files from the archive.
3. Use the following command to set arguments and start a Ripple server.
```shell
java -jar -Darg1=value1 -Darg2=value2 ripple-client.jar
```

The arguments supported are:
- (Required) Use the `-Dnodes=[nodes]` argument to specify the node list inside the server cluster. The address of a node is given by the format `id:address:port`, and addresses are split by commas.
- (Required) Use the `-Daddress=[address]` argument to specify the local address to bind.
- (Optional) Use the `-DapiPort=[apiPort]` argument to specify the port used by Ripple client.
- (Optional) Use the `-DuiPort=[uiPort]` argument to specify the port of Web console.
- (Optional) Use the `-DstorageLocation=[storageLocation]` argument to specify the location of persistent storage.
- (Optional) Use the `-DnodeSelector=[nodeSelector]` argument to specify the node selector.

For example, to start a client connected to the 3-node server cluster and use the load balanced consistent hashing based node selector:
```shell
java -jar -Daddress="127.0.0.1" -DapiPort=5001 -DuiPort=6001 -DstorageLocation=client-1.db -Dnodes="1:127.0.0.1:3001,2:127.0.0.1:3002,3:127.0.0.1:3003" -DnodeSelector=loadbalance ripple-client.jar
```

A simple Web console is provided via `uiPort` and can be used for publishing/subscribing. Use `Ctrl+C` to stop the client.

### Use the client in embedded mode
1. Add dependency in `pom.xml`
```xml
<dependency>
    <groupId>ripple</groupId>
    <artifactId>ripple-client</artifactId>
    <version>1.0.0</version>
</dependency>
```
2. Create the instance of `RippleClient` by calling the constructor and give the list of server nodes and the storage location. For example, to create a client connected to the server cluster with 3 nodes (`127.0.0.1:3001`, `127.0.0.1:3002`, `127.0.0.1:3003`), and use the consistent hashing based node selector:
```java
String DATABASE_PATH = "/root/ripple-client-storage";
Files.createDirectories(Paths.get(DATABASE_PATH));

List<NodeMetadata> nodeList = new ArrayList<>();
nodeList.add(new NodeMetadata(1,"127.0.0.1",3001));
nodeList.add(new NodeMetadata(2,"127.0.0.1",3002));
nodeList.add(new NodeMetadata(3,"127.0.0.1",3003));
String storageLocation = DATABASE_PATH + "/client.db";
RippleClient rippleClient = new RippleClient(nodeList, new HashingBasedSelector(new ModHashing(6, 200)), storageLocation, "127.0.0.1");
rippleClient.start();
```
3. Call the `subscribe()`, `unsubscribe()`, `get()`, `put()` or `delete()` for publishing and subscribing.
- Initialize the connection
```java
rippleClient.findOrConnectToServer("testApp", "testKey");
```
- Subscribe
```java
rippleClient.subscribe("testApp", "testKey");
```
- Unsubscribe
```java
rippleClient.unsubscribe("testApp", "testKey");
```
- Get
```java
rippleClient.get("testApp","testKey");
```
- Put
```java
rippleClient.put("testApp", "testKey", "testValue");
```
- Delete
```java
rippleClient.delete("testApp","testKey");
```
- Increamental update
```java
UUID baseMessageUuid = UUID.fromString("uuid-to-base-message");
rippleClient.incrementalUpdate("testApp", "testKey", baseMessageUuid, Constants.ATOMIC_OPERATION_ADD_ENTRY, "newEntry");
```
4. `ripple-client` also has a simple Web console. Please use the `getUiPort` methods to get the port if it is set randomly.
5. Call the `stop` method to stop the client.

## Benchmarking

### Preparing the server cluster
It is highly recommended to deploy only one server node in a physical machine/VM. Different servers need to be initialized with different ids. Also, you may clean up persistent storage for each round of test and suppress logs by passing the argument `-Dorg.slf4j.simpleLogger.defaultLogLevel=ERROR`.

For example, run the following command to start the node-1 of a 10-node server cluster in different VMs from `192.168.0.1` to `192.168.0.10`. For other nodes, simply change the `-Did` argument.

```shell
rm -rf database.sqlite && java -jar -Did=1 -DapiPort=3001 -DuiPort=4001 -Dnodes="1:192.168.0.1:3001,2:192.168.0.2:3001,3:192.168.0.3:3001,4:192.168.0.4:3001,5:192.168.0.5:3001,6:192.168.0.6:3001,7:192.168.0.7:3001,8:192.168.0.8:3001,9:192.168.0.9:3001,10:192.168.0.10:3001" -Dorg.slf4j.simpleLogger.defaultLogLevel=ERROR ripple-server.jar
```

### Creating clients
You can use the thread pool to create and maintain multiple clients. For example, to create 100 clients connected to the server cluster created above.
```java
String DATABASE_PATH = "/path/to/ripple-test-dir";
List<NodeMetadata> CLUSTER_VM_LAB = new ArrayList<>(Arrays.asList(
        new NodeMetadata(1, "192.168.0.1", 3001)
        , new NodeMetadata(2, "192.168.0.2", 3001)
        , new NodeMetadata(3, "192.168.0.3", 3001)
        , new NodeMetadata(4, "192.168.0.4", 3001)
        , new NodeMetadata(5, "192.168.0.5", 3001)
        , new NodeMetadata(6, "192.168.0.6", 3001)
        , new NodeMetadata(7, "192.168.0.7", 3001)
        , new NodeMetadata(8, "192.168.0.8", 3001)
        , new NodeMetadata(9, "192.168.0.9", 3001)
        , new NodeMetadata(10, "192.168.0.10", 3001)));

System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");
int serverClusterSize = 10;
int clientClusterSize = 100;

int i = 0;
List<RippleClient> rippleClients = new ArrayList<>();

ExecutorService pool = Executors.newFixedThreadPool(clientClusterSize);
for (i = 0; i < clientClusterSize; i++) {
    Files.createDirectories(Paths.get(DATABASE_PATH));
    String storageLocation = DATABASE_PATH + "/" + UUID.randomUUID().toString() + ".db";
    RippleClient rippleClient = new RippleClient(CLUSTER_VM_LAB.subList(0, serverClusterSize), new HashingBasedSelector(new ModHashing(6, 200)), storageLocation, "127.0.0.1");
    pool.submit(new StartTask(rippleClient, i + 1));
    rippleClients.add(rippleClient);
}
pool.shutdown();
```

The `StartTask` is used to start the client.
```java
class StartTask implements Callable<Void> {
    private RippleClient client;
    private int id;

    public StartTask(RippleClient client, int id) {
        this.client = client;
        this.id = id;
    }

    @Override
    public Void call() throws Exception {
        this.client.start();
        System.out.println("Started: " + this.id);
        return null;
    }
}
```

Then, you may control specific clients in the `rippleClients` and act as the publishers, subscribers, or the admin clients.

### Measuring the latency
You may record the timestamp of sending requests as a part of the payload, and calcualte the latency in the handlers of the clients. For example, to randomly select a client from the cluster, creating a value with timestamp, and the publish it in the speed of 5 req/s, lasting for 120 seconds.

```java
int qps = 5;
int duration = 120;
int payloadSize = 1024;
int sleepTime = 1000 / qps;
Random random = new Random();
int i = 0;
for (i = 0; i < duration * qps; i++) {
    RippleClient client = clientCluster.get(random.nextInt(clientCluster.size()));
    long currentTime = System.currentTimeMillis();
    String value = (currentTime - startTime) + " " + (int) Math.floor((currentTime - startTime + 0.0) / 1000) + " " + currentTime + " " + generatePayload(payloadSize);
    client.put("testApp", "test", value);
    Thread.sleep(sleepTime);
}
```

Modify the `ripple.client.core.tcp.handler.DispatchRequestHandler` and `ripple.client.core.tcp.handler.SyncRequestHandler` to extract the timestamp from the message for round-one and round-two of message delivery. For example, add the following code to the `DispatchRequestHandler` to output the timestamp and latency to the console.

```java
// For logging
try {
    boolean loadTestEnabled = true;
    if (loadTestEnabled) {
        long endTime = System.currentTimeMillis();
        String[] source = dispatchRequest.getValue().split(" ");
        long startTime = Long.parseLong(source[2]);
        System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                + "] Received: " + source[0] + "," + source[1] + "," + (endTime - startTime) + "ms. From DISPATCH.");
    }
} catch (NumberFormatException ignored) {

}
```

Finally, save the console output to file for further analysis.

### Analyzing

You can simply extract latency from the console output and save the timestamp and latency in a csv format.
```java
List<String> pushing = new ArrayList<>();
pushing.add("timestamp,second,latency");
for (String line : content) {
    if (line.contains("] Received: ")) {
        pushing.add(line.substring(line.indexOf("] ") + 2, line.indexOf("ms")).replace("Received: ", ""));
    }
}
```

Then it can be used to calculate the max/min/average latency for each second.
