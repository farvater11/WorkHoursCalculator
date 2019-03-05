package com.example.workhourscalculator;

import java.util.Date;
import java.util.UUID;

public class WorkSession {
    private Date mDateStart;
    private Date mDateEnd;
    private boolean mWorkedOut;
    private UUID mUUID;

    public WorkSession(){
        mUUID = getUUID();
    }

    public Date getDateStart() {
        return mDateStart;
    }
    public void setDateStart(Date dateStart) {
        mDateStart = dateStart;
    }

    public Date getDateEnd() {
        return mDateEnd;
    }
    public void setDateEnd(Date dateEnd) {
        mDateEnd = dateEnd;
    }

    public boolean isWorkedOut() {
        return mWorkedOut;
    }
    public void setWorkedOut(boolean workedOut) {
        mWorkedOut = workedOut;
    }

    public UUID getUUID() {
        return mUUID;
    }
}
