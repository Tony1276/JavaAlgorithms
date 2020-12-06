import java.io.*;
import java.util.*;


// This solution was adapted from the online resource:
// https://github.com/mission-peace/interview/blob/master/src/com/interview/dynamic/WeightedJobSchedulingMaximumProfit.java

public class Solver {
	//HashMap to store the bid information from the provided data files
	static HashMap<Integer, Bids> bids;
	static String PATH = "codes/data/";
	
	//variable for the total number of lots 
	int numberOfLots;
	//variable for the total number of bids
	int numberOfBids;

	// A inner class to store the beginning lot, ending lot and price of bid so that easier computation can be achieved in solve()
	class Bids {
		int beginningLot;
		int endLot;
		int priceOfLot;
		
		
		Bids(int beginningLot, int endLot, int priceOfLot){
			this.beginningLot = beginningLot;
			this.endLot = endLot;
			this.priceOfLot = priceOfLot;
		}
	}

	

	//A comparator that sorts the bids according to the ending lot numbers in increasing order
	class LotComparator implements Comparator<Bids>{
		
		@Override
		public int compare(Bids currentBid, Bids nextBid) {
			if(currentBid.endLot <= nextBid.endLot) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	//This method is to create a object array that holds all the bids.
	public Bids[] populateBids() {
		int bidsLength = bids.size();
		int index = 0;
		
		Bids[] auctionBids = new Bids[bidsLength];
		for(Map.Entry<Integer, Bids> auctions: bids.entrySet()) {
			auctionBids[index] = auctions.getValue();
			index++;
		}
		
		
		
		
		
		return auctionBids;
	
	}



	/**
	 * You can use this to test your program without running the jUnit test,
	 * and you can use your own input file. You can of course also make your
	 * own tests and add it to the jUnit tests.
	 */
	public static void main(String[] args) {
		Solver m = new Solver();
		


		try {
			m.readData("data/Test_case_1.in");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		int answer = m.solve("data/Test_case_1.in");

		System.out.println(answer);
		


	}

	/** The solve method accepts a String containing the
	 * path to the input file for the problem (as described
	 * in the assignment specification) and returns an integer
	 * denoting the maximum income
	 *
	 * @param infile the file containing the input
	 * @return maximum income for this set of input
	 */

	public int solve(String infile) {

		try {
			readData(infile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//A temporary object array of bids
		Bids[] numBids = populateBids();
		//A temporary integer array to hold the maximum value
		int[] storeMaxRev = new int[numBids.length];
		//A temporary variable for the maximum revenue that will be updated in the main loop.
		int maxRev = 0;
		
		//Invokes the comparator method defined earlier
		LotComparator compareFinishTimes = new LotComparator();
		Arrays.sort(numBids,compareFinishTimes);
		
		
		//Edge case check if the number of bids is less than 0
		if(numBids.length <= 0) {
			return 0;
		}
		
		//Choose the first bid in the object array.
		storeMaxRev[0] = numBids[0].priceOfLot;
		
		//From the second element of the object array, iterate through the array and store the price
		//of the element that is larger than the previous element
		for(int i = 1; i < numBids.length; i++) {
			storeMaxRev[i] = Math.max(numBids[i].priceOfLot, storeMaxRev[i - 1]);
			
			//Iterating backwards from the second last element relative to i, check if the current element is compatible.
			//A compatible bid is when the ending lot number is less than the starting lot number of the next bid.
			//If it is an compatible bid, add the price of the current bid to the previous bid and take the maximum price out of the two.
			for(int j = i - 1; j >= 0; j--) {
				if(numBids[j].endLot < numBids[i].beginningLot) {
					storeMaxRev[i] = Math.max(storeMaxRev[i], numBids[i].priceOfLot + storeMaxRev[j]);
					break;
				}
			}
		}
		
		
		//Once all of the bids in numBid object array is processed, we get an integer array that will contain the maximum revenue.
		//This loop iterates through storeMaxRev and selects the largest integer which is then set to the variable maxRev and returned as the answer
		for(int priceIdx = 0; priceIdx < storeMaxRev.length; priceIdx++) {
			if(maxRev < storeMaxRev[priceIdx]) {
				maxRev = storeMaxRev[priceIdx];
			}
		}
		
		return maxRev;
	}

	/**
	 * The readData method accepts a String containing the
	 * path to the input file for the problem.
	 * Please see the assignment specification for more information
	 * on the input format.
	 *
	 * You should use this method to populate this class with
	 * the information that you need to solve the problem.
	 *
	 * @param infile the input file containing the problem
	 * @throws Exception if file is not found or if there is an input reading error
	 */
	public void readData(String infile) throws Exception {
		//A scanner object to read the input file line by line
		Scanner in = new Scanner(new FileReader(infile));
		
		//Instantiate the bid HashMap to store the bid information
		bids = new HashMap<Integer, Bids>();
		
		//Associate the number of lots and number of bids with the respective variables
		numberOfLots = in.nextInt();
		numberOfBids = in.nextInt();
		
		
		//Move the cursor to the next line in the input file.
		in.nextLine();
		
		//All of the bids that is specified by the variable "numberOfBids" in the input file is looked at and added to the HashMap
		for(int i = 0; i < numberOfBids; i++) {
			bids.put(in.nextInt(), new Bids(in.nextInt(), in.nextInt(), in.nextInt()));

		}
	}


}
