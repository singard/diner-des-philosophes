package com.ecole;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import com.ecole.model.entities.Etat;
import com.ecole.model.entities.Philosophe;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationMain {

	public static int NB_PHILOSOPHE = 0;
	public static int EXECUTION_TIME_IN_SEC = 0;
	
	static ExecutorService executor = null;
	static Philosophe[] philosophes = null;

	public static void main(String[] args) {

		userRequest();
		
		createThreadsAndSemaphores();
		
		for (int i = 0; i < EXECUTION_TIME_IN_SEC; i++) {

			display(philosophes);
		}


		executor.shutdown();
		System.out.println("fin du programme");
	}
	
	public static void createThreadsAndSemaphores() {
		/* Création d'une liste de semaphore corespondant au nombre de couverts sur 
		 * la table, qui correspond aussi au nombre de philosophes présent sur cette même table
		 */
		Semaphore eatingSem[] = new Semaphore[NB_PHILOSOPHE];
		for (int i = 0; i < NB_PHILOSOPHE; i++) {
			eatingSem[i] = new Semaphore(1);
			log.info("create semaphor");
		}
		/* Création des philosophes autour de la table et
		 * création de la pool de threads. Un thread par philosophe
		 */
		philosophes = new Philosophe[NB_PHILOSOPHE];
		executor = Executors.newFixedThreadPool(NB_PHILOSOPHE);

		for (int i = 0; i < NB_PHILOSOPHE; i++) {

			philosophes[i] = new Philosophe(i , Etat.PENSER, eatingSem);
			executor.execute(philosophes[i]);
			log.info("create philosophe");
		}

		
	}

	public static void display(Philosophe[] philosophes) {
		try {
			log.info("------------------------------------------------");
			for (Philosophe philosophe : philosophes) {

				log.info("| P " + philosophe.getNumero() + " : " + philosophe.getEtat());
			}
			log.info("------------------------------------------------");
			Thread.sleep(1000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void userRequest() {
		Scanner scanner = new Scanner(System.in);
		//demande le nombre de philosophes à la table
		log.info("Combien de philosophes voulez-vous créer ? ");
		NB_PHILOSOPHE = scanner.nextInt();
		log.info("quelle durer le repas va-t-il durer ? ");
		EXECUTION_TIME_IN_SEC = scanner.nextInt();
		scanner.close();
		log.info("nombre n = "+NB_PHILOSOPHE);

	}
}
