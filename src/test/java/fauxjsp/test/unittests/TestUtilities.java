package fauxjsp.test.unittests;

import org.junit.Test;

import fauxjsp.impl.Utils;
import static org.junit.Assert.*;
/**
 * Test {@link Utils}
 * @author george georgovassilis
 *
 */
public class TestUtilities {

	@Test
	public void test_cast(){
		assertNull(Utils.cast(null, String.class));
		assertEquals(true,Utils.cast("true", Boolean.class));
		assertEquals((byte)2,Utils.cast("2", Byte.class));
		assertEquals((short)3,Utils.cast("3", Short.class));
		assertEquals((long)-4,Utils.cast("-4", Long.class));
		assertEquals((float)3.14,Utils.cast("3.14", Float.class));
		assertEquals((double)-2.67,Utils.cast("-2.67", Double.class));
	}
}
