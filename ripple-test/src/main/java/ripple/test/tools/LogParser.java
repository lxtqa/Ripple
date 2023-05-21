// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

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
            String dir = "D:\\new-test-results";
            String outputDir = "D:\\new-test-results\\clean";
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
                    pushing.add("timestamp,second,latency");
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
                    Path pushingPath = Path.of(outputDir, originalFileName.substring(0, originalFileName.lastIndexOf(".txt")) + "-pushing.csv");
                    System.out.println("Saving to " + pushingPath);
                    Files.write(pushingPath, pushing);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
