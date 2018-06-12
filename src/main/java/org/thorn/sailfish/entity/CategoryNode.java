package org.thorn.sailfish.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yfchenyun
 * @Since: 13-12-5 下午2:56
 * @Version: 1.0
 */
public class CategoryNode implements Comparable<CategoryNode> {

    private Category root;

    private List<CategoryNode> leaves = new ArrayList<CategoryNode>();

    public Category getRoot() {
        return root;
    }

    public void setRoot(Category root) {
        this.root = root;
    }

    public List<CategoryNode> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<CategoryNode> leaves) {
        this.leaves = leaves;
    }

    @Override
    public int compareTo(CategoryNode o) {

        if(this.getRoot().getSort() == null && o.getRoot().getSort() == null) {
            return 0;
        } else if(this.getRoot().getSort() == null) {
            return 1;
        } else if(o.getRoot().getSort() == null) {
            return -1;
        } else {
            return this.getRoot().getSort() - o.getRoot().getSort();
        }
    }
}
