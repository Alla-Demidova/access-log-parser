import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        int fileCounter = 0;
        Statistics stats = new Statistics();

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

            // Чтение файла с обработкой исключений
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);

                int totalLines = 0;
                int yandexBotCount = 0;
                int googleBotCount = 0;
                String line;

                while ((line = reader.readLine()) != null) {
                    int length = line.length();

                    // Проверка на превышение лимита длины строки
                    if (length > 1024) {
                        throw new LineTooLongException(
                                "Строка длиной " + length +
                                        " символов превышает лимит в 1024 символа"
                        );
                    }

                    totalLines++;

                    // Создаем объект LogEntry для парсинга строки
                    LogEntry entry = new LogEntry(line);

                    // Добавляем запись в статистику
                    stats.addEntry(entry);

                    // Подсчет ботов через UserAgent
                    String browser = entry.getUserAgent().getBrowser();
                    if ("YandexBot".equals(browser)) {
                        yandexBotCount++;
                    } else if ("Googlebot".equals(browser)) {
                        googleBotCount++;
                    }
                }

                reader.close();

                // Вывод статистики по ботам
                System.out.println("Общее количество строк в файле: " + totalLines);

                if (totalLines > 0) {
                    double yandexShare = (double) yandexBotCount / totalLines * 100;
                    double googleShare = (double) googleBotCount / totalLines * 100;

                    System.out.printf("Доля запросов от YandexBot: %.2f%% (%d из %d)%n",
                            yandexShare, yandexBotCount, totalLines);
                    System.out.printf("Доля запросов от Googlebot: %.2f%% (%d из %d)%n",
                            googleShare, googleBotCount, totalLines);

                    // Вывод статистики трафика
                    System.out.println("\n--- Статистика трафика ---");
                    System.out.println("Общий объем трафика: " + stats.getTotalTraffic() + " байт");
                    System.out.println("Первая запись: " + stats.getMinTime());
                    System.out.println("Последняя запись: " + stats.getMaxTime());
                    System.out.printf("Средний трафик в час: %.2f байт/час%n", stats.getTrafficRate());
                } else {
                    System.out.println("Файл пуст");
                }

            } catch (LineTooLongException e) {
                System.err.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
                // Прерываем выполнение при слишком длинной строке
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
}