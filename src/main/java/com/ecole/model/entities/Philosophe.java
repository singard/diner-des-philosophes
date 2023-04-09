package com.ecole.model.entities;

import java.util.Random;
import java.util.concurrent.Semaphore;

import com.ecole.ApplicationMain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public class Philosophe implements Runnable {

	private int numero;
	private Etat etat;
	private Semaphore[] baguettes;

	@Override
	public void run() {
		 //calcule à chaque passage, le temps qui s'est écoulé pour s'arrêter à la durée paramétrée
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;
		while (elapsedTime < ApplicationMain.EXECUTION_TIME_IN_SEC * 1000) {
			try {
				/* Si le philosophe change d'état principale (manger ou penser) alors une diré aléatoire lui est attribuer 
				 * qui sera la durer qu'il va attendre avant de changer d'état.
				 * C'est nombre aléatoire entre 1 et 3 seconde
				 */
				if (etat==Etat.MANGER || etat==Etat.PENSER) {
					Random random = new Random();
					int randomNumber = random.nextInt(1+numero%3) + 1;
					Thread.sleep(randomNumber * 1000);
				}
				log.trace("numéro "+numero+" fait des choses");

				switch (etat) {
				case PENSER:

					synchronized (baguettes) {
						if (fourchettesDisponibles()) {
							// Acquisition des baguettes
							baguettes[numero ].acquire(); // Baguette de droite
							baguettes[(numero +1) % ApplicationMain.NB_PHILOSOPHE].acquire(); // Baguette de gauche

							etat = Etat.MANGER;
							changerEtat();

						}else {
							etat = Etat.BESOIN_DE_MANGER;
							changerEtat();
						}
					}
					break;

				case MANGER:
					// Libération des baguettes
					baguettes[numero].release(); // Baguette de droite
					baguettes[(numero +1) % ApplicationMain.NB_PHILOSOPHE].release(); // Baguette de gauche

					etat = Etat.PENSER;
					changerEtat();
					break;

				case BESOIN_DE_MANGER:
					synchronized (baguettes) { 
						if (fourchettesDisponibles()) {
							// Acquisition des baguettes
							baguettes[numero].acquire(); // Baguette droite
							baguettes[(numero +1) % ApplicationMain.NB_PHILOSOPHE].acquire(); // Baguette gauche

							etat = Etat.MANGER;
							changerEtat();							
						}
					}
					break;
				}	
			}catch(Exception e) {
				e.printStackTrace();
			}
			//calcule à chaque passage, le temps qui s'est écoulé pour s'arrêter à la durée paramétrée
			elapsedTime = System.currentTimeMillis() - startTime;
		}
	}

	private  void changerEtat() throws InterruptedException {
		log.debug("je suis numéro "+numero+" j'ai changer d'état "+etat);
	}

	private boolean fourchettesDisponibles() {
		// Cette action est dans un synchronized cela veut dire que les philosophes peuvent y avoir accés une personne à la fois
		return baguettes[numero ].availablePermits() > 0 && baguettes[(numero +1) % ApplicationMain.NB_PHILOSOPHE].availablePermits() > 0;
	}






}
