package by.gean.admin.service.menu;

import by.gean.admin.bean.MenuItem;

import java.awt.*;
import java.util.List;

/**
 * Created by Kiryl_Parfiankou on 6/17/2016.
 */
public interface MenuService {
    List<MenuItem> get();
    MenuItem get(String link);
}
