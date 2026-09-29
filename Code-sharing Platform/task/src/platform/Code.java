package platform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Entity
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String date;
    private String uuid;
    private long time;
    private int views;
    private LocalDateTime createdAt;
    private boolean viewsRestricted;

    public Code() {
    }

    public Code(String code, String date, long time, int views) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.views = views;
        this.uuid = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        this.viewsRestricted = views > 0;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUuid() {
        return uuid;
    }

    public long getTime() {
        return time;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isViewsRestricted() {
        return viewsRestricted;
    }
}