import java.io.*;
import java.util.*;

public class TextFormatter {
    private final String inputFile;
    private final String outputFile;
    private final int width;

    public TextFormatter(String inputFile, String outputFile, int width) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.width = width;
    }

    public void formatText() throws IOException {
        List<String> words = readWords();
        List<String> lines = alignLines(words);
        writeLines(lines);
    }

    private List<String> readWords() throws IOException {
        List<String> words = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            for (String word : parts) {
                if (!word.isEmpty()) words.add(word);
            }
        }
        reader.close();
        return words;
    }

    private List<String> alignLines(List<String> words) {
        List<String> result = new ArrayList<>();
        List<String> currentLine = new ArrayList<>();
        int currentLength = 0;

        for (String word : words) {
            if (currentLength + word.length() + currentLine.size() <= width) {
                currentLine.add(word);
                currentLength += word.length();
            } else {
                result.add(justifyLine(currentLine, currentLength));
                currentLine.clear();
                currentLine.add(word);
                currentLength = word.length();
            }
        }

        if (!currentLine.isEmpty()) {
            result.add(leftAlignLine(currentLine));
        }

        return result;
    }

    private String justifyLine(List<String> words, int totalWordLength) {
        if (words.size() == 1) {
            return padRight(words.get(0), width);
        }

        int totalSpaces = width - totalWordLength;
        int gaps = words.size() - 1;
        int spacePerGap = totalSpaces / gaps;
        int extraSpaces = totalSpaces % gaps;

        StringBuilder line = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            line.append(words.get(i));
            if (i < gaps) {
                int spaces = spacePerGap + (i < extraSpaces ? 1 : 0);
                line.append(" ".repeat(spaces));
            }
        }
        return line.toString();
    }

    private String leftAlignLine(List<String> words) {
        String line = String.join(" ", words);
        return padRight(line, width);
    }

    private String padRight(String line, int targetLength) {
        return line + " ".repeat(targetLength - line.length());
    }

    private void writeLines(List<String> lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }
}
