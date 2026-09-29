package platform;

public class CodeResponse {

    private String code;
    private String date;
    private long time;
    private int views;

    public CodeResponse(String code, String date, long time, int views) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.views = views;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public long getTime() {
        return time;
    }

    public int getViews() {
        return views;
    }
}