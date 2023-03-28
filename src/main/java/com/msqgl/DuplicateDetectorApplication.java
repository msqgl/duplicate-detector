package com.msqgl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@SpringBootApplication
@Slf4j
public class DuplicateDetectorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DuplicateDetectorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java -jar duplicate-files-checker.jar <directory>");
            return;
        }
        String directory = args[0];
        checkDuplicateFiles(directory);

    }

    private void checkDuplicateFiles(String directory) throws IOException, NoSuchAlgorithmException {
        Map<Long, List<Path>> filesBySize = new HashMap<>();
        Files.walk(Path.of(directory))
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        long size = Files.size(path);
                        filesBySize.computeIfAbsent(size, k -> new ArrayList<>()).add(path);
                    } catch (IOException e) {
                        System.err.println("Error getting size of file " + path + ": " + e.getMessage());
                    }
                });

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        Map<String, List<Path>> filesByHash = new HashMap<>();
        for (List<Path> files : filesBySize.values()) {
            if (files.size() > 1) {
                for (Path file : files) {
                    try {
                        byte[] content = Files.readAllBytes(file);
                        byte[] hash = md.digest(content);
                        String hashStr = Base64.getEncoder().encodeToString(hash);
                        filesByHash.computeIfAbsent(hashStr, k -> new ArrayList<>()).add(file);
                    } catch (IOException e) {
                        System.err.println("Error reading content of file " + file + ": " + e.getMessage());
                    }
                }
            }
        }

        List<List<Path>> collect = filesByHash.values().stream().filter(files -> files.size() > 1).toList();
        for (List<Path> files : collect) {
            if (files.size() > 1) {
                log.info("Duplicate files:");
                files.forEach(path -> log.info(path.toString()));
            }
        }
    }
}
