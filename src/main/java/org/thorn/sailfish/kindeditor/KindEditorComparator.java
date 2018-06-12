package org.thorn.sailfish.kindeditor;

import org.apache.commons.lang.StringUtils;

import java.util.Comparator;

/**
 * @Author: yfchenyun
 * @Since: 13-11-28 下午2:21
 * @Version: 1.0
 */
public class KindEditorComparator implements Comparator<KindResource> {

    private enum ORDER {
        NAME, SIZE, TYPE
    }

    private ORDER order;

    public KindEditorComparator(String order) {
        if(StringUtils.equalsIgnoreCase(order, "size")) {
            this.order = ORDER.SIZE;
        } else if(StringUtils.equalsIgnoreCase(order, "name")) {
            this.order = ORDER.NAME;
        } else if(StringUtils.equalsIgnoreCase(order, "type")) {
            this.order = ORDER.TYPE;
        } else {
            this.order = ORDER.NAME;
        }
    }

    @Override
    public int compare(KindResource o1, KindResource o2) {

        if(o1.isIs_dir() && !o2.isIs_dir()) {
            return -1;
        } else if(!o1.isIs_dir() && o2.isIs_dir()) {
            return 1;
        } else {

            if(order == ORDER.NAME) {
                return o1.getFilename().compareTo(o2.getFilename());
            } else if(order == ORDER.SIZE) {
                return (int) (o1.getFilesize() - o2.getFilesize());
            } else {
                return o1.getFiletype().compareTo(o2.getFiletype());
            }
        }
    }
}
