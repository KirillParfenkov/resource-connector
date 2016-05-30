package by.silicium.service.file;

import by.silicium.domain.Page;

import java.util.List;

/**
 * Created by parf on 29.5.16.
 */
public interface PageService {

    List<Page> getFolderPages();
    List<Page> getFolderPages(String path);
    Page getPageById(String id);

}
