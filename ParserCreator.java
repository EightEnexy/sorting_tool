package sorting;
import java.util.Map;
import java.util.Scanner;

// Simple factory pattern
public class ParserCreator {

    public static Parser CreateParser(Map<String,String> opts, Scanner scanner){

        Parser toBeCreated = null;

        String action_opt =  opts.get("-sortingType") != null ? opts.get("-sortingType") : "natural";

        ParserAction action = switch (action_opt) {
            case "natural" -> ParserAction.SORT_NATURAL;
            case "byCount" -> ParserAction.SORT_BY_COUNT;
            case "greatest" -> ParserAction.FIND_GREATEST;
            default -> {
                System.err.println("Type not found");
                yield ParserAction.ERR;
            }
        };


        return toBeCreated = switch (opts.get("-dataType")){
            case "long" -> new LongParser(scanner,action);
            case "word" -> new WordParser(scanner,action);
            case "line" -> new LineParser(scanner,action);
            default -> {
                System.err.println("Type not found");
                yield  null;
            }
        };

    }

}