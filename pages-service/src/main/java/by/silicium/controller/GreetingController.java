package by.silicium.controller;

import by.silicium.service.file.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Kiryl Parfiankou on 17.5.16.
 */
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final static Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    private FileService fileService;

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        logger.info("Greeting was started!");
        return new Greeting(counter.incrementAndGet(),
                String.format(template, fileService.getList()));
    }
}
