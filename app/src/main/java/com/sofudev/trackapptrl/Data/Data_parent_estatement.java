package com.sofudev.trackapptrl.Data;

import java.util.List;

public class Data_parent_estatement {
    private String parentTitle, parentSubtotal;
    private List<Data_child_estatement> listChildEstatement;

    public Data_parent_estatement(String parentTitle, String parentSubtotal, List<Data_child_estatement> listChildEstatement) {
        this.parentTitle = parentTitle;
        this.parentSubtotal = parentSubtotal;
        this.listChildEstatement = listChildEstatement;
    }

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public String getParentSubtotal() {
        return parentSubtotal;
    }

    public void setParentSubtotal(String parentSubtotal) {
        this.parentSubtotal = parentSubtotal;
    }

    public List<Data_child_estatement> getListChildEstatement() {
        return listChildEstatement;
    }

    public void setListChildEstatement(List<Data_child_estatement> listChildEstatement) {
        this.listChildEstatement = listChildEstatement;
    }
}
