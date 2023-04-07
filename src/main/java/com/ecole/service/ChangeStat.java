package com.ecole.service;

import java.util.concurrent.Callable;

import com.ecole.model.entities.Etat;
import com.ecole.model.entities.Philosophe;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChangeStat implements Callable{
	
	private Philosophe philosophe;


	@Override
	public Etat call() throws Exception {
	
		return philosophe.getEtat();
	}

}
