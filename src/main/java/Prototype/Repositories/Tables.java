package Prototype.Repositories;

/**
 * ToDo make the table definitions correspond to an interface
 */
public class Tables {
    public static class Document {
        public static String NAME = "d";

        public static class TableFamiles {
            public static class Data {
                public static String NAME = "d";

                public static class Columns {
                    public static String DOC_ID = "i";
                    public static String DRE_REF = "r";

                }
            }

            public static class DocumentInfo {
                public static String NAME = "i";

                public static class Columns {
                    public static String CREATE_DATE = "c";
                    public static String LAST_MODIFIED_DATE = "l";

                }
            }

            public static class PolicyStatus {
                public static String NAME = "s";

                public static class Columns {
                    public static String getPolicyStatus(String policyId) {
                        return "p-" + policyId;
                    }
                }
            }

            public static class CollectionMembership {
                public static String NAME = "c";

                public static class Columns {
                    public static String getCollectionMembership(Long collectionId) {
                        return "c-" + collectionId;
                    }
                }
            }
        }
    }
}
