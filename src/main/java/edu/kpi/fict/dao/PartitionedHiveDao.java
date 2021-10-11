package edu.kpi.fict.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class PartitionedHiveDao<T> {

    public void save(final Connection connection, final List<T> models, final int partition) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(getSaveQuery(partition, models));
        preparedStatement.execute();
    }

    protected abstract String getSaveQuery(final int partition, final List<T> models);
}
