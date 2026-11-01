

public final class Balance {
    private final long summa;
    private final Currency currency;

    Balance(long summa, Currency currency){
        if (summa < 0){
            throw new IllegalArgumentException("Баланс не может быть отрицательным");//исключения?????
        }
        if (currency== null){
            throw new IllegalArgumentException("Валюта не может быть пустой");

        }
        this.summa = summa;
        this.currency = currency;
    }

    public long getSumma(){
        return summa;
    }

    public Currency getCurrency(){
        return currency;
    }

    public Balance add(long newsumma){
        if (newsumma < 0){
            throw new IllegalArgumentException("Нельзя прибавлять отрициательные значения");

        }
        return new  Balance(summa+newsumma, currency);
    }

    public Balance subtract(long newsumma){
        if(newsumma < 0){
            throw new IllegalArgumentException("Нельзя вычесть отрицательную сумму");
        }
        if (newsumma > summa){
            throw new IllegalStateException("Недостаточно средств: есть " + summa + ", нужно " + newsumma);
        }
        return new Balance(summa-newsumma, currency);
    }

    public Balance convert(Currency val){
        if(val == null){
            throw new IllegalArgumentException("Пусто, Должна быть валюта");
        }
        if (val == currency){
            return this; /// нужна ли вообще это проверка????????????7
        }
        long vkop = summa * currency.getPriceInRublesKopecks()/100;
        long vnewval = vkop*100/ val.getPriceInRublesKopecks();
        return new Balance(vnewval, val);
    }

    public long summavKop(){
        return summa * currency.getPriceInRublesKopecks()/100;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", summa / 100.0, currency.getCode());
    }


}
