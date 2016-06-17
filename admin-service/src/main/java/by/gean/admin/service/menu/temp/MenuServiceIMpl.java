package by.gean.admin.service.menu.temp;

import by.gean.admin.bean.MenuItem;
import by.gean.admin.service.menu.MenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiryl_Parfiankou on 6/17/2016.
 */
@Service
public class MenuServiceImpl implements MenuService {

    private List<MenuItem> menuList;

    public MenuServiceImpl() {
        menuList = new ArrayList<>();

        MenuItem services = new MenuItem("Services", "Services");
        List<MenuItem> servicesChildren = new ArrayList<>();
        MenuItem articleServices = new MenuItem("Article Services ", "Services-Articles");
        MenuItem menuServices = new MenuItem("Menu Services ", "Services-Menu");
        MenuItem userServices = new MenuItem("User Services ", "Services-Users");
        MenuItem imageServices = new MenuItem("Image Services ", "Services-Images");
        MenuItem variableServices = new MenuItem("Variable Services ", "Services-Variables");
        servicesChildren.add(articleServices);
        servicesChildren.add(menuServices);
        servicesChildren.add(userServices);
        servicesChildren.add(imageServices);
        servicesChildren.add(variableServices);
        services.setChildren(servicesChildren);
        menuList.add(services);

        MenuItem menu = new MenuItem("Menu", "Menu");
        menuList.add(menu);

        MenuItem images = new MenuItem("Images", "Images");
        menuList.add(images);

        MenuItem users = new MenuItem("Users","Users");
        menuList.add(users);

        MenuItem variables = new MenuItem("Variables", "Variables");
        menuList.add(variables);

    }

    @Override
    public List<MenuItem> get() {
        return menuList;
    }

    @Override
    public MenuItem get(String link) {

        MenuItem result = null;
        for (MenuItem menuItem: get()) {
            result = getFromItemAndChildren(menuItem, link);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    private MenuItem getFromItemAndChildren(MenuItem menuItem, String link) {
        List<MenuItem> menuItems;
        if (menuItem != null) {

            if (link != null) {
                if (link.equals(menuItem.getLink())) {
                    return menuItem;
                }
            }

            menuItems = menuItem.getChildren();
            if (menuItems != null && menuItems.size() > 0) {
                MenuItem childItem = null;
                for (MenuItem item: menuItems) {
                    childItem = getFromItemAndChildren(item, link);
                    if(childItem != null) {
                        return childItem;
                    }
                }
            }
        }
        return null;
    }
}