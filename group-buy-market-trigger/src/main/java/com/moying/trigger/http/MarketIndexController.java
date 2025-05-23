package com.moying.trigger.http;

import com.alibaba.fastjson.JSON;
import com.moying.api.IMarketIndexService;
import com.moying.api.dto.GoodsMarketRequestDTO;
import com.moying.api.dto.GoodsMarketResponseDTO;
import com.moying.api.response.Response;
import com.moying.domain.activity.model.entity.MarketProductEntity;
import com.moying.domain.activity.model.entity.TrialBalanceEntity;
import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.moying.domain.activity.model.valobj.TeamStatisticVO;
import com.moying.domain.activity.service.IIndexGroupBuyMarketService;
import com.moying.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import com.moying.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: moying
 * @CreateTime: 2025-05-13
 * @Description:
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/index/")
public class MarketIndexController implements IMarketIndexService {

    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;

    @RequestMapping(value = "query_group_buy_market_config", method = RequestMethod.POST)
    @Override
    public Response<GoodsMarketResponseDTO> queryGroupBuyMarketConfig(@RequestBody GoodsMarketRequestDTO requestDTO) {
        try {
            log.info("查询拼团营销配置开始:{} goodsId:{}", requestDTO.getUserId(), requestDTO.getGoodsId());
            // 1.参数校验
            if (StringUtils.isBlank(requestDTO.getUserId()) || StringUtils.isBlank(requestDTO.getSource())
                    || StringUtils.isBlank(requestDTO.getChannel()) || StringUtils.isBlank(requestDTO.getGoodsId())) {
                return Response.<GoodsMarketResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            // 2.营销优惠试算
            TrialBalanceEntity trialBalanceEntity = indexGroupBuyMarketService.indexMarketTrial(MarketProductEntity
                    .builder()
                    .userId(requestDTO.getUserId())
                    .goodsId(requestDTO.getGoodsId())
                    .channel(requestDTO.getChannel())
                    .source(requestDTO.getSource())
                    .build());


            GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = trialBalanceEntity.getGroupBuyActivityDiscountVO();
            Long activityId = groupBuyActivityDiscountVO.getActivityId();

            // 3. 查询拼团组队
           List<UserGroupBuyOrderDetailEntity> userGroupBuyOrderDetailEntityList = indexGroupBuyMarketService
                   .queryInProgressUserGroupBuyOrderDetailList(activityId, requestDTO.getUserId(), 1, 2);

           // 4. 统计拼团数据
            TeamStatisticVO teamStatisticVO = indexGroupBuyMarketService.queryTeamStatisticByActivityId(activityId);

            GoodsMarketResponseDTO.Goods goods = GoodsMarketResponseDTO.Goods
                    .builder()
                    .originalPrice(trialBalanceEntity.getOriginalPrice())
                    .payPrice(trialBalanceEntity.getPayPrice())
                    .deductionPrice(trialBalanceEntity.getDeductionPrice())
                    .goodsId(trialBalanceEntity.getGoodsId())
                    .build();

            List<GoodsMarketResponseDTO.Team> teamList = new ArrayList<>();

            if(null != userGroupBuyOrderDetailEntityList&&!userGroupBuyOrderDetailEntityList.isEmpty()){
                    for (UserGroupBuyOrderDetailEntity userGroupBuyOrderDetailEntity : userGroupBuyOrderDetailEntityList) {
                        GoodsMarketResponseDTO.Team team = GoodsMarketResponseDTO.Team.builder()
                                .userId(userGroupBuyOrderDetailEntity.getUserId())
                                .teamId(userGroupBuyOrderDetailEntity.getTeamId())
                                .activityId(userGroupBuyOrderDetailEntity.getActivityId())
                                .targetCount(userGroupBuyOrderDetailEntity.getTargetCount())
                                .completeCount(userGroupBuyOrderDetailEntity.getCompleteCount())
                                .lockCount(userGroupBuyOrderDetailEntity.getLockCount())
                                .validStartTime(userGroupBuyOrderDetailEntity.getValidStartTime())
                                .validEndTime(userGroupBuyOrderDetailEntity.getValidEndTime())
                                .validTimeCountdown(GoodsMarketResponseDTO.Team.differenceDateTime2Str(new Date(), userGroupBuyOrderDetailEntity.getValidEndTime()))
                                .outTradeNo(userGroupBuyOrderDetailEntity.getOutTradeNo())
                                .build();
                        teamList.add(team);
                }
            }

            GoodsMarketResponseDTO.TeamStatistic teamStatistic = GoodsMarketResponseDTO.TeamStatistic.builder()
                    .allTeamCount(teamStatisticVO.getAllTeamCount())
                    .allTeamCompleteCount(teamStatisticVO.getAllTeamCompleteCount())
                    .allTeamUserCount(teamStatisticVO.getAllTeamUserCount())
                    .build();

            Response<GoodsMarketResponseDTO> response = Response.<GoodsMarketResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(GoodsMarketResponseDTO.builder()
                            .activityId(activityId)
                            .goods(goods)
                            .teamList(teamList)
                            .teamStatistic(teamStatistic)
                            .build())
                    .build();

            log.info("查询拼团营销配置完成:{} goodsId:{} response:{}", requestDTO.getUserId(), requestDTO.getGoodsId(), JSON.toJSONString(response));

            return response;

        } catch (Exception e) {
            log.error("查询拼团营销配置失败:{} goodsId:{}", requestDTO.getUserId(), requestDTO.getGoodsId(), e);
            return Response.<GoodsMarketResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
