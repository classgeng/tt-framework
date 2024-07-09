package cn.cloud9.async;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 参考视频资料
 * https://www.bilibili.com/video/BV1nA411g7d2
 * https://www.bilibili.com/video/BV1S54y1u79K
 *
 * ThreadPoolExecutor 参数概述
 * https://www.cnblogs.com/dafanjoy/p/9729358.html
 */
public class AsyncThreadTest {

    private ThreadPoolExecutor getThreadPoolExecutor() {
        /**
         * 线程数量参数 计算公式参考
         * https://www.cnblogs.com/warehouse/p/10810338.html
         */
        int cpuCount = Runtime.getRuntime().availableProcessors();

        int corePoolSize = 2 * cpuCount; // 核心线程数量（初始化和空闲存留时的线程数量）
        int maximumPoolSize = 100; // 最大线程数量 线程占用峰值上限数量
        long keepAliveTime = 100; // 保留时长（当前线程数量大于核心数量时保留多久后释放多余线程）
        TimeUnit timeUnit = TimeUnit.SECONDS; // 保留时长单位


        /**
         * SynchronousQueue队列
         *  SynchronousQueue是一个特殊的BlockingQueue
         *  它没有容量，每执行一个插入操作就会阻塞，需要再执行一个删除操作才会被唤醒
         *  反之每一个删除操作也都要等待对应的插入操作。
         */
        SynchronousQueue<Runnable> synchronousQueue = new SynchronousQueue<>();

        /**
         * ArrayBlockingQueue有界任务队列，若有新的任务需要执行时，线程池会创建新的线程，
         * 直到创建的线程数量达到corePoolSize时，则会将新的任务加入到等待队列中。
         * 若等待队列已满，即超过ArrayBlockingQueue初始化的容量，则继续创建线程，直到线程数量达到maximumPoolSize设置的最大线程数量，
         * 若大于maximumPoolSize，则执行拒绝策略。在这种情况下，线程数量的上限与有界任务队列的状态有直接关系，
         * 如果有界队列初始容量较大或者没有达到超负荷的状态，线程数将一直维持在corePoolSize以下，
         * 反之当任务队列已满时，则会以maximumPoolSize为最大线程数上限。
         */
        ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<>(10);

        /**
         * 无界任务队列，线程池的任务队列可以无限制的添加新的任务，
         * 而线程池创建的最大线程数量就是你corePoolSize设置的数量，
         * 也就是说在这种情况下maximumPoolSize这个参数是无效的，
         * 哪怕你的任务队列中缓存了很多未执行的任务，当线程池的线程数达到corePoolSize后，就不会再增加了；\
         * 若后续有新的任务加入，则直接进入队列等待，
         * 当使用这种任务队列模式时，一定要注意你任务提交与处理之间的协调与控制，
         * 不然会出现队列中的任务由于无法及时处理导致一直增长，直到最后资源耗尽的问题。
         */
        LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>();

        /**
         * PriorityBlockingQueue它其实是一个特殊的无界队列，它其中无论添加了多少个任务，线程池创建的线程数也不会超过corePoolSize的数量
         * ，只不过其他队列一般是按照先进先出的规则处理任务，而PriorityBlockingQueue队列可以自定义规则根据任务的优先级顺序先后执行。
         */
        PriorityBlockingQueue<Runnable> priorityBlockingQueue = new PriorityBlockingQueue<>();

        /**
         * ThreadFactory
         */
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        /**
         * 当线程需求数量超出预设峰值上限，如何拒绝线程资源获取
         * 1、直接丢弃
         * 2、替换工作队列的最后一个
         * 3、抛异常中断
         * 4、哪个线程提交的任务就让那个线程执行
         * 5、自定义
         */
        ThreadPoolExecutor.DiscardPolicy discardPolicy = new ThreadPoolExecutor.DiscardPolicy();
        ThreadPoolExecutor.DiscardOldestPolicy discardOldestPolicy = new ThreadPoolExecutor.DiscardOldestPolicy();
        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        RejectedExecutionHandler handler = (r, executor) -> {
            // 入参一个runnable 和 executor
        };

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                synchronousQueue,
                threadFactory,
                callerRunsPolicy
        );


