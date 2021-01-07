package dailyproject.moon.elasticsearch.configUtil;

import com.alibaba.fastjson.JSON;
import dailyproject.moon.elasticsearch.entity.ElasticEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @program: moon
 * @description: es核心操作类
 * @create: 2020-11-23 17:34
 **/

@Slf4j
@Component
public class BaseElasticUtil {

    @Autowired
    RestHighLevelClient restHighLevelClient;


    /**
     * 创建索引
     */
    public  void createIndex(){


        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "moroal po qi");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "ha uyt op");
        IndexRequest indexRequest = new IndexRequest("second")
                .id("1").source(jsonMap);

        try {
            IndexResponse index =restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
            System.out.println(index);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 断某个index是否存在
     */
    public boolean indexExist(String idxName) throws Exception {
        GetIndexRequest request = new GetIndexRequest(idxName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }




    /**
     * 判断文档是否存在
     * @throws IOException
     */
    public void testExistDocument() throws IOException {
//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("user","张三");

        //测试文档的 没有index
        GetRequest request= new GetRequest("zhweb","1");

        //没有indices()了
        boolean exist = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println("测试文档是否存在-----"+exist);
    }



    /**
     * 断某个index是否存在
     */
    public boolean isExistsIndex(String idxName) throws Exception {
        return restHighLevelClient.indices().exists(new GetIndexRequest(idxName),RequestOptions.DEFAULT);
    }

    /**
     * 设置分片
     */
    public void buildSetting(CreateIndexRequest request){
        request.settings(Settings.builder().put("index.number_of_shards",3)
                .put("index.number_of_replicas",2));
    }


    /**
     * 搜索数据
     * @throws IOException
     */
    public String testSearchDocument() throws IOException {
        SearchRequest request = new SearchRequest("zhweb");
        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //设置了高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("user");
        highlightBuilder.requireFieldMatch(false);// 关闭多个高亮显示
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
//        sourceBuilder.highlighter();
        //term name为a的

//        分词查询
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("user", "hello nih");

//        不分词查询
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("user", "hello nih");

        QueryStringQueryBuilder field = QueryBuilders.queryStringQuery("hi").field("user");//左右模糊

//        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.query(field);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        System.out.println("测试查询文档-----"+JSON.toJSONString(response.getHits()));
        System.out.println("=====================");
//        for (SearchHit documentFields : response.getHits().getHits()) {
//            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
//            HighlightField title = highlightFields.get("user");
//            System.out.println("--遍历参数--"+documentFields.getSourceAsMap());
//            System.out.println("高亮显示为--"+title);
//        }

        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            // 解析高亮的字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("user");
            Map<String, Object> map = hit.getSourceAsMap();// 原来的结果
            // 解析高亮的字段
            if (title != null) {
                // 将高亮的字段替换成原来没有高亮的字段
                Text[] fragments = title.fragments();
                String newTitle = "";
                for (Text text : fragments) {
                    newTitle += text;
                }
                map.put("title", newTitle);
            }
            list.add(map);
        }
        System.out.println(list);

        return JSON.toJSONString(response.getHits());

    }





    /** 批量插入数据
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:26
     * @param idxName index
     * @param list 带插入列表
     * @return void
     * @throws
     * @since
     */
    public void insertBatch(String idxName, List<ElasticEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(idxName).id(item.getId())
                .source(item.getData(), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 批量删除
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:14
     * @param idxName index
     * @param idList    待删除列表
     * @return void
     * @throws
     * @since
     */
    public <T> void deleteBatch(String idxName, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(idxName, item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:14
//     * @param idxName index
//     * @param builder   查询参数
//     * @return java.util.List<T>
     * @throws
     * @since
     */
    public <T> List<T> search(SearchRequest request) {
//        SearchRequest request = new SearchRequest(idxName);
//        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            System.out.println(response);
            System.out.println("*************************************");
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
//            for (SearchHit hit : hits) {
//                res.add(JSON.parseObject(hit.getSourceAsString(), c));
//            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 删除index
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:13
     * @param idxName
     * @return void
     * @throws
     * @since
     */
    public void deleteIndex(String idxName) {
        try {
            if (!this.indexExist(idxName)) {
                log.error(" idxName={} 已经存在",idxName);
                return;
            }
            restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName), RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:13
     * @param idxName
     * @param builder
     * @return void
     * @throws
     * @since
     */
    public void deleteByQuery(String idxName, QueryBuilder builder) {

        DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
