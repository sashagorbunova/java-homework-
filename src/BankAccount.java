import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private final String user;
    private Balance balance;
    private final long withdrawLimitRubKopecks;
    private boolean blocked;
    private String blockReason;
    private final List<String> history = new ArrayList<>();

    public BankAccount(String user, Balance balance, long withdrawLimitRubKopecks) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("Пользователь не может быть пустым");
        }
        if (balance == null) {
            throw new IllegalArgumentException("Баланс не может быть null");
        }
        if (withdrawLimitRubKopecks < 0) {
            throw new IllegalArgumentException("Лимит не может быть отрицательным");
        }
        this.user = user;
        this.balance = balance;
        this.withdrawLimitRubKopecks = withdrawLimitRubKopecks;
    }

    public String getUser() { return user; }
    public Balance getBalance() { return balance; }
    public boolean isBlocked() { return blocked; }
    public String getBlockReason() { return blockReason; }

    public List<String> getHistory() {
        return List.copyOf(history);
    }

    public void block(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Причина блокировки не может быть пустой");
        }
        if (blocked) {
            throw new IllegalStateException("Счёт уже заблокирован: " + user);
        }
        blocked = true;
        blockReason = reason;
        history.add("Блокировка: " + reason);
    }

    public void blockByLostCard() {
        block("Потеря карты");
    }

    public void unblock() {
        if (!blocked) {
            throw new IllegalStateException("Счёт не заблокирован: " + user);
        }
        blocked = false;
        blockReason = null;
        history.add("Разблокировка");
    }

    public void deposit(long amount) {
        checkNotBlocked();
        checkPositiveAmount(amount, "пополнения");
        balance = balance.add(amount);
        history.add("Пополнение: +" + formatAmount(amount));
    }

    public void withdraw(long amount) {
        checkNotBlocked();
        checkPositiveAmount(amount, "снятия");
        checkLimit(amount);
        balance = balance.subtract(amount);
        history.add("Снятие: -" + formatAmount(amount));
    }

    public void transferTo(BankAccount target, long amount) {
        if (target == null) {
            throw new IllegalArgumentException("Получатель не может быть null");
        }
        if (target == this) {
            throw new IllegalArgumentException("Нельзя перевести самому себе");
        }
        checkPositiveAmount(amount, "перевода");
        checkNotBlocked();
        target.checkNotBlocked();
        checkLimit(amount);

        // конвертируем сумму в валюту получателя
        Balance debit = new Balance(amount, balance.getCurrency());
        Balance credit = debit.convert(target.balance.getCurrency());

        balance = balance.subtract(amount);
        target.balance = target.balance.add(credit.getSumma());

        history.add("Перевод " + target.user + ": -" + debit);
        target.history.add("Перевод от " + user + ": +" + credit);
    }

    private void checkNotBlocked() {
        if (blocked) {
            throw new IllegalStateException(
                    "Счёт заблокирован: " + user
                            + (blockReason != null ? " (" + blockReason + ")" : ""));
        }
    }

    private void checkPositiveAmount(long amount, String operation) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма " + operation + " не может быть отрицательной");
        }
    }

    private void checkLimit(long amount) {
        long amountInRub = new Balance(amount, balance.getCurrency()).summavKop();
        if (amountInRub > withdrawLimitRubKopecks) {
            throw new IllegalStateException(
                    "Превышен лимит на операцию: " + (amountInRub / 100.0) + " RUB > "
                            + (withdrawLimitRubKopecks / 100.0) + " RUB");
        }
    }

    private String formatAmount(long kopecks) {
        return new Balance(kopecks, balance.getCurrency()).toString();
    }

    @Override
    public String toString() {
        return user + ": " + balance
                + (blocked ? " (ЗАБЛОКИРОВАН: " + blockReason + ")" : "");
    }
}