        /**
         * 线程池工具类
         */
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);


        return threadPoolExecutor;
    }


    @SneakyThrows
    private static String getThreadName() {
        return Thread.currentThread().getName();
    }

    @SneakyThrows
    private static void awaitSec() {
        Thread.sleep(1000L);
    }

    @SneakyThrows
    private static void awaitSec(int sec) {
        Thread.sleep(sec * 1000L);
    }
    /**
     * 表示有两件事情需要同时执行
     * 例如 开发一个功能 前端和后端同时进行
     */
    @Test
    @SneakyThrows
    public void demo01() {
        System.out.println("开会讨论需求");

        // 前端开发前台页面
        CompletableFuture<Void> async = CompletableFuture.runAsync(() -> {
            int times = 6; // 假设前端开发耗时为6
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }
        });

        int times = 3; // 假设后端开发耗时为3
        while (times != 0) {
            System.out.println(getThreadName() +" 后端开发后台接口... 剩余时间 " + times);
            awaitSec();
            -- times;
        }

        // join 等待该任务完成后才能继续下一步
        async.join();
        System.out.println("功能开发完成!");
    }

    /**
     * 如果需要知晓异步任务的结果，设置返回值返回
     */
    @Test
    @SneakyThrows
    public void demo02() {
        System.out.println("开会讨论需求");

        // 前端开发前台页面
        CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> {
            int times = 6; // 假设前端开发耗时为6
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }
            return "编写的操作手册";
        });

        int times = 3; // 假设后端开发耗时为3
        while (times != 0) {
            System.out.println(getThreadName() + " 后端开发后台接口... 剩余时间 " + times);
            Thread.sleep(1000L);
            -- times;
        }

        // join 等待该任务完成后才能继续下一步 (和get一样 get要求处理异常， join不要求)
        String res = async.join();
        // get方法本身自带join 阻塞执行
        String res2 = async.get();
        System.out.println("功能开发完成!" + res + " " + res2);
    }

    /**
     * thenCompose方法，当之前一个任务完成之后再开始执行方法内的异步任务
     * 连接上一个任务
     */
    @Test
    @SneakyThrows
    public void demo03() {
        System.out.println("开会讨论需求");

        // 前端开发前台页面
        CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> {
            int times = 6; // 假设前端开发耗时为6
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }
            return "页面组件编写完成！";
        }).thenCompose(before -> CompletableFuture.supplyAsync(() -> {
            int times = 3; // 假设前端开发耗时为
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发联调接口... 剩余时间 " + times);
                awaitSec();
                --times;
            }

            return "接口联调完成！" + before;
        }));

        int times = 3; // 假设后端开发耗时为3
        while (times != 0) {
            System.out.println(getThreadName() + " 后端开发后台接口... 剩余时间 " + times);
            awaitSec();
            -- times;
        }

        String res = async.join();
        // String res2 = async.get();
        // System.out.println("功能开发完成!" + res + " " + res2);
        System.out.println("功能开发完成!" + res);
    }

    /**
     * thenCombine方法，追加一个新的异步任务，和之前的任务同时启动，两个任务都执行完成后回调一个合并方法，返回结果
     * 合并两个任务 异步的线程使用同一个
     */
    @Test
    @SneakyThrows
    public void demo05() {
        System.out.println("开会讨论需求");

        // 前端开发前台页面 现在需要两个前端来开发页面
        CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> {
            int times = 3;
            while (times != 0) {
                System.out.println("前端开发1前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }
            return "页面组件1编写完成！";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            int times = 3;
            while (times != 0) {
                System.out.println("前端开发2前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }

            return "页面组件2编写完成";
        }), (a, b) -> {
            System.out.println(a);
            System.out.println(b);
            System.out.println("先摸会儿鱼再说");
            return "页面完成";
        });

        int times = 3; // 假设后端开发耗时为3
        while (times != 0) {
            System.out.println("后端开发后台接口... 剩余时间 " + times);
            awaitSec();
            -- times;
        }

        String res = async.join();
        // String res2 = async.get();
        // System.out.println("功能开发完成!" + res + " " + res2);
        System.out.println("功能开发完成!" + res);
    }

    /**
     * thenApply 方法，当之前一个任务完成之后再开始执行方法内的异步任务
     * 连接上一个任务
     * 和thenCompose不一样，不需要返回CompleteStage实现，默认放置了，只需要逻辑
     */
    @Test
    @SneakyThrows
    public void demo06() {
        System.out.println("开会讨论需求");

        // 前端开发前台页面
        CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> {
            int times = 6; // 假设前端开发耗时为6
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }
            return "页面组件编写完成！";
        }).thenApply(before -> {
            int times = 3; // 假设前端开发耗时为
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发联调接口... 剩余时间 " + times);
                awaitSec();
                --times;
            }

            return "接口联调完成！" + before;
        });

        int times = 3; // 假设后端开发耗时为3
        while (times != 0) {
            System.out.println(getThreadName() + " 后端开发后台接口... 剩余时间 " + times);
            awaitSec();
            -- times;
        }

        String res = async.join();
        // String res2 = async.get();
        // System.out.println("功能开发完成!" + res + " " + res2);
        System.out.println("功能开发完成!" + res);
    }

    /**
     * thenApply 方法，当之前一个任务完成之后再开始执行方法内的异步任务
     * 连接上一个任务
     * 和thenCompose不一样，不需要返回CompleteStage实现，默认放置了，只需要逻辑
     */
    @Test
    @SneakyThrows
    public void demo07() {
        System.out.println("开会讨论需求");

        // 前端开发前台页面
        CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> {
            int times = 6; // 假设前端开发耗时为6
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }
            return "页面组件编写完成！";
        }).thenApplyAsync(before -> {
            int times = 3; // 假设前端开发耗时为
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发联调接口... 剩余时间 " + times);
                awaitSec();
                --times;
            }

            return "接口联调完成！" + before;
        });

        int times = 3; // 假设后端开发耗时为3
        while (times != 0) {
            System.out.println(getThreadName() + " 后端开发后台接口... 剩余时间 " + times);
            awaitSec();
            -- times;
        }

        String res = async.join();
        // String res2 = async.get();
        // System.out.println("功能开发完成!" + res + " " + res2);
        System.out.println("功能开发完成!" + res);
    }

    /**
     * applyToEither 同时执行两个异步任务，取最先完成的任务的结果返回，附带一个处理方法
     *
     */
    @Test
    @SneakyThrows
    public void demo08() {
        System.out.println("开会讨论需求");
        // 周一上班 两种情况
        CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> {
            awaitSec();
            awaitSec();
            System.out.println("老板没来，摸鱼！");
            return "今天我tm摸爆";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            awaitSec();
            System.out.println("老板来了，干活！");
            return "今天我被老板爆叼";
        }), toDay -> "今天的结果是：" + toDay);

        String res = async.join();
        System.out.println(res);
    }

    /**
     * exceptionally 在上面的任务中出现异常进入该方法，并将这个异常传入
     * (异步任务链上的所有异常都会被 exceptionally 捕获) 一般放在在最后声明异常的处理
     *
     */
    @Test
    @SneakyThrows
    public void demo09() {
        System.out.println("开会讨论需求");
        // 周一上班 两种情况
        CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> {
            awaitSec();
            awaitSec();
            System.out.println("老板没来，摸鱼！");
            return "今天我tm摸爆";
        }).exceptionally(e -> {
            // 这里记录错误日志
            return "老板高兴直接宣布今天休息！直接下班";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            awaitSec();
            System.out.println("老板来了，干活！");
            if (true) throw new RuntimeException();
            return "今天我被老板爆叼";
        }), toDay -> "今天的结果是：" + toDay).exceptionally(e -> {
            // 这里记录错误日志
            return "老板高兴直接宣布今天休息！直接下班";
        });

        String res = async.join();
        System.out.println(res);
    }

    /**
     * thenAccept 将不会返回结果，对任务完成之后执行一些你想做的事情
     */
    @Test
    public void demo10() {
        System.out.println("周一上班");

        CompletableFuture<Void> empty = CompletableFuture.supplyAsync(() -> {
            int times = 6; // 假设前端开发耗时为6
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }
            return "页面组件编写完成！";
        }).thenAccept(result -> {
            System.out.println("今天的任务完成：" + result);
            System.out.println("提前下班回家");
        });
        empty.join();
    }

    /**
     * thenAccept 将不会返回结果，对任务完成之后执行一些你想做的事情
     * (会把返回结果传入)
     */
    @Test
    public void demo11() {
        System.out.println("周一上班");

        CompletableFuture<Void> empty = CompletableFuture.supplyAsync(() -> {
            int times = 6; // 假设前端开发耗时为6
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }
            return "页面组件编写完成！";
        }).thenAccept(result -> {
            // 上一个任务完成后调用
            System.out.println("今天的任务完成：" + result);
            System.out.println("提前下班回家");
        });
        empty.join();
    }

    /**
     * thenRun 将不会返回结果，对任务完成之后执行一些你想做的事情
     * (不会把返回结果传入)
     */
    @Test
    public void demo12() {
        System.out.println("周一上班");

        CompletableFuture<Void> empty = CompletableFuture.supplyAsync(() -> {
            int times = 6; // 假设前端开发耗时为6
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }
            return "页面组件编写完成！";
        }).thenRun(() -> {
            // 上一个任务完成后调用
            System.out.println("今天的任务完成");
            System.out.println("提前下班回家");
        });
        empty.join();
    }

    /**
     * runAsync 使用CompletableFuture 就说明一定需要开启一个异步线程来完成任务
     * (不会把返回结果传入)
     */
    @Test
    public void demo13() {
        System.out.println("周一上班");

        CompletableFuture<Void> empty = CompletableFuture.runAsync(() -> {
            int times = 6; // 假设前端开发耗时为6
            while (times != 0) {
                System.out.println(getThreadName() + " 前端开发前台页面... 剩余时间 " + times);
                awaitSec();
                --times;
            }
        });
        empty.join();
    }

    /**
     * anyOf 所有置入的异步任务中，获取最快完成任务的结果
     *
     */
    @Test
    public void demo14() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            awaitSec(3);
            return "Future1的结果";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            awaitSec(4);
            return "Future2的结果";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            awaitSec(5);
            return "Future3的结果";
        });

        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(future1, future2, future3);

        // 输出Future2的结果
        System.out.println(anyOfFuture.join());
    }

    /**
     * allOf 所有置入的异步任务全部完成后调用此任务
     * (allOf的任务不接受所有的任务的结果)，必须要提取任务引用，一个个手动join获取
     * 或者封装到集合容器遍历join
     */
    @Test
    public void demo15() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            awaitSec(3);
            return "Future1的结果";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            awaitSec(4);
            return "Future2的结果";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            awaitSec(5);
            return "Future3的结果";
        });

        CompletableFuture<String>[] futures = new CompletableFuture[]{future1, future2, future3};

        // step 4: 使用allOf方法合并多个异步任务
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futures);

        // step 5: 当多个异步任务都完成后，使用回调操作文件结果，统计符合条件的文件个数
        CompletableFuture<String> countFuture = allOfFuture.thenApply(v -> Arrays.stream(futures).map(f -> f.join()).collect(Collectors.joining()));

        // step 6: 主线程打印输出文件个数
        System.out.println("count = " + countFuture.join());
    }

    /**
     * handle方法，在任务链中处理前一次的异常，不会报错，继续传递结果给下一个任务
     */
    @Test
    public void demo16() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            int r = 1 / 0;
            return "result1";
        }).handle((ret, ex) -> {
            if (ex != null) {
                System.out.println("我们得到异常：" + ex.getMessage());
                return "Unknown1";
            }
            return ret;
        }).thenApply(result -> {
            String str = null;
            int len = str.length();
            return result + " result2";
        }).handle((ret, ex) -> {
            if (ex != null) {
                System.out.println("我们得到异常：" + ex.getMessage());
                return "Unknown2";
            }
            return ret;
        }).thenApply(result -> {
            return result + " result3";
        });

        String ret = future.join();
    }

    /**
     * acceptEither 同时执行两个异步任务，取最先完成的任务的结果返回，附带一个处理方法
     * (applyToEither 会返回处理结果，acceptEither不处理结果返回，纯消费)
     *
     */
    @Test
    @SneakyThrows
    public void demo17() {
        System.out.println("开会讨论需求");
        // 周一上班 两种情况
        CompletableFuture<Void> async = CompletableFuture.supplyAsync(() -> {
            awaitSec();
            awaitSec();
            System.out.println("老板没来，摸鱼！");
            return "今天我tm摸爆";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            awaitSec();
            System.out.println("老板来了，干活！");
            return "今天我被老板爆叼";
        }), System.out::println);

        async.join();
    }

    /**
     * runAfterEither 同时执行两个异步任务，不关心哪个任务最快，只是最快的任务执行完后通知你要做点什么
     * 没有返回值也没有入参
     */
    @Test
    @SneakyThrows
    public void demo18() {
        System.out.println("开会讨论需求");
        // 周一上班 两种情况
        CompletableFuture<Void> async = CompletableFuture.supplyAsync(() -> {
            awaitSec();
            awaitSec();
            System.out.println("老板没来，摸鱼！");
            return "今天我tm摸爆";
        }).runAfterEither(CompletableFuture.supplyAsync(() -> {
            awaitSec();
            System.out.println("老板来了，干活！");
            return "今天我被老板爆叼";
        }), () -> System.out.println("ssss") );

        async.join();
    }
}
