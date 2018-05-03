package com.gromoks.container.testentity;

import java.util.Objects;

public class TestClass {
    private String field1;
    private int field2;
    private boolean field3;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public int getField2() {
        return field2;
    }

    public void setField2(int field2) {
        this.field2 = field2;
    }

    public boolean getField3() {
        return field3;
    }

    public void setField3(boolean field3) {
        this.field3 = field3;
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "field1='" + field1 + '\'' +
                ", field2=" + field2 +
                ", field3=" + field3 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestClass testClass = (TestClass) o;
        return field2 == testClass.field2 &&
                field3 == testClass.field3 &&
                Objects.equals(field1, testClass.field1);
    }

    @Override
    public int hashCode() {

        return Objects.hash(field1, field2, field3);
    }
}
