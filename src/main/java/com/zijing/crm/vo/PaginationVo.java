package com.zijing.crm.vo;

import java.util.List;

public class PaginationVo<E> {
    private Integer total;
    private List<E> dataList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<E> getDataList() {
        return dataList;
    }

    public void setDataList(List<E> dataList) {
        this.dataList = dataList;
    }
}
