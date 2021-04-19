package learning.springcloud.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Wang Xu
 * @date 2021/3/21
 */
@Data
@ConfigurationProperties(prefix = "idol.lze")
public class ApolloConfigTestDto {
    private String name;
    private Integer age;
}