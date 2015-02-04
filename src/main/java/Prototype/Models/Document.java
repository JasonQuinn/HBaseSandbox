package Prototype.Models;

import org.joda.time.DateTime;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by Jason.Quinn on 19/01/2015.
 */
public class Document {
    public UUID docId;
    public String docRef;
    public DateTime createDate;
    public DateTime lastModifiedTime;

    public Collection<Long> collections;

    public Document(String docRef, DateTime createDate, DateTime lastModifiedTime, Collection<Long> collections){
        this.docRef = docRef;
        this.createDate = createDate;
        this.lastModifiedTime = lastModifiedTime;
        this.collections = collections;
        docId = UUID.randomUUID();
    }

}
