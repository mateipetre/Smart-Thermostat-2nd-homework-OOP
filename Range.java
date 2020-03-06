import java.util.*;

/**
 * 
 * @author Matei Alexandru-Petrut, 325CB
 *
 */
public class Range {

    public Vector<Double> temperaturesFromRoom;
    public Vector<Long> timestampsFromRoom;
    public Vector<Double> humiditiesFromRoom;
    public Vector<Long> timestampsFromRoomHum;
    /**
     * Constructor fara parametri care instantiaza cei 4 vectori de temperaturi, umiditati si timestamp-uri
     */
    public Range() {
    	
		temperaturesFromRoom = new Vector<>();
		timestampsFromRoom = new Vector<>();
		humiditiesFromRoom = new Vector<>();
		timestampsFromRoomHum = new Vector<>();
		
	}
	
}
