/**
 * 
 */
package com.renren.sns.webtalk.util;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * @author inshion(xin.yin@opi-corp.com)
 * @since 2011-3-4
 */
public class InvokeUtilTest {
	//private InvokeUtil test;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		//test = new InvokeUtil();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public final void testGetInvokingMethodName() {
		String index = InvokeUtil.getInvokingMethodName();
		System.out.println("["+index+"]");
		Assert.notNull(index);
	}

}
