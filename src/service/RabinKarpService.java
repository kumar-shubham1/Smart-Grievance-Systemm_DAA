package service;

import model.Complaint;
import java.util.*;

public class RabinKarpService {

    private final int PRIME = 101;

    public boolean isDuplicate(String desc, Collection<Complaint> complaints) {

        if (desc == null) return false;

        String pattern = desc.trim().toLowerCase();
        long hash = computeHash(pattern);

        for (Complaint c : complaints) {
            String text = c.getDescription().trim().toLowerCase();

            if (computeHash(text) == hash && text.equals(pattern)) {
                return true;
            }
        }

        return false;
    }

    private long computeHash(String s) {
        long h = 0;
        for (int i = 0; i < s.length(); i++) {
            h = h * PRIME + s.charAt(i);
        }
        return h;
    }
    public boolean containsPattern(String text, String pattern) {

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int m = pattern.length();

        for (int i = 0; i <= text.length() - m; i++) {

            String sub = text.substring(i, i + m);

            if (computeHash(sub) == computeHash(pattern) && sub.equals(pattern)) {
                return true;
            }
        }

        return false;
    }
}