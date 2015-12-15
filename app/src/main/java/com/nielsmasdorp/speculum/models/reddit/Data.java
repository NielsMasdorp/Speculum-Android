package com.nielsmasdorp.speculum.models.reddit;

import java.util.ArrayList;
import java.util.List;

public class Data {

    public String modhash;
    public List<Child> children = new ArrayList<>();
    public String after;
    public Object before;
}
