package service;

import model.Complaint;
import java.util.*;

public class TopKService {

    public List<Complaint> getTopK(Collection<Complaint> complaints, int k) {

        PriorityQueue<Complaint> minHeap =
                new PriorityQueue<>(Comparator.comparingDouble(Complaint::getPriority));

        for (Complaint c : complaints) {
            minHeap.add(c);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        List<Complaint> result = new ArrayList<>(minHeap);
        result.sort((a, b) -> Double.compare(b.getPriority(), a.getPriority()));

        return result;
    }
}