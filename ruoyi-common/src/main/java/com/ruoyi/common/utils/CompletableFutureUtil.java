package com.ruoyi.common.utils;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.core.task.TaskExecutor;

/**
 * 异步处理工具
 *
 * @author 张景春
 * @date 2024/9/12 10:57:25
 */
@Slf4j
public class CompletableFutureUtil {

    private static final Integer BATCH_SIZE = 10;

    public static <T> void asyncProcess(List<T> data, Consumer<T> consumer,
        TaskExecutor taskExecutor) {
        asyncProcess(data, null, consumer, taskExecutor);
    }

    public static <T> void asyncProcess(List<T> data, Integer batchSize, Consumer<T> consumer,
        TaskExecutor taskExecutor) {
        if (batchSize == null) {
            batchSize = BATCH_SIZE;
        }

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        ListUtil.partition(data, batchSize).forEach(l -> {
            CompletableFuture<Void> partRet = CompletableFuture
                .runAsync(() -> l.forEach(consumer), taskExecutor)
                .exceptionally(e -> {
                    log.error("asyncProcess run exception", e);
                    return null;
                });
            futures.add(partRet);
        });
        CompletableFuture<Void> allof = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allof.join();
    }

    public static <T, U> List<U> asyncProcess(List<T> data, Function<T, U> function,
        TaskExecutor taskExecutor) {
        return asyncProcess(data, null, function, taskExecutor);
    }


    public static <T, U> List<U> asyncProcess(List<T> data, Integer batchSize, Function<T, U> function,
        TaskExecutor taskExecutor) {
        if (batchSize == null) {
            batchSize = BATCH_SIZE;
        }

        List<CompletableFuture<List<U>>> futures = new ArrayList<>();
        List<List<T>> lists = ListUtil.partition(data, batchSize);
        for (List<T> l : lists) {
            CompletableFuture<List<U>> partRet = CompletableFuture.supplyAsync(() -> l.stream().map(function).collect(Collectors.toList()), taskExecutor).handle((o, e) -> {
                if (e != null) {
                    log.error("asyncProcess supply exception", e);
                     return Lists.newArrayList();
                }
                return o;
            });
            futures.add(partRet);
        }
        CompletableFuture<Void> allof = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allof.join();

        List<U> list = new ArrayList<>();
        for (CompletableFuture<List<U>> ret : futures) {
            try {
                List<U> o = ret.get();
                list.addAll(o);
            } catch (Exception e) {
                log.error("asyncProcess supply exception", e);
            }
        }

        return list;
    }

    public static <T, U> List<U> getFastestResult(List<T> data, Integer batchSize, Function<T, U> function,
        TaskExecutor taskExecutor) {
        if (batchSize == null) {
            batchSize = BATCH_SIZE;
        }

        List<CompletableFuture<List<U>>> futures = new ArrayList<>();
        List<List<T>> lists = ListUtil.partition(data, batchSize);
        for (List<T> l : lists) {
            CompletableFuture<List<U>> partRet = CompletableFuture.supplyAsync(() -> l.stream().map(function).collect(Collectors.toList()), taskExecutor)
                .handle((o, e) -> {
                    if (e != null) {
                        log.error("getFastestResult exception", e);
                        return Lists.newArrayList();
                    }
                    return o;
            });
            futures.add(partRet);
        }
        CompletableFuture[] cfs = futures.toArray(new CompletableFuture[0]);
        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(cfs);

        // 处理结果（当任一任务完成时触发）
        try {
            // 阻塞获取结果（包含正常结果或异常）
            Object result = anyFuture.get();
            // 取消未完成的任务
            cancelRemainingTasks(anyFuture, cfs);

            List<U> typedResult = (List<U>) result;

            return typedResult;

        } catch (Exception e) {
            log.error("getFastestResult supply exception", e);
        }
        return null;
    }

    // 取消未完成的任务
    private static void cancelRemainingTasks( CompletableFuture<Object> anyFuture, CompletableFuture<?>... futures) {
        for (CompletableFuture<?> future : futures) {
            if (future != anyFuture && !future.isDone()) { // 跳过 anyOf 本身，取消未完成的任务
                future.cancel(true); // 参数 true 表示中断正在执行的任务（若任务支持中断）
                log.info("取消任务{}", JSON.toJSONString(future));
            }
        }
    }
}
