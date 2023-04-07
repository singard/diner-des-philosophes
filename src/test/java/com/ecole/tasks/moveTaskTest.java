package com.ecole.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

class moveTaskTest {


    @Test
    public void getTargetFolderTest() throws IOException {
	File targetFolder = new File("work/ecoute");
	Files.list(targetFolder.toPath())
		.forEach(f -> System.out.println(f.toAbsolutePath() + File.separator + f.getFileName()));
    }
}
