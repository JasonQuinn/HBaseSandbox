package Prototype.Repositories;

import java.io.IOException;

/**
 * Created by Jason.Quinn on 19/01/2015.
 */
public interface Repository {
    void createTable() throws IOException;

    void dropTable() throws IOException;
}
