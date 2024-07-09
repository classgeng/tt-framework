package cn.cloud9.server.test.mapstruct;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DemoEntity {
    private Integer fieldD;
    private Boolean fieldE;
    private String fieldF;
    private Long fieldG;
}
