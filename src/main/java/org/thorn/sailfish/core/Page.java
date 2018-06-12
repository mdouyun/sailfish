package org.thorn.sailfish.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Page
 * @Description:
 * @author chenyun
 * @date 2012-4-26 下午04:55:47
 */
public class Page<T> implements Serializable {

	public static final long DEFAULT_LIMIT = 20;

	/** */
	private static final long serialVersionUID = 5951452024298265919L;
	/** 结果集总数 */
	private long total;
	/** 结果集 */
	private List<T> resultSet = new ArrayList<T>();

	private long pageSize;
	
	private long pageIndex;

	public Page(Long pageIndex, Long pageSize) {
		if (pageSize == null || pageSize <= 0) {
			pageSize = DEFAULT_LIMIT;
		}
		
		if(pageIndex == null || pageIndex <= 0) {
			pageIndex = 1L;
		}

		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}

	public Page() {

	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

    public List<T> getResultSet() {
        return resultSet;
    }

    public void setResultSet(List<T> resultSet) {
        this.resultSet = resultSet;
    }

    public long getStart() {
		return (pageIndex - 1) * pageSize;
	}
	
	public long getPageSize() {
		return pageSize;
	}

	public long getPageIndex() {
		return pageIndex;
	}

	public void setPageData(Page<T> page) {

		if (page != null) {
			this.resultSet = page.resultSet;
			this.total = page.getTotal();
		}
	}

}
