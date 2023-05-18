// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.client.core.ui;

import ripple.client.RippleClient;

import javax.servlet.http.HttpServlet;

/**
 * @author Zhen Tang
 */
public class BaseServlet extends HttpServlet {
    private RippleClient client;

    public BaseServlet(RippleClient client) {
        this.setClient(client);
    }

    public RippleClient getClient() {
        return client;
    }

    private void setClient(RippleClient client) {
        this.client = client;
    }
}
