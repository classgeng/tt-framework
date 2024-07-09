package cn.cloud9.server.test.methodhandle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleClass {
    private String name;

    private String code = "1001";

    public SimpleClass(String name) {
        this.name = name;
    }

    public void connectName(String name) {
        this.name = this.name + " " + name;
    }

    private String getSAP() {
        return "SAP";
    }

    private void learnPrograming(String lang) {
        System.out.println(String.format("I am learning %s ", lang));
    }


    private String forFunctionInterface(int i) {
        return this.code + " |_| " + i;
    }

    public static String declaration(String author) {
        return author + ": " + "吾生也有涯，而知也无涯。以有涯随无涯，殆己";
    }
}
