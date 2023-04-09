package com.ecole;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.ecole.model.entities.Etat;
import com.ecole.model.entities.Philosophe;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationMain {
	
	public static int NB_PHILOSOPHE = 0;
	public static int EXECUTION_TIME_IN_SEC = 0;
	
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		List<Philosophe> listPhilosophes = new ArrayList<Philosophe>();
		//demande le nombre de philosophes à la table
		log.info("Combien de philosophes voulez-vous créer ? ");
		NB_PHILOSOPHE = scanner.nextInt();
		int nbBagettes = NB_PHILOSOPHE ;
		log.info("quelle durer le repas va-t-il durer ? ");
		EXECUTION_TIME_IN_SEC = scanner.nextInt();
		scanner.close();
		log.info("nombre n = "+nbBagettes);
		/* Création d'une liste de semaphore corespondant au nombre de couverts sur 
		 * la table, qui correspond aussi au nombre de philosophes présent sur cette même table
		 */
		Semaphore eatingSem[] = new Semaphore[nbBagettes];
		for (int i = 0; i < nbBagettes; i++) {
			eatingSem[i] = new Semaphore(1);
			log.info("create semaphor");
		}
		/* Création des philosophes autour de la table et
		 * création de la pool de threads. Un thread par philosophe
		 */
		Philosophe[] philosophes = new Philosophe[NB_PHILOSOPHE];
		ExecutorService executor = Executors.newFixedThreadPool(NB_PHILOSOPHE);

		for (int i = 0; i < NB_PHILOSOPHE; i++) {

			philosophes[i] = new Philosophe(i , Etat.PENSER, eatingSem);
			executor.execute(philosophes[i]);
			listPhilosophes.add(philosophes[i]);
			log.info("create philosophe");
		}

		try {
			for (int i = 0; i < EXECUTION_TIME_IN_SEC; i++) {
				log.info("------------------------------------------------");
				for (Philosophe philosophe : listPhilosophes) {

					log.info("| P " + philosophe.getNumero() + " : " + philosophe.getEtat());
				}
				log.info("------------------------------------------------");
				Thread.sleep(1000);
			}
			
			executor.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("fin du programme");
	}


}
