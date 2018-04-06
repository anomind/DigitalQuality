package testServer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GetController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody String ask (@RequestParam("url") String url){
        return url;
    }
}
