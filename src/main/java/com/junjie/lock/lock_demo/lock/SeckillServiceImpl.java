package com.junjie.lock.lock_demo.lock;

import com.junjie.lock.lock_demo.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.StampedLock;

/**
 * 秒杀接口实现类，测试用例在 LockDemoApplicationTests中
 */
@Service
public class SeckillServiceImpl implements ISeckillService {
    private final StampedLock stampedLock = new StampedLock();
    @Autowired
    private IGoodsService iGoodsService;

    @Override
    public void buy(String item) {
        //返回值是可用于解锁或转换模式的戳记
        long stamp = stampedLock.readLock();
        try {
            Integer good = iGoodsService.getGoodById(item);
            if (good == null) {
                System.err.println(String.format("对不起商品%s不存在！", item));
                return;
            }
            if (good <= 0) {
                System.err.println(String.format("对不起商品%s已售完！", item));
                return;
            }
            long tempStamp = stampedLock.tryConvertToWriteLock(stamp);
            //如果=0表名尝试获取写锁失败，则释放当前读锁，重新获取写锁
            if (tempStamp == 0) {
                stampedLock.unlock(stamp);
                stamp = stampedLock.writeLock();
            } else {
                stamp = tempStamp;
            }
            //到这里说明已经成功获取写锁
            good--;
            System.out.println("商品编号:" + item + "被秒杀一件，剩余:" + good);
            iGoodsService.updateGoodById(item, good);
        } finally {
            stampedLock.unlock(stamp);
        }
    }

    @Override
    public Integer showRemaining(String item) {
        //乐观读锁
        Integer number = null;
        long stamp = stampedLock.tryOptimisticRead();
        try {
            if (stampedLock.validate(stamp)) {
                number = iGoodsService.getGoodById(item);
                return number;
            }
            try {
                //如果无效，则获取悲观读锁
                stamp = stampedLock.readLock();
                number = iGoodsService.getGoodById(item);
            } finally {
                stampedLock.unlock(stamp);
            }
        } finally {
            if (number == null) {
                System.err.println(String.format("对不起商品%s不存在！", item));
            } else if (number < 0) {
                System.err.println("系统系统，商品卖超了,Id>" + item);
            }
            System.out.println(String.format("此时商品 %s 还剩余 %d 件！", item, number));
        }
        return number;
    }

    @Override
    public StampedLock stampedLock() {
        return stampedLock;
    }
}
