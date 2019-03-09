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

    public void updateSession(WorkSession workSession){
        for (int i = 0; i< mWorkSessionList.size(); i++ ) {
            if(mWorkSessionList.get(i).getUUID().equals(workSession.getUUID()))
                mWorkSessionList.set(i,workSession);
        }
    }

    public void addSession(WorkSession workSession){
        workSession.setDateStart(new Date());
        workSession.setDateEnd(new Date());
        mWorkSessionList.add(workSession);
    }

    public void remSession(UUID uuid){
        for (WorkSession session:mWorkSessionList){
            if(session.getUUID().equals(uuid)){
                mWorkSessionList.remove(session);
                return;
            }
        }
    }

    public List<WorkSession> getWorkSessions(){
        return mWorkSessionList;
    }

    private WorkSessionLab(){
        mWorkSessionList = new ArrayList<>();
        for(int i = 0 ; i < 1; i++){
            WorkSession workSession = new WorkSession();
            workSession.setDateStart(new Date());
            workSession.setDateEnd(new Date());
            mWorkSessionList.add(workSession);
        }
    }
}
