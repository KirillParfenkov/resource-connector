package by.silicium.controller;

import by.silicium.domain.Page;
import by.silicium.service.file.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Kiryl Parfiankou on 29.5.16.
 */
@RestController
public class PageController {

    @Autowired
    private PageService pageService;

    @RequestMapping("/pages")
    public List<Page> getList(@RequestParam(value="folderName") String folderName) {
        return pageService.getFolderPages(folderName);
    }
}
