package service;

import model.Complaint;
import java.util.*;

public class RabinKarpService {

    private final int PRIME = 101;

    private String normalize(String text) {
        if (text == null)
            return "";
        return text.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean isSimilar(String a, String b) {
        String[] wordsA = a.split(" ");
        String[] wordsB = b.split(" ");

        Set<String> setA = new HashSet<>(Arrays.asList(wordsA));
        Set<String> setB = new HashSet<>(Arrays.asList(wordsB));

        int common = 0;

        for (String word : setA) {
            if (setB.contains(word)) {
                common++;
            }
        }

        int minSize = Math.min(setA.size(), setB.size());
        
        if (minSize == 0) return false;

        double similarity = (double) common / minSize;

        return similarity >= 0.6; // 60% similarity threshold
    }

    public List<Complaint> getDuplicates(Complaint target, Collection<Complaint> complaints) {
        List<Complaint> duplicates = new ArrayList<>();
        if (target == null || target.getTitle() == null || target.getDescription() == null)
            return duplicates;

        String base = normalize(target.getTitle() + " " + target.getDescription());

        for (Complaint c : complaints) {
            if (c.getId() == target.getId())
                continue;

            if (c.getTitle() == null || c.getDescription() == null)
                continue;

            String current = normalize(c.getTitle() + " " + c.getDescription());

            if (isSimilar(base, current)) {
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