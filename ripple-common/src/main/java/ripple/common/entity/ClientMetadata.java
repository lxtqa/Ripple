// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.entity;

import java.util.Objects;

/**
 * @author Zhen Tang
 */
public class ClientMetadata implements Comparable<ClientMetadata> {
    private String address;
    private int port;

    public ClientMetadata(String address, int port) {
        this.setAddress(address);
        this.setPort(port);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientMetadata clientMetadata = (ClientMetadata) o;
        return Objects.equals(address, clientMetadata.address) &&
                Objects.equals(port, clientMetadata.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }

    @Override
    public String toString() {
        return "ClientMetadata{" +
                "address='" + address + '\'' +
                ", port='" + port + '\'' +
                '}';
    }

    @Override
    public int compareTo(ClientMetadata o) {
        int result = this.getAddress().compareTo(o.getAddress());
        if (result == 0) {
            result = Integer.compare(this.getPort(), o.getPort());
        }
        return result;
    }
}
