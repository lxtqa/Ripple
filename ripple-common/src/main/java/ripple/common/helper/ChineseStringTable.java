// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.helper;

/**
 * @author Zhen Tang
 */
public class ChineseStringTable implements StringTable {
    @Override
    public String applicationName() {
        return "应用名称";
    }

    @Override
    public String key() {
        return "键";
    }

    @Override
    public String value() {
        return "值";
    }

    @Override
    public String addConfig() {
        return "添加配置";
    }

    @Override
    public String htmlLanguage() {
        return "zh-CN";
    }

    @Override
    public String operationSubmitted() {
        return "操作已成功提交。";
    }

    @Override
    public String addSubscription() {
        return "订阅新配置";
    }

    @Override
    public String messageTypeDelete() {
        return "删除";
    }

    @Override
    public String messageTypeUpdate() {
        return "更新";
    }

    @Override
    public String messageTypeIncrementalUpdate() {
        return "增量更新";
    }

    @Override
    public String getConfig() {
        return "查询配置";
    }

    @Override
    public String totalNumberOfConfiguration() {
        return "本地存储中的配置总数为";
    }

    @Override
    public String home() {
        return "主页";
    }

    @Override
    public String configManagement() {
        return "配置管理";
    }

    @Override
    public String modifyConfig() {
        return "修改配置";
    }

    @Override
    public String incrementalUpdate() {
        return "增量更新";
    }

    @Override
    public String removeConfig() {
        return "删除配置";
    }

    @Override
    public String subscriptionManagement() {
        return "订阅管理";
    }

    @Override
    public String clientGetSubscription() {
        return "已订阅配置";
    }

    @Override
    public String removeSubscription() {
        return "取消订阅";
    }

    @Override
    public String basicInformation() {
        return "基本信息";
    }

    @Override
    public String connectedServer() {
        return "已连接的服务器";
    }

    @Override
    public String lineNumber() {
        return "序号";
    }

    @Override
    public String serverIpAddress() {
        return "服务器IP地址";
    }

    @Override
    public String serverApiPort() {
        return "服务器API端口号";
    }

    @Override
    public String serverUiPort() {
        return "服务器UI端口号";
    }

    @Override
    public String history() {
        return "历史记录";
    }

    @Override
    public String clientIpAddress() {
        return "客户端IP地址";
    }

    @Override
    public String clientApiPort() {
        return "客户端API端口号";
    }

    @Override
    public String clientUiPort() {
        return "客户端UI端口号";
    }

    @Override
    public String type() {
        return "类型";
    }

    @Override
    public String baseVersion() {
        return "基准版本";
    }

    @Override
    public String atomicOperation() {
        return "原子操作";
    }

    @Override
    public String lastUpdate() {
        return "最后修改时间";
    }

    @Override
    public String serverId() {
        return "服务器ID";
    }

    @Override
    public String clientNumberOfSubscription() {
        return "当前客户端节点订阅配置总数为";
    }

    @Override
    public String clientNumberOfServerConnection() {
        return "当前客户端节点服务器连接总数为";
    }

    @Override
    public String success() {
        return "操作已成功完成。";
    }

    @Override
    public String error() {
        return "出现错误，请重试。";
    }

    @Override
    public String serverGetSubscription() {
        return "查询订阅关系";
    }

    @Override
    public String clusterManagement() {
        return "集群管理";
    }

    @Override
    public String serverCluster() {
        return "服务器集群信息";
    }

    @Override
    public String connectedClients() {
        return "已连接的客户端";
    }

    @Override
    public String ackServer() {
        return "已推送到的服务器ID";
    }

    @Override
    public String totalNumberOfClients() {
        return "连接到当前服务器节点并订阅配置的客户端总数为";
    }

    @Override
    public String totalNumberOfSubscriptions() {
        return "当前服务器节点的订阅信息总数为";
    }

    @Override
    public String totalNumberOfServers() {
        return "当前服务器集群节点总数为";
    }
}
