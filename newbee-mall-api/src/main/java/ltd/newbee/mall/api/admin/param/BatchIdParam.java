package ltd.newbee.mall.api.admin.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mhx
 */
@Data
public class BatchIdParam implements Serializable {
    private static final long serialVersionUID = 286246359499622989L;

    Long[] ids;
}
