package com.ecole;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import com.ecole.model.entities.Etat;
import com.ecole.model.entities.Philosophe;
import com.ecole.service.ChangeStat;

public class ApplicationMain {

	public static void main(String[] args) {

		 Scanner scanner = new Scanner(System.in);
	        List<Etat> listFutureResult = new ArrayList<Etat>();
	        List<Philosophe> listPhilosophes = new ArrayList<Philosophe>();

	        System.out.print("Combien de philosophes voulez-vous créer ? ");
	        int nbPhilosophes = scanner.nextInt();
//	        int n = (nbPhilosophes % 2 == 0) ? nbPhilosophes / 2 : (nbPhilosophes + 1) / 2;
	        int n = nbPhilosophes ;
	        System.out.println("nombre n"+n);
	        Semaphore eatingSem[] = new Semaphore[n];
	        for (int i = 0; i < n; i++) {
	            eatingSem[i] = new Semaphore(1);
	            System.out.println("create semaphor");
	        }

	        // Création des philosophes et attribution des voisins
	        Philosophe[] philosophes = new Philosophe[nbPhilosophes];
	        for (int i = 0; i < nbPhilosophes; i++) {
	           
	            philosophes[i] = new Philosophe(i + 1, Etat.PENSER, eatingSem,nbPhilosophes);
	            }
	        
	        for (int i = 0; i < nbPhilosophes; i++) {
	            Philosophe voisinGauche = philosophes[(i + nbPhilosophes - 1) % nbPhilosophes];
	            Philosophe voisinDroite = philosophes[(i + 1) % nbPhilosophes];
	            philosophes[i] = new Philosophe(i , Etat.PENSER, eatingSem,nbPhilosophes);
//	            System.out.println("Philosophe " + philosophes[i].getNumero() + " voisinGauche " + philosophes[i].getVoisinGauche().getNumero() + " voisinDroite " + philosophes[i].getVoisinDroite().getNumero());
	        }



		ExecutorService executor = Executors.newFixedThreadPool(nbPhilosophes);
		CompletionService<Etat> completionService = new ExecutorCompletionService<>(executor);

		for (Philosophe philosophe : philosophes) {
			listPhilosophes.add(philosophe);
			completionService.submit(philosophe, null);

			System.out.println("create philosophe");
		}

		try {
			int numIterations = 20;
			for (int i = 0; i < numIterations; i++) {

				synchronized (completionService) {
					System.out.println("\n\r------------------------------------------------");
					for (Philosophe philosophe : listPhilosophes) {
						System.out.flush();
						System.out.print("| P " + philosophe.getNumero() + " : " + philosophe.getEtat());
						System.out.flush();
					}
				}
				System.out.println("\n\r------------------------------------------------");
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("fin du programme");
		executor.shutdown();

	}


}
