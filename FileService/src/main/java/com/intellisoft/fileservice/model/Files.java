package com.intellisoft.fileservice.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class Files {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")

    private String id;

    private String name;

    private String type;

    @Lob
    private byte[] filecontent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

    public Files(String name, String type, byte[] filecontent) {
        super();
        this.name = name;
        this.type = type;
        this.filecontent = filecontent;
        // TODO Auto-generated constructor stub
    }

    public Files(String id, String name, String type, byte[] filecontent) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.filecontent = filecontent;
    }

    public Files() {
        super();
        // TODO Auto-generated constructor stub
    }





}
