package com.example.workhourscalculator;

import android.support.v4.app.ListFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WorkSessionLab {
    private static WorkSessionLab sWorkSessionLab;
    private List<WorkSession> mWorkSessionList;

    public static WorkSessionLab get(){
        if(sWorkSessionLab == null)
            sWorkSessionLab = new WorkSessionLab();
        return sWorkSessionLab;
    }

    public WorkSession getWorkSession(UUID uuid){
        for (WorkSession session:mWorkSessionList) {
            if(session.getUUID().equals(uuid))
                return session;
        }
        return null;
    }

    public List<WorkSession> getWorkSessions(){
        return mWorkSessionList;
    }

    private WorkSessionLab(){
        mWorkSessionList = new ArrayList<>();
        for(int i = 0 ; i < 10; i++){
            WorkSession workSession = new WorkSession();
            workSession.setDateStart(new Date());
            workSession.setDateEnd(new Date());
            mWorkSessionList.add(workSession);
        }
    }
}
