package com.learning.es.bean.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Wang Xu
 * @date 2020/10/19
 */
@Data
@NoArgsConstructor
public class OrderInfo {
    /** 主键ID */
    private Long id;
    /** 订单编号  **/
    private String appCode;
    /** 所属销售 **/
    private Long belongSale;
    /** 销售主管 **/
    private Long belongSaleManager;
    /** 所属运营 **/
    private Long belongOperator;
    /** 团队 **/
    private Long team;
    /** 所属城市 **/
    private Integer city;
    /** 金融产品 **/
    private Long product;
    /** 审批流 **/
    private Integer flowSeq;
    /** 首次提交时间 **/
    private Date firstSubmit;
    /** 放款状态  0 未放款  1 已放款 **/
    private Integer loanStatus;
    /** 放款时间 **/
    private Date loanTime;
    /** 放款金额 **/
    private BigDecimal loanAmount;
    /** 订单状态 **/
    private Integer status;
    /** gps状态 0：待提交  1：待审核 2：审核退回 3：审核通过 **/
    private Integer gpsStatus;
    /**  商户备案状态  0：待提交  1：待审核 2：审核退回 3：审核通过 **/
    private Integer dealerRecordStatus;
    /** 是否删除 0 否  1 是 **/
    private Integer isDeleted;
    /** 是否需要安装gps 0 不需要  1 需要 **/
    private Integer isNeedGps;
    /** gps数量 **/
    private Integer gpsCount;
    /** gps属性 **/
    private String gpsPro;
    /** 有线gps数量 **/
    private Integer wiredGpsCount;
    /** 无线gps数量 **/
    private Integer wirelessGpsCount;
    /** 备案运营 **/
    private Long recordOperator;
    /** 导标退件状态 **/
    private Integer guideStatus;
    /** 导标退件原因 **/
    private String guideRemark;
    /** 退件时间 **/
    private Date guideReturnTime;
    /** 进件类型  0 作为原单  1 作为新单 **/
    private Integer guideType;
    /** 订单来源 **/
    private Integer source;
    /** 配偶是否共签 **/
    private Integer signWithSpouse;
    /** 担保人是否共签 **/
    private Integer signWithGuarantee;
    /** 投保状态 0 未投保 1 已投保 **/
    private Integer insureStatus;
    /** 复审通过备注 **/
    private String secondAuditRemark;
    /** 订单取消类型 */
    private Integer cancelType;
    /** 所属运营姓名 */
    private String belongOperatorName;
    /** 销售姓名 */
    private String salerName;
    /** 团队名称 */
    private String teamName;
    /** 销售主管姓名 */
    private String managerName;
    /** 城市编号 */
    private Long saleCity;
    /** 城市名称 */
    private String saleCityName;
    /** 城市经理  */
    private Long saleCityManagerId;
    /** 城市经理  */
    private String saleCityManagerName;
    /** 区域ID */
    private Long saleAreaId;
    /** 区域名称 */
    private String saleAreaName;
    /** 区域经理ID */
    private Long saleAreaManagerId;
    /** 区域经理名称 */
    private String saleAreaManagerName;
    /** Zhao Yun 2018/9/4 金融产品 */
    private String productName;
    /**  Zhao Yun 2019/1/15 金融产品版本 */
    private String productVersion;
    /**  Zhao Yun 2018/9/11 产品类型，0旧产品，1新产品 */
    private Integer productType;
    /** 2018-9-19 是否已投保，0否，1是 */
    private Integer isInsure;
    /** 2018-10-22 是否推送抵押APP，0否1是 */
    private Integer isMort;
    /** 2018-11-12 所属省份 */
    private Integer belongProvince;
    /** 2018-11-12 所属城市 */
    private Integer belongCity;
    /** 2018-11-12 所属区域 */
    private Integer belongDistrict;
    /** 2018/12/21 影像分类二进制编码 */
    private String imageBinCode;
    /** 2019/03/04子状态 */
    private Integer subStatus;
    /** 2019/03/04产品渠道1直营产品2渠道产品 */
    private Integer productChannel;
    /** 2019/03/04渠道产品选择原因 */
    private Integer channelReason;
    /** 2019/03/08直营产品是否拒绝：0否，1是  */
    private Integer isSelfReject;
    /** 补件状态:0待补件，1补件审批中，2补件审批退回，3补件通过 */
    private Integer supplyStatus;
    /** 请款类型：1公司垫资、2渠道垫资、3先回后放、4直投 */
    private Integer cashRequestType;
    /** 归档状态：1待归档、2归档审批中、3归档审批退回、4已归档 */
    private Integer archiveStatus;
    /** 请款审批中领单人id */
    private Integer trialGetOrderId;
    /** 请款审批中领单人姓名 */
    private String trialGetOrderName;
    /** 请款复核领单人id */
    private Integer reviewGetOrderId;
    /** 请款复核领单人姓名  */
    private String reviewGetOrderName;
    /** 抵押审核领单人id */
    private Integer mortgageApprovalsId;
    /** 抵押审核领单人姓名 */
    private String mortgageApprovalsName;
    /** 补件材料code数组 */
    private String patchMaterial;
    /** 垫资手续费 */
    private BigDecimal handlingCharge;
    /** 请款补件审核备注 */
    private String supplyRemark;
    /** 回款时间 */
    private Date returnedTime;
    /** 回款状态0未回款，1已回款 */
    private Integer returnedStatus;
    /** 抵押备注   */
    private String mortgageRemark;
    /** 特殊收支  */
    private BigDecimal specialPayments;
    /** 是否秒批：0否，1是 */
    private Integer isSecondReply;
    /** 二级取消原因     */
    private Integer secondCancelType;
    /** 进件备注      */
    private String channelNote;
    /** 未读标记 0:已读，1：未读，为了方便统计这么设计 */
    private String notReadFlag;
    /**  BOC进度状态   **/
    private Integer bocProgressStatus;
    /** 资方 1无  2WX */
    private Integer fundCode;
    /** 预审方式 1现场  2远程 */
    private Integer preMode;
    /** 资方授权方式*/
    private Integer fundCreditAuthMode;
    /** 业务标识 */
    private String businessFlag;
    /** 业务类型: 直营 - DIRECTLY，SP - SP [2020-05新增]*/
    private String businessType;
    /** 是否需要视频审核 1是  0否 */
    private Integer isNeedVideo;
    /** 易秒状态：PASS-通过，REFUSE-不通过，UNDER_APPROVAL-审批中，EXCEPTION-异常 */
    private String easySecondStatus;
    /** 页面跳转锚点 */
    private String midPageAnchor;
    /** 是否签约一通*/
    private Integer onePassFlag;

    public OrderInfo(Long id, String appCode) {
        this.id = id;
        this.appCode = appCode;
    }
}