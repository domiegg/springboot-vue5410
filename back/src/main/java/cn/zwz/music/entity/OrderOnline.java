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
 * 线上订单 实体类
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_order_online")
@TableName("a_order_online")
@ApiModel(value = "线上订单")
public class OrderOnline extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "租赁人ID")
    private String userId;

    @ApiModelProperty(value = "租赁人")
    private String userName;

    @ApiModelProperty(value = "乐器ID")
    private String insId;

    @ApiModelProperty(value = "乐器名称")
    private String insName;

    @ApiModelProperty(value = "订单状态")
    private String status;

    @ApiModelProperty(value = "赔付金额")
    private BigDecimal price2 = BigDecimal.ZERO;

    @ApiModelProperty(value = "维修金额")
    private BigDecimal price3 = BigDecimal.ZERO;

    @ApiModelProperty(value = "押金")
    private BigDecimal price4 = BigDecimal.ZERO;

    @ApiModelProperty(value = "租赁费用")
    private BigDecimal price5 = BigDecimal.ZERO;

    @ApiModelProperty(value = "租赁时间")
    private String time;
}