import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите путь к папке для архивации: ");
        String folderPath = scanner.nextLine();

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Ошибка: путь не существует или это не папка.");
            return;
        }

        String zipName = folder.getAbsolutePath() + ".zip";

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipName))) {
            Path sourcePath = folder.toPath();

            Files.walk(sourcePath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry entry = new ZipEntry(sourcePath.relativize(path).toString());
                        try {
                            zipOut.putNextEntry(entry);
                            Files.copy(path, zipOut);
                            zipOut.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Ошибка при добавлении файла: " + path);
                        }
                    });

            System.out.println("Архив создан: " + zipName);
        } catch (IOException e) {
            System.err.println("Ошибка архивации: " + e.getMessage());
        }
    }
}
