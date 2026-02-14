import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        int count = 0;
        while (true) {
            System.out.println("Введите путь к файлу: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (fileExists == false || isDirectory == true) {
                System.out.println("Файл не существует или это не путь к файлу");
                continue;
            }

            System.out.println("Путь указан верно");
            count++;
            System.out.println("Это файл номер " + count);


            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);

                int totalLines = 0;
                int maxLength = 0;
                int minLength = Integer.MAX_VALUE;
                String line;

                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length > 1024) {
                        throw new LineTooLongException(
                                "Обнаружена строка длиной " + length +
                                        " символов, что превышает лимит в 1024 символа"
                        );
                    }

                    totalLines++;

                    if (length > maxLength) {
                        maxLength = length;
                    }
                    if (length < minLength) {
                        minLength = length;
                    }
                }

                reader.close();


                System.out.println("Общее количество строк в файле: " + totalLines);
                System.out.println("Длина самой длинной строки: " + maxLength);
                System.out.println("Длина самой короткой строки: " +
                        (minLength == Integer.MAX_VALUE ? 0 : minLength));

            } catch (LineTooLongException e) {
                System.err.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Неизвестная ошибка: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
