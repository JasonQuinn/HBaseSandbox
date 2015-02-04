package Prototype;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;

import java.io.IOException;

/**
 * Created by Jason.Quinn on 19/01/2015.
 */
public class ConnectionManager {

    private static HConnection hConnection;

    static{
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "v-quinnja-ubuntu");
        try {
            hConnection = HConnectionManager.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HTableInterface getTable(String tableName) throws IOException {
        return hConnection.getTable(tableName);
    }

    public Configuration getConfig() {
        return hConnection.getConfiguration();
    }
}
