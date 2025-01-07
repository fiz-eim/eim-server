package com.soflyit.chattask.dx.modular.resource.resource.domain.param;

import com.soflyit.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Package: com.soflyit.chattask.dx.modular.resource.resource.domain.param
 *
 * @Description:
 * @date: 2023/11/28 16:30
 * @author: dddgoal@163.com
 */
@ApiModel
@Data
public class ResourceDetailParam extends PageDomain {


    @ApiModelProperty("主键;资源ID")
    private Long resourceId;


}
