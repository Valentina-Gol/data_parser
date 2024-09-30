package parser;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class Parser {
    private HashMap<String, HashSet<String>> groups = new HashMap<>();

    public void processStrings(String filePath) throws FileNotFoundException{
        File file = new File(filePath);
        try (Scanner reader = new Scanner(file)){
            while (reader.hasNextLine()) {
                String readString = reader.nextLine();
                String[] arr = parseString(readString);
                if (arr == null) continue;

                HashSet<String> curGroupElements = new HashSet<>();
                curGroupElements.add(readString);
                HashSet<String> newSet = curGroupElements;
               
                for (int i = 0; i < arr.length; i++){
                    String rowValue = arr[i];
                    if ((rowValue.equals("\"\""))) continue;
                    String key = String.valueOf(i) + rowValue;
                    if (groups.containsKey(key)){
                        newSet.addAll(groups.get(key));
                    } else {
                        groups.put(key, newSet);
                    }
                }
                updateGroups(newSet);
            }
        }
    }

    private void updateGroups(HashSet<String> newSet){
        for (String stringToUpdate: newSet){
            String[] arr = parseString(stringToUpdate);
            for (int i = 0; i < arr.length; i++){
                String rowValue = arr[i];
                if ((rowValue.equals("\"\""))) continue;
                String key = String.valueOf(i) + rowValue;
                groups.get(key).addAll(newSet);
            }
        }
    }

    class CompareHashSets implements Comparator<HashSet<String>>{
        public int compare(HashSet<String> group1, HashSet<String> group2){
            if (group1.size() > group2.size()) return -1;
            if (group1.size() < group2.size()) return 1;
            return 0;
        }
    }

    public String[] parseString(String s){
        String[] strings = s.split(";");
        for (String part: strings){
            if (part.indexOf("\"", 1) != part.length() - 1){
                return null;
            }
        }
        return strings;
    }

    public void findUniqueGroups() throws FileNotFoundException{
        ArrayList<HashSet<String>> groupsList = new ArrayList<>(new HashSet<>(groups.values()));
        Collections.sort(groupsList, new CompareHashSets());

        File file = new File("./output.txt");
        file.getParentFile().mkdirs();
        try (PrintWriter writer = new PrintWriter(file)){
            for (int i = 0; i < groupsList.size(); i++){
                writer.print(String.format("Группа %d\n", i + 1) + String.join("\n", groupsList.get(i)));
                writer.print("\n\n");
            }
        }
    }

    public void printCount(){
        int number = 0;
        int threshold = 1;
        HashSet<HashSet<String>> res = new HashSet<>(groups.values());
        for (HashSet<String> set: res){
            if (set.size() > threshold)
                number++;
        }
        System.out.printf("Count of groups with members count more than %d: %d\n", threshold, number);
        System.out.printf("Total groups count: %d\n", res.size());
    }
}
