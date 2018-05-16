package com.gromoks.container.testdata;

import java.util.Objects;

public class ProductService {
    private String stringField;
    private int intField;
    private boolean booleanField;

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductService that = (ProductService) o;
        return intField == that.intField &&
                booleanField == that.booleanField &&
                Objects.equals(stringField, that.stringField);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stringField, intField, booleanField);
    }

    @Override
    public String toString() {
        return "ProductService{" +
                "stringField='" + stringField + '\'' +
                ", intField=" + intField +
                ", booleanField=" + booleanField +
                '}';
    }
}
