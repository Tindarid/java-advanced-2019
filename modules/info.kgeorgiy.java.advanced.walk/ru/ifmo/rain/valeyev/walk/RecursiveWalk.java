package ru.ifmo.rain.valeyev.walk;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class RecursiveWalk {
    private static final int FNV_INIT = 0x811c9dc5;
    private static final int FNV_PRIME = 0x01000193;
    private static final String ERROR_HASH = "00000000";

    private static String getHash(String filename) {
        try (FileInputStream in = new FileInputStream(filename)) {
            int hash = FNV_INIT;
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) >= 0) {
                for (int i = 0; i < read; ++i) {
                    hash = (hash * FNV_PRIME) ^ (buffer[i] & 0xff);
                }
            }
            return String.format("%08x", hash);
        } catch (IOException | SecurityException e) {
            return ERROR_HASH;
        }
    }

    private static void resolve(BufferedWriter out, String name, boolean good) throws IOException {
        out.write((good ? getHash(name) : ERROR_HASH) + " " + name);
        out.newLine();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: RecursiveWalk [input file, output file]");
            return;
        }
        try (
            BufferedReader in = Files.newBufferedReader(Paths.get(args[0]));
            BufferedWriter out = Files.newBufferedWriter(Paths.get(args[1]));
        ) {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    try {
                        Files.walkFileTree(Paths.get(line), new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                throws IOException
                            {
                                resolve(out, file.toString(), true);
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult visitFileFailed(Path file, IOException e) 
                                throws IOException
                            {
                                resolve(out, file.toString(), false);
                                return FileVisitResult.CONTINUE;
                            }
                        });
                    } catch (InvalidPathException | SecurityException e) {
                        resolve(out, line, false);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (InvalidPathException e) {
            System.out.println("Error with path " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Cannot open file " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("Cannot open file (security reason) " + e.getMessage());
        }
    }
}
