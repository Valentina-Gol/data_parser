package parser;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class Parser{
    private ArrayList<String> inputStrings = new ArrayList<>();
    private ArrayList<ArrayList<String>> groups = new ArrayList<>();
    private HashMap<Integer, Integer> groupsMergeHistory = new HashMap<>();
    private ArrayList<HashMap<String, Integer>> groupByColumn = new ArrayList<>();
    
    public void readStrings(String filePath) throws FileNotFoundException{
        File file = new File(filePath);
        try (Scanner reader = new Scanner(file)){
            while (reader.hasNextLine()) {
                String readString = reader.nextLine();
                inputStrings.add(readString);   
            }
        }
    }

    public void findGroups(){        
        for (String str : inputStrings) {
            HashSet<Integer> groupsToMerge = new HashSet<>();
            HashMap<String, Integer> stringPartsToAdd = new HashMap<>();
            String[] parts = parseString(str);
            if (parts != null) {
                findGroupsToMerge(parts, groupsToMerge, stringPartsToAdd);
                updateGroups(str, groupsToMerge, stringPartsToAdd);
            }
        }
    }

    public String[] parseString(String str){
        String[] strings = str.split(";");
        for (String part: strings){
            if (part.indexOf("\"", 1) != part.length() - 1){
                return null;
            }
        }
        return strings;
    }

    private void findGroupsToMerge(String[] parts, HashSet<Integer> groupsToMerge, HashMap<String, Integer> partsToAdd){
        for (int i = 0; i < parts.length; i++) {
            if (i == groupByColumn.size()){
                groupByColumn.add(new HashMap<>());
            } 
            String part = parts[i];
            if (part.equals("\"\"") || part.equals("")) continue;
            if (groupByColumn.get(i).containsKey(part)){
                groupsToMerge.add(findFinalGroup(groupByColumn.get(i).get(part)));
            } else {
                partsToAdd.put(part, i);
            }
        }
    }

    private Integer findFinalGroup(Integer groupNumer){
        while (groupsMergeHistory.containsKey(groupNumer)){
            groupNumer = groupsMergeHistory.get(groupNumer);
        }
        return groupNumer;
    }

    private void updateGroups(String str, HashSet<Integer> groupsToMerge, HashMap<String, Integer> partsToAdd){
        int primeInd = findGroupToMergeIndex(groupsToMerge);
        if (primeInd == groups.size()){
            groups.add(new ArrayList<>());
        }
        for (int curInd: groupsToMerge) {
            if (curInd != primeInd) {
                mergeTwoGroups(curInd, primeInd);
            }
        }
        for (Map.Entry<String, Integer> entry: partsToAdd.entrySet()) {
            groupByColumn.get(entry.getValue()).put(entry.getKey(), primeInd);
        }
        groups.get(primeInd).add(str);
    }

    private int findGroupToMergeIndex(HashSet<Integer> groupsToMerge){
        if (groupsToMerge.size() > 0){
            return groupsToMerge.iterator().next();
        }
        return groups.size();  
    }

    public void mergeTwoGroups(Integer fromInd, Integer toInd){
        groups.get(toInd).addAll(groups.get(fromInd));
        groups.set(fromInd, null);
        groupsMergeHistory.put(fromInd, toInd);
    }

    public void findUniqueGroups(){
        ArrayList<String> toRemove = new ArrayList<>();
        toRemove.add(null);
        groups.removeAll(toRemove);
        Collections.sort(groups, new CompareArrayLists());

        int number = 0;
        int threshold = 1;
        for (ArrayList<String> set: groups){
            if (set.size() > threshold)
                number++;
        }
        System.out.printf("Count of groups with members count more than %d: %d\n", threshold, number);
        System.out.printf("Total groups count: %d\n", groups.size());
    }

    class CompareArrayLists implements Comparator<ArrayList<String>>{
        public int compare(ArrayList<String> group1, ArrayList<String> group2){
            if (group1.size() > group2.size()) return -1;
            if (group1.size() < group2.size()) return 1;
            return 0;
        }
    }

    public void writeGroups() throws FileNotFoundException{
        File file = new File("./output.txt");
        file.getParentFile().mkdirs();
        try (PrintWriter writer = new PrintWriter(file)){
            for (int i = 0; i < groups.size(); i++){
                writer.print(
                    String.format("Группа %d\n", i + 1) + String.join("\n", 
                    new HashSet<String>(groups.get(i)))
                );
                writer.print("\n\n");
            }
        }
    }
}
