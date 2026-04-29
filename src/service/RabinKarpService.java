package service;

import model.Complaint;
import java.util.*;

public class RabinKarpService {

    private final int PRIME = 101;

    private String normalize(String s) {
        if (s == null) return "";
        return s.toLowerCase().trim().replaceAll("\\s+", " ");
    }

    public List<Complaint> getDuplicates(Complaint target, Collection<Complaint> complaints) {
        List<Complaint> duplicates = new ArrayList<>();
        if (target == null || target.getDescription() == null) return duplicates;

        String pattern = normalize(target.getDescription());
        long hash = computeHash(pattern);

        for (Complaint c : complaints) {
            if (c.getId() == target.getId()) continue;
            if (c.getDescription() == null) continue;

            String text = normalize(c.getDescription());

            if (computeHash(text) == hash && text.equals(pattern)) {
                duplicates.add(c);
            }
        }

        return duplicates;
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