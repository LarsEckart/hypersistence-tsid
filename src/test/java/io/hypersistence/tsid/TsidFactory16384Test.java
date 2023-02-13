package io.hypersistence.tsid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TsidFactory16384Test extends TsidFactory00000Test {

	private static final int NODE_BITS = 14;
	private static final int COUNTER_BITS = 8;
	
	private static final int NODE_MAX = (int) Math.pow(2, NODE_BITS);
	private static final int COUNTER_MAX = (int) Math.pow(2, COUNTER_BITS);

	@Test
	public void testGetTsid1() {

		long startTime = System.currentTimeMillis();

		TSID.Factory factory = TSID.Factory.builder().withNodeBits(NODE_BITS).withRandom(random).build();

		long[] list = new long[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.generate().toLong();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkOrdering(list));
		assertTrue(checkMaximumPerMs(list, COUNTER_MAX));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsid1WithNode() {

		long startTime = System.currentTimeMillis();

		int node = random.nextInt(NODE_MAX);
		TSID.Factory factory = TSID.Factory.builder().withNode(node).withNodeBits(NODE_BITS).withRandom(random).build();

		long[] list = new long[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.generate().toLong();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkOrdering(list));
		assertTrue(checkMaximumPerMs(list, COUNTER_MAX));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsidString1() {

		long startTime = System.currentTimeMillis();

		TSID.Factory factory = TSID.Factory.builder().withNodeBits(NODE_BITS).withRandom(random).build();

		String[] list = new String[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.generate().toString();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkOrdering(list));
		assertTrue(checkMaximumPerMs(list, COUNTER_MAX));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsidString1WithNode() {

		long startTime = System.currentTimeMillis();

		int node = random.nextInt(NODE_MAX);
		TSID.Factory factory = TSID.Factory.builder().withNode(node).withNodeBits(NODE_BITS).withRandom(random).build();

		String[] list = new String[LOOP_MAX];
		for (int i = 0; i < LOOP_MAX; i++) {
			list[i] = factory.generate().toString();
		}

		long endTime = System.currentTimeMillis();

		assertTrue(checkNullOrInvalid(list));
		assertTrue(checkUniqueness(list));
		assertTrue(checkOrdering(list));
		assertTrue(checkMaximumPerMs(list, COUNTER_MAX));
		assertTrue(checkCreationTime(list, startTime, endTime));
	}

	@Test
	public void testGetTsid1Parallel() throws InterruptedException {

		TestThread.clearHashSet();
		Thread[] threads = new Thread[THREAD_TOTAL];
		int counterMax = COUNTER_MAX / THREAD_TOTAL;

		// Instantiate and start many threads
		for (int i = 0; i < THREAD_TOTAL; i++) {
			TSID.Factory factory = TSID.Factory.builder().withNode(i).withNodeBits(NODE_BITS).withRandom(random).build();
			threads[i] = new TestThread(factory, counterMax);
			threads[i].start();
		}

		// Wait all the threads to finish
		for (Thread thread : threads) {
			thread.join();
		}

		// Check if the quantity of unique UUIDs is correct
		assertEquals(DUPLICATE_UUID_MSG, (counterMax * THREAD_TOTAL), TestThread.hashSet.size());
	}
}
