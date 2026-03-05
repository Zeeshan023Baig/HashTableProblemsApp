import java.util.*;

public class AutocompleteSystem {

    private HashMap<String, Integer> frequency = new HashMap<>();

    public void addQuery(String query) {
        frequency.put(query, frequency.getOrDefault(query, 0) + 1);
    }

    public void updateFrequency(String query) {
        frequency.put(query, frequency.getOrDefault(query, 0) + 1);
    }

    public void search(String prefix) {

        PriorityQueue<Map.Entry<String,Integer>> pq =
                new PriorityQueue<>((a,b) -> b.getValue() - a.getValue());

        for(Map.Entry<String,Integer> entry : frequency.entrySet()) {
            if(entry.getKey().startsWith(prefix)) {
                pq.add(entry);
            }
        }

        int count = 0;

        System.out.println("search(\"" + prefix + "\") →");

        while(!pq.isEmpty() && count < 10) {
            Map.Entry<String,Integer> e = pq.poll();
            System.out.println((count+1) + ". \"" + e.getKey() + "\" (" + e.getValue() + " searches)");
            count++;
        }

        if(count == 0) {
            System.out.println("No suggestions found");
        }
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java download");
        system.addQuery("java tutorial");
        system.addQuery("java 21 features");

        system.search("jav");

        system.updateFrequency("java 21 features");

        system.search("jav");
    }
}