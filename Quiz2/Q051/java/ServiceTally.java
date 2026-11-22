// ServiceTally.java - Q051: orders and kilograms per service, and a ranking.
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ServiceTally {

    static final String LOG = "WF:4.2 IR:1.3 WDI:2.6 WF:6.3 SH:1.0 WF:3.0 "
                            + "IR:2.4 WDI:5.1 WF:3.8 SH:0.8 IR:1.9 WF:2.2";

    public static void main(String[] args) {
        // (a) and (b): one pass over the log fills two maps keyed by service code.
        Map<String, Integer> count = new TreeMap<>();   // TreeMap: keys come out sorted
        Map<String, Double> kg = new TreeMap<>();
        for (String entry : LOG.split(" ")) {
            String[] part = entry.split(":");
            String code = part[0];
            double weight = Double.parseDouble(part[1]);
            // TODO(Q051): count.merge(code, 1, Integer::sum);
            // TODO(Q051): kg.merge(code, weight, Double::sum);
        }
        System.out.println("(a)+(b) per service, in code order:");
        for (Map.Entry<String, Integer> e : count.entrySet()) {
            System.out.printf(Locale.ROOT, "  %-3s  orders=%d  kg=%.1f%n", e.getKey(), e.getValue(), kg.get(e.getKey()));
        }

        // (c) ranking: more orders first; equal orders -> more kilograms first.
        List<String> codes = new ArrayList<>(count.keySet());
        // TODO(Q051): sort codes by count descending, then by kilograms descending.
        System.out.println("(c) ranking: " + codes);

        // (d) the same counts in a HashMap: the order is the hash table's, not ours.
        Map<String, Integer> hashed = new HashMap<>();
        for (String entry : LOG.split(" ")) {
            hashed.merge(entry.split(":")[0], 1, Integer::sum);
        }
        System.out.println("(d) HashMap iteration order: " + hashed.keySet());

        // Second method: a stream that groups and counts in one expression.
        Map<String, Long> viaStream = java.util.Arrays.stream(LOG.split(" "))
                .collect(Collectors.groupingBy(s -> s.split(":")[0], TreeMap::new, Collectors.counting()));
        System.out.println("second method: " + viaStream);
    }
}
