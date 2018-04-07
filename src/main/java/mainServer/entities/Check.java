package mainServer.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "checks")
public class Check {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Host.class, fetch = FetchType.LAZY)
    private Host host;

    @ManyToOne(targetEntity = Server.class, fetch = FetchType.LAZY)
    private Server server;

    private Integer statusCode;
    private Timestamp checkTime;
    private Long headRequestTime;
    private Long fullPageLoadTime;
    private Long fullPageSize;
    private Long fullPageLoadSpeed;

    public Check (Host host, Server server, Long headRequestTime, Long fullPageLoadTime, Long fullPageLoadSize) {
        this.host=host;
        this.server=server;
        this.headRequestTime=headRequestTime;
        this.fullPageLoadTime=fullPageLoadTime;
        this.fullPageSize=fullPageLoadSize;
        this.fullPageLoadSpeed=fullPageLoadSize/fullPageLoadTime;
        this.checkTime=new Timestamp(System.currentTimeMillis());
    }
    public Check (Long headRequestTime, Long fullPageLoadTime, Long fullPageLoadSize, Integer statusCode) {
        this.headRequestTime=headRequestTime;
        this.fullPageLoadTime=fullPageLoadTime;
        this.fullPageSize=fullPageLoadSize;
        this.fullPageLoadSpeed=fullPageLoadSize/fullPageLoadTime;
        this.checkTime=new Timestamp(System.currentTimeMillis());
        this.statusCode=statusCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Timestamp getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }

    public Long getHeadRequestTime() {
        return headRequestTime;
    }

    public void setHeadRequestTime(Long headRequestTime) {
        this.headRequestTime = headRequestTime;
    }

    public Long getFullPageLoadTime() {
        return fullPageLoadTime;
    }

    public void setFullPageLoadTime(Long fullPageLoadTime) {
        this.fullPageLoadTime = fullPageLoadTime;
    }

    public Long getFullPageSize() {
        return fullPageSize;
    }

    public void setFullPageSize(Long fullPageSize) {
        this.fullPageSize = fullPageSize;
    }

    public Long getFullPageLoadSpeed() {
        return fullPageLoadSpeed;
    }

    public void setFullPageLoadSpeed(Long fullPageLoadSpeed) {
        this.fullPageLoadSpeed = fullPageLoadSpeed;
    }
}
