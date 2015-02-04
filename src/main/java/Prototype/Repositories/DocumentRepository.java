package Prototype.Repositories;

import Prototype.HBaseHelper;
import Prototype.Models.Document;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jason.Quinn on 15/01/2015.
 */
public class DocumentRepository extends RepositoryBase {


    @Override
    public void createTable() throws IOException {
        //todo get to create automatically for the table definition
        HBaseHelper helper = HBaseHelper.getHelper();
        helper.dropTable(getTableName());
        helper.createTable(Tables.Document.NAME, Tables.Document.TableFamiles.Data.NAME, Tables.Document.TableFamiles.DocumentInfo.NAME,
                Tables.Document.TableFamiles.PolicyStatus.NAME, Tables.Document.TableFamiles.CollectionMembership.NAME);
    }

    @Override
    public void dropTable() throws IOException {
        HBaseHelper helper = HBaseHelper.getHelper();
        helper.dropTable(Tables.Document.NAME);
    }

    public void addDocument(UUID projectId, Document document) {
        Collection<Document> documents = new LinkedList<Document>();
        documents.add(document);

        addDocuments(projectId, documents);
    }

    public void addDocuments(UUID projectId, Collection<Document> documents) {
        try {
            List<Put> puts = new LinkedList<Put>();
            for (Document document: documents) {
                Put put = new Put(Bytes.toBytes(projectId + "\0" + document.docId.toString()));
                put.add(Bytes.toBytes(Tables.Document.TableFamiles.Data.NAME), Bytes.toBytes(Tables.Document.TableFamiles.Data.Columns.DRE_REF),
                        Bytes.toBytes(document.docRef));

                put.add(Bytes.toBytes(Tables.Document.TableFamiles.DocumentInfo.NAME), Bytes.toBytes(Tables.Document.TableFamiles.DocumentInfo.Columns.CREATE_DATE),
                        Bytes.toBytes(document.createDate.toDateTime(DateTimeZone.UTC).getMillis()));

                put.add(Bytes.toBytes(Tables.Document.TableFamiles.DocumentInfo.NAME), Bytes.toBytes(Tables.Document.TableFamiles.DocumentInfo.Columns.LAST_MODIFIED_DATE),
                        Bytes.toBytes(document.lastModifiedTime.toDateTime(DateTimeZone.UTC).getMillis()));

                if (document.collections != null) {
                    for (Long collection : document.collections) {
                        put.add(Bytes.toBytes(Tables.Document.TableFamiles.CollectionMembership.NAME), Bytes.toBytes(Tables.Document.TableFamiles.CollectionMembership.Columns.getCollectionMembership(collection)),
                                Bytes.toBytes(new DateTime(DateTimeZone.UTC).getMillis()));
                    }
                }

                puts.add(put);
            }

            HTableInterface tableInterface = getTable();
            tableInterface.put(puts);
            tableInterface.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDocuments(UUID projectId, Long collectionId, DateTime createDate, DateTime lastModifiedDate) throws IOException {
        FilterList filterList = new FilterList();
        filterList.addFilter(new PrefixFilter(Bytes.toBytes(projectId+ "\\")));

        FilterList policy1FilterList = new FilterList();
        policy1FilterList.addFilter(new DependentColumnFilter(Bytes.toBytes(Tables.Document.TableFamiles.CollectionMembership.NAME),
                Bytes.toBytes(Tables.Document.TableFamiles.CollectionMembership.Columns.getCollectionMembership(collectionId))));

        policy1FilterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes(Tables.Document.TableFamiles.DocumentInfo.NAME),
                Bytes.toBytes(Tables.Document.TableFamiles.DocumentInfo.Columns.CREATE_DATE),
                        CompareFilter.CompareOp.LESS_OR_EQUAL,
                new BinaryComparator(Bytes.toBytes(createDate.toDateTime(DateTimeZone.UTC).getMillis()))));

        policy1FilterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes(Tables.Document.TableFamiles.DocumentInfo.NAME),
                Bytes.toBytes(Tables.Document.TableFamiles.DocumentInfo.Columns.LAST_MODIFIED_DATE),
                CompareFilter.CompareOp.LESS_OR_EQUAL,
                new BinaryComparator(Bytes.toBytes(lastModifiedDate.toDateTime(DateTimeZone.UTC).getMillis()))));

        filterList.addFilter(policy1FilterList);


        Scan scan = new Scan();
//        scan.setFilter(filterList);
        scan.setStartRow(Bytes.toBytes(projectId.toString() + "\0"));
        scan.setStopRow(Bytes.toBytes(projectId.toString() + "\1"));
//        scan.set(Bytes.toBytes(projectId.toString()));
        HTableInterface tableInterface = getTable();
        try {
            ResultScanner resultScanner = tableInterface.getScanner(scan);

            int i = 0;
//            Result[] results = resultScanner.next(10);

            for (Result result: resultScanner){
                i++;
                System.out.println("Count " + i + result.toString());
            }

            resultScanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            tableInterface.close();
        }
    }

    @Override
    public String getTableName() {
        return Tables.Document.NAME;
    }
}
