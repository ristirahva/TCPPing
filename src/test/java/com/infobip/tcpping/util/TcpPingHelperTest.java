package com.infobip.tcpping.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Пользователь on 08.07.2017.
 */
public class TcpPingHelperTest {

    @Test
    public void testCalculateAverage() {
        int result;

        result = TcpPingHelper.calculateAverage(2, 1, 5);
        assertEquals(3, result);

        result = TcpPingHelper.calculateAverage(3, result, 12);
        assertEquals(6 , result);
    }
}
