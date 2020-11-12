package com.learning.es.service;

import com.learning.commons.utils.BeanUtils;
import com.learning.es.bean.db.GoodInfo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wang Xu
 * @date 2020/10/26
 */
@Service
@Slf4j
public class ElasticService {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    public <T> List<T> search(Class<T> clazz, String keyword, Integer pageNo, Integer pageSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(5));

        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", keyword);
//        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(queryBuilder);

        // 分页
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        // 如果要多个字段高亮，这项要为 false
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");

        // 下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
        // 最大高亮分片数
        highlightBuilder.fragmentSize(800000);
        // 从第一个分片获取高亮片段
        highlightBuilder.numOfFragments(0);

        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return Arrays.stream(searchResponse.getHits().getHits())
                .peek(this::handleHighLine)
                .map(item -> BeanUtils.DozerBeanMapperUtil.map(item.getSourceAsMap(), clazz))
                .collect(Collectors.toList());
    }

    private void handleHighLine(SearchHit hit) {
        HighlightField title = hit.getHighlightFields().get("title");
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        Optional.ofNullable(title).ifPresent(t -> {
            StringBuilder sb = new StringBuilder();
            for (Text fragment : t.getFragments()) {
                sb.append(fragment);
            }
            sourceAsMap.put("title", sb.toString());
        });
    }

    public static void main(String[] args) {
        Map<String, Object> sourceAsMap = new HashMap<>();
        sourceAsMap.put("title", new GoodInfo("a","b","c","d"));
        List<GoodInfo> goods = new ArrayList<>();
        goods.add(new GoodInfo("1","2","3","4"));
        Optional.ofNullable(goods).ifPresent(t -> {
            StringBuilder sb = new StringBuilder();
            for (GoodInfo fragment : goods) {
                sb.append(fragment.getTitle());
            }
            sourceAsMap.put("title", sb.toString());
        });
        System.out.println(sourceAsMap.toString());

    }
}