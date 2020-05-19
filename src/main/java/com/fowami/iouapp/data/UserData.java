package com.fowami.iouapp.data;

import java.util.List;

public class UserData {

    private String name;
    private List<IouData> owes;
    private List<IouData> owedBy;
    private Double balance;

    public UserData() {
    }

    public UserData(String name, List<IouData> owes, List<IouData> owedBy, Double balance) {
        this.name = name;
        this.owes = owes;
        this.owedBy = owedBy;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IouData> getOwes() {
        return owes;
    }

    public void setOwes(List<IouData> owes) {
        this.owes = owes;
    }

    public List<IouData> getOwedBy() {
        return owedBy;
    }

    public void setOwedBy(List<IouData> owedBy) {
        this.owedBy = owedBy;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void updateBalance() {
        Double totalOwed = this.owedBy.stream()
                .mapToDouble(x -> x.getAmount())
                .sum();

        Double totalOwes = this.owes.stream()
                .mapToDouble(x -> x.getAmount())
                .sum();
        this.balance = (totalOwed - totalOwes);
    }
}
