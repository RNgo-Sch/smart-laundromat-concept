package com.laundromat.server.db;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.laundromat.server.model.Machine;
import com.laundromat.server.model.User;

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
            (rs, rowNum) -> buildMachine(rs),
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
            (rs, rowNum) -> buildMachine(rs),
            type
        );
        return machines.toArray(Machine[]::new);
    }

    // Shared row-to-Machine mapping used by machineFromId and machinesOfType.
    private static Machine buildMachine(java.sql.ResultSet rs) throws java.sql.SQLException {
        int id = rs.getInt("id");
        Machine.State state = Machine.State.valueOf(rs.getString("status").toUpperCase());
        int currentUserId = rs.getInt("current_user");
        User currentUser = rs.wasNull() ? null : userFromId(currentUserId);
        return new Machine(id, state, currentUser);
    }
}
