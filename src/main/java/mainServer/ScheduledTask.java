package mainServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mainServer.entities.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private HttpClient client = HttpClients.createDefault();
    private GsonBuilder builder = new GsonBuilder();
    private final Gson gson = builder.create();
    private ExecutorService service = Executors.newFixedThreadPool(5);
    List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
    private int CONNECTION_TIMEOUT_MS = 30 * 1000; // Timeout in millis.
    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
            .setConnectTimeout(CONNECTION_TIMEOUT_MS)
            .setSocketTimeout(CONNECTION_TIMEOUT_MS)
            .build();


    @Autowired
    private ServerRepository serverRepository; //
    @Autowired
    private HostRepository hostRepository;     // Hibernate Repositories
    @Autowired
    private CheckRepository checkRepository;   //


    @Scheduled(fixedRate = 3600000) //a hour
    public void askTask() throws InterruptedException {
        ArrayList<Server> serverList = (ArrayList<Server>) serverRepository.findAll();
        ArrayList<Host> hostList = (ArrayList<Host>) hostRepository.findAll();
        hostList.forEach(host -> {
            serverList.forEach(server -> {
                tasks.add(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        String hostUrl = host.getUrl();
                        String serverUrl = server.getUrl();
                        int count = 0;
                        int maxTries = 3;
                        while (true) {
                            try {
                                HttpGet request = new HttpGet(serverUrl + "?url=" + hostUrl);
                                //log.info(request.getURI().toString());
                                request.setConfig(requestConfig);
                                HttpResponse response = client.execute(request);
                                Check check=gson.fromJson(new InputStreamReader(response.getEntity().getContent()), Check.class);
                                check.setHost(host);
                                check.setServer(server);
                                checkRepository.save(check);
                                //log.info(check.getCheckTime().toString());
                                return 0;
                            } catch (SocketTimeoutException e) {
                                if (++count == maxTries) {
                                    log.info("Timeout");
                                    e.printStackTrace();
                                    return 1;
                                }
                            }
                        }
                    }
                });
            });
        });

        service.invokeAll(tasks);

    }
}