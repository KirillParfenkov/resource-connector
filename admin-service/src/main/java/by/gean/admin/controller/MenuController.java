package by.gean.admin.controller;

import by.gean.admin.bean.MenuItem;
import by.gean.admin.service.menu.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Kiryl_Parfiankou on 6/17/2016.
 */
@RestController
public class MenuController {

    @Autowired
    MenuService menuService;

    private static Logger logger = LoggerFactory.getLogger(MenuController.class);

    @RequestMapping("/menu")
    public List<MenuItem> getList() {
        logger.info("getList started!");
        return menuService.get();
    }

    @RequestMapping("/menu/{link}")
    public MenuItem get(@PathVariable String link) {
        return menuService.get(link);
    }

}
