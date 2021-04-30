package ltd.newbee.mall.api.admin.param;

import lombok.Data;

/**
 * @author mhx
 */
@Data
public class UpdateAdminPasswordParam {

    private String originalPassword;

    private String newPassword;
}
