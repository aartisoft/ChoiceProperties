package com.example.choiceproperties.Models;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Plots implements Serializable {

    private String plotnumber;
    private String plotarea;

    private String customerNmae;
    private String plotPrice;
    private String depositAmount;
    private String depositType;
    private String remainingAmount;
    private String installment;
    ArrayList<String> installments;
    private String installmentType;
    private String payedAmount;
    private String agentName;
    private String comissionStatus;

    private String status;
    private String ploteId;
    private Long createdDateTime;


    //empty constructor is neaded
    public Plots() {
    }

    @Exclude
    public Long getCreatedDateTimeLong() {
        return createdDateTime;
    }

    public Map<String, String> getCreatedDateTime() {
        return ServerValue.TIMESTAMP;
    }

    public void setCreatedDateTime(Long createdDateTime) {
        this.createdDateTime = (Long) createdDateTime;
    }

    public String getPlotnumber() {
        return plotnumber;
    }

    public void setPlotnumber(String plotnumber) {
        this.plotnumber = plotnumber;
    }

    public String getPlotarea() {
        return plotarea;
    }

    public void setPlotarea(String plotarea) {
        this.plotarea = plotarea;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPloteId() {
        return ploteId;
    }

    public void setPloteId(String ploteId) {
        this.ploteId = ploteId;
    }

    public String getCustomerNmae() {
        return customerNmae;
    }

    public void setCustomerNmae(String customerNmae) {
        this.customerNmae = customerNmae;
    }

    public String getPlotPrice() {
        return plotPrice;
    }

    public void setPlotPrice(String plotPrice) {
        this.plotPrice = plotPrice;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getInstallmentType() {
        return installmentType;
    }

    public void setInstallmentType(String installmentType) {
        this.installmentType = installmentType;
    }

    public String getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(String payedAmount) {
        this.payedAmount = payedAmount;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getComissionStatus() {
        return comissionStatus;
    }

    public void setComissionStatus(String comissionStatus) {
        this.comissionStatus = comissionStatus;
    }

    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    public ArrayList<String> getInstallments() {
        return installments;
    }

    public void setInstallments(ArrayList<String> installments) {
        this.installments = installments;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("plotnumber", plotnumber);
        result.put("plotarea", plotarea);
        result.put("status", status);
        result.put("ploteId", ploteId);

        result.put("customerNmae", customerNmae);
        result.put("plotPrice", plotPrice);
        result.put("depositAmount", depositAmount);
        result.put("depositType", depositType);
        result.put("remainingAmount", remainingAmount);
        result.put("installment", installment);
        result.put("installmentType", installmentType);
        result.put("payedAmount", payedAmount);
        result.put("agentName", agentName);
        result.put("comissionStatus", comissionStatus);
        result.put("twodays",getInstallments() );

        result.put("createdDateTime", getCreatedDateTime());

        return result;
    }

}