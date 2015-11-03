package net.gongmingqm10.traintimer.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class IOUtilsTest {

    @Test
    public void shouldExtractTimeFromMessage() {
        String message = "K1298次列车，西安站正点出发时间为12:25，预计列车正点出发";

        String extractedTime = IOUtils.extractTime(message);

        assertThat(extractedTime).isEqualTo("12:25");
    }

    @Test
    public void shouldExtractEmptyWhenNoTimeInfo() {
        String message = "目前暂无Z136次列车西安站到达时间，请稍候重新查询";

        String extractedMessage = IOUtils.extractTime(message);

        assertThat(extractedMessage).isEmpty();
    }

}