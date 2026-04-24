package com.laundromat.server.db;

import java.sql.Timestamp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.laundromat.server.model.Machine;
import com.laundromat.server.model.User;

@Component
public class Update {

    private static JdbcTemplate jdbcTemplate;

    public Update(JdbcTemplate jdbcTemplate) {
        Update.jdbcTemplate = jdbcTemplate;
    }

    public static void updateUser(User user) {
        jdbcTemplate.update(
            "UPDATE \"users\" SET wallet = ?, reputation = ? WHERE id = ?",
            user.wallet.getBalance(),
            user.reputation.getScore(),
            user.getId()
        );
        System.out.println("Update: user " + user.getId() + " synced to DB");
    }

    public static void syncMachines(Machine[] machines) {
        for (Machine machine : machines) {
            User currentUser = machine.getCurrentUser();
            jdbcTemplate.update(
                "UPDATE machine SET status = ?, \"current_user\" = ? WHERE id = ?",
                machine.getState().toLowerCase(),
                currentUser != null ? currentUser.getId() : null,
                machine.getId()
            );
        }
        // System.out.println("Update: " + machines.length + " machines synced to DB");
    }

    public static void syncMachineTimestamp(int machineId, Timestamp time) {
        jdbcTemplate.update(
            "UPDATE machine SET timeout_time = ? WHERE id = ?",
            time,
            machineId
        );
    }
}
