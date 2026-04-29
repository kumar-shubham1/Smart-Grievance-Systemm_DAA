package service;

import model.Complaint;
import java.util.*;

public class RabinKarpService {

    private final int PRIME = 101;

    private String normalize(String s) {
        if (s == null)
            return "";
        return s.toLowerCase().trim().replaceAll("\\s+", " ");
    }

    public List<Complaint> getDuplicates(Complaint target, Collection<Complaint> complaints) {
        List<Complaint> duplicates = new ArrayList<>();
        if (target == null || target.getTitle() == null || target.getDescription() == null)
            return duplicates;

        String normalizedTarget = normalize(target.getTitle() + " " + target.getDescription());
        long hash = computeHash(normalizedTarget);

        for (Complaint c : complaints) {
            if (c.getId() == target.getId())
                continue;
            if (c.getTitle() == null || c.getDescription() == null)
                continue;

            String normalizedText = normalize(c.getTitle() + " " + c.getDescription());

            if (computeHash(normalizedText) == hash &&
                    (normalizedText.contains(normalizedTarget) || normalizedTarget.contains(normalizedText))) {
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