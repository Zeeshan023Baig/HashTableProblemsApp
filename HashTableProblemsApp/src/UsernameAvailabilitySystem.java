import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsernameAvailabilitySystem {

    private final HashMap<String, Integer> users = new HashMap<>();
    private final HashMap<String, Integer> attempts = new HashMap<>();
    private int userCounter = 1;

    public synchronized boolean checkAvailability(String username) {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        return !users.containsKey(username);
    }

    public synchronized void registerUser(String username) {
        if (!users.containsKey(username)) {
            users.put(username, userCounter++);
        }
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            String candidate = username + i;
            if (!users.containsKey(candidate)) {
                suggestions.add(candidate);
            }
        }

        String modified = username.replace("_", ".");
        if (!users.containsKey(modified)) {
            suggestions.add(modified);
        }

        return suggestions;
    }

    public String getMostAttempted() {
        String maxUser = null;
        int max = 0;

        for (String user : attempts.keySet()) {
            int count = attempts.get(user);
            if (count > max) {
                max = count;
                maxUser = user;
            }
        }

        return maxUser + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        UsernameAvailabilitySystem system = new UsernameAvailabilitySystem();

        system.registerUser("john_doe");
        system.registerUser("admin");
        system.registerUser("root");

        System.out.println("checkAvailability(\"john_doe\") → " + system.checkAvailability("john_doe"));
        System.out.println("checkAvailability(\"jane_smith\") → " + system.checkAvailability("jane_smith"));

        System.out.println("suggestAlternatives(\"john_doe\") → " + system.suggestAlternatives("john_doe"));

        for (int i = 0; i < 5; i++) system.checkAvailability("admin");
        for (int i = 0; i < 3; i++) system.checkAvailability("guest");

        System.out.println("getMostAttempted() → " + system.getMostAttempted());
    }
}