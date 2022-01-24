package com.accountify.demo.repository;

import com.accountify.demo.service.ConnectionService;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LancamentoRepository {

    private final ConnectionService connectionService;

    public LancamentoRepository(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    public List<String> executeQuery(String sql) {
        try (Statement stmt = connectionService.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<String> result = new ArrayList<>();

            while (resultSet.next()) {
                for (int i = 1; i<= metaData.getColumnCount(); i++) {
                    result.add(metaData.getColumnLabel(i) + ": " +resultSet.getString(i));
                }
            }

            return result;
        } catch (SQLException e) {
            return List.of("erro!");
        }
    }
}
