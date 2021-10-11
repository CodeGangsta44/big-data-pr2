package edu.kpi.fict.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class HiveDao<T> {

    public void save(final Connection connection, final List<T> models) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(getSaveQuery(models));
        preparedStatement.execute();
    }

    protected abstract String getSaveQuery(final List<T> models);
}
