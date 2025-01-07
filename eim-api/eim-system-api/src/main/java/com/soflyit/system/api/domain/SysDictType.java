package com.soflyit.system.api.domain;

import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.annotation.Excel.ColumnType;
import com.soflyit.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 字典类型表 sys_dict_type
 *
 * @author soflyit
 */
public class SysDictType extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "字典主键", cellType = ColumnType.NUMERIC)
    private Long dictId;


    @Excel(name = "字典名称")
    private String dictName;


    @Excel(name = "字典类型")
    private String dictType;


    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;


    private Boolean inherDict;

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    @NotBlank(message = "字典名称不能为空")
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符")
    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getInherDict() {
        return inherDict;
    }

    public void setInherDict(Boolean inherDict) {
        this.inherDict = inherDict;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("dictId", getDictId())
                .append("dictName", getDictName())
                .append("dictType", getDictType())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
