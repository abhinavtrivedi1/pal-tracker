package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class TimeEntryController {

    @Autowired
    private TimeEntryRepository timeEntryRepository;
    @Autowired
    private TimeEntryController controller;


    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        
    }
    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable(name = "id") long id) {
        TimeEntry timeEntry = timeEntryRepository.find(id);
        if(null==timeEntry) { return new ResponseEntity(timeEntry, HttpStatus.NOT_FOUND);}
        return new ResponseEntity(timeEntry, HttpStatus.OK);
    }

    public ResponseEntity<List<TimeEntry>> list() {
        return null;
    }

    public ResponseEntity update(long timeEntryId, TimeEntry expected) {
        return null;
    }

    public ResponseEntity delete(long timeEntryId) {
        return null;
    }
}
