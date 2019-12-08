package com.example.rumi.tourassistant.EventClasses;

import java.util.Date;

public class Note {
    private String noteId;
    private String note;
    private Date noteDate;

    public Note() {
    }

    public Note(String noteId, String note, Date noteDate) {
        this.noteId = noteId;
        this.note = note;
        this.noteDate = noteDate;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }
}
