import java.util.concurrent.ThreadLocalRandom;

public class tempCodeRunnerFile {

    static class DNSEntry {
        String domain;
        String ip;
        long expiry;

        DNSEntry(String domain, String ip, int ttl) {
            this.domain = domain;
            this.ip = ip;
            this.expiry = System.currentTimeMillis() + ttl * 1000;
        }

        boolean expired() {
            return System.currentTimeMillis() > expiry;
        }
    }

    private DNSEntry[] table;
    private int size;

    public tempCodeRunnerFile(int capacity) {
        table = new DNSEntry[capacity];
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    public void put(String domain, String ip, int ttl) {
        int index = hash(domain);

        while (table[index] != null && !table[index].domain.equals(domain)) {
            index = (index + 1) % table.length;
        }

        table[index] = new DNSEntry(domain, ip, ttl);
        size++;
    }

    public String resolve(String domain) {
        int index = hash(domain);

        while (table[index] != null) {

            if (table[index].domain.equals(domain)) {

                if (!table[index].expired()) {
                    return "Cache HIT → " + table[index].ip;
                }

                table[index] = null;
                return "Cache EXPIRED → querying upstream";
            }

            index = (index + 1) % table.length;
        }

        String ip = fakeIP();
        put(domain, ip, 60);

        return "Cache MISS → Upstream IP: " + ip;
    }

    private String fakeIP() {
        return ThreadLocalRandom.current().nextInt(1, 255) + "."
                + ThreadLocalRandom.current().nextInt(1, 255) + "."
                + ThreadLocalRandom.current().nextInt(1, 255) + "."
                + ThreadLocalRandom.current().nextInt(1, 255);
    }

    public static void main(String[] args) throws Exception {

        tempCodeRunnerFile dns = new tempCodeRunnerFile(10);

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("openai.com"));

        Thread.sleep(2000);

        System.out.println(dns.resolve("google.com"));
    }
}