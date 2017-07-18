package com.patrick.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Desciption 购物清单，测试自动生成表
 * Create By  li.bo
 * CreateTime 2017/7/3 13:24
 * UpdateTime 2017/7/3 13:24
 */
@Entity
public class Purchase {

    @Id
    @GeneratedValue
    private int id;

    private String purchaseOrder;
    private String gm_audit;
    private String finance_audit;

    public Purchase(String purchaserOrder, String gm_audit, String finance_audit){
        this.purchaseOrder = purchaserOrder;
        this.gm_audit = gm_audit;
        this.finance_audit = finance_audit;
    }

    public Purchase(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getGm_audit() {
        return gm_audit;
    }

    public void setGm_audit(String gm_audit) {
        this.gm_audit = gm_audit;
    }

    public String getFinance_audit() {
        return finance_audit;
    }

    public void setFinance_audit(String finance_audit) {
        this.finance_audit = finance_audit;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", purchaseOrder='" + purchaseOrder + '\'' +
                ", gm_audit='" + gm_audit + '\'' +
                ", finance_audit='" + finance_audit + '\'' +
                '}';
    }
}
