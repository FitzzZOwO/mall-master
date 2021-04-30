package ltd.newbee.mall.api.admin.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author mhx
 */
@Data
public class AdminLoginParam implements Serializable {

    private static final long serialVersionUID = 7330486701138860855L;

    @NotEmpty(message = "登录名不能为空")
    @ApiModelProperty("登录名")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    @ApiModelProperty("用户密码(需要MD5加密)")
    private String passwordMd5;
}
