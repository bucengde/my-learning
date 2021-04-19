package learning.springcloud.web.contrller;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import learning.springcloud.bean.ApolloConfigTestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Wang Xu
 * @date 2021/3/21
 */
@RestController
@EnableApolloConfig(value = {"application"})
@RequestMapping("/apollo")
//@EnableConfigurationProperties(ApolloConfigTestDto.class)
public class ApolloTestController {

    @Value("dev.music.lze")
    private String musicFromLze;

//    @Resource
    private ApolloConfigTestDto apolloConfigTestDto;

    @GetMapping("/value/test")
    public String testValue() {
        return "############### apollo config value test, key: dev.music.lze - value: " + musicFromLze;
    }

    @GetMapping("/bean/test")
    public String testBean() {
        return "############### apollo config bean test, apolloConfigTestDto toSting is: " + apolloConfigTestDto.toString();
    }
}