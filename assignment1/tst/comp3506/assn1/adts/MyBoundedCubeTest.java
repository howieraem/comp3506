package comp3506.assn1.adts;

import static org.junit.Assert.*;
import org.junit.Test;

public class MyBoundedCubeTest {
	@Test(timeout=500)
	@SuppressWarnings("unused")
	public void testInvalidConstructor() {
		try {
			Cube<Object> objCube1 = new BoundedCube<>(0, 3, 5);
			Cube<Object> objCube2 = new BoundedCube<>(5, 5, -2);
			Cube<Object> objCube3 = new BoundedCube<>(5, -5, 2);
			Cube<Object> objCube4 = new BoundedCube<>(-3, -5, 2);
			Cube<Object> objCube5 = new BoundedCube<>(-3, 5, -2);
			Cube<Object> objCube6 = new BoundedCube<>(3, -5, 0);
			fail();
		} catch (IllegalArgumentException e) {}
	}
	
	@Test(timeout=500)
	public void testMultipleElementsAt() {
		Cube<Object> objCube = new BoundedCube<>(3, 3, 3);
		Object obj1 = new Object();
		Object obj2 = new Object();
		Object obj3 = new Object();
		objCube.add(1, 1, 1, obj1);
		objCube.add(1, 1, 1, obj2);
		objCube.add(2, 2, 2, obj3);
		assertTrue(objCube.isMultipleElementsAt(1, 1, 1));
		assertFalse(objCube.isMultipleElementsAt(2, 2, 2));
	}
	
	@Test(timeout=500)
	public void testBoundaries() {
		Cube<Object> objCube = new BoundedCube<>(1, 1, 1);
		Object obj1 = new Object();
		Object obj2 = new Object();
		objCube.add(1, 1, 1, obj1);
		objCube.add(0, 0, 0, obj2);
		assertEquals(objCube.get(1, 1, 1), obj1);
		assertEquals(objCube.get(0, 0, 0), obj2);
	}
	
	@Test(timeout=500)
	@SuppressWarnings("unused")
	public void testOutOfBound() {
		Cube<Object> objCube = new BoundedCube<>(1, 1, 1);
		Object obj = new Object();
		try {
			objCube.add(2, 2, 2, obj);
			fail();
		} catch (IndexOutOfBoundsException e) {}
		try {
			Object objNew = objCube.get(2, 2, 2);
			fail();
		} catch (IndexOutOfBoundsException e) {}
		try {
			objCube.remove(2, 2, 2, obj);
			fail();
		} catch (IndexOutOfBoundsException e) {}
		try {
			objCube.removeAll(2, 2, 2);
			fail();
		} catch (IndexOutOfBoundsException e) {}
		try {
			objCube.isMultipleElementsAt(2, 2, 2);
			fail();
		} catch (IndexOutOfBoundsException e) {}
		try {
			objCube.getAll(2, 2, 2);
			fail();
		} catch (IndexOutOfBoundsException e) {}
	}
	
	@Test(timeout=2000)
	public void testMaximumElementAndRemove() {
		Cube<Object> objCube = new BoundedCube<>(2, 2, 2);
		objCube.add(0, 0, 0, new Object());
		for (int i = 0; i < 19998; ++i) {
			Object obj = new Object();
			objCube.add(1, 1, 2, obj);
		}
		objCube.add(2, 2, 2, new Object());
		try {
			objCube.add(2, 2, 2, new Object());
			fail();
		} catch (IllegalStateException e) {}
		IterableQueue<Object> objQueue1 = objCube.getAll(1, 1, 2);
		assertEquals(objQueue1.size(), 19998);
		objCube.removeAll(1, 1, 2);
		assertNull(objCube.getAll(1, 1, 2));
	}
	
	@Test(timeout=500)
	public void testRemoveOne() {
		Cube<Object> objCube = new BoundedCube<>(2, 2, 2);
		Object obj1 = new Object();
		Object obj2 = new Object();
		objCube.add(1, 2, 1, obj1);
		objCube.add(1, 2, 1, obj2);
		assertTrue(objCube.remove(1, 2, 1, obj2));
		assertEquals(objCube.get(1, 2, 1), obj1);
	}
	
	@Test(timeout=500)
	public void testClear() {
		Cube<Object> objCube = new BoundedCube<>(3, 3, 3);
		Object obj1 = new Object();
		Object obj2 = new Object();
		objCube.add(2, 1, 1, obj1);
		objCube.add(2, 1, 1, obj2);
		objCube.clear();
		assertNull(objCube.get(2, 1, 1));
	}
}
