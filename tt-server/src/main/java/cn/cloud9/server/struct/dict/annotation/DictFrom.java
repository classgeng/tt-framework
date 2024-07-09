package cn.cloud9.server.struct.dict.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictFrom {
    /* 翻译的来源表 */
    String srcTable() default "default";

    /* 翻译的指定类别 */
    String srcCate();

    /* 翻译的来源字段 */
    String srcField();

    /* 翻译的字段是否是多个的, 默认单个 */
    boolean isMulti() default false;

    /* 如果是多个的，每个值的分隔符是？ */
    String separator() default ",";
}
