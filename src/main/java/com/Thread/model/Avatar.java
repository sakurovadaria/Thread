package com.Thread.model;

import jakarta.persistence.*;

@Entity
public class Avatar {

    @Id
    @GeneratedValue
    private long id;

    private String filePath;

    private long fileSize;

    private String mediaType;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @OneToOne
    private Student student;

    public Avatar() {

    }


    public Avatar(long id, String filePath, long fileSize, String mediaType, byte[] data, Student student) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.student = student;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {this.filePath = filePath;}

    public long getFileSize() {return fileSize;}

    public void setFileSize(long fileSize) {this.fileSize = fileSize;}

    public String getMediaType() {return mediaType;}

    public void setMediaType(String mediaType) {this.mediaType = mediaType;}

    public byte[] getData() {return data;}

    public void setData(byte[] data) {this.data = data;}

    public void setStudent(Student student) {
        this.student = student;
    }
}