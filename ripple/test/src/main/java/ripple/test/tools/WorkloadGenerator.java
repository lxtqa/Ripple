package ripple.test.tools;

import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkloadGenerator {
    private WorkloadGenerator() {

    }

    public static String generateKeyValuePair(int count, int sizePerEntry) {
        StringBuilder builder = new StringBuilder();
        String dict = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
        Random random = new Random();
        int i = 0;
        int j = 0;
        for (i = 0; i < count; i++) {
            int keySize = random.nextInt(sizePerEntry - 3) + 1;
            int valueSize = sizePerEntry - 1 - 1 - keySize;
            for (j = 0; j < keySize; j++) {
                builder.append(dict.charAt(random.nextInt(dict.length())));
            }
            builder.append("=");
            for (j = 0; j < valueSize; j++) {
                builder.append(dict.charAt(random.nextInt(dict.length())));
            }
            builder.append(";");
        }
        System.out.println(builder.toString());
        return builder.toString();
    }

    public static void main(String[] args) {
        WorkloadGenerator.generateKeyValuePair(5, 10);
    }
}
