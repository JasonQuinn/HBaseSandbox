package Prototype;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

/**
 * Used by the book examples to generate tables and fill them with test data.
 */
public class HBaseHelper {

    private Configuration conf = null;
    private HBaseAdmin admin = null;

    protected HBaseHelper() throws IOException {
        ConnectionManager tablePool = new ConnectionManager();

        this.conf = tablePool.getConfig();
        this.admin = new HBaseAdmin(conf);
    }

    public static HBaseHelper getHelper() throws IOException {
        return new HBaseHelper();
    }

    public boolean existsTable(String table)
            throws IOException {
        return admin.tableExists(table);
    }

    public void createTable(String table, String... colfams)
            throws IOException {
        createTable(table, null, colfams);
    }

    public void createTable(String table, byte[][] splitKeys, String... colfams)
            throws IOException {
        HTableDescriptor desc = new HTableDescriptor(table);
        for (String cf : colfams) {
            HColumnDescriptor coldef = new HColumnDescriptor(cf);
            desc.addFamily(coldef);
        }
        if (splitKeys != null) {
            admin.createTable(desc, splitKeys);
        } else {
            admin.createTable(desc);
        }
    }

    public void disableTable(String table) throws IOException {
        admin.disableTable(table);
    }

    public void dropTable(String table) throws IOException {
        if (existsTable(table)) {
            disableTable(table);
            admin.deleteTable(table);
        }
    }
}
