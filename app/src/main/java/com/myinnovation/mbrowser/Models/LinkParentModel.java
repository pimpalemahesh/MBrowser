package com.myinnovation.mbrowser.Models;

import java.util.ArrayList;

public class LinkParentModel {

    String title;
    ArrayList<LinkChildModel> List;

    public LinkParentModel(String title, ArrayList<LinkChildModel> list) {
        this.title = title;
        List = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<LinkChildModel> getList() {
        return List;
    }

    public void setList(ArrayList<LinkChildModel> list) {
        List = list;
    }
}
