package com.infobip.tcpping;

import com.infobip.tcpping.enumeration.Constant;
import com.infobip.tcpping.enumeration.Mode;
import com.infobip.tcpping.exception.InvalidArgumentException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Пользователь on 07.07.2017.
 */
public class TcpPingFactoryTest {
    @Test
    public void testCreateObjectPitcher() {
        String args[] = {"-p", "-port", "1234", "-hostname", "localhost", "-size", "2000", "-mps", "3"};
        TcpPing object = TcpPingFactory.createObject(Mode.PITCHER, args);
        assertNotNull(object);
        Map<String, String> params = object.getParameters();
        assertNotNull(params);
        assertEquals(params.size(), 4);
        assertNotNull(params.get("port"));
        assertEquals("1234", params.get("port"));
        assertNotNull(params.get("hostname"));
        assertEquals("localhost", params.get("hostname"));
        assertNotNull(params.get("size"));
        assertEquals("2000", params.get("size"));
        assertNotNull(params.get("mps"));
        assertEquals("3", params.get("mps"));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testCreateObjectPitcherSizeOverMax() {
        String args[] = {"-p", "-port", "1234", "-hostname", "localhost", "-size", "200000", "-mps", "3"};
        TcpPing object = TcpPingFactory.createObject(Mode.PITCHER, args);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testCreateObjectPitcherSizeUnderMin() {
        String args[] = {"-p", "-port", "1234", "-hostname", "localhost", "-size", "20", "-mps", "3"};
        TcpPing object = TcpPingFactory.createObject(Mode.PITCHER, args);
    }

    @Test
    public void testCreateObjectPitcherOptionalDefaults() {
        String args[] = {"-p", "-port", "1234", "-hostname", "localhost"};
        TcpPing object = TcpPingFactory.createObject(Mode.PITCHER, args);
        assertNotNull(object);
        Map<String, String> params = object.getParameters();
        assertNotNull(params);
        assertEquals(params.size(), 4);
        assertNotNull(params.get("port"));
        assertEquals("1234", params.get("port"));
        assertNotNull(params.get("hostname"));
        assertEquals("localhost", params.get("hostname"));
        assertNotNull(params.get("size"));
        assertEquals(Constant.SIZE_DEFAULT.toString(), params.get("size"));
        assertNotNull(params.get("mps"));
        assertEquals(Constant.MPS_DEFAULT.toString(), params.get("mps"));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testCreateObjectNegativeWrongMode() {
        String args[] = {"-d", "-port", "1234", "-hostname", "localhost"};
        TcpPingFactory.createObject(Mode.PITCHER, args);
    }

    @Test
    public void testCreateObjectCatcher() {
        String args[] = {"-c", "-port", "1234", "-bind", "127.0.0.1"};
        TcpPing object = TcpPingFactory.createObject(Mode.CATCHER, args);
        assertNotNull(object);
        Map<String, String> params = object.getParameters();
        assertEquals("1234", params.get("port"));
        assertEquals("127.0.0.1", params.get("bind"));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testCreateObjectCatcherWrongBind() {
        String args[] = {"-c", "-port", "1234", "-bind", "a127.0.0.1"};
        TcpPing object = TcpPingFactory.createObject(Mode.CATCHER, args);
    }
}
