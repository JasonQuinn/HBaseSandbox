package Prototype;


import Prototype.Models.Document;
import Prototype.Repositories.DocumentRepository;
import Prototype.Repositories.Repository;
import org.apache.commons.lang.time.StopWatch;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by Jason.Quinn on 19/01/2015.
 */
public class App {
    public static void main(String[] args) throws IOException {
        Collection<Repository> repositories = new LinkedList<Repository>();

        DocumentRepository documentRepository = new DocumentRepository();
        repositories.add(documentRepository);

        //Drop the tables in case they already exist
        for (Repository repository: repositories){
            repository.dropTable();
        }

        //Create the tables
        for (Repository repository: repositories){
            repository.createTable();
        }

        //do work



        Collection<UUID> projectIds = new LinkedList<UUID>();

        StopWatch stopWatch1 = new StopWatch();
        stopWatch1.start();
        for (int j = 0; j < 2; j++) {
            UUID projectId = UUID.randomUUID();
            projectIds.add(projectId);
            for (int k = 0; k < 1000; k++) {
                Collection<Document> documents = new LinkedList<Document>();
                for (int z = 0; z < 10; z++) {
                    for (int i = 0; i < 10; i++) {
                        Collection<Long> collectionIds = new LinkedList<Long>();

                        collectionIds.add(1L);
                        collectionIds.add(2L);

                        DateTime createDate = DateTime.now().plusDays(5).minusMonths(3).minusMonths(i);
                        DateTime lastAccessDate = DateTime.now().minusMonths(i);
                        Document document = new Document("ref" + i, createDate, lastAccessDate, collectionIds);
                        documents.add(document);
                    }
                }

                documentRepository.addDocuments(projectId, documents);
                System.out.println("j - " + j + " k - " + k);
            }

//            Collection<Long> collectionIdsThree = new LinkedList<Long>();
//
//            collectionIdsThree.add(3L);
//
//            DateTime createDate = DateTime.now().minusMonths(3);
//            DateTime lastAccessDate = DateTime.now();
//            Document document = new Document("ref" + 20, createDate, lastAccessDate, collectionIdsThree);
//            documentRepository.addDocument(projectId, document);

            Collection<Document> documents = new LinkedList<Document>();
            for (int i = 0; i < 10; i++) {
                Collection<Long> collectionIds = new LinkedList<Long>();

                collectionIds.add(3L);

                DateTime createDate = DateTime.now().plusDays(5).minusMonths(3).minusMonths(i);
                DateTime lastAccessDate = DateTime.now().minusMonths(i);
                Document document = new Document("ref" + i, createDate, lastAccessDate, collectionIds);
                documents.add(document);
            }

            documentRepository.addDocuments(projectId, documents);
        }
        stopWatch1.stop();
        System.out.println("Wrote data - " + stopWatch1);

        DateTime sixMonthsAgo = DateTime.now().minusMonths(6);
        DateTime threeMonthsAgo = DateTime.now().minusMonths(3);

        StopWatch stopWatch2 = new StopWatch();
        UUID projectId = projectIds.stream().findFirst().get();
        System.out.println("ProjectId - " + projectId);
        stopWatch2.start();
        documentRepository.getDocuments(projectId, 3L, sixMonthsAgo, threeMonthsAgo);
        stopWatch2.stop();

        System.out.println("Got result data - " + stopWatch2);
    }
}
