package test;

import main.network.*;
import main.network.protocol.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.*;
import java.util.concurrent.*;

@Timeout(10)
public class ConnectionTests{
    
    private ServerSocket server;
    private Connection a, b;
    private Recorder listenerA, listenerB;

    private static class Recorder implements ConnectionListener{
        final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();
        final CountDownLatch disconnected = new CountDownLatch(1);

        @Override
        public void onMessage(Message message){
            messages.add(message);
        }

        @Override
        public void onDisconnect(){
            disconnected.countDown();
        }
    }

    @BeforeEach
    public void setUp() throws Exception{
        server = new ServerSocket(0);
        Socket clientSocket = new Socket("localhost", server.getLocalPort());
        Socket acceptedSocket = server.accept();

        a = new Connection(clientSocket);
        b = new Connection(acceptedSocket);

        listenerA = new Recorder();
        listenerB = new Recorder();
        
        a.start(listenerA);
        b.start(listenerB);
    }

    @AfterEach
    public void tearDown() throws Exception{
        a.close();
        b.close();
        server.close();
    }

    @Test
    public void MessageArrives_Test() throws Exception{
        assertTrue(a.send(Message.attack(3,2)), "send reported failure");

        Message received = listenerB.messages.poll(2, TimeUnit.SECONDS);
        assertNotNull(received, "no message received within 2s");
        assertEquals(MessageType.ATTACK, received.getType());
        assertEquals(3, received.getRow());
        assertEquals(2, received.getCol());
    }

    @Test
    public void MessagesKeepOrder_Test() throws Exception{
        a.send(Message.attack(0, 0));
        a.send(Message.attack(1, 1));
        a.send(Message.shipsReady());

        Message first = listenerB.messages.poll(2, TimeUnit.SECONDS);
        Message second = listenerB.messages.poll(2, TimeUnit.SECONDS);
        Message third = listenerB.messages.poll(2, TimeUnit.SECONDS);

        assertNotNull(first);
        assertNotNull(second);
        assertNotNull(third);
        assertEquals(0, first.getRow());
        assertEquals(1, second.getRow());
        assertEquals(MessageType.SHIPS_READY, third.getType());
    }

    @Test
    public void BothDirections_Test() throws Exception{
        b.send(Message.joinAccepted("Host", Role.GUEST));

        Message received = listenerA.messages.poll(2, TimeUnit.SECONDS);
        assertNotNull(received);
        assertEquals("Host", received.getName());
        assertEquals(Role.GUEST, received.getFirstPlayer());
    }

    @Test
    public void DisconnectIsReported_Test() throws Exception{
        a.close();

        assertTrue(listenerB.disconnected.await(2, TimeUnit.SECONDS), "the other side was not notified");
        assertFalse(listenerA.disconnected.await(300, TimeUnit.MILLISECONDS), "the side that closed got a disconnect callback");
    }
}
