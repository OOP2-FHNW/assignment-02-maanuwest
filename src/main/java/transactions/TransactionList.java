package transactions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Dieter Holz
 */
public class TransactionList {
    private final List<Transaction> allTransactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        allTransactions.add(transaction);
    }

    public int size() {
        return allTransactions.size();
    }

    public List<Transaction> transactionsInYear(int year) {
        return allTransactions.stream()
                .filter(x -> x.getYear() == year)
                .sorted(Comparator.comparingInt(Transaction::getValue))
                .collect(Collectors.toList());
    }

    public List<String> cities() {
        return allTransactions.stream()
                .map(x -> x.getTrader().getCity()).distinct()
                .collect(Collectors.toList());
    }

    /**
     * @param city the trader's city
     * @return all traders from given city sorted by name.
     */
    public List<Trader> traders(String city) {
        List<Trader> traders =
                allTransactions.stream()
                        .map(Transaction::getTrader)
                        .filter(x -> x.getCity() == city)
                        .sorted(Comparator.comparing(Trader::getName))
                        .distinct()
                        .collect(Collectors.toList());
        return traders;
    }

    /**
     * Returns a Map of all transactions.
     *
     * @return a Map with the year as key and a list of all transaction of this year as value
     */
    public Map<Integer, List<Transaction>> transactionsByYear() {
        return allTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getYear));
    }

    /**
     * @param city the city
     * @return true if there are any trader based in given city
     */
    public boolean traderInCity(String city) {
        return allTransactions.stream().anyMatch(x -> x.getTrader().getCity().equals(city));
    }

    /**
     * @param from the trader's current location
     * @param to   the trader's new location
     */
    public void relocateTraders(String from, String to) {
        allTransactions.stream()
                .map(Transaction::getTrader)
                .filter(x -> x.getCity() == from)
                .forEach(x -> x.setCity(to));
    }

    /**
     * @return the highest value in all the transactions
     */
    public int highestValue() {
        return allTransactions.stream()
                .map(Transaction::getValue)
                .max(Integer::compare).get();
    }

    //collectors. summarizingint

    /**
     * @return the sum of all transaction values
     */
    public int totalValue() {
        return allTransactions.stream()
                .map(Transaction::getValue)
                .collect(Collectors.summingInt(Integer::intValue));
    }

    /**
     * @return the Transaction with the lowest value
     */
    public Transaction getLowestValueTransaction(){
        return allTransactions.stream()
                .sorted(Comparator.comparing(Transaction::getValue)).findFirst().get();
    }

    /**
     * @return a string of all traders’ names sorted alphabetically
     */
    public String traderNames() {
        return allTransactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .sorted()
                .distinct()
                .collect(Collectors.joining(""));
    }

}
