package taotaomall.model;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Date created_at;
    private Date updated_at;
} 