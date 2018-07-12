package org.thorn.sailfish.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.thorn.sailfish.core.Configuration;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: chen.chris
 * @Since: 13-11-20 下午3:23
 * @Version: 1.0
 */
@Service
public class ResourceService {

    public List<String> getAllTemplates(String rootPath) {
        File templateDir = new File(rootPath + Configuration.TEMPLATE_PATH);
        String absPath = templateDir.getAbsolutePath();

        List<String> list = new ArrayList<String>();
        loopDir(templateDir, list);

        for (int i = 0; i < list.size(); i++) {
            String path = list.get(i);

            path = StringUtils.removeStart(path, absPath);
            path = path.replaceAll("\\\\", "/");
            list.set(i, path);
        }

        return list;
    }

    private void loopDir(File dir, List<String> files) {

        File[] fileArray = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }

                if (StringUtils.endsWithIgnoreCase(file.getName(), Configuration.TEMPLATE_SUFFIX)) {
                    return true;
                }
                return false;
            }
        });

        for (File file : fileArray) {
            if (file.isDirectory()) {
                loopDir(file, files);
            } else {
                files.add(file.getAbsolutePath());
            }
        }
    }

}
