// QuotePanel.java - Q060: a price-quote panel whose listeners are traced event by event.
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class QuotePanel {

    enum Service {
        WF("Wash & Fold", 7000, 3.0, 48), IR("Iron Only", 5000, 2.0, 24), WDI("Wash, Dry & Iron", 10000, 3.0, 72);

        final String label;
        final long rate;
        final double minKg;
        final int hours;

        Service(String label, long rate, double minKg, int hours) {
            this.label = label;
            this.rate = rate;
            this.minKg = minKg;
            this.hours = hours;
        }
    }

    /** The model: pure rules, no Swing. */
    static String quote(String kgText, Service s, boolean express) {
        double kg;
        try {
            kg = Double.parseDouble(kgText == null ? "" : kgText.trim());
        } catch (NumberFormatException e) {
            return "invalid weight: enter a number";
        }
        if (!Double.isFinite(kg)) {
            return "invalid weight: enter a number";
        }
        if (kg <= 0 || kg > 50) {
            return "weight must be > 0 and <= 50 kg";
        }
        long halfKg = (long) Math.ceil(2.0 * Math.max(kg, s.minKg));
        long price = s.rate * halfKg / 2;
        int hours = s.hours;
        if (express) {
            price = 3 * price / 2;
            hours = Math.max(6, s.hours / 4);
        }
        return String.format(Locale.ROOT, "%s %.1f kg%s: Rp%,d, ready in %d h",
                s.name(), halfKg / 2.0, express ? " express" : "", price, hours);
    }

    /** The view: components plus listeners that only ask the model. */
    static final class View extends JPanel {
        private static final long serialVersionUID = 1L;
        final JTextField kg = new JTextField(6);
        final JComboBox<Service> service = new JComboBox<>(Service.values());
        final JCheckBox express = new JCheckBox("Express");
        final JButton quoteButton = new JButton("Quote");
        final JLabel result = new JLabel("(no quote yet)");
        boolean quoted = false;      // once the user has asked, later changes re-quote at once
        int handled = 0;

        View() {
            super(new BorderLayout());
            JPanel form = new JPanel(new GridLayout(0, 2));
            form.add(new JLabel("Weight (kg)"));
            form.add(kg);
            form.add(new JLabel("Service"));
            form.add(service);
            form.add(express);
            form.add(quoteButton);
            add(form, BorderLayout.CENTER);
            add(result, BorderLayout.SOUTH);

            quoteButton.addActionListener(e -> refresh(true));                 // a click
            kg.addActionListener(e -> refresh(true));                          // Enter in the field
            service.addActionListener(e -> refresh(false));                    // a new choice
            express.addItemListener(e -> {                                     // ticked or unticked
                if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
                    refresh(false);
                }
            });
        }

        private void refresh(boolean explicit) {
            handled++;
            if (explicit) {
                quoted = true;
            }
            if (quoted) {
                result.setText(quote(kg.getText(), (Service) service.getSelectedItem(), express.isSelected()));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("--selftest")) {
            System.setProperty("java.awt.headless", "true");
            SwingUtilities.invokeAndWait(QuotePanel::selfTest);
            return;
        }
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Profitina Laundry - quote");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(new View());
            f.pack();
            f.setVisible(true);
        });
    }

    private static void selfTest() {
        View v = new View();
        step(v, "0. start", () -> { });
        step(v, "1. type 4.2", () -> v.kg.setText("4.2"));
        step(v, "2. click Quote", v.quoteButton::doClick);
        step(v, "3. tick Express", v.express::doClick);
        step(v, "4. choose WDI", () -> v.service.setSelectedItem(Service.WDI));
        step(v, "5. type abc", () -> v.kg.setText("abc"));
        step(v, "6. press Enter", v.kg::postActionEvent);
        step(v, "7. untick Express", v.express::doClick);
        step(v, "8. choose IR", () -> v.service.setSelectedItem(Service.IR));
        step(v, "9. type 0.4, click", () -> {
            v.kg.setText("0.4");
            v.quoteButton.doClick();
        });
        System.out.println("self-test finished on the EDT: " + SwingUtilities.isEventDispatchThread());
    }

    private static void step(View v, String what, Runnable action) {
        action.run();
        System.out.printf("%-20s handled=%d  label: %s%n", what, v.handled, v.result.getText());
    }
}
