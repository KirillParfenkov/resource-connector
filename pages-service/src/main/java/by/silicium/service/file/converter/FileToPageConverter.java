package by.silicium.service.file.converter;

import by.silicium.domain.Page;
import com.google.api.services.drive.model.File;

/**
 * Created by parf on 29.5.16.
 */
public class FileToPageConverter {

    public static Page convert(File file) {
        Page page = new Page();
        page.setId(file.getId());
        page.setName(file.getName());

        return page;
    }
}
