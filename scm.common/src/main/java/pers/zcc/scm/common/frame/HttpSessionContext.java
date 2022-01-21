package pers.zcc.scm.common.frame;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

public class HttpSessionContext {

    private static ConcurrentHashMap<String, HttpSession> sessionContext = new ConcurrentHashMap<>();

    public static void addSession(HttpSession session) {
        sessionContext.putIfAbsent(session.getId(), session);
    }

    public static void deleteSession(HttpSession session) {
        sessionContext.remove(session.getId());
    }

    public static HttpSession getSession(String sessionId) {
        return sessionContext.get(sessionId);
    }

}
