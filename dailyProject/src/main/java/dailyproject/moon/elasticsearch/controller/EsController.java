package dailyproject.moon.elasticsearch.controller;

import com.alibaba.fastjson.JSONObject;
import dailyproject.moon.elasticsearch.configUtil.BaseElasticUtil;
import dailyproject.moon.elasticsearch.entity.IdxVo;
import dailyproject.moon.zookeeper.entity.UserData;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import dailyproject.moon.elasticsearch.entity.IdxVo;

import java.io.IOException;
import java.util.List;

/**
 * @program: moon
 * @description:
 * @create: 2020-11-23 17:54
 **/


@Slf4j
@RestController
@RequestMapping(value = "/es")
public class EsController {

    @Autowired
    BaseElasticUtil baseElasticUtil;

    @RequestMapping(value = "/createIndex",method = {RequestMethod.GET})
    public void createIndex(){
        baseElasticUtil.createIndex();
    }

    @RequestMapping(value = "/IndexIsExist",method = {RequestMethod.GET})
    public void IndexIsExist(){
        try {
            boolean zhweb = baseElasticUtil.indexExist("zhweb");
            System.out.println("是否存在："+zhweb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/searchData",method = {RequestMethod.GET})
    public String getDocument(){
        String result = null;
        try {
            result = baseElasticUtil.testSearchDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


   @RequestMapping(value = "/testExistDocument",method = {RequestMethod.GET})
    public void testExistDocument(){
        try {
            baseElasticUtil.testExistDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "/insertZkData",method = {RequestMethod.GET})
    public String insertZkData(){

        try {
            //索引不存在，再创建，否则不允许创建
//            if(!baseElasticUtil.indexExist("first")){
                String idxSql = JSONObject.toJSONString(new UserData(125,"王五",23,5));
//                log.warn(" idxName={}, idxSql={}",idxVo.getIdxName(),idxSql);
//                baseElasticUtil.createIndex("first",idxSql);
//            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return "nice";
    }




    @RequestMapping(value = "/searchEsData",method = {RequestMethod.GET})
    public String searchEsData(){

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10);
//        sourceBuilder.fetchSource(new String[]{"age"}, new String[]{});
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "四");
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "张");
//        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime");
//        rangeQueryBuilder.gte("2018-01-26T08:00:00Z");
//        rangeQueryBuilder.lte("2018-01-26T20:00:00Z");
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(matchQueryBuilder);
//        boolBuilder.must(termQueryBuilder);
//        boolBuilder.must(rangeQueryBuilder);
        sourceBuilder.query(boolBuilder);
        SearchRequest searchRequest = new SearchRequest("first");
        searchRequest.source(sourceBuilder);
        try {
            List<Object> search = baseElasticUtil.search(searchRequest);
            System.out.println(search);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "haha";
    }

}
