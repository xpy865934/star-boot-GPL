package com.star.starboot.uploaddata.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.star.starboot.common.entity.AbstractEntity;
import com.star.starboot.constant.SystemConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 上报的数据
 * </p>
 *
 * @author xpy
 * @since 2020-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_upload_data")
public class UploadData extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @TableField("USER_ID")
    private String userId;
    @TableId("UPLOAD_DATA_ID")
    private String uploadDataId;
    /**
     * ICU专科医师
     */
    @TableField("ICU_ZKYS")
    private Integer icuZkys;
    /**
     * ICU床位数
     */
    @TableField("ICU_CWS")
    private Integer icuCws;
    /**
     * ICU护士总数
     */
    @TableField("ICU_HSZS")
    private Integer icuHszs;
    /**
     * 医院总床位数
     */
    @TableField("YYZCWS")
    private Integer yyzcws;
    /**
     * ICU床位率
     */
    @TableField("ICU_CWL")
    private String icuCwl;
    /**
     * ICU医师床位比
     */
    @TableField("ICU_YSCWB")
    private String icuYscwb;
    /**
     * ICU护士床位比
     */
    @TableField("ICU_HSCWB")
    private String icuHscwb;
    /**
     * ICU原有病人数
     */
    @TableField("ICU_YYBRS")
    private Integer icuYybrs;
    /**
     * 新收治病人数
     */
    @TableField("ICU_XSZBRS")
    private Integer icuXszbrs;
    /**
     * 收治患者总人数
     */
    @TableField("ICU_SZHZZRS")
    private Integer icuSzhzzrs;
    /**
     * 医院原有病人数
     */
    @TableField("YY_YSBRS")
    private Integer yyYsbrs;
    /**
     * 医院新收治病人数
     */
    @TableField("YY_XSZBRS")
    private Integer yyXszbrs;
    /**
     * 医院收治患者总人数
     */
    @TableField("YY_SZHZZRS")
    private Integer yySzhzzrs;
    /**
     * ICU患者收治率
     */
    @TableField("ICU_HZSZL")
    private String icuHzszl;
    /**
     * APACHE评分患者数
     */
    @TableField("APACHE_HZS")
    private Integer apacheHzs;
    /**
     * APACHE
     */
    @TableField("APACHE")
    private String apache;
    /**
     * ICU患者收治总床日数
     */
    @TableField("ICU_HZSZZCRS")
    private Integer icuHzszzcrs;
    /**
     * 医院收治患者总床日数
     */
    @TableField("YY_SZZCRS")
    private Integer yySzzcrs;
    /**
     * ICU患者收治床日率
     */
    @TableField("ICU_HZSZCRL")
    private String icuHzszcrl;
    /**
     * SEPSIS患者总数
     */
    @TableField("SEPSIS_HZZS")
    private Integer sepsisHzzs;
    /**
     * 3h bundle患者数
     */
    @TableField("TBUNDLE_HZS")
    private Integer tbundleHzs;
    /**
     * 6H bundle患者数
     */
    @TableField("SBUNDLE_HZS")
    private Integer sbundleHzs;
    /**
     * 3h 完成率
     */
    @TableField("TBUNDLE_WCL")
    private String tbundleWcl;
    /**
     * 6h完成率
     */
    @TableField("SBUNDLE_WCL")
    private String sbundleWcl;
    /**
     * 抗菌药物病例总数
     */
    @TableField("KJYSBLZS")
    private Integer kjysblzs;
    /**
     * 抗菌药物送检例数
     */
    @TableField("KJYWSJLS")
    private Integer kjywsjls;
    /**
     * 抗菌药物送检率
     */
    @TableField("KJYWSJL")
    private String kjywsjl;
    /**
     * DVT病例总数
     */
    @TableField("DVTBLZS")
    private Integer dvtblzs;
    /**
     * DVT预防率
     */
    @TableField("DVTYFL")
    private String dvtyfl;
    /**
     * 临床死亡人数
     */
    @TableField("LCSWRS")
    private Integer lcswrs;
    /**
     * 自动出院人数
     */
    @TableField("ZDCYRS")
    private Integer zdcyrs;
    /**
     * ICU总死亡人数
     */
    @TableField("ICUZSWRS")
    private String icuzswrs;
    /**
     * 预计病死率之和
     */
    @TableField("ICU_YJBSLZH")
    private Double icuYjbslzh;
    /**
     * 实际病死率
     */
    @TableField("ICU_SJBSL")
    private String icuSjbsl;
    /**
     * 预计病死率
     */
    @TableField("ICU_YJBSL")
    private String icuYjbsl;
    /**
     * 病死指数
     */
    @TableField("ICU_BSZS")
    private String icuBszs;
    /**
     * 非计划ICU患者数
     */
    @TableField("FJH_ICU_HZS")
    private Integer fjhIcuHzs;
    /**
     * 转入ICU患者数
     */
    @TableField("ZR_ICU_HZS")
    private Integer zrIcuHzs;
    /**
     * 重返ICU患者数
     */
    @TableField("CF_ICU_HZS")
    private Integer cfIcuHzs;
    /**
     * 转出ICU患者数
     */
    @TableField("ZC_ICU_HZS")
    private Integer zcIcuHzs;
    /**
     * 非计划转入率
     */
    @TableField("FJH_ICU_ZRL")
    private String fjhIcuZrl;
    /**
     * 重返率
     */
    @TableField("CFL")
    private String cfl;
    /**
     * 脱出次数
     */
    @TableField("FJH_TCCS")
    private Integer fjhTccs;
    /**
     * ICU插管总例数
     */
    @TableField("ICU_CGZLS")
    private Integer icuCgzls;
    /**
     * 非计划拔管率
     */
    @TableField("FJH_BGL")
    private String fjhBgl;
    /**
     * 再插管例数
     */
    @TableField("ZCGLS")
    private Integer zcgls;
    /**
     * ICU拔管总例数
     */
    @TableField("ICU_BGZLS")
    private Integer icuBgzls;
    /**
     * 再插管率
     */
    @TableField("ZCGL")
    private String zcgl;
    /**
     * 肺炎病人总例数
     */
    @TableField("ICU_FYBRZLS")
    private Integer icuFybrzls;
    /**
     * ICU通气天数
     */
    @TableField("ICU_TQTS")
    private Integer icuTqts;
    /**
     * VAP发病率
     */
    @TableField("VAP_FBL")
    private String vapFbl;
    /**
     * ICU中心静脉导管相关性血流感染总例数

     */
    @TableField("ICU_XLGLZLS")
    private Integer icuXlglzls;
    /**
     * ICU中心静脉导管总天数

     */
    @TableField("ICU_JMDGZTS")
    private Integer icuJmdgzts;
    /**
     * CRBSI发病率
     */
    @TableField("CRBSI_FBL")
    private String crbsiFbl;
    /**
     * ICU泌尿感染总例数
     */
    @TableField("ICU_MNGLZLS")
    private Integer icuMnglzls;
    /**
     * ICU尿管留置总天数
     */
    @TableField("ICU_NGLZZTS")
    private Integer icuNglzzts;
    /**
     * CAUTIFBL
     */
    @TableField("CAUTI_FBL")
    private String cautiFbl;
    /**
     * 上报时间
     */
    @TableField("SBSJ")
    @JsonFormat(pattern = SystemConstant.DATE_PATTERN,timezone = "GMT+8")
    private Date sbsj;
    /**
     * 确认标记1 确认 0 未确认
     */
    @TableField("QRBJ")
    private Integer qrbj;

}
