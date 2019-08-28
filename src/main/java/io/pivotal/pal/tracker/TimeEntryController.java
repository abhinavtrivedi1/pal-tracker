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



    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository=timeEntryRepository;
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

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {

        List<TimeEntry> timeEntries= timeEntryRepository.list();
        if(null!=timeEntries && !timeEntries.isEmpty())
        return new ResponseEntity(timeEntries, HttpStatus.OK);

        return new ResponseEntity(timeEntries, HttpStatus.NOT_FOUND);


    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable (name="id")  long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry timeEntry= timeEntryRepository.update(timeEntryId,expected);
        if(null!=timeEntry)
        return new ResponseEntity(timeEntry, HttpStatus.OK);

        return new ResponseEntity(timeEntry, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable(name="id") long timeEntryId) {

            timeEntryRepository.delete(timeEntryId);
            return new ResponseEntity(HttpStatus.NO_CONTENT);


    }
}
