import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {

        InetAddress fmi = InetAddress.getByName("64.44.101.138");
        System.out.println(fmi.getHostAddress());
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }
}