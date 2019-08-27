package org.seckill.service.impl;

import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import org.seckill.dao.cache.RedisDao;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CanalThread implements Runnable {
    @Autowired
    RedisDao redisDao;
    @Autowired
    SeckillService seckillService;

    public void run() {
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(AddressUtils.getHostIp(),
                11111), "example", "", "");
        int batchSize = 1000;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            while (true) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                } else {
                    putIntoRedis(message.getEntries());
                }

                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }
        } finally {
            connector.disconnect();
        }
    }

    private void putIntoRedis(List<Entry> entrys){
        Seckill seckill;
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }

            EventType eventType = rowChage.getEventType();
            if ("seckill".equals(entry.getHeader().getTableName()) && eventType == EventType.UPDATE) {
                for (RowData rowData : rowChage.getRowDatasList()) {
                    List<Column> columns = rowData.getAfterColumnsList();
                    int seckillId = Integer.parseInt(columns.get(2).getValue());
                    seckill = redisDao.getSeckill(Long.parseLong(columns.get(0).getValue()));
                    if(Objects.isNull(seckill)){
                        seckill = seckillService.getById(seckillId);
                        redisDao.putSeckill(seckill);
                    }
                    seckill.setNumber(seckillId);
                    redisDao.putSeckill(seckill);
                }
            }
        }
    }
}
