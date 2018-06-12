package org.thorn.sailfish.kindeditor;

/**
 * @Author: yfchenyun
 * @Since: 13-11-22 下午7:20
 * @Version: 1.0
 */
public class KindResource {

    private boolean is_dir;

    private boolean has_file;

    private long filesize;

    private boolean is_photo;

    private String filetype;

    private String filename;

    private String datetime;

    public boolean isIs_dir() {
        return is_dir;
    }

    public void setIs_dir(boolean is_dir) {
        this.is_dir = is_dir;
    }

    public boolean isHas_file() {
        return has_file;
    }

    public void setHas_file(boolean has_file) {
        this.has_file = has_file;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public boolean isIs_photo() {
        return is_photo;
    }

    public void setIs_photo(boolean is_photo) {
        this.is_photo = is_photo;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
