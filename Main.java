package sorting;
import java.io.File;
import java.util.*;
import java.io.PrintStream;

public class Main {
    public static void main(final String[] args) {

        Map<String,String> opts = new HashMap<>();

        for (var arg : args) {
            switch (arg) {
                case "-sortingType", "-dataType", "-inputFile", "-outputFile" -> opts.putIfAbsent(arg, null);
                case "natural", "byCount" -> opts.putIfAbsent("-sortingType", arg);
                case "long", "line", "word" -> opts.putIfAbsent("-dataType", arg);
                default -> {
                    if (!opts.containsKey("-inputFile") && !opts.containsKey("-outputFile"))
                        System.out.println("\"-arg\" is not a valid parameter. It will be skipped.");
                    else {
                        if (opts.containsKey("-outputFile"))
                            opts.compute("-outputFile", (key,val) -> (val == null) ? arg : val);
                        if (opts.containsKey("-inputFile"))
                            opts.compute("-inputFile", (key,val) -> (val == null) ? arg : val);
                    }
                }
            }
        }

        if (opts.containsKey("-sortingType") && opts.get("-sortingType") == null) {
            System.out.println("No sorting type defined!");
            System.exit(0);
        }


        if (opts.get("-dataType") == null) {
            System.out.println("No data type defined!");
            System.exit(0);
        }


        opts.putIfAbsent("-sortingType","natural");

        Scanner scanner = new Scanner(System.in);

        if (opts.containsKey("-inputFile")){
            try {
                scanner = new Scanner(new File(opts.get("-inputFile")));
            }
            catch (Exception e){
                System.out.println("file not found");
                System.exit(0);
            }
        }


        Parser parser = ParserCreator.CreateParser(opts,scanner);

        if (parser == null) {
            System.exit(0);
        }

        parser.parseInput();

        if (opts.containsKey("-outputFile")) {
            try {

                File out = new File(opts.get("-outputFile"));
                out.createNewFile();
                System.setOut(new PrintStream(out));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(parser.getData(parser.getAction(),parser.getMap()));
        scanner.close();
    }
}