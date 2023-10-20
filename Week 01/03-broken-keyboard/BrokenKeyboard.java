public class BrokenKeyboard {
    private static boolean containsBrokenLetters(String word, String keys) {
        for (int i = 0; i < keys.length(); i++) {
            if (word.contains(String.valueOf(keys.charAt(i)))) {
                return true;
            }
        }
        return false;
    }

    public static int calculateFullyTypedWords(String message, String brokenKeys) {
        String[] words = message.split(" ");
        int counter = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].isBlank() == false) {
                //System.out.println(words[i]);
                if (containsBrokenLetters(words[i], brokenKeys) == false) {
                    counter++;
                }
            }
        }

        return counter;
    }
}
