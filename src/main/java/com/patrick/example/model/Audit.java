package com.patrick.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/7/6 14:35
 * UpdateTime 2017/7/6 14:35
 */
@Entity
public class Audit {

    @Id
    @GeneratedValue
    private int id;

    private int expense;
    private String dm_audit;
    private String finance_audit;
    private String gm_audit;

    public Audit(int expense, String dm_audit, String finance_audit, String gm_audit) {
        this.expense = expense;
        this.dm_audit = dm_audit;
        this.finance_audit = finance_audit;
        this.gm_audit = gm_audit;
    }

    public Audit() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public String getDm_audit() {
        return dm_audit;
    }

    public void setDm_audit(String dm_audit) {
        this.dm_audit = dm_audit;
    }

    public String getFinance_audit() {
        return finance_audit;
    }

    public void setFinance_audit(String finance_audit) {
        this.finance_audit = finance_audit;
    }

    public String getGm_audit() {
        return gm_audit;
    }

    public void setGm_audit(String gm_audit) {
        this.gm_audit = gm_audit;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "id='" + id + '\'' +
                ", expense=" + expense +
                ", dm_audit='" + dm_audit + '\'' +
                ", finance_audit='" + finance_audit + '\'' +
                ", gm_audit='" + gm_audit + '\'' +
                '}';
    }
}
