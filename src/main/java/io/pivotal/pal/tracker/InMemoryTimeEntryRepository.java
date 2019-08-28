package io.pivotal.pal.tracker;


import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private Map<Long,TimeEntry> userTable= new HashMap<>();
    long id=0L;

    private long createId(){
       return ++id;
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        long createdId=createId();
        userTable.put(createdId,new TimeEntry(createdId,timeEntry.getProjectId(),timeEntry.getUserId(), timeEntry.getDate(),timeEntry.getHours()));
       return userTable.get(id);
    }

    @Override
    public TimeEntry find(long id) {
        return userTable.get(id);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        userTable.replace(id,new TimeEntry(id,timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getDate(),timeEntry.getHours()));
        return userTable.get(id);

    }

    @Override
    public void delete(long id) {
        userTable.remove(id);
    }

    @Override
    public List<TimeEntry> list(){
        List<TimeEntry> list = new ArrayList<TimeEntry>();
        for (long  id : userTable.keySet()){

           list.add(userTable.get(id));
        }
        return list;
    }

}
