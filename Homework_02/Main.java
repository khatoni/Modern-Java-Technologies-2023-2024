import bg.sofia.uni.fmi.mjt.space.MJTSpaceScanner;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;

import javax.security.auth.kerberos.EncryptionKey;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        try (Reader reader1 = new FileReader(
            "C:\\Users\\Hp\\Desktop\\Modern-Java-Technologies-2023-2024\\Homework2\\all-missions-from-1957.csv");
             Reader reader2 = new FileReader(
                 "C:\\Users\\Hp\\Desktop\\Modern-Java-Technologies-2023-2024\\Homework2\\all-rockets-from-1957.csv")) {

            MJTSpaceScanner scanner = new MJTSpaceScanner(reader1, reader2, new EncryptionKey(new byte[10], 5));
            Collection<Mission> missionCollection = scanner.getAllMissions();
            Collection<Rocket> rocketCollection = scanner.getAllRockets();
            List<Rocket> rocketList = scanner.getTopNTallestRockets(15);
            System.out.println(1);
        } catch (IOException e) {

        }
    }
}