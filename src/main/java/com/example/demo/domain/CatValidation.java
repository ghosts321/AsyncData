package com.example.demo.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName CatValiDation
 * @Description TODO
 * @Author gaomingyaoxuan
 * @Date 2019-08-01 18:14
 * @Version 1.0
 */
public class CatValidation {

	private int id;

	private String valType;

	private String name;

	private String description;

	private int pid;

	private int balance;

	private int hardLimit;

	private int craetion;

	private int lastModified;

	private int expiration;

	private int state;

	private int primaryPin;

	private String secondaryPin;

	private int parId;

	private int locationId;

	private int nonbillable;

	private int freeMoney;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValType() {
        return valType;
    }

    public void setValType(String valType) {
        this.valType = valType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getHardLimit() {
        return hardLimit;
    }

    public void setHardLimit(int hardLimit) {
        this.hardLimit = hardLimit;
    }

    public int getCraetion() {
        return craetion;
    }

    public void setCraetion(int craetion) {
        this.craetion = craetion;
    }

    public int getLastModified() {
        return lastModified;
    }

    public void setLastModified(int lastModified) {
        this.lastModified = lastModified;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPrimaryPin() {
        return primaryPin;
    }

    public void setPrimaryPin(int primaryPin) {
        this.primaryPin = primaryPin;
    }

    public String getSecondaryPin() {
        return secondaryPin;
    }

    public void setSecondaryPin(String secondaryPin) {
        this.secondaryPin = secondaryPin;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getNonbillable() {
        return nonbillable;
    }

    public void setNonbillable(int nonbillable) {
        this.nonbillable = nonbillable;
    }

    public int getFreeMoney() {
        return freeMoney;
    }

    public void setFreeMoney(int freeMoney) {
        this.freeMoney = freeMoney;
    }
}
