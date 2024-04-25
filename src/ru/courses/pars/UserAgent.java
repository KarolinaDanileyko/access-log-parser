package ru.courses.pars;

public class UserAgent {
    private final BrowserType browser;
    private final OsType os;

    public UserAgent(String userAgent) { //конструктор принимает строку User-Agent и извлекает из неё свойства
        this.os = getOsType(userAgent);
        this.browser = getBrowserType(userAgent);
    }

    public BrowserType getBrowser() {
        return browser;
    }

    public OsType getOsType() {
        return os;
    }


    private static OsType getOsType(String userAgentString) {
        if (userAgentString.contains("Windows")) {
            return OsType.WINDOWS;
        } else if (userAgentString.contains("Mac OS")) {
            return OsType.MACOS;
        } else if (userAgentString.contains("Linux")) {
            return OsType.LINUX;
        } else {
            return OsType.ANOTHER_OS;
        }

    }

    private BrowserType getBrowserType(String userAgentStr) {
        if (userAgentStr.contains("Edg")) {
            return BrowserType.EDGE;
        } else if (userAgentStr.contains("Firefox")) {
            return BrowserType.FIREFOX;
        } else if (userAgentStr.contains("Opera") || userAgentStr.contains("OPR")) {
            return BrowserType.OPERA;
        } else if (userAgentStr.contains("Chrome")) {
            return BrowserType.CHROME;
        } else {
            return BrowserType.ANOTHER_BROWSER;
        }
    }
}
