package com.infobip.tcpping;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Пользователь on 09.07.2017.
 */
public class Message {

    long number;

    long creationTime;
    long replyTime = -1;
    String content;

    public Message(long number, long creationTime, String content) {
        this.number = number;
        this.creationTime = creationTime;
        this.content = content;
    }

    public Message(long number, long creationTime, long replyTime, String content) {
        this.number = number;
        this.creationTime = creationTime;
        this.replyTime = replyTime;
        this.content = content;
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

    /**
     * Create message object from text.
     *
     * @param stringMessage message text
     * @return Message object
     */
    public static Message createMessageFromString(String stringMessage) {
        String[] messagePart = stringMessage.split("#");
        return new Message(Long.parseLong(messagePart[0]),
                Long.parseLong(messagePart[1]),
                StringUtils.isBlank(messagePart[2]) ? -1 : Long.parseLong(messagePart[2]),
                messagePart[3]);
    }
}
