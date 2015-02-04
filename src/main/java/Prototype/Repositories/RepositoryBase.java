package Prototype.Repositories;

import Prototype.ConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;

import java.io.IOException;

/**
 * Created by Jason.Quinn on 19/01/2015.
 */
public abstract class RepositoryBase implements Repository {
    public abstract String getTableName();

    protected HTableInterface getTable() throws IOException {
        ConnectionManager connectionManager = new ConnectionManager();
        return connectionManager.getTable(getTableName());
    }
}
