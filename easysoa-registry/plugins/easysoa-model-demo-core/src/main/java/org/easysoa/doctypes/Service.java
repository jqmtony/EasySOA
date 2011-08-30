package org.easysoa.doctypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Mirror of the properties defined in the XML contributions. Defines all the
 * Service related data.
 * 
 * @author mkalam-alami
 * 
 */
public class Service extends EasySOADoctype {

    public static final String DOCTYPE = "Service";
    public static final String SCHEMA = "servicedef";
    public static final String SCHEMA_PREFIX = "serv:";

    public static final String PROP_URL = "url";
    public static final String PROP_LIGHTURL = "lightUrl";
    public static final String PROP_CALLCOUNT = "callcount";
    public static final String PROP_RELATEDUSERS = "relatedUsers";
    public static final String PROP_HTTPMETHOD = "httpMethod";
    public static final String PROP_CONTENTTYPEIN = "contentTypeIn";
    public static final String PROP_CONTENTTYPEOUT = "contentTypeOut";
    public static final String PROP_FILEURL = "fileUrl"; // TODO: Switch to EasySOADoctype?

    public static final String PROP_PARENTURL = "parentUrl";

    private static Map<String, String> propertyList = null;

    /**
     * Returns a map containing all properties from the Service.SCHEMA schema,
     * with a short description.
     */
    public static Map<String, String> getPropertyList() {
        if (propertyList == null) {
            propertyList = new HashMap<String, String>();
            propertyList.put(PROP_URL, "(mandatory) Service URL.");
            propertyList.put(PROP_CALLCOUNT, "Times the service has been called since last notification");
            propertyList.put(PROP_RELATEDUSERS, "Users that have been using the service");
            propertyList.put(PROP_HTTPMETHOD, "POST, GET...");
            propertyList.put(PROP_CONTENTTYPEIN, "HTTP content type of the request body");
            propertyList.put(PROP_CONTENTTYPEOUT, "HTTP content type of the result body");
            propertyList.put(PROP_FILEURL, "The URL of a file to attach to the document");
            propertyList.put(PROP_PARENTURL, "(mandatory) Service API URL");
        }
        return propertyList;
    }
}
