package io.pivotal.pal.tracker;


import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.*;


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
    public List<TimeEntry> list() {
        return new ArrayList<TimeEntry>(userTable.values());
    }
}
