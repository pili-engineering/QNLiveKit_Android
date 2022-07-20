package com.qlive.coreimpl.http;

import java.io.Serializable;
import java.util.List;

public class PageData<T> implements Serializable {

    public int total_count;
    public int page_total;
    public int end_page;
    public List<T> list;

}
