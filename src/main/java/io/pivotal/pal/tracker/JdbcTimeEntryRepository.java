package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.PreparedStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.sql.Date;

import java.sql.SQLException;
import java.util.List;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository

public class JdbcTimeEntryRepository implements TimeEntryRepository{


   JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate= new JdbcTemplate(dataSource);
    }


    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }





    @Override
    public TimeEntry find(long id) {
        return jdbcTemplate.queryForObject(
                "select * from time_entries where id = ?",
                new Object[]{id},
                (rs, rowNum) -> {
                    return new TimeEntry(
                            rs.getLong("id"),
                            rs.getLong("project_id"),
                            rs.getLong("user_id"),
                            rs.getDate("date").toLocalDate(),
                            rs.getInt("hours")
                    );
                }
        );
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        String sql="update time_entries set project_id = ?, user_id = ?, date = ? , hours = ? " +
        "where id = ?";

        jdbcTemplate.update(sql,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours()
                );

        return find(id);
    }

    @Override
    public void delete(long id) {
    jdbcTemplate.update("delete from time_entries where id =? ",id);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query(
                "select * from time_entries",
                (rs, rowNum) ->
                        new TimeEntry(
                                rs.getLong("id"),
                                rs.getLong("project_id"),
                                rs.getLong("user_id"),
                                rs.getDate("date").toLocalDate(),
                                rs.getInt("hours")
                        )
        );
    }
}
