// WildcardDesk.java - Q054: producer extends, consumer super, at the sorting desk.
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WildcardDesk {

    static class Garment {
        final String name;
        final int grams;

        Garment(String name, int grams) {
            this.name = name;
            this.grams = grams;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static class Shirt extends Garment {
        Shirt(String name) {
            super(name, 250);
        }
    }

    static class Towel extends Garment {
        Towel(String name) {
            super(name, 600);
        }

        Towel(String name, int grams) {
            super(name, grams);
        }
    }

    static class BathTowel extends Towel {
        BathTowel(String name) {
            super(name, 900);
        }
    }

    /** src only PRODUCES garments for us to read: ? extends. */
    static double totalKg(List<? extends Garment> items) {
        // TODO(Q054): sum the grams of every garment; return grams / 1000.0.
        return 0.0;
    }

    /** dst only CONSUMES towels we put in: ? super. */
    static void addSpareTowels(List<? super Towel> dst, int n) {
        // TODO(Q054): for (int i = 1; i <= n; i++) dst.add(new Towel("spare-" + i));
    }

    /** Both at once: read from a producer of T, write into a consumer of T. */
    static <T> int transfer(Collection<? extends T> src, Collection<? super T> dst) {
        // TODO(Q054): move every item from src to dst, counting them; src.clear(); return moved;
        return 0;
    }

    public static void main(String[] args) {
        List<Garment> basket = new ArrayList<>();
        List<Shirt> shirts = new ArrayList<>(List.of(new Shirt("batik"), new Shirt("polo")));
        List<Towel> towels = new ArrayList<>(List.of(new Towel("hand")));
        List<BathTowel> bathTowels = new ArrayList<>(List.of(new BathTowel("bath-1"), new BathTowel("bath-2")));
        List<Object> anything = new ArrayList<>();

        System.out.println("(1) totalKg(shirts)          = " + totalKg(shirts));
        addSpareTowels(basket, 2);                                  // (3) List<Garment> accepts Towels
        addSpareTowels(anything, 1);                                // (3') so does List<Object>
        System.out.println("(3) basket after spares      = " + basket + ", anything = " + anything);
        int moved = transfer(bathTowels, towels);                   // (5) T inferred as Towel
        System.out.println("(5) transfer(bath -> towels) = " + moved + " moved, towels = " + towels
                + ", bathTowels = " + bathTowels);
        moved = WildcardDesk.<Garment>transfer(shirts, basket);    // explicit T = Garment
        System.out.println("    transfer(shirts -> basket) = " + moved + " moved, basket = " + basket);
        List<? extends Garment> view = towels;                      // (8) a read-only window onto towels
        Garment first = view.get(0);
        System.out.println("(8) view.get(0) = " + first + ", totalKg(view) = " + totalKg(view));
        view.clear();                                               // clear and remove are fine; add(x) is not
        System.out.println("    after view.clear(): towels = " + towels);
    }
}
