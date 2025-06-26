package com.ruoyi.common.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * 类功能描述
 *
 * @author 小旋风
 * @date 2025/6/19 22:02:45
 */
@Data
public class BaseModel {

    @TableField("create_time")
    private Date createTime;

    @TableField("modify_time")
    private Date modifyTime;

    @TableField("user_id")
    private Long userId;

}
