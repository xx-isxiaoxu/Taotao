package taotaomall.model;

import lombok.Data;

@Data
public class GoodsDetail {
    private Integer id;
    private Integer goodsId;
    private String detailHtml;
    private String specs;
    private String params;
    private String images;
    private String createdAt;
    private String updatedAt;
}
