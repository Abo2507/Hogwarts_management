package interfaces;

public interface Printable {
    String formatForPrint();

    default void print() {
        System.out.println(formatForPrint());
    }

    static String formatHeader(String title) {
        String border = "=".repeat(title.length()+4);
        return border + "\n " + title + "\n" + border;
    }
}
