package learning.springcloud.config.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Collection;
import java.util.Map;

/**
 * 对OrderClient的feign请求进行拦截，在请求头中加入自定义的头信息后续服务的进行权限认证
 * @author Wang Xu
 * @date 2020/10/11
 */
public class OrderClientConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("H1", "11111");
        requestTemplate.header("H2", "22222");
        requestTemplate.header("H3", "33333");
    }
}