package comp3506.assn1.adts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class BoundedCubeTest {

	@Test(timeout=500)
	public void testGetWithOneElement() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element = new Object();
		testCube.add(1, 1, 1, element);
		assertThat("Only element at a position was not returned.", testCube.get(1, 1, 1), is(equalTo(element)));
	}

	@Test(timeout=500)
	public void testGetWithMultipleElements() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element1 = new Object();
		Object element2 = new Object();
		testCube.add(1, 1, 1, element1);
		testCube.add(1, 1, 1, element2);
		assertThat("First element added at a position was not returned.", testCube.get(1, 1, 1), is(equalTo(element1)));
	}

	@Test(timeout=500)
	public void testGetAllWithMultipleElementsSize() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element1 = new Object();
		Object element2 = new Object();
		testCube.add(1, 1, 1, element1);
		testCube.add(1, 1, 1, element2);
		IterableQueue<Object> queue = testCube.getAll(1, 1, 1);
		assertThat("Returned queue was wrong size.", queue.size(), is(equalTo(2)));
	}

	@Test(timeout=500)
	public void testIsMultipleElementsAtWithOneElement() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		testCube.add(1, 1, 1, new Object());
		assertThat("One element at a position indicates it is multiple.", 
				   testCube.isMultipleElementsAt(1, 1, 1), is(equalTo(false)));
	}

	@Test(timeout=500)
	public void testClearCube() {
		Cube<Object> testCube = new BoundedCube<>(3, 3, 3);
		testCube.add(0, 0, 0, new Object());
		testCube.add(1, 1, 1, new Object());
		testCube.add(1, 1, 1, new Object());
		testCube.add(2, 2, 2, new Object());
		testCube.clear();
		for (int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				for (int k=0; k<3; k++)
					assertThat("", testCube.get(i, j, k), is(equalTo(null)));
	}

}
