import java.util.*;

/**
 * 
 * @author Matei Alexandru-Petrut, 325CB
 *
 */
public class Room {

	public ArrayList<Range> ranges;
	public String roomId;
	public String deviceId;
	public Integer areaRoom;
	public long reference_timestamp;
	/**
	 * Constructor fara parametri care creeaza si adauga in arraylist-ul ranges 24 de instante ale clasei Range
	 */
	public Room() {
		
		ranges = new ArrayList<>(24);
		for(int i = 0; i < 24; i++) {
			
			Range range = new Range();
			ranges.add(i, range);
		}
	}
}
