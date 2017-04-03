
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.whitepages.ivr.config.TestConfig;

public class WhitepagesApiTest {
	
	private static String TEST_NUMBER_1 = "8004444444";

	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void whitepagesSpamLookupTest() {
		WhitepagesProApi api = new WhitepagesProApi(TestConfig.get("api_key"));
		int reputation = api.lookupSpamScore(TEST_NUMBER_1);

		Assert.assertEquals(1, reputation);
	}
	
	@Test
	public void whitepagesReverseLookupTest() {
		WhitepagesProApi api = new WhitepagesProApi(TestConfig.get("api_key"));
		HashMap<String, Object> results = api.reversePhoneLookup(TEST_NUMBER_1);

		String belongsTo = (String)results.get("whitepages.belongs_to[0].name");
		double latitude = (Double)results.get("whitepages.current_addresses[0].lat_long.latitude");
		double longitude = (Double)results.get("whitepages.current_addresses[0].lat_long.longitude");
		
		Assert.assertEquals("Pizza Hut", belongsTo);
		Assert.assertEquals(30.271917, latitude, 0.000001);
		Assert.assertEquals(-97.698326, longitude, 0.000001);
	}
	
}
