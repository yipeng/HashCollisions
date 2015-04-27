package hash.counting.uuid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class UuidHashTest {

	enum HashCode { DEFAULT, STRING };
	static HashCode selectedCode = HashCode.DEFAULT;
	static HashMap<Integer, Integer> uuidHashCodeMap;
	static int iterationPerRun = 77163;
	static int collidedRun = 0;

	public static void main(String[] args) {
		int runs = 1000;
		for (int j = 1; j < runs+1; j++) {
			fillUuidMap();
			int collisionCount = getCollisionCount();
			//printRunDetails(collisionCount, j);
			float collisionProb = computeCollisionProb(collisionCount, j);
			printCollisionProb(collisionProb);
			releaseUuidMap();
		}
	}

	private static void printCollisionProb(float collisionProb) {
		System.out.println("Empirical probability of a hash collision: " + collisionProb);
	}

	private static float computeCollisionProb(int collisionCount, int runIter) {
		if (collisionCount != 0){
			collidedRun += 1;
		}
		return (float) collidedRun / runIter;
	}

	@SuppressWarnings("unused")
	private static void printRunDetails(int collisionCount, int runIter) {
		if (collisionCount == 0){
			System.out.println("No collisions on run " + runIter);
		} else {
			System.out.println(collisionCount + " collisions on run " + runIter);
		}
	}

	private static int getCollisionCount() {
		Iterator<Entry<Integer, Integer>> it = uuidHashCodeMap.entrySet().iterator();
		int aggregateMax = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Integer> pair = (Map.Entry<Integer, Integer>) it.next();
			if (pair.getValue() > aggregateMax){
				aggregateMax = pair.getValue();
			}
		}
		return aggregateMax-1;
	}

	private static void releaseUuidMap() {
		uuidHashCodeMap = null;
	}

	private static void fillUuidMap() {
		uuidHashCodeMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < iterationPerRun; i++) {
			
			int uuid = (selectedCode == HashCode.DEFAULT) 
					? defaultHashCode() : stringHashCode();
			int aggregateCount = 1;
			if (uuidHashCodeMap.containsKey(uuid)){
				aggregateCount = uuidHashCodeMap.get(uuid);
				aggregateCount += 1;
			}
			uuidHashCodeMap.put(uuid, aggregateCount);
		}
	}

	private static int defaultHashCode() {
		return UUID.randomUUID().hashCode();
	}

	private static int stringHashCode() {
		String uuidString = UUID.randomUUID().toString();
		int hash = uuidString.hashCode();
		final int count = uuidString.length();
		final int offset = 0;
		final char value[] = uuidString.toCharArray();
		
		// code below is copied from String object class
		int h = hash;
		if (h == 0 && count > 0) {
			int off = offset;
			char val[] = value;
			int len = count;

			for (int i = 0; i < len; i++) {
				h = 31*h + val[off++];
			}
			hash = h;
		}
		return h;
	}

}
