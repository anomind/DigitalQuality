package mainServer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import mainServer.entities.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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

    @Autowired
    private ServerRepository serverRepository; //
    @Autowired
    private HostRepository hostRepository;     // Hibernate Repositories
    @Autowired
    private CheckRepository checkRepository;   //


    @Scheduled(fixedRate = 86400000) //a day
    public void askTask() throws IOException {
        ArrayList<Server> serverList = (ArrayList<Server>) serverRepository.findAll();
        ArrayList<Host> hostList = (ArrayList<Host>) hostRepository.findAll();
        hostList.forEach(host -> {
            String url=host.getUrl();
            serverList.forEach(server -> { //todo: add multithreading
                HttpGet request = new HttpGet(url);
              //  HttpResponse response = client.execute(request);
            });
        });


        log.info(dateFormat.format(new Date())+" ");
    }
}