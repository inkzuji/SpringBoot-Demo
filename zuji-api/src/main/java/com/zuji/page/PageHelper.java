package com.zuji.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collections;
import java.util.List;

/**
 * 分页
 *
 * @author Ink足迹
 * @create 2020-06-07 17:01
 **/
public class PageHelper<T> implements IPage<T> {
    private static final long serialVersionUID = 234234L;

    /**
     * 查询数据列表
     */
    @ApiModelProperty("数据列表")
    private List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    @ApiModelProperty("总数")
    private long total = 0;

    /**
     * 每页显示条数，默认 5
     */
    @ApiModelProperty("每页显示条数")
    private long size = 5;

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private long current = 1;

    /**
     * 获取排序信息，排序的字段和正反序
     *
     * @return 排序信息
     */
    @Override
    public List<OrderItem> orders() {
        return null;
    }

    /**
     * 自动优化 COUNT SQL
     */
    private boolean optimizeCountSql = true;

    /**
     * 是否进行 count 查询
     */
    private boolean isSearchCount = true;

    public PageHelper() {
    }

    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size    每页显示条数
     */
    public PageHelper(long current, long size) {
        this(current, size, 0);
    }

    public PageHelper(long current, long size, long total) {
        this(current, size, total, true);
    }

    public PageHelper(long current, long size, long total, boolean isSearchCount) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
        this.isSearchCount = isSearchCount;
    }

    /**
     * 是否存在上一页
     *
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public PageHelper<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public PageHelper<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public PageHelper<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public PageHelper<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    @Override
    public boolean optimizeCountSql() {
        return optimizeCountSql;
    }

    @Override
    public boolean isSearchCount() {
        if (total < 0) {
            return false;
        }
        return isSearchCount;
    }

}
