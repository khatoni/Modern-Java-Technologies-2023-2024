public class IPValidator {
    private static boolean isValid(String str) {
        if (str.isBlank()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                return false;
            }
        }
        if (str.startsWith("0") && str.length() > 1) {
            return false;
        }
        if (Integer.parseInt(str) < 0 || Integer.parseInt(str) > 255) {
            return false;
        }
        return true;
    }

    public static boolean validateIPv4Address(String str) {

        if (str.isBlank()) {
            return false;
        }
        String[] tmp = str.split("\\.");
        if (tmp.length != 4) {
            return false;
        }
        for (int i = 0; i < tmp.length; i++) {
            if (isValid(tmp[i]) == false) {
                return false;
            }
        }
        return true;

    }
}
