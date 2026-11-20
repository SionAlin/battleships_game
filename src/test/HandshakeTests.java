package test;

import main.network.*;
import main.network.handshake.*;
import main.network.protocol.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.*;
import java.util.concurrent.*;

public class HandshakeTests{
    
    @Test
    public void ExactlyOneSideStarts_Test(){
        HandshakeResult host = new HandshakeResult(null, Role.HOST, "Guest");
        HandshakeResult guest = new HandshakeResult(null, Role.GUEST, "Host");

        assertTrue(host.isStartFirst());
        assertFalse(guest.isStartFirst());
        assertEquals("Guest", host.getOpponentName());
    }
    
    private static class Outcome implements HandshakeListener{
        final CompletableFuture<Object> outcome = new CompletableFuture<>();
        @Override public void onSuccess(HandshakeResult r){ outcome.complete(r); }
        @Override public void onFailure(HandshakeFailure f){ outcome.complete(f); }
    }

    @Test
    @Timeout(10)
    public void HandshakeSucceeds_Test() throws Exception{
        try(ServerSocket server = new ServerSocket(0)){
            Socket guestSocket = new Socket("localhost", server.getLocalPort());
            Socket hostSocket = server.accept();

            Outcome hostOutcome = new Outcome();
            Outcome guestOutcome = new Outcome();
            HandshakeSession.asHost(new Connection(hostSocket), "HostName", 3000, hostOutcome).begin();
            HandshakeSession.asGuest(new Connection(guestSocket), "GuestName", 3000, guestOutcome).begin();

            Object h = hostOutcome.outcome.get(5, TimeUnit.SECONDS);
            Object g = guestOutcome.outcome.get(5, TimeUnit.SECONDS);
            assertTrue(h instanceof HandshakeResult, "host got: " + h);
            assertTrue(g instanceof HandshakeResult, "guest got: " + g);

            HandshakeResult host = (HandshakeResult) h;
            HandshakeResult guest = (HandshakeResult) g;
            assertEquals("GuestName", host.getOpponentName());
            assertEquals("HostName", guest.getOpponentName());
            assertNotEquals(host.isStartFirst(), guest.isStartFirst());
        }
    }

    @Test
    @Timeout(10)
    public void GuestTimesOut_Test() throws Exception{
        try(ServerSocket server = new ServerSocket(0)){
            Socket guestSocket = new Socket("localhost", server.getLocalPort());
            Socket silent = server.accept();

            Outcome o = new Outcome();
            HandshakeSession.asGuest(new Connection(guestSocket), "GuestName", 300, o).begin();

            assertEquals(HandshakeFailure.TIMEOUT, o.outcome.get(5, TimeUnit.SECONDS));
            silent.close();
        }
    }
}
