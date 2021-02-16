
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

public class CustomerFilterTest {

	/**
	 * Tests if no file exists at given directory. Here we assert that the method
	 * would throw exception when it doesn't
	 */
	@Test
	public void testWhenNoFileExists() {
		CustomerFilter obj = new CustomerFilter();
		Assert.assertThrows(FileNotFoundException.class, () -> {
			obj.processInputFile("abc.txt");
		});
	}

	/**
	 * Tests if the input file has no data present or the input file is blank.
	 * Asserts that the customerMap should be empty
	 */
	@Test
	public void testEmptyFile() {
		CustomerFilter obj = new CustomerFilter();
		try {
			obj.processInputFile(System.getProperty("user.dir") + "/TestData/empty.txt");
		} catch (FileNotFoundException e) {
			Assert.fail();
		}
		Assert.assertEquals(0, obj.customerMap.size());
	}

	/**
	 * Giving a mock input file to the processInputFile method Here, we assert that
	 * the output map will contain only 3 customers with user ids 4, 5, & 6.
	 */
	@Test
	public void checkProcessInputFile() {
		CustomerFilter obj = new CustomerFilter();
		try {
			obj.processInputFile(System.getProperty("user.dir") + "/TestData/mockInput.txt");
		} catch (FileNotFoundException e) {
			Assert.fail();
		}
		Assert.assertEquals(3, obj.customerMap.size());
		Assert.assertNotNull(obj.customerMap.get(4));
		Assert.assertNotNull(obj.customerMap.get(5));
		Assert.assertNotNull(obj.customerMap.get(6));
	}

	/**
	 * Input data is a blank string or not in JSON format. Assert throws JSON
	 * Exception whenever it encounters a string which is not JSON.
	 */
	@Test
	public void processCustomer() {
		CustomerFilter obj = new CustomerFilter();

		Assert.assertThrows(JSONException.class, () -> {
			obj.processCustomer("");
		});

		Assert.assertThrows(JSONException.class, () -> {
			obj.processCustomer("{ ");
		});

	}

	/**
	 * Assert that the method would throw JSONException if the input data doesn't
	 * contains keys (latitude, longitude, user_id, name)
	 */
	@Test
	public void incorrectJSONFormat() {
		CustomerFilter obj = new CustomerFilter();
		Assert.assertThrows(JSONException.class, () -> {
			// JSON does not contain latitude
			obj.processCustomer("{\"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"} ");
		});
		Assert.assertThrows(JSONException.class, () -> {
			// JSON does not contain user_id
			obj.processCustomer(
					"{\"latitude\": \"52.986375\", \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"} ");
		});
		Assert.assertThrows(JSONException.class, () -> {
			// JSON does not contain name
			obj.processCustomer("{\"latitude\": \"52.986375\", \"user_id\": 12, \"longitude\": \"-6.043701\"} ");
		});
		Assert.assertThrows(JSONException.class, () -> {
			// JSON does not contain longitude
			obj.processCustomer("{\"latitude\": \"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\"} ");
		});

	}

	/**
	 * Checks if the user_id / customer id is not an integer ie. the method asserts
	 * that processCustomer() would throw JSONException
	 */
	@Test
	public void testCustomerId() {
		CustomerFilter obj = new CustomerFilter();
		Assert.assertThrows(JSONException.class, () -> {
			// User_id is a string
			obj.processCustomer(
					"{\"latitude\": \"52.986375\", \"user_id\": \"abc\", \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"} ");
		});
	}

	/**
	 * Method verifies that the distance calculated is correct and only the
	 * customers with distance less than 100 km can be found in the customer map.
	 */
	@Test
	public void testCustomerMapWithDistance() {
		CustomerFilter obj = new CustomerFilter();
		// test with a location in India
		obj.processCustomer(
				"{\"latitude\": \"28.5355\", \"user_id\": 1, \"name\": \"Vijaya\", \"longitude\": \"77.3910\"} ");
		Assert.assertNull(obj.customerMap.get(1));
		// test with co-ordinates of Dublin
		obj.processCustomer(
				"{\"latitude\": \"53.339428\", \"user_id\": 2, \"name\": \"Dublin_Office\", \"longitude\": \"-6.257664\"} ");
		Assert.assertEquals("Dublin_Office", obj.customerMap.get(2));
	}

	/**
	 * Asserting the method calculateDistance() would return 0 if the both
	 * co-ordinates are same.
	 */
	@Test
	public void ifSameLocation() {
		CustomerFilter obj = new CustomerFilter();
		double distance = obj.calculateDistance(obj.LATITUDE_DUBLIN, obj.LONGITUDE_DUBLIN, obj.LATITUDE_DUBLIN,
				obj.LONGITUDE_DUBLIN);
		Assert.assertEquals(0, distance, 0.0);

	}

	/**
	 * checks that it outputs map correctly to file . Here we assert the method
	 * writeCustomerMap() would write correctly to the output file
	 */
	@Test
	public void testOutputUserMap() {
		CustomerFilter obj = new CustomerFilter();
		TreeMap<Integer, String> tree = new TreeMap();
		tree.put(1, "Vijaya");
		tree.put(5, "Param");
		tree.put(3, "John");
		try {
			obj.writeCustomerMap(tree, "testOutput.txt");
		} catch (IOException e) {
			Assert.fail();
		}
		try {
			File inputFileObj = new File(System.getProperty("user.dir") + "/testOutput.txt");
			Scanner fileReader = new Scanner(inputFileObj);
			Assert.assertEquals("user_id : 1, user_name : Vijaya", fileReader.nextLine());
			Assert.assertEquals("user_id : 3, user_name : John", fileReader.nextLine());
			Assert.assertEquals("user_id : 5, user_name : Param", fileReader.nextLine());
			Assert.assertFalse(fileReader.hasNextLine());
		} catch (IOException e) {
			Assert.fail();
		}

	}

}
