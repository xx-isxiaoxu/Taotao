package taotaomall.model;

import lombok.Data;

@Data
public class Userorder {
    private Integer id;
    private Integer number;
    private Double price;
    private String goodsname;
    private String time;
    private Long uid;

    public Userorder(int number, Double price, String goodsname, Long uid) {
        this.number = number;
        this.price = price;
        this.goodsname = goodsname;
        this.uid = uid;
    }

    public Userorder() {
    }
}
