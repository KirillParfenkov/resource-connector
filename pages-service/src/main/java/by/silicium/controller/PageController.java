package by.silicium.controller;

import by.silicium.domain.Page;
import by.silicium.service.file.PageService;
import by.silicium.service.file.google.GoogleDriveFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Kiryl Parfiankou on 29.5.16.
 */
@RestController
public class PageController {

    private static Logger logger = LoggerFactory.getLogger(GoogleDriveFileService.class);

    @Autowired
    private PageService pageService;

    @RequestMapping("/pages")
    public List<Page> getList(@RequestParam(value="folderName") String folderName) {
        return pageService.getFolderPages(folderName);
    }

    @RequestMapping("/pages/{id}")
    public Page get(@PathVariable String id) {
        logger.info("GET page by id {}", id);
        return pageService.getPageById(id);
    }
}
