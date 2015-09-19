
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
	
}
