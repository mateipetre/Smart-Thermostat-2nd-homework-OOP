import java.util.*;

/**
 * 
 * @author Matei Alexandru-Petrut, 325CB
 *
 */
public class House {

	public ArrayList<Room> rooms;
	/**
	 * Constructor fara parametri care instantiaza arraylist-ul de camere
	 */
	public House() {
		
		rooms = new ArrayList<>();
	}
	/**
	 * Metoda care intoarce indicele camerei in care s-a gasit dispozitivul, al carui id a fost citit
	 * @param observeDeviceID id-ul dispozitivului
	 * @return returneaza indicele camerei in care exista dispozitivul respectiv sau -1 daca dispozitivul nu s-a gasit in nicio camera 
	 */
	public Integer IsDeviceValid(String observeDeviceID) {
		
		for(int i = 0; i < rooms.size(); i++)
			if(observeDeviceID.equals(rooms.get(i).deviceId))
				return i;
		return -1;
				
	}
}
