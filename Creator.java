import java.util.*;
import java.text.DecimalFormat;

/**
 * 
 * @author Matei Alexandru-Petrut, 325CB
 *
 */
public class Creator {
	
	/**
	 * Metoda care verifica daca timestamp-ul la care s-a observat temperatura / umiditatea e din ultimele 24 de ore fata de referinta
	 * @param observeTimestamp timestamp-ul la care s-a obervat temperatura / umiditatea
	 * @param referenceTimestamp timestamp-ul de referinta
	 * @return returneaza true daca timestamp-ul e din ultimele 24 de ore, false daca nu
	 */
	public static Boolean IfValidTimestamp(long observeTimestamp, long referenceTimestamp) {
		
	     return (observeTimestamp >= (referenceTimestamp - 3600*24)) && (observeTimestamp <= referenceTimestamp);
	}
	/**
	 * Metoda care adauga temperatura si timestamp-ul la care s-a observat aceasta in cei 2 vectori pt cazul temperaturilor
	 * si timestamp-uri pt temperaturi ai intervalului corespunzator fiecarei ore ( una din cele 24 fata de cea de referinta )
	 * @param room camera in care se face adaugarea
	 * @param referenceTimestamp timestamp-ul de referinta
	 * @param observeTimestamp timestamp-ul la care s-a obsevat temperatura
	 * @param observeTemperature temperatura observata
	 */
	public static void addInRange(Room room, Long referenceTimestamp, Long observeTimestamp, Double observeTemperature) {
		
		int i = 24; //plec de la timestamp-ul de referinta minus 24 de ore pana cand ajung la o ora in urma fata de el
		while(i >= 1) {
			//verific in care din cele 24 de ore se incadreaza acest timestamp fata de referinta
			//daca scad i*3600 din referinta inseamna ora fata de timestamp-ul de referinta cu i ore in urma
			if((observeTimestamp >= (referenceTimestamp - i*3600)) && (observeTimestamp < (referenceTimestamp - (i-1)*3600)) && (observeTimestamp <= referenceTimestamp)) {
				
				//adaug la cei 2 vectori temperatura si timestampul respective 
				room.ranges.get(24-i).timestampsFromRoom.add(observeTimestamp);
				room.ranges.get(24-i).temperaturesFromRoom.add(observeTemperature);
				//sortez crescator vectorul de temperaturi observate pentru a retine pe prima pozitie temperatura minima din ora respectiva
				Collections.sort(room.ranges.get(24-i).temperaturesFromRoom);
			}
			i--;
		}
	}
	/**
	 * Metoda care adauga umiditatea si timestamp-ul la care s-a observat aceasta in cei 2 vectori pt cazul umiditatilor
	 * @param room camera in care se face adaugarea
	 * @param referenceTimestamp timestamp-ul de referinta
	 * @param observeTimestampHum timestamp-ul la care s-a observat umiditatea 
	 * @param observeHumidity umiditatea observata
	 */
	public static void addInRangeHum(Room room, Long referenceTimestamp, Long observeTimestampHum, Double observeHumidity) {
		
		//analog metodei addInRange, acum pt umiditati
		int i = 24;
		while(i >= 1) {
			if((observeTimestampHum >= (referenceTimestamp - i*3600)) && (observeTimestampHum < (referenceTimestamp - (i-1)*3600)) && (observeTimestampHum <= referenceTimestamp)) {
				
				room.ranges.get(24-i).timestampsFromRoomHum.add(observeTimestampHum);
				room.ranges.get(24-i).humiditiesFromRoom.add(observeHumidity);
				Collections.sort(room.ranges.get(24-i).humiditiesFromRoom);
			}
			i--;
		}
	}
	/**
	 * Metoda care intoarce temperatura minima din ultima ora in care s-au observat temperaturi in camera respectiva
	 * @param room camera in care se cauta temperatura minima din ultima ora
	 * @return returneaza temperatura minima observata in ultima ora
	 */
	public static Double getMinimumTempRoom(Room room) {
		
		Double minTempRoom = 1000000.00; //initializare temperatura minima, nu se ajunge sa se intoarca aceasta valoare 
		//parcurg fiecare interval din cele 24, pornind de la cel mai recent fata de referinta 
		for(int i = 23; i >= 0 ; i--)
			if(room.ranges.get(i).timestampsFromRoom.size() > 0) //verific ca intervalul sa nu contina temperaturi observate
			{	minTempRoom = room.ranges.get(i).temperaturesFromRoom.get(0); //temperatura minima e prima din vectorul de temperaturi in urma sortarii
				break;}
		
		return minTempRoom;
	}
	/**
	 * Metoda care intoarce umiditatea minima din ultima ora in care s-au observat umiditati in camera respectiva
	 * @param room camera in care se cauta umiditatea minima din ultima ora
	 * @return returneaza umiditatea minima observata in ultima ora
	 */
	public static Double getMinimumHumRoom(Room room) {
		
		Double minHumRoom = 10000.00;
		for(int i = 23; i >= 0 ; i--)
			if(room.ranges.get(i).timestampsFromRoomHum.size() > 0) 
			{	minHumRoom = room.ranges.get(i).humiditiesFromRoom.get(0);
				break;}

		return minHumRoom;
	}
	/**
	 * Metoda care verifica daca s-au observat temperaturi in camera respectiva
	 * @param room camera in care se face verificarea
	 * @return intoarce true daca s-a observat minim una si false daca niciuna
	 */
	public static Boolean verifyIfEmptyRoom(Room room) {
		
		for(int i = 0; i < 24; i++)
			
			if(room.ranges.get(i).temperaturesFromRoom.isEmpty() != true)
				return true;
		return false;
	}
	/**
	 * Metoda care afiseaza temperaturile corespunzatoare timestamp-urilor din intervalul [BeginOfRange, EndOfRange]
	 * prin concatenarea acestora ca string-uri, sortate crescator pe intervale de o ora
	 * @param room camera din care se face listarea
	 * @param BeginOfRange timestamp-ul care reprezinta capatul inferior al intervalului
	 * @param EndOfRange timestamp-ul care reprezinta capatul superior al intervalului
	 * @param roomID id-ul camerei din care se face listarea
	 * @return returneaza un string ce reprezinta intreaga secventa de temperaturi ce trebuie listate
	 */
	public static String list(Room room, Long BeginOfRange, Long EndOfRange, String roomID) {
		
		ArrayList<Double> forList = new ArrayList<>(); //arraylist folosit pentru a retine temperaturile 
													   //,ce trebuie listate, in ordinea potrivita
		String listPrint = new String(); //string-ul folosit pentru afisare
		listPrint = listPrint + roomID + " "; //listarea incepe cu id-ul camerei
		
		//parcurg fiecare interval din cele 24 din camera si adaug in arraylist doar temperaturile observate 
		//la un timestamp cuprins in cele 2 date ca parametri
		for(int i = 23; i >= 0; i--) 
			if(room.ranges.get(i).timestampsFromRoom.size() > 0)
				for(int j = 0; j < room.ranges.get(i).timestampsFromRoom.size(); j++)
					if((room.ranges.get(i).timestampsFromRoom.get(j) >= BeginOfRange) && (room.ranges.get(i).timestampsFromRoom.get(j) <= EndOfRange))
						forList.add(room.ranges.get(i).temperaturesFromRoom.get(j));
		
		String copyDoubleValue = " "; //il folosesc pentru verificarea unei eventuale aparitii de duplicate in afisare
		for(int i = 0; i < forList.size(); i++) {
			
			String doubleValue = new DecimalFormat("0.00").format(forList.get(i)); //in afisare se doreste temperatura de forma xx.yy (cu 2 zecimale)
			char[] chars_array = doubleValue.toCharArray(); //folosesc vectorul de caractere pentru a converti stringul de la xx,yy la xx.yy
			for(int j = 0; j < chars_array.length; j++)
				if(chars_array[j] == ',') //inlocuiesc virgula cu punctul 
					chars_array[j] = '.';
			String doubleValueConverted = new String(chars_array); //string-ul care intra acum convertit in concatenare
			if(doubleValue.equals(copyDoubleValue) == false) //pun conditie sa nu am duplicate in afisare
				listPrint = listPrint + doubleValueConverted + " "; //se construieste string-ul ce va fi intors prin concatenare
			copyDoubleValue = new String(doubleValue);
		}
		
		return listPrint;
	}
	
}
