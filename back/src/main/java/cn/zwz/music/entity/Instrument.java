package cn.zwz.music.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * 乐器库存 实体类
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_instrument")
@TableName("a_instrument")
@ApiModel(value = "乐器库存")
public class Instrument extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "乐器名称")
    private String title;

    @ApiModelProperty(value = "乐器品类")
    private String type;

    @ApiModelProperty(value = "乐器进价")
    private BigDecimal price1;

    @ApiModelProperty(value = "租赁状态")
    private String status;

    @ApiModelProperty(value = "赔付金额")
    private BigDecimal price2;

    @ApiModelProperty(value = "维修金额")
    private BigDecimal price3;

    @ApiModelProperty(value = "押金")
    private BigDecimal price4;

    @ApiModelProperty(value = "租赁费用")
    private BigDecimal price5;
}