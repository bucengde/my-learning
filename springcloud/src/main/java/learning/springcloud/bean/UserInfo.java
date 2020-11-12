package learning.springcloud.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Wang Xu
 * @date 2020/10/27
 */
@Data
public class UserInfo {
    private Integer id;
    private String account;
    private String password;
    private LocalDateTime created;
    private LocalDateTime modified;
}