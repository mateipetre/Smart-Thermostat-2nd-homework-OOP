MATEI Alexandru-Petrut, 325CB
		
			============== Termostat inteligent ================

     Am ales sa folosesc pentru implementare structurile ArrayList si Vector din Java Collection 
 Framework. Cu ajutorul ArrayList, stochez fiecare camera (cu informartiile aferente) intr-un ArrayList de
 camere si stochez cele 24 de ore (privite ca niste intervale) fata de timpul de referinta (in urma) intr-un
 ArrayList de intervale. Cu ajutorul Vector, stochez temperaturile observate la anumite timestamp-uri intr-un 
 Vector de valori de tip Double, timestamp-urile corespunzatoare acestora intr-un Vector de valori de tip Long,
 umiditatiile intr-un Vector de valori de tip Double si timestamp-urile corespunzatoare acestora intr-un Vector
 de valori de tip Long. Am ales asa pentru ca Vector este o structura sincronizata si am nevoie de acest lucru
 pentru a stoca cumva "in paralel" temperaturile cu timestamp-urile corespunzatoare, respectiv umiditatiile cu
 timestamp-urile corespunzatoare. De exemplu cand salvez temperatura 22.00 la timestampul 1573706510, imi doresc
 ca in vectorul temperaturesFromRoom 22.00 sa fie pe pozitia 0, dar si in vectorul timestampsFromRoom 1573706510
 sa fie pe pozitia 0. Pentru stocarea datelor despre camere si a celor 24 de intervale pentru fiecare din acestea,
 am ales ArrayList pentru ca este o structura mai rapida. Ea trebuie sa retina multe informatii care se ramifica
 in stocari de alte informatii, spre deosebire de vectorii de temperaturi, timestamp-uri sau umiditati.

    Am creat 5 clase pentru implementarea temei - Test, Creator, House, Room si Range.
 
 - Clasa House retine un ArrayList de camere -  ArrayList<Room> rooms, pe care il instantiez in constructorul
 acestei clase. Mai contine metoda IsDeviceValid care intoarce indicele camerei in care s-a gasit dispozitivul,
 al carui id a fost citit.

 - Clasa Room retine un ArrayList de 24 de intervale - ArrayList<Range> ranges, pe care il instantiez in
 constructorul acestei clase. Mai contine urmatoarele campuri:
		- roomId - String care reprezinta id-ul camerei
		- deviceId - String care reprezinta id-ul dispozitivului
		- areaRoom - Integer care reprezinta aria camerei
		- reference_timestamp - timestamp-ul de referinta care este tip long

 - Clasa Range retine 4 vectori:
		- Vector<Double> temperaturesFromRoom - temperaturile observate in fiecare ora
		- Vector<Long> timestampFromRoom - timestamp-urile corespunzatoare temperaturilor observate
		- Vector<Double> humiditiesFromRoom - umiditatiile observate in fiecare ora
		- Vector<Long> timestampsFromRoomHum - timestamp-urile corespunzatoare umiditatiilor observate
		- constructorul fara parametrii care instantiaza cei 4 vectori

- Clasa Creator in care am creat majoritatea metodelor pe care le folosesc pentru stocarea temperaturilor,
 a timestamp-urilor, umiditatiilor, diverse verificari etc. Contine metodele: 
		- IfValidTimestamp - verifica daca timestamp-ul la care s-a observat temperatura / umiditatea 
		                     e din ultimele 24 de ore fata de referinta
		- addInRange - Metoda care adauga temperatura si timestamp-ul la care s-a observat aceasta 
			       in cei 2 vectori pt cazul temperaturilorsi timestamp-uri pt temperaturi ai 
			       intervalului corespunzator fiecarei ore (una din cele 24 fata de cea de 
			       referinta)
		- addInRangeHum - Metoda care adauga umiditatea si timestamp-ul la care s-a observat aceasta
				in cei 2 vectori pt cazul umiditatiilor
		- getMinimumTempRoom - Metoda care intoarce temperatura minima din ultima ora in care s-au
					observat temperaturi in camera respectiva
		- getMinimumHumRoom -  Metoda care intoarce umiditatea minima din ultima ora in care s-au 
					observat umiditati in camera respectiva
		- verifyIfEmptyRoom - Metoda care verifica daca s-au observat temperaturi in camera respectiva
				     in ultimele 24 de ore
		- list - Metoda care afiseaza temperaturile corespunzatoare timestamp-urilor  din intervalul
			[BeginOfRange, EndOfRange] prin concatenarea acestora ca string-uri, sortate crescator
			pe intervale de o ora

- Clasa Test in care se gaseste metoda main si implementarea generala a temei. In aceasta clasa citesc din 
 fisierul de intrare therm.in si afisez informatiile cerute in fisierul de iesire therm.out. Sunt folosite
 metodele create in Creator pe care le utilizez in stocarea informatiilor despre temperatura, umiditate si
 timpul la care acestea au fost observate. Creez o instanta la o casa in care adaug toate camerele din fisier in
 vectorul de camere, apoi adaug cele 24 de intervale in fiecare camera unde stochez in cei 4 vectori ai 
 intervalelor temperaturile observate, umiditatiile observate si timestamp-urile corespunzatoare. Folosesc 
 un bloc de try-catch pentru a evita o eroare la deschiderea fisierului de input sau o eroare la incercarea
 citirii datelor din el pentru efectuarea parsarilor necesare pentru stocarea datelor. Folosesc StringTokenizer
 pentru obtinerea de tokens din fiecare linie citita din fisierul de intrare pentru simplitate. Folosesc un
 switch pentru implementarea fiecarei comenzi a termostatului si in fiecare caz se efectueaza implementarea
 necesara. Am ales ca toate metodele clasei Creator sa fie statice pentru a evita crearea unei instante la aceasta
 si deci sa ma ajute sa o folosesc intr-un cadru direct.

     Pentru mai multe detalii legate de implementare a se citi comentariile din codul fiecarei clase create.