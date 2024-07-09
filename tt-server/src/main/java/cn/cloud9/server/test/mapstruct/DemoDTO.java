package cn.cloud9.server.test.mapstruct;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DemoDTO {
    private Integer fieldA;
    private Boolean fieldB;
    private String fieldC;
    private Long fieldG;
}
