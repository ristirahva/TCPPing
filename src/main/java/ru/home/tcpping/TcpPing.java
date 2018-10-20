package ru.home.tcpping;

import org.apache.commons.text.RandomStringGenerator;

import java.io.IOException;
import java.util.Map;

/**
 * Interface for both Pitcher and Catcher
 */
public interface TcpPing {
    /**
     * Get parameters
     *
     * @return parameters
     */
    Map<String, String> getParameters();
    /**
     * execute TCPPing in respective mode
     */
    void execute() throws IOException;

    /**
     * Message creation.
     *
     * @param number message number
     * @param size content size
     * @return
     */
    default Message createMessage(int number, int size) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('A', 'z').build();
        String content = generator.generate(size);
        Message message = new Message(number, System.currentTimeMillis(), content);
        return message;
    }
}
