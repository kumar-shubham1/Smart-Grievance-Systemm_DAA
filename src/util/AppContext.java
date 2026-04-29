package util;

import service.ComplaintService;
import dao.ComplaintDAO;

public class AppContext {
    public static int currentUserId = -1;
    public static ComplaintService service = new ComplaintService();
    public static ComplaintDAO dao = new ComplaintDAO();
}