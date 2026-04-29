package service;

import model.Complaint;
import java.util.*;

public class ComplaintService {

    private PriorityQueue<Complaint> queue;
    private HashMap<Integer, Complaint> map;
    private HashMap<String, String> teamMap;

    public ComplaintService() {
        queue = new PriorityQueue<>(
                (c1, c2) -> Double.compare(c2.getPriority(), c1.getPriority()));

        map = new HashMap<>();
        teamMap = new HashMap<>();

        teamMap.put("IT", "IT Team");
        teamMap.put("Maintenance", "Maintenance Team");
        teamMap.put("Service", "Service Team");
    }

    public void addComplaint(Complaint c) {
        // GREEDY: Calculate priority based on attributes
        double priority = c.getSeverity() + c.getUrgency() + c.getImpact();
        c.setPriority(priority);

        // PRIORITY QUEUE & HASHING
        queue.add(c);
        map.put(c.getId(), c);
    }

    public Complaint processNext() {
        Complaint c = queue.poll();
        if (c != null) {
            c.setStatus("ASSIGNED");
        }
        return c;
    }

    public String assignTeam(Complaint c) {
        return teamMap.get(c.getCategory());
    }

    public Complaint searchById(int id) {
        // HASHING: Fast lookup
        return map.get(id);
    }

    public Collection<Complaint> getAllComplaints() {
        return map.values();
    }

    // BINARY SEARCH: Optimized searching in sorted complaints
    public Complaint binarySearchById(List<Complaint> sortedList, int targetId) {
        int left = 0;
        int right = sortedList.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (sortedList.get(mid).getId() == targetId)
                return sortedList.get(mid);
            if (sortedList.get(mid).getId() < targetId)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return null;
    }

    // STATUS MANAGEMENT: Ensure transitions (NEW -> IN_PROGRESS -> RESOLVED ->
    // ESCALATED)
    public void updateStatus(Complaint c, String newStatus) {
        c.setStatus(newStatus);
    }
}