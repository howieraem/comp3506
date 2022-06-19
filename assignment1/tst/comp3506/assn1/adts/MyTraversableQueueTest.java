package comp3506.assn1.adts;

import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;
import org.junit.Test;

public class MyTraversableQueueTest {
	@Test(timeout=2000)
	public void testMaximumAirplanes() {
		final int MAX_AIRPLANES = 20000;
		IterableQueue<Object> iq = new TraversableQueue<>();
		for (int i = 0; i < MAX_AIRPLANES; i++) {
			Object obj = new Object();
			iq.enqueue(obj);
		}
		assertEquals(MAX_AIRPLANES, iq.size());
		for (int i = 0; i < MAX_AIRPLANES; i++) {
			iq.dequeue();
		}
		assertEquals(iq.size(), 0);
	}
	
	@Test(timeout=500)
	public void testIteratorBasic() {
		IterableQueue<Object> iq = new TraversableQueue<>();
		Iterator<Object> it = iq.iterator();
		assertFalse(it.hasNext());
		Object obj = new Object();
		iq.enqueue(obj);
		assertTrue(it.hasNext());
		assertEquals(it.next(), obj);
	}
	
	@Test
	public void testIteratorComplex() {
		IterableQueue<Object> iq = new TraversableQueue<>();
		Object obj1 = new Object();
		Object obj2 = new Object();
		Object obj3 = new Object();
		Object obj4 = new Object();
		iq.enqueue(obj1);
		iq.enqueue(obj2);
		iq.enqueue(obj3);
		Iterator<Object> it = iq.iterator();
		iq.dequeue();	// Remove obj1
		Object obj1T = it.next();
		Object obj2T = it.next();
		assertEquals(obj1T, obj1);
		assertEquals(obj2T, obj2);
		iq.dequeue();	// Remove obj2
		iq.enqueue(obj4);
		Object obj3T = it.next();
		assertEquals(obj3T, obj3);
		iq.dequeue();	// Remove obj3
		assertTrue(it.hasNext());
		assertEquals(it.next(), obj4);
	}
	
	@Test(timeout=500)
	public void testIndexException() {
		IterableQueue<Object> iq = new TraversableQueue<>();
		try {
			iq.dequeue();
			fail();
		} catch (IndexOutOfBoundsException e) {}
	}
	
	@Test(timeout=500)
	public void testDequeueException() {
		IterableQueue<Object> iq = new TraversableQueue<>();
		Object obj = new Object();
		iq.enqueue(obj);
		Iterator<Object> it = iq.iterator();
		try {
			if (it.hasNext()) {
				iq.dequeue();
			} 
			it.next();
			fail();
		} catch (NoSuchElementException e) {}
	}
}
