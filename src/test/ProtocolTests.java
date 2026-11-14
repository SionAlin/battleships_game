package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.network.protocol.*;
import main.model.ShotResult;

public class ProtocolTests{

    @Test
    public void Message_Test() throws Exception{
        Message joinRequest = Message.joinRequest("GuestName");
        assertEquals(MessageType.JOIN_REQUEST, joinRequest.getType(), "Join Request Type Test: Passed");
        assertEquals("GuestName", joinRequest.getName(), "Join Request Name Test: Passed");

        Message joinAccepted = Message.joinAccepted("HostName", Role.HOST);
        assertEquals(MessageType.JOIN_ACCEPTED, joinAccepted.getType(), "Join Accepted Type Test: Passed");
        assertEquals("HostName", joinAccepted.getName(), "Join Accepted Name Test: Passed");
        assertEquals(Role.HOST, joinAccepted.getFirstPlayer(), "Join Accepted Role Test: Passed");
        
        Message joinRejected = Message.joinRejected();
        assertEquals(MessageType.JOIN_REJECTED, joinRejected.getType(), "Join Rejected Type Test: Passed");

        Message shipsReady = Message.shipsReady();
        assertEquals(MessageType.SHIPS_READY, shipsReady.getType(), "Ships Ready Type Test: Passed");

        Message attack = Message.attack(3, 2);
        assertEquals(MessageType.ATTACK, attack.getType(), "Attack Type Test: Passed");
        assertEquals(3, attack.getRow(), "Attack Row Test: Passed");
        assertEquals(2, attack.getCol(), "Attack Col Test: Passed");

        Message attackResult = Message.attackResult(3, 2, ShotResult.HIT, false);
        assertEquals(MessageType.ATTACK_RESULT, attackResult.getType(), "Attack Result Type Test: Passed");
        assertEquals(3, attackResult.getRow(), "Attack Result Row Test: Passed");
        assertEquals(2, attackResult.getCol(), "Attack Result Col Test: Passed");
        assertEquals(ShotResult.HIT, attackResult.getResult(), "Attack Result Result Test: Passed");
        assertEquals(false, attackResult.isGameOver(), "Attack Result Game State Test: Passed");

        Message boardReveal = Message.boardReveal("0010...");
        assertEquals(MessageType.BOARD_REVEAL, boardReveal.getType(), "Board Reveal Type Test: Passed");
        assertEquals("0010...", boardReveal.getLayout(), "Board Reveal Layout Test: Passed");
    }

    @Test
    public void MessageSerializer_Test() throws Exception{
        String joinRequestSerialize = MessageSerializer.serialize(Message.joinRequest("  \nGuest Name  "));
        assertEquals("JOIN_REQUEST Guest Name", joinRequestSerialize, "Join Request & clean Tests: Passed");

        String joinAcceptedSerialize = MessageSerializer.serialize(Message.joinAccepted("HostName", Role.HOST));
        assertEquals("JOIN_ACCEPTED HOST HostName", joinAcceptedSerialize, "Join Accepted & Role Tests: Passed");

        String joinRejectedSerialize = MessageSerializer.serialize(Message.joinRejected());
        assertEquals("JOIN_REJECTED", joinRejectedSerialize, "Join Rejected Test: Passed");

        String shipsReadySerialize = MessageSerializer.serialize(Message.shipsReady());
        assertEquals("SHIPS_READY", shipsReadySerialize, "Ships Ready Test: Passed");

        String attackSerialize = MessageSerializer.serialize(Message.attack(3, 2));
        assertEquals("ATTACK 3 2", attackSerialize, "Attack Test: Passed");

        String attackResultSerialize = MessageSerializer.serialize(Message.attackResult(3, 2, ShotResult.HIT, false));
        assertEquals("ATTACK_RESULT 3 2 HIT 0", attackResultSerialize, "Attack Result Test: Passed");

        String layout = "0".repeat(95) + "11111";
        String boardRevealSerialize = MessageSerializer.serialize(Message.boardReveal(layout));
        assertEquals("BOARD_REVEAL " + layout, boardRevealSerialize, "Board Reveal Test: Passed");

        Message joinRequest = MessageSerializer.parse(joinRequestSerialize);
        assertEquals(MessageType.JOIN_REQUEST, joinRequest.getType(), "Join Request Type Test: Passed");
        assertEquals("Guest Name", joinRequest.getName(), "Join Request Name Test: Passed");

        Message joinAccepted = MessageSerializer.parse(joinAcceptedSerialize);
        assertEquals(MessageType.JOIN_ACCEPTED, joinAccepted.getType(), "Join Accepted Type Test: Passed");
        assertEquals("HostName", joinAccepted.getName(), "Join Accepted Name Test: Passed");
        assertEquals(Role.HOST, joinAccepted.getFirstPlayer(), "Join Accepted Role Test: Passed");
        
        Message joinRejected = MessageSerializer.parse(joinRejectedSerialize);
        assertEquals(MessageType.JOIN_REJECTED, joinRejected.getType(), "Join Rejected Type Test: Passed");

        Message shipsReady = MessageSerializer.parse(shipsReadySerialize);
        assertEquals(MessageType.SHIPS_READY, shipsReady.getType(), "Ships Ready Type Test: Passed");

        Message attack = MessageSerializer.parse(attackSerialize);
        assertEquals(MessageType.ATTACK, attack.getType(), "Attack Type Test: Passed");
        assertEquals(3, attack.getRow(), "Attack Row Test: Passed");
        assertEquals(2, attack.getCol(), "Attack Col Test: Passed");

        Message attackResult = MessageSerializer.parse(attackResultSerialize);
        assertEquals(MessageType.ATTACK_RESULT, attackResult.getType(), "Attack Result Type Test: Passed");
        assertEquals(3, attackResult.getRow(), "Attack Result Row Test: Passed");
        assertEquals(2, attackResult.getCol(), "Attack Result Col Test: Passed");
        assertEquals(ShotResult.HIT, attackResult.getResult(), "Attack Result Result Test: Passed");
        assertEquals(false, attackResult.isGameOver(), "Attack Result Game State Test: Passed");

        Message boardReveal = MessageSerializer.parse(boardRevealSerialize);
        assertEquals(MessageType.BOARD_REVEAL, boardReveal.getType(), "Board Reveal Type Test: Passed");
        assertEquals(layout, boardReveal.getLayout(), "Board Reveal Layout Test: Passed");
    }

    @Test
    public void MessageSerializer_InvalidInput_Test(){
        assertThrows(IllegalArgumentException.class, () -> MessageSerializer.parse("BOARD_REVEAL 001001010...."));
        assertThrows(IllegalArgumentException.class, () -> MessageSerializer.parse("BOARD_REVEAL " + "0".repeat(99)));
        assertThrows(IllegalArgumentException.class, () -> MessageSerializer.parse("BOARD_REVEAL " + "2".repeat(100)));
        assertThrows(IllegalArgumentException.class, () -> MessageSerializer.parse("NOPE 1 2"));
        assertThrows(IllegalArgumentException.class, () -> MessageSerializer.parse("ATTACK 10 2"));
        assertThrows(IllegalArgumentException.class, () -> MessageSerializer.parse("ATTACK x 2"));
        assertThrows(IllegalArgumentException.class, () -> MessageSerializer.parse("ATTACK 3"));
        assertThrows(IllegalArgumentException.class, () -> MessageSerializer.parse(""));
        assertThrows(IllegalArgumentException.class, () -> MessageSerializer.parse("JOIN_ACCEPTED HOST"));
    }

}
