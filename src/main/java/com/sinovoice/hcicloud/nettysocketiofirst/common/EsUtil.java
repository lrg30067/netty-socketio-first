package com.sinovoice.hcicloud.nettysocketiofirst.common;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by songxueyan on 2016/12/19.
 */
public class EsUtil {

    public enum EsType {
        success,
        content
    }
    //TODO 正式上
    private static String HOST = "http://192.169.51.4:9200/";
    //TODO 测试上
//    private static String HOST = "http://10.0.1.227:9200/";

    private static String SUCCESS_INDEX = "pa_success";
    private static String ANNOY_INDEX = "pa_annoy/";
    private static String PRODUCT_INDEX = "pa_product";
    private static String UPLOAD_INDEX = "pa_upload";
    

    private static String TYPE_SUCCESS = "success";
    private static String TYPE_CONTENT = "content";
    private static String TYPE_ANNOY = "annoy";
    private static String TYPE_PRODUCT = "product";
    
    

    //pa_success2016-12/success/
//    private static String query = "{\"size\":0,\"aggs\":{\"quality\":{\"terms\":{\"field\":\"QUALITY_NAME\"},\"aggs\":{\"platform\":{\"terms\":{\"field\":\"PLAT_FORM\"}}}}}}";
    private static String contentMap = "{\"mappings\":{\"content\":{\"_all\":{\"analyzer\":\"ik\",\"search_analyzer\":\"ik\",\"term_vector\":\"no\"},\"properties\":{\"VOICE_ID\":{\"type\":\"string\",\"index\":\"not_analyzed\"},\"ORDER_ID\":{\"type\":\"string\",\"index\":\"not_analyzed\"},\"PLAT_FORM\":{\"type\":\"string\",\"index\":\"not_analyzed\"},\"USER_PHONE\":{\"type\":\"string\",\"index\":\"not_analyzed\"},\"CALL_LENGTH\":{\"type\":\"integer\",\"index\":\"not_analyzed\"},\"xmlData\":{\"type\":\"string\",\"ignore_above\":10000,\"analyzer\":\"ik\",\"search_analyzer\":\"ik\"},\"CALL_CONTENT\":{\"type\":\"string\",\"ignore_above\":10000,\"analyzer\":\"ik\",\"search_analyzer\":\"ik\",\"term_vector\":\"with_positions_offsets\"}}}},\"settings\":{\"index\":{\"max_result_window\":100000000}}}";
    //pa_product 索引结构
    private static String productMap="{\"mappings\": {\"product\": {\"_all\": {\"analyzer\": \"ik\",\"search_analyzer\": \"ik\",\"term_vector\": \"no\"},\"properties\": {\"VOICE_ID\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"STAFF_ID\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"VOICE_PATH\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"PLAT_FORM\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"PRODUCT_TYPE\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"PRODUCT_WORD\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"CALL_CONTENT\": {\"type\": \"string\",\"analyzer\": \"ik\",\"search_analyzer\": \"ik\",\"term_vector\": \"with_positions_offsets\",\"ignore_above\": 10000},\"CALL_START_TIME\": {\"type\": \"date\",\"format\": \"yyyy-MM-dd HH:mm:ss\"},\"CALL_END_TIME\": {\"type\": \"date\",\"format\": \"yyyy-MM-dd HH:mm:ss\"},\"CREATE_TIME\": {\"type\": \"date\",\"format\": \"yyyy-MM-dd HH:mm:ss\"},\"CALL_PHONE\": {\"type\": \"string\",\"index\": \"not_analyzed\"}}}},\"settings\": {\"index\": {\"max_result_window\": 100000000}}}";
    //pa_annoy 索引结构
    private static String annoyMap="{\"mappings\": {\"annoy\": {\"_all\": {\"analyzer\": \"ik\",\"search_analyzer\": \"ik\",\"term_vector\": \"no\"},\"properties\": {\"VOICE_ID\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"STAFF_ID\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"VOICE_PATH\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"PLAT_FORM\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"ANNOY_TYPE\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"ANNOY_WORD\": {\"type\": \"string\",\"index\": \"not_analyzed\"},				 \"ANNOY_NUM\" : {					\"type\" : \"long\",					\"index\": \"not_analyzed\"				},\"CALL_CONTENT\": {\"type\": \"string\",\"analyzer\": \"ik\",\"search_analyzer\": \"ik\",\"term_vector\": \"with_positions_offsets\",					\"ignore_above\":10000},				\"TRANS_CONTENT\": {\"type\": \"string\",\"analyzer\": \"ik\",\"search_analyzer\": \"ik\",\"term_vector\": \"with_positions_offsets\",\"ignore_above\":10000},\"CALL_START_TIME\": {\"type\": \"date\",\"format\": \"yyyy-MM-dd HH:mm:ss\"},\"CALL_END_TIME\": {\"type\": \"date\",\"format\": \"yyyy-MM-dd HH:mm:ss\"},\"CREATE_TIME\":{\"type\": \"date\",\"format\": \"yyyy-MM-dd HH:mm:ss\"},\"CALL_PHONE\":{\"type\": \"string\",\"index\": \"not_analyzed\"},\"ANNOY_HISTORY_TYPE\":{\"type\": \"string\",\"index\": \"not_analyzed\"},\"SEAT_ATTITUDE\":{\"type\": \"string\",\"index\": \"not_analyzed\"}}}},\"settings\":{\"index\" : { \"max_result_window\" : 100000000}}}";
    
    

