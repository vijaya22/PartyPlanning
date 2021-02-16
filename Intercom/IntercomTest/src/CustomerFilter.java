
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;


public class CustomerFilter {
	/**
	 * a tree map which stores user_id of customers as key and their names as
	 * values.
	 */
	TreeMap<Integer, String> customerMap = new TreeMap<Integer, String>();

	double LATITUDE_DUBLIN = 53.339428;
	double LONGITUDE_DUBLIN = -6.257664;
	final int RADIUS_OF_EARTH = 6357;
	final int MAX_DISTANCE = 100;

	/**
	 * takes filePath as input and process the file line wise
	 * @param filePath
	 */
	void processInputFile(String filePath) throws FileNotFoundException {

		File inputFileObj = new File(filePath);
		Scanner fileReader = new Scanner(inputFileObj);
		while (fileReader.hasNextLine()) {
			String eachCustomerData = fileReader.nextLine();
			try {
				processCustomer(eachCustomerData);
			} catch (JSONException e) {
				// to skip malfunctioned JSON and move forward to next line in file
				System.out.println("skip." + e);
			}
		}
		fileReader.close();

	}

	/**
	 * converts customer data into a JSON object and parse the JSON to get user's
	 * co-ordinates (latitude & longitude).
	 * @param data
	 */
	void processCustomer(String data) throws JSONException {
		JSONObject json = new JSONObject(data);

		double userLatitude = Double.parseDouble(json.getString("latitude"));
		double userLongitude = Double.parseDouble(json.getString("longitude"));
		int customerId = json.getInt("user_id");
		String customerName = json.getString(("name"));
		double distance = calculateDistance(LATITUDE_DUBLIN, LONGITUDE_DUBLIN, userLatitude, userLongitude);

		if (distance <= MAX_DISTANCE) {
			customerMap.put(customerId, customerName);
		}
	}

	/**
	 * calculates the distance between two locations, given their latitudes and
	 * longitudes according to the first formula given at
	 * https://en.wikipedia.org/wiki/Great-circle_distance
	 * @param latitude1
	 * @param longitude1
	 * @param latitude2
	 * @param longitude2
	 * @return distance between two locations
	 */
	double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {

		double longitudeDiff = Math.toRadians(Math.abs(longitude1 - longitude2));
		double del = (Math.sin(Math.toRadians(latitude1)) * Math.sin(Math.toRadians(latitude2)))
				+ (Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * Math.cos(longitudeDiff));
		// as the double data type in java has very high precision,
		// thus our above method to calculate del gives value 1.00000002 when calculating the del between two same co-ordinates
		// thus we have approximately estimated it as 1.
		if (del > 1) {
			del = 1;
		}
		if (del < -1) {
			del = -1;
		}
		double centralAngle = Math.acos(del);
		double distance = RADIUS_OF_EARTH * centralAngle;

		return distance;
	}

	/**
	 * creates output text file and writes the userList and returns the directory to
	 * find the output file.
	 * @param userMap
	 * @return location of output file
	 */
	String writeCustomerMap(Map<Integer, String> userMap, String outputFilename) throws IOException {
		// create new file C
		File fileObj = new File(outputFilename);
		if (fileObj.createNewFile()) {
			System.out.println("File created: " + fileObj.getName());
		} else {
			System.out.println("File already exists. Previous content removed and write new data.");
		}

		// write to the file
		FileWriter writer = new FileWriter(outputFilename);
		for (Map.Entry<Integer, String> i : userMap.entrySet()) {
			writer.write("user_id : " + i.getKey() + ", user_name : " + i.getValue());
			writer.write("\n");
		}
		writer.close();
		return System.getProperty("user.dir") + "/" + outputFilename;

	}

	public static void main(String[] args) {
		try {
			CustomerFilter object = new CustomerFilter();
			Scanner scan = new Scanner(System.in);
			System.out.println("Input file directory or press C to continue with downloaded input file ");
			String fileDirectory = scan.nextLine();
			if(fileDirectory.equals("C")) {
				fileDirectory = System.getProperty("user.dir") + "/TestData/customers.txt";
			}
			object.processInputFile(fileDirectory);
			String outputFileLocation = object.writeCustomerMap(object.customerMap, "output.txt");
			System.out.println("Please find output file here:- " + outputFileLocation);
		} catch (IOException e) {
			System.out.println(e.getMessage());

		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}

	}

}
