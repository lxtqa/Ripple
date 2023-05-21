// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.client.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.common.entity.NodeMetadata;
import ripple.common.hashing.Hashing;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class LoadBalancedSelector implements NodeSelector {
    private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);
    private Hashing hashing;
    private RippleClient rippleClient;
    private double backupRatio;
    private double cpuThreshold;

    public Hashing getHashing() {
        return hashing;
    }

    private void setHashing(Hashing hashing) {
        this.hashing = hashing;
    }

    public RippleClient getRippleClient() {
        return rippleClient;
    }

    public void setRippleClient(RippleClient rippleClient) {
        this.rippleClient = rippleClient;
    }

    public double getBackupRatio() {
        return backupRatio;
    }

    private void setBackupRatio(double backupRatio) {
        this.backupRatio = backupRatio;
    }

    public double getCpuThreshold() {
        return cpuThreshold;
    }

    private void setCpuThreshold(double cpuThreshold) {
        this.cpuThreshold = cpuThreshold;
    }

    public LoadBalancedSelector(Hashing hashing, double backupRatio, double cpuThreshold) {
        this.setHashing(hashing);
        this.setBackupRatio(backupRatio);
        this.setCpuThreshold(cpuThreshold);
    }

    //  Randomly select
    @Override
    public NodeMetadata selectNodeToConnect(String applicationName, String key, List<NodeMetadata> nodeList) {
        List<NodeMetadata> totalList = this.getHashing().hashing(applicationName, key, nodeList);
        boolean backupEnabled = false;
        int lastIndex = (int) Math.ceil(totalList.size() * (1 - this.getBackupRatio()));
        if (lastIndex >= totalList.size()) {
            lastIndex = totalList.size() - 1;
        }
        int i = 0;
        for (i = 0; i < lastIndex; i++) {
            NodeMetadata nodeMetadata = totalList.get(i);
            if (this.getRippleClient().getServerCpuUsage().containsKey(nodeMetadata)) {
                if (this.getRippleClient().getServerCpuUsage().get(nodeMetadata) > this.getCpuThreshold()) {
                    LOGGER.info("[LoadBalancedSelector] Server {} is overload. Enabling backup nodes.", nodeMetadata.getId());
                    backupEnabled = true;
                    break;
                }
            }
        }
        List<NodeMetadata> candidates = backupEnabled ? totalList : totalList.subList(0, lastIndex);
        int index = (int) (Math.random() * candidates.size());
        return candidates.get(index);
    }
}
