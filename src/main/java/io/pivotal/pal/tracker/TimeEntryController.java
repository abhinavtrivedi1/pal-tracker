package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
public class TimeEntryController {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;


    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository=timeEntryRepository;
        this.timeEntrySummary= meterRegistry.summary("timeEntry.summary");
        this.actionCounter= meterRegistry.counter("timeEntry.actionCounter");
    }
    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable(name = "id") long id) {
        TimeEntry timeEntry = timeEntryRepository.find(id);
        if(null==timeEntry) {
            return new ResponseEntity(timeEntry, HttpStatus.NOT_FOUND);
        }
        actionCounter.increment();
        return new ResponseEntity(timeEntry, HttpStatus.OK);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {

        List<TimeEntry> timeEntries= timeEntryRepository.list();
        actionCounter.increment();

        return new ResponseEntity(timeEntries, HttpStatus.OK);



    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable (name="id")  long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry timeEntry= timeEntryRepository.update(timeEntryId,expected);
        if(null!=timeEntry){
            actionCounter.increment();
            return new ResponseEntity(timeEntry, HttpStatus.OK);
        }

        return new ResponseEntity(timeEntry, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable(name="id") long timeEntryId) {

            timeEntryRepository.delete(timeEntryId);
            actionCounter.increment();
            timeEntrySummary.record(timeEntryRepository.list().size());
            return new ResponseEntity(HttpStatus.NO_CONTENT);


    }
}
