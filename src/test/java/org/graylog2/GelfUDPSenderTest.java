package org.graylog2;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GelfUDPSenderTest extends TestCase {

    @Test
    public void testReopenOfChannel() throws IOException {
        GelfUDPSender gelfUDPSender = new GelfUDPSender("localhost", 1234);
        assertThat(gelfUDPSender.getChannel().isOpen(), is(true));

        GelfMessage error = new GelfMessage("Test short", "Test long", new Date().getTime(), "ERROR");
        error.setHost("localhost");
        error.setVersion("1.3");
        error.setFacility("F");

        GelfSenderResult result = gelfUDPSender.sendMessage(error);

        assertThat(result, is(GelfSenderResult.OK));

        gelfUDPSender.getChannel().close();

        assertThat(gelfUDPSender.getChannel().isOpen(), is(false));

        GelfSenderResult secondMessage = gelfUDPSender.sendMessage(error);
        assertThat(secondMessage, is(GelfSenderResult.OK));
        assertThat(gelfUDPSender.getChannel().isOpen(), is(true));
    }

}