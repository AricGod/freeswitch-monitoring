package gr.gnostix.freeswitch;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;

/**
 * Created by rebel on 17/7/15.
 */
public class MyEslConnection {

    private Client conn = null;

    public MyEslConnection(ActorRef centralMessageRouter, ActorSystem system) {
        conn = new Client();
        try {
            conn.connect("localhost", 8021, "ClueCon", 60);
//            conn.connect("192.168.10.128", 8021, "ClueCon", 60);
//            conn.connect("fs-instance.com", 8021, "ClueCon", 60);

            if (conn.canSend() == true) System.out.println("connected");
            //conn.setEventSubscriptions( "plain", "CHANNEL_HANGUP_COMPLETE CHANNEL_CALLSTATE  CHANNEL_CREATE CHANNEL_EXECUTE CHANNEL_EXECUTE_COMPLETE CHANNEL_DESTROY" );
            //conn.setEventSubscriptions( "plain", "all" );
            conn.setEventSubscriptions( "plain", "CHANNEL_HANGUP_COMPLETE CHANNEL_ANSWER  HEARTBEAT" );
            conn.addEventListener(new MyEslEventListener(centralMessageRouter));

            // on failure catch the exception and don't start the CallRouter!!!
        } catch (InboundConnectionFailure e) {
            System.out.println("------- ESL connection failed. Actor System is shutting down !!");
            system.shutdown();
            e.printStackTrace();
        }
    }

    public void deinitConnection() {
        conn.close();
        conn = null;
    }
}