    public static String successUrl(){
        return HOST+SUCCESS_INDEX+"*/"+TYPE_SUCCESS;
    }

    public static String successDateUrl(){
        return HOST+SUCCESS_INDEX+successIndexPostfix()+"/"+TYPE_SUCCESS;
    }

    public static String contentUrl(){
        return HOST+SUCCESS_INDEX+successIndexPostfix()+"/"+TYPE_CONTENT;
    }
    
    public static String getUploadUrl(){
    	return HOST+UPLOAD_INDEX+"/"+TYPE_CONTENT;
    }

    public static void createIndex() {
    	System.out.println("创建success索引时间："+new Date());
        createIndex(successUrl(),"{}");
        String createIndex = createIndex(HOST+SUCCESS_INDEX+successIndexPostfix()+"/",contentMap);
        String productMapIndex =createIndex(HOST+PRODUCT_INDEX+successIndexPostfix()+"/",productMap);
        String annoyMapIndex = createIndex(HOST+ANNOY_INDEX.substring(0,ANNOY_INDEX.length()-1)+successIndexPostfix()+"/",annoyMap);
        
        System.out.println(annoyMapIndex+"======="+annoyMapIndex);
        System.out.println(createIndex+"======="+productMapIndex);
        
    }

    public static String createIndex(String url, String query) {
        return HTTPMethod.doPostQuery(url, query, 3000);
    }

    private static String successIndexPostfix() {
    	Calendar cl = Calendar.getInstance();
        cl.setTime(new Date());
        cl.add(Calendar.MONTH, 1);
        return StringUtil.dateToString(cl.getTime(), "yyyy-MM");
    }

    public static String querySuccess(String json) {
        return queryAll(json);
    }

/*    public static String successReport() {
        return queryAll(query);
    }
*/
    public static String queryAll(String json) {
        return HTTPMethod.doPostQuery(HOST+SUCCESS_INDEX+"*/" + TYPE_SUCCESS + "/_search", json, 30000);
    }

    public static String put(String id, String json) {
        return HTTPMethod.doPutQuery(HOST+SUCCESS_INDEX + successIndexPostfix() + "/"+TYPE_SUCCESS + "/" + id, json, 30000);
    }

    //all index
    public static String getContentQueryUrl() {
        return HOST+SUCCESS_INDEX + "*/" + TYPE_CONTENT + "/_search";
    }

    public static String getAnnoyQueryUrl() {
        return HOST+ANNOY_INDEX.substring(0, ANNOY_INDEX.length()-1) +"*/"+ TYPE_ANNOY + "/_search";
    }
    
    public static String getProductQueryUrl() {
    	return HOST+PRODUCT_INDEX +"*/"+ TYPE_PRODUCT + "/_search";
    }
    
    
    
}
