package comp3506.assn1.adts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.util.Iterator;


public class TraversableQueueTest {

	@Test(timeout=500)
	public void testNewQueueIsEmpty() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		assertThat("A newly created queue does not have a size of 0.", testQueue.size(), is(equalTo(0)));
	}

	@Test(timeout=500, expected = IndexOutOfBoundsException.class)
	public void testDequeueEmptyQueue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.dequeue();	// Nothing to dequeue.
	}

	@Test(timeout=500)
	public void testSingleElementQueueSize() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.enqueue(new Object());
		assertThat("A queue with one element does not have a size of 1.", testQueue.size(), is(equalTo(1)));
	}

	@Test(timeout=500)
	public void testSingleElementQueue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		testQueue.enqueue(element);
		assertThat("Enqueing and Dequeing one element does not return that element.", 
				   testQueue.dequeue(), is(equalTo(element)));
	}

	@Test(timeout=500)
	public void testIteratorHasNextOnSingleEntityQueue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.enqueue(new Object());
		Iterator<Object> it = testQueue.iterator();
		assertThat("Iterator before first position on a queue of one element does not have a next.", 
				   it.hasNext(), is(equalTo(true)));
		it.next();
		assertThat("Iterator before second position on a queue of one element has a next.", 
				   it.hasNext(), is(equalTo(false)));
	}

}
