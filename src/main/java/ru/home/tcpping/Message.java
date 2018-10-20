package ru.home.tcpping;

import org.apache.commons.lang3.StringUtils;
import ru.home.tcpping.exception.InvalidMessageFormatException;

/**
 * Message class.
 */
public final class Message {

    private final long number;

    private final long creationTime;
    private long replyTime = -1;

    private final String content;

    /**
     * Create message object with number, creation time and the context.
     *
     * @param number        message number
     * @param creationTime  message creation time in millis
     * @param content       message content
     */
    public Message(long number, long creationTime, String content) {
        this.number = number;
        this.creationTime = creationTime;
        this.content = content;
    }

    /**
     * Create message object by raw input string.
     *
     * @param rawMessage    raw string
     */
    public Message(String rawMessage) {
        String[] messagePart = rawMessage.split("#");
        try {
            number = Long.parseLong(messagePart[0]);
            creationTime = Long.parseLong(messagePart[1]);
        }
        catch (NumberFormatException e) {
            throw new InvalidMessageFormatException("Message '" + rawMessage + "' has invalid format");
        }
        replyTime = StringUtils.isBlank(messagePart[2]) ? -1 : Long.parseLong(messagePart[2]);
        content = messagePart[3];
    }

    public void setReplyTime(long replyTime) {
        this.replyTime = replyTime;
    }


    public long getNumber() {
        return number;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getReplyTime() {
        return replyTime;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("%010d#", number)
                + String.format("%015d#", creationTime)
                + ((replyTime < 0) ? "               #" : String.format("%015d#", replyTime))
                + content;
    }
}
