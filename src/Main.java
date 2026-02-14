import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        int fileCounter = 0;
        while (true) {
            System.out.println("Введите путь к файлу: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExists || isDirectory) {
                System.out.println("Файл не существует или это не путь к файлу");
                continue;
            }

            System.out.println("Путь указан верно");
            fileCounter++;
            System.out.println("Это файл номер " + fileCounter);

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);

                int totalLines = 0;
                int yandexBotCount = 0;
                int googleBotCount = 0;
                String line;

                while ((line = reader.readLine()) != null) {
                    int length = line.length();

                    if (length > 1024) {
                        throw new LineTooLongException(
                                "Строка длиной " + length +
                                        " символов превышает лимит в 1024 символа"
                        );
                    }

                    totalLines++;


                    String userAgent = extractUserAgent(line);
                    if (userAgent != null) {
                        String botName = extractBotName(userAgent);

                        if ("YandexBot".equals(botName)) {
                            yandexBotCount++;
                        } else if ("Googlebot".equals(botName)) {
                            googleBotCount++;
                        }
                    }
                }

                reader.close();


                System.out.println("Общее количество строк в файле: " + totalLines);

                if (totalLines > 0) {
                    double yandexShare = (double) yandexBotCount / totalLines * 100;
                    double googleShare = (double) googleBotCount / totalLines * 100;

                    System.out.printf("Доля запросов от YandexBot: %.2f%% (%d из %d)%n",
                            yandexShare, yandexBotCount, totalLines);
                    System.out.printf("Доля запросов от Googlebot: %.2f%% (%d из %d)%n",
                            googleShare, googleBotCount, totalLines);
                } else {
                    System.out.println("Файл пуст");
                }

            } catch (LineTooLongException e) {
                System.err.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
                break;
            } catch (IOException e) {
                System.err.println("Ошибка ввода-вывода: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Общая ошибка: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private static String extractUserAgent(String logLine) {
        int lastQuoteIndex = logLine.lastIndexOf('"');
        if (lastQuoteIndex > 0) {
            int firstQuoteIndex = logLine.lastIndexOf('"', lastQuoteIndex - 1);
            if (firstQuoteIndex >= 0) {
                return logLine.substring(firstQuoteIndex + 1, lastQuoteIndex);
            }
        }
        return null;
    }


    private static String extractBotName(String userAgent) {
        try {
            int openBracket = userAgent.indexOf('(');
            int closeBracket = userAgent.indexOf(')', openBracket);

            if (openBracket >= 0 && closeBracket > openBracket) {
                String bracketsContent = userAgent.substring(openBracket + 1, closeBracket);

                String[] parts = bracketsContent.split(";");

                if (parts.length >= 2) {
                    String fragment = parts[1].trim();

                    int slashIndex = fragment.indexOf('/');
                    if (slashIndex > 0) {
                        return fragment.substring(0, slashIndex);
                    }
                    return fragment;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}