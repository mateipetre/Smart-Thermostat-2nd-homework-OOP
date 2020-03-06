import java.util.*;
import java.io.*;
/**
 * 
 * @author Matei Alexandru-Petrut, 325CB
 * 
 */  
public class Test {
	/**
	 * Constructor fara parametri al clasei Test
	 */
	public Test() {
		
	}
	/**
	 * Metoda main in care se face citirea din fisier, afisarea in fisier si implementarea metodelor din Creator
	 * @param args argumentele pentru linia de comanda
	 * @throws Exception exceptie pe care o arunca si o prinde in cazul in care fisierul de intrare nu exista / citirea din el e imposibila
	 */
	public static void main(String[] args) throws Exception {
		
		//declarare entitati necesare pentru stocarea datelor
		int number_of_rooms;
		Double global_temperature, global_humidity = 0.00;
		long reference_timestamp;
		File inputFile = new File("therm.in");
		File outputFile = new File("therm.out");
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		String current_line, current_token;
		StringTokenizer tokenizer;
		House newHouse = new House();
		//blocul de try - catch
		try {
			
			current_line = br.readLine(); //prima linie din fisierul de intrare
			tokenizer = new StringTokenizer(current_line); //e impartita in tokens pentru a stoca fiecare informatie
			//verifica daca fisierul contine pe prima linie 3 sau 4 informatii
			if(tokenizer.countTokens() == 3) { // daca sunt 3, fisierul de intrare nu contine date despre umiditate si retine cele 3 informatii
			number_of_rooms = Integer.parseInt(tokenizer.nextToken()); //numar de camere
			global_temperature = Double.parseDouble(tokenizer.nextToken()); //temperatura globala
			reference_timestamp = Long.parseLong(tokenizer.nextToken()); //timestamp-ul de referinta
			}
			else { //fisierul contine 4 informatii pe prima linie, deci contine date despre umiditate
				number_of_rooms = Integer.parseInt(tokenizer.nextToken()); 
				global_temperature = Double.parseDouble(tokenizer.nextToken());
				global_humidity = Double.parseDouble(tokenizer.nextToken()); //umiditatea globala
				reference_timestamp = Long.parseLong(tokenizer.nextToken());
				
			}
			//stochez date in fiecare dintre camere, pe baza numarului de camere
			for(int i = 0; i < number_of_rooms; i++) { 
				
				current_line = br.readLine();
				tokenizer = new StringTokenizer(current_line);	
				
				Room currentRoom = new Room(); //camera curenta 
				currentRoom.roomId = tokenizer.nextToken();	//id-ul camerei
				currentRoom.deviceId = tokenizer.nextToken(); //id-ul dispozitivului
				currentRoom.areaRoom = Integer.parseInt(tokenizer.nextToken()); //aria camerei
				currentRoom.reference_timestamp = reference_timestamp; //timpul de referinta
				newHouse.rooms.add(currentRoom); //adaug la vectorul de camere din casa camera curenta creeata
			}
			//citesc mai departe fisierul linie cu linie pentru implementarea comenzilor, pana la sfarsitul acestuia
			current_line = br.readLine();
			while(current_line != null) {
				
				tokenizer = new StringTokenizer(current_line); //impartire in tokens
				if(tokenizer.hasMoreTokens()) {
				
					current_token = tokenizer.nextToken(); //primul token reprezinta numele comenzii
					
					//folosesc un switch pentru diferitele valori pe care le poate lua token-ul
					switch(current_token) {
						case "OBSERVE":
							current_token = tokenizer.nextToken(); //retine id-ul dispozitivului
							Integer indexRoom = newHouse.IsDeviceValid(current_token); //retine indicele camerei in care se afla dispozitivul
							current_token = tokenizer.nextToken(); //timestamp-ul la care e observata temperatura
							String next_token = tokenizer.nextToken(); //temperatura observata
							//verifica daca timestamp-ul retinut se afla in intervalul [timestamp de referinta - 24 de ore, timestamp de referinta]
							if(Creator.IfValidTimestamp(Long.parseLong(current_token), reference_timestamp) == true) {
						
								//adaug intr-unul din cele 24 de intervale, aferente camerei respective, timestamp-ul si temperatura observate in vectorii de timestampuri pentru temperaturi si temperaturi
								Creator.addInRange(newHouse.rooms.get(indexRoom), reference_timestamp, Long.parseLong(current_token), Double.parseDouble(next_token));
							}
							break;
						case "OBSERVEH":
							current_token = tokenizer.nextToken(); 
							Integer indexRoomHum = newHouse.IsDeviceValid(current_token);
							current_token = tokenizer.nextToken(); 
							String next_token_1 = tokenizer.nextToken(); //umiditatea observata
							if(Creator.IfValidTimestamp(Long.parseLong(current_token), reference_timestamp) == true) {
								
								//adaug intr-unul din cele 24 de intervale aferente camerei respective, timestamp-ul si umiditatea observate in vectorii de timestampuri pentru umiditati si umiditati
								Creator.addInRangeHum(newHouse.rooms.get(indexRoomHum), reference_timestamp, Long.parseLong(current_token), Double.parseDouble(next_token_1));
							}
							break;
						case "TRIGGER":
							Double sumOfTemperatures = 0.00; //numaratorul mediei ponderate care reprezinta temperatura medie observata in ultima ora
							Integer sumOfPonders = 0; //numitorul mediei, suma ariilor camerelor in care s-au observat temperaturi / umiditati in ultimele 24 de ore
							Double sumOfHumidities = 0.00; //numaratorul umiditatii medii din ultima ora
							
							//parcurg fiecare camera si verific daca s-a inregistrat in ultimele 24 de ore ceva  si calculez cele de mai sus
							for(int i = 0; i < number_of_rooms; i++) 
								if(Creator.verifyIfEmptyRoom(newHouse.rooms.get(i)) == true)
							{
								sumOfTemperatures = sumOfTemperatures + Creator.getMinimumTempRoom(newHouse.rooms.get(i)) * newHouse.rooms.get(i).areaRoom;
								sumOfHumidities = sumOfHumidities + Creator.getMinimumHumRoom(newHouse.rooms.get(i)) * newHouse.rooms.get(i).areaRoom;
								sumOfPonders = sumOfPonders + newHouse.rooms.get(i).areaRoom;
							}
							Double averageTemperatureOfHouse = sumOfTemperatures / sumOfPonders; //temperatura medie din ultima ora
							Double averageHumiditiesOfHouse = sumOfHumidities / sumOfPonders; //umiditatea medie din ultima ora
							
							//daca nu exista stocarea umiditatii in casa 
							if(global_humidity == 0.00) {
								
							//cazul cand porneste centrala
							if(averageTemperatureOfHouse < global_temperature){
								System.out.println(global_temperature);
								bw.write("YES");
								bw.newLine();
							}
							//cazul cand nu porneste centrala
							else {
								bw.write("NO");
								bw.newLine();
							}
							}
							//daca exista stocarea umiditatii in casa
							else {
								//porneste centrala
								if(global_humidity < averageHumiditiesOfHouse) {
									bw.write("YES");
									bw.newLine();
								}
								//nu porneste centrala
								else {
									bw.write("NO");
									bw.newLine();
								}
							}
							break;
						case "TEMPERATURE":
							current_token = tokenizer.nextToken(); //noua temperatura globala
							global_temperature = Double.parseDouble(current_token); //pe care o retin aici
							break;
						case "LIST":
							current_token = tokenizer.nextToken(); //id-ul camerei din care sa se faca listarea
							String next1_token = tokenizer.nextToken(); //timestamp-ul care e capatul inferior al intervalului
							String next2_token = tokenizer.nextToken(); //timestamp-ul care e capatul superior al intervalului
							//parcurg fiecare camera si aleg camera in care sa se faca listarea dupa id-ul retinut mai sus
							for(int i = 0; i < newHouse.rooms.size(); i++)
								if(newHouse.rooms.get(i).roomId.equals(current_token)) {
									bw.write(Creator.list(newHouse.rooms.get(i), Long.parseLong(next1_token), Long.parseLong(next2_token), current_token));
									System.out.println(Creator.list(newHouse.rooms.get(i), Long.parseLong(next1_token), Long.parseLong(next2_token), current_token));
									bw.newLine();
								}
							break;
					}
					
				}
				
				current_line = br.readLine();
			}
			
			br.close();
			bw.close();
		}
		catch(Exception e) {
			
			e.printStackTrace();
			System.out.println("Error parsing file"); 
		}
	}

}
