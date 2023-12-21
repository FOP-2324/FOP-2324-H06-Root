package h06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) throws IOException {
        var path = "/home/nyanyan/Downloads/rubrics_H06/rubrics/csv"; // "Gesamt,32,18,"
               Files.list(Path.of(path)).filter(p -> {
            try {
                var lines = Files.lines(p).toList();
                var last = lines.get(lines.size() - 1);
                var parts = last.split(",");
                return parts[2].equals("0") || parts[2].equals("1") || parts[2].equals("2");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);
    }
}
