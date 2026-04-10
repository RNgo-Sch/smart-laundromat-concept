package com.laundromat.server.db;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.laundromat.server.model.Dryer;
import com.laundromat.server.model.Machine;
import com.laundromat.server.model.User;
import com.laundromat.server.model.Washer;

// TODO catch exceptions
@Component
public class Query {

    private static JdbcTemplate jdbcTemplate;

    public Query(JdbcTemplate jdbcTemplate) {
        Query.jdbcTemplate = jdbcTemplate;
    }

    public static User userFromId(int userId) {
        return jdbcTemplate.queryForObject(
            "SELECT id, username, password, wallet, reputation FROM \"users\" WHERE id = ?",
            (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getFloat("wallet"),
                rs.getInt("reputation")
            ),
            userId
        );
    }

    public static Machine machineFromId(int machineId) {
        return jdbcTemplate.queryForObject(
            "SELECT id, type, status, \"current_user\" FROM machine WHERE id = ?",
            (rs, rowNum) -> buildMachine(rs, rs.getString("type")),
            machineId
        );
    }

    public static Machine[] allWashers() {
        return machinesOfType("washer");
    }

    public static Machine[] allDryers() {
        return machinesOfType("dryer");
    }

    private static Machine[] machinesOfType(String type) {
        List<Machine> machines = jdbcTemplate.query(
            "SELECT id, status, \"current_user\" FROM machine WHERE \"type\" = ?",
            (rs, rowNum) -> buildMachine(rs,type),
            type
        );
        return machines.toArray(Machine[]::new);
    }

    public static int machineIdFromUserId(int userId) {
        return jdbcTemplate.queryForObject(
            "SELECT id FROM machine WHERE \"current_user\" = ?",
            (rs, rowNum) -> rs.getInt("id"),
            userId
        );
    }

    // Shared row-to-Machine mapping used by machineFromId and machinesOfType.
    private static Machine buildMachine(java.sql.ResultSet rs, String type) throws java.sql.SQLException {
        int id = rs.getInt("id");
        String rawStatus = rs.getString("status");

        Machine.State state;
        if (rawStatus == null) {
            state = Machine.State.AVAILABLE;
        } else {
            state = Machine.State.valueOf(rawStatus.trim().toUpperCase());
        }
        int currentUserId = rs.getInt("current_user");
        User currentUser = rs.wasNull() ? null : userFromId(currentUserId);

        Machine newMachine;
        switch (type) {
            case "washer":
                newMachine = new Washer(id, state, currentUser);
                break;
            case "dryer":
                newMachine = new Dryer(id, state, currentUser);
                break;
            default:
                newMachine = null;
        }
        return newMachine;
    }
}

