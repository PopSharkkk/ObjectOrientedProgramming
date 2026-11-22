// OrderListViewModel.java - Q075: a filtered, sorted list between an order book and its view.
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class OrderListViewModel {

    enum Status { RECEIVED, WASHING, DRYING, READY, PICKED_UP }

    static final class Order {
        final String id;
        final String customer;
        final String service;
        final int readyHour;
        Status status;

        Order(String id, String customer, String service, Status status, int readyHour) {
            this.id = id;
            this.customer = customer;
            this.service = service;
            this.status = status;
            this.readyHour = readyHour;
        }
    }

    /** The model: orders, and a single "something changed" notification. */
    static final class OrderBook {
        private final List<Order> orders = new ArrayList<>();
        private final List<Runnable> listeners = new ArrayList<>();

        void addListener(Runnable r) {
            listeners.add(r);
        }

        void add(Order o) {
            orders.add(o);
            listeners.forEach(Runnable::run);
        }

        void advance(String id) {
            for (Order o : orders) {
                if (o.id.equals(id)) {
                    o.status = Status.values()[o.status.ordinal() + 1];
                }
            }
            listeners.forEach(Runnable::run);
        }

        List<Order> orders() {
            return List.copyOf(orders);
        }
    }

    // ---------------- the view-model: all the view's decisions, none of its widgets ----------------
    static final Comparator<Order> BY_READY =
            Comparator.comparingInt((Order o) -> o.readyHour).thenComparing(o -> o.id);
    static final Comparator<Order> BY_CUSTOMER =
            Comparator.comparing((Order o) -> o.customer, String.CASE_INSENSITIVE_ORDER).thenComparing(o -> o.id);

    private final OrderBook book;
    private final Consumer<List<String>> view;
    private String filter = "";
    private Status onlyStatus = null;               // null: every status
    private Comparator<Order> sort = BY_READY;
    private List<String> shown = null;
    int updates = 0;

    OrderListViewModel(OrderBook book, Consumer<List<String>> view) {
        this.book = book;
        this.view = view;
        book.addListener(this::recompute);
        recompute();
    }

    void setFilter(String text) {
        filter = (text == null ? "" : text).strip().toLowerCase(Locale.ROOT);
        recompute();
    }

    void setOnlyStatus(Status s) {
        onlyStatus = s;
        recompute();
    }

    void setSortByCustomer(boolean byCustomer) {
        sort = byCustomer ? BY_CUSTOMER : BY_READY;
        recompute();
    }

    private static String formatRow(Order o) {
        return String.format(Locale.ROOT, "%s %s %s %s %02d:00",
                o.id, o.customer, o.service, o.status, o.readyHour);
    }

    private void recompute() {
        List<String> rows = book.orders().stream()
                .filter(o -> o.customer.toLowerCase(Locale.ROOT).contains(filter))
                .filter(o -> onlyStatus == null || o.status == onlyStatus)
                .sorted(sort)
                .map(OrderListViewModel::formatRow)
                .toList();
        if (!rows.equals(shown)) {
            shown = rows;
            updates++;
            view.accept(rows);
        }
    }

    public static void main(String[] args) {
        OrderBook book = new OrderBook();
        book.add(new Order("A1", "Sari", "WF", Status.WASHING, 14));
        book.add(new Order("A2", "Budi", "WDI", Status.READY, 11));
        book.add(new Order("A3", "Ayu", "IR", Status.RECEIVED, 16));
        book.add(new Order("A4", "Dewi", "WF", Status.READY, 12));
        book.add(new Order("A5", "Rahmat", "WDI", Status.WASHING, 13));
        book.add(new Order("A6", "Citra", "WF", Status.DRYING, 15));

        String[] step = {"0. attach"};
        List<String> log = new ArrayList<>();
        OrderListViewModel vm = new OrderListViewModel(book, rows -> log.add(step[0] + " -> " + rows));
        Object[][] script = {
            {"1. filter 'ra'", (Runnable) () -> vm.setFilter("ra")},
            {"2. sort by customer", (Runnable) () -> vm.setSortByCustomer(true)},
            {"3. add A7 Budi", (Runnable) () -> book.add(new Order("A7", "Budi", "IR", Status.RECEIVED, 17))},
            {"4. advance A5", (Runnable) () -> book.advance("A5")},
            {"5. filter 'RA'", (Runnable) () -> vm.setFilter("RA")},
            {"6. only DRYING", (Runnable) () -> vm.setOnlyStatus(Status.DRYING)},
            {"7. advance A6", (Runnable) () -> book.advance("A6")},
            {"8. filter ''", (Runnable) () -> vm.setFilter("")},
            {"9. only READY", (Runnable) () -> vm.setOnlyStatus(Status.READY)}};
        for (Object[] s : script) {
            step[0] = (String) s[0];
            int before = vm.updates;
            ((Runnable) s[1]).run();
            if (vm.updates == before) {
                log.add(step[0] + " -> (no update)");
            }
        }
        log.forEach(System.out::println);
        System.out.println("view updates: " + vm.updates + " for 10 events");
    }
}
