# Ripple

A flexible pub-sub system for large scale clusters

## 1. Install

### 1.1 Requirements
#### JDK >= 1.8.0

For RHEL/CentOS/Fedora/openEuler
```shell
dnf install java-1.8.0-openjdk-devel
```

For Ubuntu/Debian
```shell
apt-get install openjdk-8-jdk
```

Confirm with `java -version` command.

#### Maven

Download and extract files
```shell
wget https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz
tar -zxvf apache-maven-3.9.9-bin.tar.gz
mv apache-maven-3.9.9 /usr/local/
```
Add the `bin` directory of the created directory apache-maven-3.9.9 to the PATH environment variable, for example, edit `/etc/profile` and add
```shell
export M2_HOME=/usr/local/apache-maven-3.9.9
export PATH=$PATH:$M2_HOME/bin
```

Start a new shell and confirm with `mvn -v` command.

### 1.2 Build project
1. Clone project
```shell
git clone https://github.com/ISCAS-SSG/Ripple.git
```

2. Use `mvn install` to compile and install to local maven repository.
```shell
cd Ripple
mvn install
```
Then add the dependency of `ripple-server` or `ripple-client` in `pom.xml` to use in embedded mode.

3. Use `mvn package` in `ripple-server` or `ripple-client` to compile and make archives of the components. For example, to build `ripple-server` and generate the archive:
``` shell
cd Ripple/ripple-server
mvn package
cp target/ripple-server-publish.tar.gz ~/
```

## 2. Quickstart

`ripple-server` and `ripple-client` support standalone and embedded modes.

### 2.1 Deploy the server in standalone mode
1. Execute `mvn package` in `ripple-server` to generate the package, or download from the release.
2. Extract files from `ripple-server-publish.tar.gz`
```shell
mkdir -p ripple-server
tar -zxvf ripple-server-publish.tar.gz -C ripple-server
```
3. Use the following command to set arguments and start a Ripple server.

For example, to start a one-node cluster:
```shell
java -jar -Did=1 -DapiPort=3001 -DuiPort=4001 -Dnodes="1:127.0.0.1:3001" ripple-server.jar
```
(Required) Use the `-Did=[id]` argument to specify the id for different nodees.

(Optional) Use the `-DapiPort=[apiPort]` argument to specify the port used by Ripple.

(Optional) Use the `-DuiPort=[uiPort]` argument to specify the port of Web console.

(Optional) Use the `-DstorageLocation=[storageLocation]` argument to specify the location of persistent storage.

(Optional) Use the `-Dprotocol=[protocol]` argument to specify the protocol.

(Required) Use the `-Dnodes=[nodes]` argument to specify the node list inside the cluster.
The address of a node is given by the format `id:address:port`, and addresses are split by commas.

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

### 2.2 Deploy the server in embedded mode
1. Add dependency in `pom.xml`
```xml
<dependency>
    <groupId>ripple</groupId>
    <artifactId>ripple-server</artifactId>
    <version>1.0.0</version>
</dependency>
```
2. Creating instance of `RippleServer`, remember to call `initCluster` method after the node list is changed. For example, to create a local cluster of 3 nodes with random ports and consistent hashing based protocol:
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
    RippleServer rippleServer = RippleServer.hashingBasedProtocol(serverId, storageLocation, new ModHashing());
    
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

### 2.3 Use the client in standalone mode
1. Execute `mvn package` in `ripple-client` to generate the package, or download from the release.
2. Extract files from `ripple-client-publish.tar.gz`
```shell
mkdir -p ripple-client
tar -zxvf ripple-client-publish.tar.gz -C ripple-client
```
1. Use the following command to set arguments and start a Ripple client. For example, to start a client connected to the local one-node cluster created in step 2.1:
```shell
java -jar -DapiPort=5001 -DuiPort=6001 -Dnodes="1:127.0.0.1:3001" ripple-client.jar
```
(Optional) Use the `-DapiPort=[apiPort]` argument to specify the port used by Ripple client.

(Optional) Use the `-DuiPort=[uiPort]` argument to specify the port of Web console.

(Optional) Use the `-DstorageLocation=[storageLocation]` argument to specify the location of persistent storage.

(Optional) Use the `-DnodeSelector=[nodeSelector]` argument to specify the node selector.

(Required) Use the `-Dnodes=[nodes]` argument to specify the node list inside the server cluster.
The address of a node is given by the format `id:address:port`, and addresses are split by commas.

For example, to start a client connected to the 3-node cluster created in step 2.1, and use the load balanced consistent hashing based node selector:
```shell
java -jar -DapiPort=5001 -DuiPort=6001 -DstorageLocation=client-1.db -Dnodes="1:127.0.0.1:3001,2:127.0.0.1:3002,3:127.0.0.1:3003" -DnodeSelector=loadbalance ripple-client.jar
```

A simple Web console is provided via `uiPort` and can be used for publishing/subscribing. Use `Ctrl+C` to stop the client.

### 2.4 Use the client in embedded mode
1. Add dependency in `pom.xml`
```xml
<dependency>
    <groupId>ripple</groupId>
    <artifactId>ripple-client</artifactId>
    <version>1.0.0</version>
</dependency>
```
2. Create the instance of `RippleClient` by calling the constructor and give the list of server nodes and the storage location. For example, to create a client connected to the server cluster with 3 nodes (127.0.0.1:3001, 127.0.0.1:3002, 127.0.0.1:3003), and use the consistent hashing based node selector:
```java
String DATABASE_PATH = "/root/ripple-client-storage";
Files.createDirectories(Paths.get(DATABASE_PATH));

List<NodeMetadata> nodeList = new ArrayList<>();
nodeList.add(new NodeMetadata(1,"127.0.0.1",3001));
nodeList.add(new NodeMetadata(2,"127.0.0.1",3002));
nodeList.add(new NodeMetadata(3,"127.0.0.1",3003));
String storageLocation = DATABASE_PATH + "/client.db";
RippleClient rippleClient = new RippleClient(nodeList, new HashingBasedSelector(new ModHashing(6, 200)), storageLocation);
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
