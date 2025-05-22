package taotaomall.model;

import lombok.Data;

import java.util.Date;

@Data
public class Goods {
    private Integer gid;
    private String gname;
    private String gpicture;
    private String gdetails;
    private Integer gprice;
    private Integer goriginal_price;
    private Integer gcategory_id;
    private Integer gsales;
    private Date created_at;
    private Date updated_at;
    private Integer is_new;
    private Integer is_hot;
}
