package com.example.rumi.tourassistant.EventClasses;


import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class Event{

    private String eventId;
    private String eventName , destinationName;
    private Date eventStartDate  , currentDate , endingDate;
    private String eventStatus;
    private int eventBudget;
    private int expenditure;
    private HashMap<String , Note> eventNotes;
    private HashMap<String , Expenditure>eventExpenditures;

    public Event() {
    }

    public Event(String eventId, String eventName, String destinationName, Date eventStartDate, Date currentDate, Date endingDate, String eventStatus, int eventBudget, int expenditure, HashMap<String, Note> eventNotes, HashMap<String, Expenditure> eventExpenditures) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.destinationName = destinationName;
        this.eventStartDate = eventStartDate;
        this.currentDate = currentDate;
        this.endingDate = endingDate;
        this.eventStatus = eventStatus;
        this.eventBudget = eventBudget;
        this.expenditure = expenditure;
        this.eventNotes = eventNotes;
        this.eventExpenditures = eventExpenditures;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public int getEventBudget() {
        return eventBudget;
    }

    public void setEventBudget(int eventBudget) {
        this.eventBudget = eventBudget;
    }

    public int getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(int expenditure) {
        this.expenditure = expenditure;
    }

    public HashMap<String, Note> getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(HashMap<String, Note> eventNotes) {
        this.eventNotes = eventNotes;
    }

    public HashMap<String, Expenditure> getEventExpenditures() {
        return eventExpenditures;
    }

    public void setEventExpenditures(HashMap<String, Expenditure> eventExpenditures) {
        this.eventExpenditures = eventExpenditures;
    }

    public double getNetExpenses(){
        double result = 0;

        if(eventExpenditures == null){
            return 0;
        }
        Set keys = eventExpenditures.keySet();
        for(Object key: keys){
            result +=  (eventExpenditures.get(key).getValue() * eventExpenditures.get(key).getQuantity());
        }
        return result;
    }

}
