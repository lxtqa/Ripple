package ripple.test.tools;

import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LogParser {
    public static void main(String[] args) {
        try {
            String dir = "D:\\SynologyDrive\\GitHub\\Ripple\\test-results\\original";
            String outputDir = "D:\\SynologyDrive\\GitHub\\Ripple\\test-results\\clean";
            List<Path> fileList = Files.list(Path.of(dir)).toList();
            for (Path path : fileList) {
                if (path.getFileName().toString().endsWith("txt")) {
                    System.out.println("Parsing " + path);
                    List<String> content;
                    try {
                        content = Files.readAllLines(path, StandardCharsets.UTF_8);
                    } catch (MalformedInputException exception) {
                        content = Files.readAllLines(path, StandardCharsets.ISO_8859_1);
                    }
                    List<String> subscribing = new ArrayList<>();
                    List<String> pushing = new ArrayList<>();
                    for (String line : content) {
                        if (line.contains("] Subscribed:") ||
                                line.contains("] Unsubscribed:")) {
                            subscribing.add(line.substring(line.indexOf("] ") + 2, line.indexOf("ms"))
                                    .replace("Subscribed: ", ""));
                        } else if (line.contains("] Received: ")) {
                            pushing.add(line.substring(line.indexOf("] ") + 2, line.indexOf("ms"))
                                    .replace("Received: ", ""));
                        }
                    }
                    if (!Files.exists(Path.of(outputDir))) {
                        Files.createDirectories(Path.of(outputDir));
                    }
                    String originalFileName = path.getFileName().toString();
                    Path subscribingPath = Path.of(outputDir, originalFileName.substring(0, originalFileName.lastIndexOf(".txt")) + "-sub.txt");
                    System.out.println("Saving to " + subscribingPath);
                    Files.write(subscribingPath, subscribing);
                    Path pushingPath = Path.of(outputDir, originalFileName.substring(0, originalFileName.lastIndexOf(".txt")) + "-pushing.txt");
                    System.out.println("Saving to " + pushingPath);
                    Files.write(pushingPath, pushing);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
