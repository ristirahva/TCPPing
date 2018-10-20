package ru.home.tcpping.util;

import ru.home.tcpping.Message;
import org.junit.Test;
import ru.home.tcpping.exception.InvalidMessageFormatException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * Message management tests.
 */
public class MessageTest {

    String clientStringMessage = "0000001234#001234567890123#               #message text";
    String replyStringMessage = "0000001234#001234567890123#001234567890234#message text";

    String invalidFormattedMessage = "af3rflse3jd43";

    @Test
    public void testToString() {
        Message message = new Message(1234, 1234567890123L, "message text");
        assertEquals(clientStringMessage, message.toString());
    }

    @Test
    public void testCreateClientMessageFromString() {
        Message message = new Message(clientStringMessage);
        assertEquals(1234, message.getNumber());
        assertEquals(1234567890123L, message.getCreationTime());
        assertEquals(-1, message.getReplyTime());
    }

    @Test
    public void testCreateReplyMessageFromString() {
        Message message = new Message(replyStringMessage);
        assertEquals(1234, message.getNumber());
        assertEquals(1234567890123L, message.getCreationTime());
        assertEquals(1234567890234L, message.getReplyTime());
    }

    @Test(expected = InvalidMessageFormatException.class)
    public void testInvalidFormattedMessage() {
        new Message(invalidFormattedMessage);
    }
}
