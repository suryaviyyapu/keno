package com.bl4k3.keno;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Note {

    private String title;
    private String message;
    private Date timestamp;
    private NoteGrade grade;
    private String userId;

    public Note() {}

    public Note(String title, String message, Date timestamp, NoteGrade grade, String userId) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.grade = grade;
        this.userId = userId;
    }

    public Note(String title, String message, NoteGrade grade, String userId) {
        this.title = title;
        this.message = message;
        this.grade = grade;
        this.timestamp = new Date(System.currentTimeMillis());
        this.userId = userId;
    }

    public Note(String title, String message, Date timestamp, String userId) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.grade = NoteGrade.Light;
        this.userId = userId;
    }

    public Note(String title, String message, String userId) {
        this.title = title;
        this.message = message;
        this.grade = NoteGrade.Light;
        this.timestamp = new Date(System.currentTimeMillis());
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public NoteGrade getGrade() {
        return grade;
    }

    public void setGrade(NoteGrade grade) {
        this.grade = grade;
    }
}
