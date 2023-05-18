// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.test.padres;

import ca.utoronto.msrg.padres.client.Client;
import ca.utoronto.msrg.padres.client.ClientConfig;
import ca.utoronto.msrg.padres.client.ClientException;
import ca.utoronto.msrg.padres.common.message.Message;
import ca.utoronto.msrg.padres.common.message.Publication;
import ca.utoronto.msrg.padres.common.message.PublicationMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestPadresClient extends Client {

    public TestPadresClient(String id) throws ClientException {
        super(id);
    }

    public TestPadresClient(String id, ClientConfig newConfig) throws ClientException {
        super(id, newConfig);
    }

    public TestPadresClient(ClientConfig newConfig) throws ClientException {
        super(newConfig);
    }

    public void processMessage(Message msg) {
        super.processMessage(msg);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        if (msg instanceof PublicationMessage) {
            Publication pub = ((PublicationMessage) msg).getPublication();
            if (pub.getClassVal().equals("ERROR")) {
                System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                        + "] Client " + this.getClientID() + " Received an Error from " + msg.getLastHopID());
                return;
            }
            // For logging
            boolean loadTestEnabled = true;
            if (loadTestEnabled) {
                String content = pub.getPairMap().get("content").toString();
                long endTime = System.currentTimeMillis();
                long startTime = Long.parseLong(content.substring(0, content.indexOf(" ")));
                System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                        + "] Received: " + (endTime - startTime) + "ms.");
            }
        }


    }
}
