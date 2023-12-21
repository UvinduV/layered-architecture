package com.example.layeredarchitecture.model;

public class CustOrderQueryDTO {
    private String oid;
    private String name;
    public CustOrderQueryDTO() {
    }
    public CustOrderQueryDTO(String oid, String name) {
        this.oid = oid;
        this.name = name;
    }
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "oid='" + oid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
