package bg.sofia.uni.fmi.mjt.cooking.command;

import bg.sofia.uni.fmi.mjt.cooking.requests.RequestBuilder;

import java.util.List;
import java.util.Scanner;

public class SearchByKeywordCommand implements Command {
    private final RequestBuilder request;

    public SearchByKeywordCommand(RequestBuilder request) {
        this.request = request;
    }

    @Override
    public void execute(Scanner scanner) {
        print();
        String[] words = readKeyWords(scanner);
        request.setKeywords(List.of(words));
    }

    private void print() {
        System.out.println("Enter keywords on one line separated with commas");
    }

    private String[] readKeyWords(Scanner scanner) {
        String line = scanner.nextLine();
        return line.split(",");
    }
}
