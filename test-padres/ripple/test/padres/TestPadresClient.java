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
    public static long startTime;

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
        long endTime = System.nanoTime();
        if (msg instanceof PublicationMessage) {
            Publication pub = ((PublicationMessage) msg).getPublication();
            if (pub.getClassVal().equals("ERROR")) {
                System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis()))
                        + "] Client " + this.getClientID() + " Received an Error from " + msg.getLastHopID());
                System.out.println(pub.getPairMap().get("message"));
                return;
            }
        }

        System.out.println("[" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "] Received: " + (endTime - startTime + 0.00) / 1000 / 1000 + "ms");
    }
}
