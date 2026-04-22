package edu.hitsz.dao;

import java.util.List;

public interface RecordDao {
    // 获取当前难度下的所有记录（已排序）
    List<PlayerRecord> getAllRecords();

    // 新增一条游戏记录
    void doAdd(PlayerRecord record);

    // 根据排名（索引）删除某条记录
    void doDelete(int index);
}