package net.gongmingqm10.traintimer.util;

import org.junit.Test;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {

    @Test
    public void shouldFormatDateToYYYYMMDD() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 30);

        assertThat(DateUtils.formatDate(calendar.getTime())).isEqualTo("2015-11-30");
    }
}