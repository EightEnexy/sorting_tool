package sorting;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
public interface Parser <T> {

    void parseInput();
    T findGreatest();
    Long findOccurrences();
    String getTotal();
    String getGreatest();
    ParserAction getAction();
    Map<T,Long> getMap();
    default String getSorted(ParserAction action, Map<T,Long> counter) {
        return action == ParserAction.SORT_NATURAL ? findSortedDataNatural(counter) : findSortedDataCountBy(counter);
    }
    default String getData(ParserAction action, Map<T,Long> counter) {
        StringBuilder str = new StringBuilder(getTotal());

        switch (action) {
            case FIND_GREATEST -> str.append(getGreatest());
            case SORT_NATURAL,SORT_BY_COUNT -> str.append(getSorted(action, counter));
        }
        return str.toString();
    }
    default String findSortedDataNatural(Map<T, Long> counter) {
        List sortedKeys = new ArrayList(counter.keySet());
        Collections.sort(sortedKeys);
        StringBuilder str = new StringBuilder();
        str.append("Sorted data: ");
        for (var type : sortedKeys) {
            for (var i = 0 ; i < counter.get(type); i++) {
                str.append(type.toString() + " ");
            }
        }
        return str.toString();
    }
    default String findSortedDataCountBy(Map<T,Long> counter) {
        Set<Long>values = new TreeSet<>(counter.values());
        StringBuilder str = new StringBuilder();
        for (var type : values) {
            Set<T> keys = counter.entrySet().stream().filter(key -> key.getValue() == type).
                    map(Map.Entry::getKey).collect(Collectors.toCollection(TreeSet::new));
            for (var key : keys) {
                str.append(MessageFormat.format("{0}: {1} time(s), {2}%).\n",
                        key.toString(),type,
                        (int)(((double)type / (double)findTotal(counter)) * 100)));
            }
        }
        return str.toString();
    }
    default void insertValue(Map<T, Long> counter,T value){
        if (counter.containsKey(value)) {
            counter.compute(value, (key, val) -> val + 1);
        } else {
            counter.put(value, 1L);
        }
    }

    default long findTotal(Map<T, Long> counter) {
        return counter.values().stream().mapToLong(Long::valueOf).sum();
    }

    Comparator<String> lenComparator =
            (String str, String str1) -> Integer.compare(str.length(), str1.length());


}

class LongParser implements Parser<Long> {
    Map<Long,Long> counter;
    Scanner scanner;
    ParserAction action;

    public LongParser(Scanner scanner, ParserAction action) {
        counter = new HashMap<>();
        this.scanner = scanner;
        this.action = action;
    }
    @Override
    public Map<Long, Long> getMap() {
        return Collections.unmodifiableMap(counter);
    }

    @Override
    public void parseInput() {
        while(scanner.hasNextLong()) {
            Long num = scanner.nextLong();
            insertValue(counter, num);
        }
    }

    @Override
    public Long findGreatest() {
        return Collections.max(counter.keySet());
    }


    @Override
    public Long findOccurrences() {
        return counter.get(findGreatest());
    }

    @Override
    public String getTotal() {
        return MessageFormat.format("Total numbers: {0}.\n",findTotal(counter));
    }

    @Override
    public String getGreatest() {
        return MessageFormat.format("The greatest number: {0} ({1} time(s), {2}%).\n",
                findGreatest(),findOccurrences(),
                (int)(((double)findOccurrences() / (double)findTotal(counter)) * 100));
    }
    @Override
    public ParserAction getAction() {
        return this.action;
    }


}

class WordParser implements Parser<String>{
    Map<String,Long> counter;
    Scanner scanner;

    ParserAction action;

    public WordParser(Scanner scanner, ParserAction action) {
        counter = new HashMap<>();
        this.scanner = scanner;
        this.action = action;
    }
    @Override
    public Map<String, Long> getMap() {
        return Collections.unmodifiableMap(counter);
    }

    @Override
    public void parseInput() {
        while(scanner.hasNext()) {
            String word = scanner.next();
            insertValue(counter,word);
        }
    }

    @Override
    public String findGreatest() {
        return Collections.max(counter.keySet(),lenComparator);
    }

    @Override
    public Long findOccurrences() {
        return counter.get(findGreatest());
    }

    @Override
    public String getTotal() {
        return MessageFormat.format("Total words: {0}.\n",findTotal(counter));
    }

    @Override
    public String getGreatest() {
        return MessageFormat.format("The longest word: {0} ({1} times(s), {2}%).\n",
                findGreatest(),findOccurrences(),
                (int)(((double)findOccurrences() / (double)findTotal(counter)) * 100));
    }

    @Override
    public ParserAction getAction() {
        return this.action;
    }

}

class LineParser implements Parser<String> {
    Map<String, Long> counter;
    Scanner scanner;

    ParserAction action;

    public LineParser(Scanner scanner, ParserAction action) {
        counter = new HashMap<>();
        this.scanner = scanner;
        this.action = action;
    }
    @Override
    public Map<String, Long> getMap() {
        return Collections.unmodifiableMap(counter);
    }

    @Override
    public void parseInput() {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            insertValue(counter,line);
        }
    }

    @Override
    public String findGreatest() {
        return Collections.max(counter.keySet(),lenComparator);
    }

    @Override
    public Long findOccurrences() {
        return counter.get(findGreatest());
    }

    @Override
    public String getTotal() {
        return MessageFormat.format("Total lines: {0}.\n",findTotal(counter));
    }

    @Override
    public String getGreatest() {
        return MessageFormat.format("The longest line:\n{0}\n({1} time(s), {2}%).\n",
                findGreatest(),findOccurrences(),
                (int)(((double)findOccurrences() / (double)findTotal(counter)) * 100));
    }
    @Override
    public ParserAction getAction() {
        return this.action;
    }

}