package com.ecole.model.entities;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Philosophe implements Runnable {

	private int numero;
	private Etat etat;
	

	private Semaphore[] eatingSem;
	private int nbPhilosophes;
	private final Object lock = new Object(); 


	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {


			try {
				if (etat==Etat.MANGER || etat==Etat.PENSER) {

					Random random = new Random();
					int randomNumber = random.nextInt(1+numero%3) + 1;
					Thread.sleep(randomNumber * 1000);
				}
				//System.out.println("numéro "+numero+" fait des choses");


				switch (etat) {
				case PENSER:

//					System.out.println("numéro "+numero+" je veux manger mais : "+voisinGauche.getEtat()+" et "+voisinDroite.getEtat());
					synchronized (eatingSem) {
						if (fourchettesDisponibles()) {

							// Acquisition des baguettes
							eatingSem[numero ].acquire(); // Baguette droite
							eatingSem[(numero +1) % nbPhilosophes].acquire(); // Baguette gauche
							synchronized (etat) {
								etat = Etat.MANGER;
								changerEtat();
							}

						}else {
							synchronized (etat) {
								etat = Etat.BESOIN_DE_MANGER;
								changerEtat();
							}

						}

					}
					break;

				case MANGER:


					// Libération des baguettes
					eatingSem[numero].release(); // Baguette droite
					eatingSem[(numero +1) % nbPhilosophes].release(); // Baguette gauche
					synchronized (etat) {

						etat = Etat.PENSER;


					}

					changerEtat();
					break;

				case BESOIN_DE_MANGER:
					synchronized (eatingSem) { 
						if (fourchettesDisponibles()) {
							// Acquisition des baguettes
							eatingSem[numero].acquire(); // Baguette droite
							eatingSem[(numero +1) % nbPhilosophes].acquire(); // Baguette gauche
						synchronized (etat) {
							etat = Etat.MANGER;
							changerEtat();
						}
					}

					}
					


					break;
				}	


			}catch(Exception e) {
				e.printStackTrace();
			}
		}




	}


	public void changerEtat() throws InterruptedException {

//		System.out.println("je suis numéro "+numero+" j'ai changer d'état "+etat);
	}
	public boolean fourchettesDisponibles() {
	    return eatingSem[numero ].availablePermits() > 0 && eatingSem[(numero +1) % nbPhilosophes].availablePermits() > 0;
	}






}
