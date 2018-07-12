package org.thorn.sailfish.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: chen.chris
 * @Since: 13-10-16 下午5:57
 * @Version: 1.0
 */
public class ResourceFolder {

    private String name;

    private String parent;

    /**
     * 完整路径
     */
    private String path;

    /**
     * 拥有的文件数
     */
    private int fileNumber;

    /**
     * 子目录
     */
    private List<ResourceFolder> childFolders = new ArrayList<ResourceFolder>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public List<ResourceFolder> getChildFolders() {
        return childFolders;
    }

    public void setChildFolders(List<ResourceFolder> childFolders) {
        this.childFolders = childFolders;
    }
}
