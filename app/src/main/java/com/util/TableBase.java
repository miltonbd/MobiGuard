package com.util;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by milton on 19/01/16.
 */
public class TableBase {
    @DatabaseField(generatedId = true,columnName = MyDatabaseHelper.id)
    public Long id;

    @DatabaseField(canBeNull = true, columnName = MyDatabaseHelper.order)
    public Long order;

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TableBase{" +
                "id=" + id +
                ", order=" + order +
                '}';
    }

}
