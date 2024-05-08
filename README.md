# Ripple

A flexible pub-sub system for large scale clusters

## Install
- Use `mvn install` to compile and install to local maven repository.
- Use `mvn package` in separate modules (ripple-client or ripple-server) to compile and make archives of the server or the client.

## Quickstart

### Deploy the server
- Execute `mvn package` in `ripple-server` to generate the package.
- Extract files from `ripple-server-publish.tar.gz`
- Use the following command to start a Ripple server:
```shell
java -jar -Did=1 -DapiPort=3000 -DuiPort=3001 -Dnodes="1:192.168.1.1:3000" ripple-server.jar
```
Please give different `id` by using `-Did=[id]` for different nodes.

Use the `-DapiPort` argument to specify the port used by Ripple.

Use the `-DuiPort` argument to specify the port of Web console.

The node list inside the cluster is given by the argument `-Dnodes=[nodes]`.
The address of a nodes is given by the format `id:address:port`, and addresses are split by commas.

For example, to start a cluster of 3 nodes (192.168.2.21, 192.168.2.22, 192.168.2.23):
```shell
java -jar -Did=1 -DapiPort=3000 -Dnodes="1:192.168.2.21:3000,2:192.168.2.22:3000,3:192.168.2.23:3000" ripple-server.jar
```

### Use the client
Create the instance of `RippleClient` by calling the constructor and give the list of server nodes, the storage location, and the node selector (Hashing based or Load based).

Call the `subscribe()`, `unsubscribe()`, `get()`, `put()` or `delete()` for publishing and subscribing.