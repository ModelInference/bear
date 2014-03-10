package ch.usi.bear.apache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.usi.star.bear.apache.ApacheTokenizer;

public class TestApacheTokenizer {
	
	private ApacheTokenizer tokenizer;
	private String logLine = "www.test.it:80 1.1.1.3 - - [20/Dec/2011:15:55:02 +0100] \"GET /cinque/ HTTP/1.1\" 200 18359 \"http://www.test.it/due/\" \"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)\"";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		tokenizer = new ApacheTokenizer();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDate() {
		try {
			Date date = tokenizer.extractDate(logLine);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int year = cal.get(Calendar.YEAR);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			int seconds = cal.get(Calendar.SECOND);
			int zone = cal.get(Calendar.ZONE_OFFSET);
			assertEquals(day, 20);
			assertEquals(year, 2011);
			assertEquals(month, 11);
			assertEquals(hour, 15);
			assertEquals(minute, 55);
			assertEquals(seconds, 02);
			assertEquals(zone, 3600000);
			
		} catch (ParseException e) {
			fail();
			e.printStackTrace();
		}
	}

}
