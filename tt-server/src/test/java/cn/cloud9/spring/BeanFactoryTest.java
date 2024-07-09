package cn.cloud9.spring;

import cn.cloud9.server.test.mapstruct.DemoConverter;
import cn.cloud9.server.test.mapstruct.DemoDTO;
import cn.cloud9.server.test.mapstruct.DemoEntity;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class BeanFactoryTest {

    // 图片压缩
    @Test
    public void pictureCompressTest() {
        String src = "C:\\Users\\Administrator\\Desktop\\test.jpg";
        String dest = "C:\\Users\\Administrator\\Desktop\\test4.jpg";
        pictureCompress(src, dest, 1024 * 1024L, 0.5);
    }


    @Test
    public void converterTest() {
        DemoDTO build = DemoDTO.builder()
                .fieldA(1001)
                .fieldB(Boolean.TRUE)
                .fieldC("TEST")
                .fieldG(3003L)
                .build();

        DemoEntity demoEntity = DemoConverter.INSTANCE.dto2entity(build);

        demoEntity.setFieldG(3003L);
        DemoDTO demoDTO = DemoConverter.INSTANCE.entity2dto(demoEntity);
        log.info("demoEntity -> {}", demoEntity);
        log.info("demoDTO -> {}", demoDTO);

//        ArrayList<DemoDTO> objects = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            objects.add(DemoDTO.builder()
//                    .fieldA(1001)
//                    .fieldB(Boolean.TRUE)
//                    .fieldC("TEST")
//                    .filedG(3003L)
//                    .build());
//        }
//        List<DemoEntity> demoEntityList = DemoConverter.INSTANCE.dto2entity(objects);
//        demoEntityList.forEach(d -> log.info("de {}", d));
    }

    /**
     * 图片压缩处理方法
     * @param srcPath 图片源文件路径
     * @param destPath 输出文件路径
     * @param fileSize 限制在多大时停止压缩
     * @param accuracy 压缩图片的精细度
     */
    @SneakyThrows
    public static void pictureCompress(String srcPath, String destPath, long fileSize, double accuracy) {
        final File srcFile = new File(srcPath);
        long length = srcFile.length();
        if (length < fileSize) return;
        BufferedImage bim = ImageIO.read(srcFile);

        int imgWidth = bim.getWidth();
        int imgHeight = bim.getHeight();

        int desWidth = new BigDecimal(imgWidth).multiply(new BigDecimal(accuracy)).intValue();
        int desHeight = new BigDecimal(imgHeight).multiply(new BigDecimal(accuracy)).intValue();

        Thumbnails.of(srcPath).size(desWidth, desHeight).outputQuality(accuracy).toFile(destPath);
        pictureCompress(destPath, destPath, fileSize, accuracy);
    }
    /**
     * 图片压缩
     * @Param desPath 目标路径
     * @Param desFileSize 目标大小，单位kb
     * @Param accuracy 每次压缩的比例 如 0.8
     */
    @SneakyThrows
    public static void compressPicCycle(String desPath , long desFileSize, double accuracy) {
        File imgFile = new File(desPath);
        long fileSize = imgFile.length();
        //判断大小,如果小于目标大小,不压缩
        if (fileSize <= desFileSize * 1024) {
            return;
        }
        //计算宽高
        BufferedImage bim = ImageIO.read(imgFile);
        int imgWidth = bim.getWidth();
        int imgHeight = bim.getHeight();
        int desWidth = new BigDecimal(imgWidth).multiply(
                new BigDecimal(accuracy)).intValue();
        int desHeight = new BigDecimal(imgHeight).multiply(
                new BigDecimal(accuracy)).intValue();
        Thumbnails.of(desPath).size(desWidth, desHeight).outputQuality(accuracy).toFile(desPath);
        //如果不满足要求,递归直至满足要求
        compressPicCycle(desPath, desFileSize, accuracy);
    }

    @Test
    public void streamTest() {
        List<String> aa = new ArrayList<>();
        aa.add("1");
        aa.add("2");
        aa.add("3");
        aa.add("4");
        String s = aa.stream().filter(a -> a.equals("3")).findFirst().get();
        System.out.println(aa);
    }

    @Test
    public void applicationInitialize() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(SimpleConfig.class).setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("config", beanDefinition);

        for (String definitionName : beanFactory.getBeanDefinitionNames()) {
            log.info("definitionName -> {}", definitionName);
        }

        /* 扫描注解 */
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        /**
         * afterAnnotationRegister -> org.springframework.context.annotation.internalConfigurationAnnotationProcessor
         * afterAnnotationRegister -> org.springframework.context.annotation.internalAutowiredAnnotationProcessor
         * afterAnnotationRegister -> org.springframework.context.annotation.internalCommonAnnotationProcessor
         * afterAnnotationRegister -> org.springframework.context.event.internalEventListenerProcessor
         * afterAnnotationRegister -> org.springframework.context.event.internalEventListenerFactory
         */
        for (String definitionName : beanFactory.getBeanDefinitionNames()) {
            log.info("afterAnnotationRegister -> {}", definitionName);
        }

        /* 后置处理器 */
        Map<String, BeanFactoryPostProcessor> beansOfType = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        beansOfType.values().forEach(v -> v.postProcessBeanFactory(beanFactory));

        /* 后置处理后 */
        for (String definitionName : beanFactory.getBeanDefinitionNames()) {
            log.info("afterBeanFactoryPosted -> {}", definitionName);
        }

        /* 从Bean工厂取Bean */
//        Bean1 bean1 = beanFactory.getBean(Bean1.class);
//        log.info("bean1 -> {}", bean1);
//
//        Bean2 bean2 = beanFactory.getBean(Bean2.class);
//        log.info("bean2 -> {}", bean2);

        /* 此时还不能自动装配内嵌的Bean成员类型 */
        // log.info("inside bean2 -> {}", bean1.getBean2());


        /* 处理Bean的生命周期，装配内嵌Bean */
        Map<String, BeanPostProcessor> beansOfType1 = beanFactory.getBeansOfType(BeanPostProcessor.class);
        beansOfType1.values().forEach(beanFactory::addBeanPostProcessor);
        for (String definitionName : beanFactory.getBeanDefinitionNames()) {
            log.info("afterBeanPosted -> {}", definitionName);
        }

        /* 调用getBean时才开始创建bean, 或者调用preInstantiateSingletons预先创建 */
        beanFactory.preInstantiateSingletons();

        /* 不可以在Posted方法之前创建bean, 否则无法注入依赖 */
        Bean1 bean1 = beanFactory.getBean(Bean1.class);
        log.info("after posted inside bean2 -> {}", bean1.getBean2());



    }


    /* 需要被注册的Bean */
    @Configuration
    static class SimpleConfig {

        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    @Data
    static class Bean1 {

        @Autowired
        private Bean2 bean2;
    }

    @Data
    static class Bean2 {

    }
}
