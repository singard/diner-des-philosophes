package com.ecole.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MoveTask implements Runnable {
    private File source;
    private File targetFolder;

    @Override
    public void run() {

	try {
	    System.out.println("thread " + Thread.currentThread().getId() + " move le fichier " + source.getName());
	    File targetFile = new File(targetFolder.getAbsolutePath() + File.separator + source.getName());
	    Files.move(source.toPath(), targetFile.toPath(), StandardCopyOption.ATOMIC_MOVE);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public MoveTask(File source, File targetFolder) {
	super();
	this.source = source;
	this.targetFolder = targetFolder;
    }

}
