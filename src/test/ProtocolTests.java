package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.network.protocol.*;
import main.model.ShotResult;

public class ProtocolTests{

    @Test
    public void Message_Test() throws Exception{
        Message joinRequest = Message.joinRequest("GuestName");
        assertEquals(MessageType.JOIN_REQUEST, joinRequest.getType(), "Join Request Type Test");
        assertEquals("GuestName", joinRequest.getName(), "Join Request Name Test");

        Message joinAccepted = Message.joinAccepted("HostName", Role.HOST);
        assertEquals(MessageType.JOIN_ACCEPTED, joinAccepted.getType(), "Join Accepted Type Test");
        assertEquals("HostName", joinAccepted.getName(), "Join Accepted Name Test");
        assertEquals(Role.HOST, joinAccepted.getFirstPlayer(), "Join Accepted Role Test");
        
        Message joinRejected = Message.joinRejected();
        assertEquals(MessageType.JOIN_REJECTED, joinRejected.getType(), "Join Rejected Type Test");

        Message shipsReady = Message.shipsReady();
        assertEquals(MessageType.SHIPS_READY, shipsReady.getType(), "Ships Ready Type Test");

        Message attack = Message.attack(3, 2);
        assertEquals(MessageType.ATTACK, attack.getType(), "Attack Type Test");
        assertEquals(3, attack.getRow(), "Attack Row Test");
        assertEquals(2, attack.getCol(), "Attack Col Test");

        Message attackResult = Message.attackResult(3, 2, ShotResult.HIT, false);
        assertEquals(MessageType.ATTACK_RESULT, attackResult.getType(), "Attack Result Type Test");
        assertEquals(3, attackResult.getRow(), "Attack Result Row Test");
        assertEquals(2, attackResult.getCol(), "Attack Result Col Test");
        assertEquals(ShotResult.HIT, attackResult.getResult(), "Attack Result Result Test");
        assertEquals(false, attackResult.isGameOver(), "Attack Result Game State Test");

        String layout = "0".repeat(95) + "11111";
        Message boardReveal = Message.boardReveal(layout);
        assertEquals(MessageType.BOARD_REVEAL, boardReveal.getType(), "Board Reveal Type Test");
        assertEquals(layout, boardReveal.getLayout(), "Board Reveal Layout Test");
    }

    @Test
    public void MessageSerializer_Test() throws Exception{
        String joinRequestSerialize = MessageSerializer.serialize(Message.joinRequest("  \nGuest Name  "));
        assertEquals("JOIN_REQUEST Guest Name", joinRequestSerialize, "Join Request & clean Tests");

        String joinAcceptedSerialize = MessageSerializer.serialize(Message.joinAccepted("HostName", Role.HOST));
        assertEquals("JOIN_ACCEPTED HOST HostName", joinAcceptedSerialize, "Join Accepted & Role Tests");

        String joinRejectedSerialize = MessageSerializer.serialize(Message.joinRejected());
        assertEquals("JOIN_REJECTED", joinRejectedSerialize, "Join Rejected Test");

        String shipsReadySerialize = MessageSerializer.serialize(Message.shipsReady());
        assertEquals("SHIPS_READY", shipsReadySerialize, "Ships Ready Test");

        String attackSerialize = MessageSerializer.serialize(Message.attack(3, 2));
        assertEquals("ATTACK 3 2", attackSerialize, "Attack Test");

        String attackResultSerialize = MessageSerializer.serialize(Message.attackResult(3, 2, ShotResult.HIT, false));
        assertEquals("ATTACK_RESULT 3 2 HIT 0", attackResultSerialize, "Attack Result Test");

        String layout = "0".repeat(95) + "11111";
        String boardRevealSerialize = MessageSerializer.serialize(Message.boardReveal(layout));
        assertEquals("BOARD_REVEAL " + layout, boardRevealSerialize, "Board Reveal Test");

        Message joinRequest = MessageSerializer.parse(joinRequestSerialize);
        assertEquals(MessageType.JOIN_REQUEST, joinRequest.getType(), "Join Request Type Test");
        assertEquals("Guest Name", joinRequest.getName(), "Join Request Name Test");

        Message joinAccepted = MessageSerializer.parse(joinAcceptedSerialize);
        assertEquals(MessageType.JOIN_ACCEPTED, joinAccepted.getType(), "Join Accepted Type Test");
        assertEquals("HostName", joinAccepted.getName(), "Join Accepted Name Test");
        assertEquals(Role.HOST, joinAccepted.getFirstPlayer(), "Join Accepted Role Test");
        
        Message joinRejected = MessageSerializer.parse(joinRejectedSerialize);
        assertEquals(MessageType.JOIN_REJECTED, joinRejected.getType(), "Join Rejected Type Test");

        Message shipsReady = MessageSerializer.parse(shipsReadySerialize);
        assertEquals(MessageType.SHIPS_READY, shipsReady.getType(), "Ships Ready Type Test");

        Message attack = MessageSerializer.parse(attackSerialize);
        assertEquals(MessageType.ATTACK, attack.getType(), "Attack Type Test");
        assertEquals(3, attack.getRow(), "Attack Row Test");
        assertEquals(2, attack.getCol(), "Attack Col Test");

        Message attackResult = MessageSerializer.parse(attackResultSerialize);
        assertEquals(MessageType.ATTACK_RESULT, attackResult.getType(), "Attack Result Type Test");
        assertEquals(3, attackResult.getRow(), "Attack Result Row Test");
        assertEquals(2, attackResult.getCol(), "Attack Result Col Test");
        assertEquals(ShotResult.HIT, attackResult.getResult(), "Attack Result Result Test");
        assertEquals(false, attackResult.isGameOver(), "Attack Result Game State Test");

        Message boardReveal = MessageSerializer.parse(boardRevealSerialize);
        assertEquals(MessageType.BOARD_REVEAL, boardReveal.getType(), "Board Reveal Type Test");
        assertEquals(layout, boardReveal.getLayout(), "Board Reveal Layout Test");
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
