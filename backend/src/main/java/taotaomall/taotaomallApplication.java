package taotaomall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;  // 添加这行导入

@SpringBootApplication
@MapperScan("taotaomall.dao")
public class taotaomallApplication {
    public static void main(String[] args) {
        SpringApplication.run(taotaomallApplication.class, args);
    }
}
