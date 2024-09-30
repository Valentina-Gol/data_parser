package parser;

import java.io.FileNotFoundException;

// add all to github and check that all correctly loads and runs
// add readme fo running

public class Main{
    public static void main(String[] args){
        Parser parser = new Parser();
        if (args.length == 0){
            System.err.println("Path to data file was not passed.");
        } else {
            try{
                long startTime = System.currentTimeMillis();
                parser.processStrings(args[0]);
                parser.findUniqueGroups();
                parser.printCount();
                long endTime = System.currentTimeMillis();
                System.out.printf("Data processing took: %.3f\n", (endTime - startTime) / 1000.0);
            } catch (FileNotFoundException err){
                System.err.printf("Given data file `%s` wasn't found.\n", args[0]);
            }
        }
    }
}
