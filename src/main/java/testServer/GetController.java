package testServer;

import mainServer.entities.Check;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class GetController {

    private CloseableHttpClient client = HttpClientBuilder.create().
            setRedirectStrategy(new LaxRedirectStrategy()).
            setUserAgent("Mozilla/5.0 (X11; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0").
            build();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody Check ask (@RequestParam("url") String url) throws IOException {
        HttpHead headRequest = new HttpHead(url);
        headRequest.addHeader("Content-type", "application/x-www-form-urlencoded");
        HttpResponse response;

        client.execute(headRequest); //empty response

        long startTime = System.currentTimeMillis();
        response=client.execute(headRequest);
        long headRequestTime = (System.currentTimeMillis()-startTime);
        int statusCode = response.getStatusLine().getStatusCode();
        HttpGet getRequest = new HttpGet(url);
        startTime = System.currentTimeMillis();
        response = client.execute(getRequest);
        long fullPageLoadTime = (System.currentTimeMillis()-startTime);
        long fullPageLoadSize = EntityUtils.toByteArray(response.getEntity()).length;
        return new Check(headRequestTime,fullPageLoadTime,fullPageLoadSize,statusCode);
    }
}
