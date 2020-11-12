package com.learning.es.mapper;

import com.alibaba.fastjson.JSON;
import com.learning.es.bean.db.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Wang Xu
 * @date 2020/10/25
 */
@Slf4j
public class ElasticTest extends BaseTestBean {

    private static final String TEST_INDEX_NAME = "java_es_test_index";

    @Resource
    private RestHighLevelClient client;

    @Test
    public void createIndex() throws IOException {
        // 创建索引的请求
        CreateIndexRequest request = new CreateIndexRequest("jd_goods");
        // 执行请求
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

        log.debug(response.toString());
    }

    @Test
    public void isExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest(TEST_INDEX_NAME);
        Boolean response = client.indices().exists(request,RequestOptions.DEFAULT);
        log.info("索引：{} 是否存在：{}", TEST_INDEX_NAME, response);
    }

    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(TEST_INDEX_NAME);
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        log.info("索引：{} 删除成功：{}", TEST_INDEX_NAME, response.isAcknowledged());
    }

    @Resource
    OrderInfoMapper orderInfoMapper;

    @Test
    public void addDocument() throws IOException {
        // 创建对象
        OrderInfo orderInfo = orderInfoMapper.queryByAppCode("SN180827155838000349");
        // 创建请求
        IndexRequest request = new IndexRequest(TEST_INDEX_NAME);
        // 规则： PUT /TEST_INDEX_NAME/_doc/1
        request.id("3");
        request.timeout(TimeValue.timeValueSeconds(1));
        // 将数据放入请求
        request.source(JSON.toJSONString(orderInfo), XContentType.JSON);
        // 执行请求
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        log.debug("添加对象到es，response：{}，response status：{}", response.toString(), response.status());

    }

    @Test
    public void isExistDocument() throws IOException {
        /*
protected String index：索引库，对应关系型数据库的Database。
private String type：类型，对应关系型数据库的表。
private String id：文档ID，对应关系型数据库表中一行的主键ID。
private String routing：路由值。
private String parent：
private String preference：get请求选取执行节点的偏好，倾向性。
private String[] storedFields：显示的指定需要返回的字段，默认会返回_source中所有字段。
private FetchSourceContext fetchSourceContext：指定需要返回字段的上下文，是storedFields的补充与完善，支持通配符。
private boolean refresh = false：是否刷新。
boolean realtime = true：是否实时执行，默认为true。
private VersionType versionType = VersionType.INTERNAL：版本类型。
private long version = Versions.MATCH_ANY：数据版本，关于数据的版本管理。
         */
        GetRequest request = new GetRequest(TEST_INDEX_NAME, "3");
        request.storedFields("_none_");
        request.fetchSourceContext(new FetchSourceContext(false));

        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        log.debug("索引：{} 中的 id = 3 的 doc 存在：{}", TEST_INDEX_NAME, exists);
    }

    @Test
    public void getDocument() throws IOException {
        GetRequest request = new GetRequest(TEST_INDEX_NAME, "3");
        /*
        "mappings": {
            "_doc": {
                "properties": {
                    "counter": {
                        "type": "integer"    //没有显示store属性，说明默认值为false
                    },
                    "tags": {
                        "type": "keyword",
                        "store": true         //store属性为true
                    }
                }
            }
        }
         */
//        request.storedFields("appCode", "businessType", "businessFlag", "created");
        //为特定字段配置源包含
        String[] includes = new String[]{"appCode", "business*", "created"};
        //为特定字段配置源排除
        String[] excludes = Strings.EMPTY_ARRAY;
        request.fetchSourceContext(new FetchSourceContext(true, includes, excludes));

        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        log.debug("索引：{} 中的 id = 3 的 doc 数据为：{}", TEST_INDEX_NAME, response.getSource());

        String sourceAsString = response.getSourceAsString();
        OrderInfo orderInfo = JSON.parseObject(sourceAsString, OrderInfo.class);
        log.debug("############### orderInfo: {}", orderInfo);

    }

    @Test
    public void updateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(TEST_INDEX_NAME, "3");
        updateRequest.timeout(TimeValue.timeValueSeconds(1));

        GetRequest request = new GetRequest(TEST_INDEX_NAME, "3");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        OrderInfo orderInfo = JSON.parseObject(response.getSourceAsString(), OrderInfo.class);
        log.debug("############### old orderInfo: {}", orderInfo);
        orderInfo.setBusinessFlag("SB");

        updateRequest.doc(JSON.toJSONString(orderInfo), XContentType.JSON);
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        log.debug("索引：{} 中的 id = 3 的 doc 更新成功?：{}", TEST_INDEX_NAME, updateResponse.status());
    }

    @Test
    public void deleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(TEST_INDEX_NAME, "1");
        deleteRequest.timeout(TimeValue.timeValueSeconds(1));

        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        log.debug("文档 1 删除成功？{}", deleteResponse.status());
    }

    @Test
    public void bulkOptionDocument() throws IOException {
        List<OrderInfo> list = Arrays.asList(
                new OrderInfo(100000L, "100000L"),
                new OrderInfo(100001L, "100001L"),
                new OrderInfo(100002L, "100002L"),
                new OrderInfo(100003L, "100003L"),
                new OrderInfo(100004L, "100004L"),
                new OrderInfo(100006L, "100005L"),
                new OrderInfo(100005L, "100006L"),
                new OrderInfo(100007L, "100007L"),
                new OrderInfo(100008L, "100008L"),
                new OrderInfo(100009L, "100009L")
        );

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(10));

        // 不写id，就是随机id
        list.forEach(item -> bulkRequest.add(new IndexRequest(TEST_INDEX_NAME).id(item.getId()+"").source(JSON.toJSONString(item), XContentType.JSON)));

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        // hasFailures 为 false 表示成功
        log.debug("################## bulkResponse status: {}", bulkResponse.hasFailures());
    }

    @Test
    public void search() throws IOException {
        SearchRequest searchRequest = new SearchRequest(TEST_INDEX_NAME);
        // 构建查询条件  xxxBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));
        // term - 精准匹配（查倒排索引）
        // match - 匹配所有
        // match：全文搜索, 通常用于对text类型字段的查询,会对进行查询的文本先进行分词操作
        // term：精确查询,通常用于对keyword和有精确值的字段进行查询,不会对进行查询的文本进行分词操作
//        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("businessType", "DIRECTLY");
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("businessType", "DIRECTLY");
        searchSourceBuilder.query(queryBuilder);
        // 排序
//        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
//        searchSourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));
        // 分页
//        searchSourceBuilder.from(1);
//        searchSourceBuilder.size(10);
        // 高亮
        /*
        * HighlightBuilder.Field highlightTitle =
                new HighlightBuilder.Field("title");
        // 设置荧光笔类型
        highlightTitle.highlighterType("unified");
        highlightBuilder.field(highlightTitle);
        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("user");
        highlightBuilder.field(highlightUser);
        searchSourceBuilder.highlighter(highlightBuilder);
        */
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("businessType");
        highlightBuilder.preTags("<font color='red'>").postTags("</font>");
        highlightBuilder.requireFieldMatch(false);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        log.debug("###################### search :{} hits is : {}", TEST_INDEX_NAME, JSON.toJSONString(searchResponse.getHits()));
        Arrays.stream(searchResponse.getHits().getHits()).forEach(item -> log.debug("###################### search :{} hits record is : {}", TEST_INDEX_NAME, item.getSourceAsMap()));
    }

}