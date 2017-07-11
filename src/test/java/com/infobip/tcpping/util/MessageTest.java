package com.infobip.tcpping.util;

import com.infobip.tcpping.Message;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by Пользователь on 09.07.2017.
 */
public class MessageTest {

    String clientStringMessage = "0000001234#001234567890123#               #message text";
    String replyStringMessage = "0000001234#001234567890123#001234567890234#message text";

    @Test
    public void testToString() {
        Message message = new Message(1234, 1234567890123L, "message text");
        assertEquals(clientStringMessage, message.toString());
    }

    @Test
    public void testCreateClientMessageFromString() {
        Message message = Message.createMessageFromString(clientStringMessage);
        assertEquals(1234, message.getNumber());
        assertEquals(1234567890123L, message.getCreationTime());
        assertEquals(-1, message.getReplyTime());
    }

    @Test
    public void testCreateReplyMessageFromString() {
        Message message = Message.createMessageFromString(replyStringMessage);
        assertEquals(1234, message.getNumber());
        assertEquals(1234567890123L, message.getCreationTime());
        assertEquals(1234567890234L, message.getReplyTime());
    }
}
