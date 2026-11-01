public class Main {
    public static void main(String[] args) {
        BankAccount ivan = new BankAccount("Саша", new Balance(100000, Currency.RUB), 50000);
        BankAccount maria = new BankAccount("Карина", new Balance(10000, Currency.USD), 500000);
        BankAccount petr = new BankAccount("Антон", new Balance(10000, Currency.RUB), 500000);

        System.out.println("Начальное состояние:");
        System.out.println(ivan);
        System.out.println(maria);
        System.out.println(petr);

        System.out.println();
        System.out.println("Пополнение на счёт Ивана: 200 руб");
        ivan.deposit(20000);

        System.out.println("Снятие  со счёта Ивана: 100 руб");
        ivan.withdraw(10000);
        System.out.println(ivan);

        System.out.println();
        System.out.println("Перевод от Ивана Марии: 50 руб");
        ivan.transferTo(maria, 5000);
        System.out.println(ivan);
        System.out.println(maria);

        System.out.println();
        System.out.println("История операций Ивана:");
        for (String s : ivan.getHistory()) System.out.println(s);

        System.out.println();
        System.out.println("История операций Марии:");
        for (String s : maria.getHistory()) System.out.println(s);

        System.out.println();
        System.out.println("Иван потерял карту, блокируем счёт");
        ivan.blockByLostCard();
        System.out.println(ivan);

        System.out.println();
        System.out.println("Пробуем снять деньги с заблокированного счёта:");
        try {
            ivan.withdraw(1000);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println("Карта нашлась, разблокируем счёт");
        ivan.unblock();
        System.out.println(ivan);

        System.out.println();
        System.out.println("Проверка ошибок:");

        System.out.println("Пополнение на отрицательную сумму:");
        try {
            ivan.deposit(-10);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Снятие больше лимита:");
        try {
            ivan.withdraw(60000);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Перевод на null:");
        try {
            ivan.transferTo(null, 10);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Перевод самому себе:");
        try {
            ivan.transferTo(ivan, 10);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Снятие суммы больше баланса:");
        try {
            petr.withdraw(20000);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println("Итоговое состояние:");
        System.out.println(ivan);
        System.out.println(maria);
        System.out.println(petr);
    }
}