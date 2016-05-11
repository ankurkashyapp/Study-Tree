package com.mounica.studytree.api.response;

import java.util.List;

/**
 * Created by ankur on 2/5/16.
 */
public class Products {
    private String deptname;
    private List<Items> items;

    public String getDeptname() {
        return deptname;
    }

    public List<Items> getItems() {
        return items;
    }
}
