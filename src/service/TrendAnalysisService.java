package service;

import model.Complaint;
import java.util.*;
import java.util.stream.Collectors;

public class TrendAnalysisService {

    public List<Complaint> getComplaintsLastNDays(Collection<Complaint> complaints, int days) {

        long now = System.currentTimeMillis();
        long window = (long) days * 24 * 60 * 60 * 1000;

        return complaints.stream()
                .filter(c -> c.getCreatedAt() != null &&
                        (now - c.getCreatedAt().getTime()) <= window)
                .collect(Collectors.toList());
    }
}