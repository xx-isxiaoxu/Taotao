package taotaomall.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import taotaomall.model.GoodsDetail;
import taotaomall.service.GoodsDetailService;
import taotaomall.utils.Result;
import taotaomall.utils.ResultCodeEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/api/goodsDetail")
public class GoodsDetailController {

    @Autowired
    private GoodsDetailService goodsDetailService;

    // 根据商品ID获取详情
    @GetMapping("/byGoodsId/{goodsId}")
    public Result getByGoodsId(@PathVariable Integer goodsId) throws JsonProcessingException {
        GoodsDetail detail = goodsDetailService.getByGoodsId(goodsId);
        String imagesStr = detail.getImages();
        List<String> images;
        if (imagesStr.startsWith("[")) {
            // JSON 字符串
            images = new ObjectMapper().readValue(imagesStr, new TypeReference<List<String>>(){});
        } else {
            // 逗号分隔
            images = Arrays.asList(imagesStr.split(","));
        }
        // 返回时 images 字段为 JSON 字符串，前端 JSON.parse 即可
        detail.setImages(new ObjectMapper().writeValueAsString(images));
        return Result.success(Collections.singletonList(detail));
    }

    // 新增商品详情
    @PostMapping("/add")
    public Result addGoodsDetail(@RequestBody GoodsDetail goodsDetail) {
        int res = goodsDetailService.addGoodsDetail(goodsDetail);
        return res > 0 ? Result.success() : Result.failure(ResultCodeEnum.valueOf("新增失败"));
    }

    // 更新商品详情
    @PutMapping("/update")
    public Result updateGoodsDetail(@RequestBody GoodsDetail goodsDetail) {
        int res = goodsDetailService.updateGoodsDetail(goodsDetail);
        return res > 0 ? Result.success() : Result.failure(ResultCodeEnum.valueOf("更新失败"));
    }
}
