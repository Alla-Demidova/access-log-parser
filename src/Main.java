import java.util.Scanner;
//
public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число:");
        int number1 = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        int number2 = new Scanner(System.in).nextInt();
        int add = number1 + number2;
        int sub = number1 - number2;
        int mult = number1 * number2;
        double div = (double) number1 / number2;
        System.out.println("Сумма чисел: " + add);
        System.out.println("Разность чисел: " + sub);
        System.out.println("Произведение чисел: " + mult);
        System.out.println("Деление чисел: " + div);
    }
}
