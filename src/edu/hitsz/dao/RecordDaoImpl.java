package edu.hitsz.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordDaoImpl implements RecordDao {
    private List<PlayerRecord> records;
    private String fileName;

    // 构造方法：根据游戏难度动态生成存储文件名
    // 例如传入 "Easy"，文件就是 "record_easy.txt"
    public RecordDaoImpl(String difficulty) {
        this.fileName = "record_" + difficulty.toLowerCase() + ".txt";
        this.records = new ArrayList<>();
        loadFromFile(); // 初始化时自动从文件加载历史数据
    }

    // 从文件中读取数据到内存列表
    private void loadFromFile() {
        records.clear();
        File file = new File(fileName);
        if (!file.exists()) {
            return; // 如果文件不存在（第一次玩），直接返回空列表
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 解析 CSV 格式数据：userName,score,recordTime
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    String time = parts[2];
                    records.add(new PlayerRecord(name, score, time));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("读取排行榜数据失败：" + e.getMessage());
        }
    }

    // 将内存中的列表写入本地文件
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (PlayerRecord record : records) {
                writer.write(record.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存排行榜数据失败：" + e.getMessage());
        }
    }

    @Override
    public List<PlayerRecord> getAllRecords() {
        return records;
    }

    @Override
    public void doAdd(PlayerRecord record) {
        records.add(record);
        // 每次新增数据后，利用 PlayerRecord 的 compareTo 进行降序排序
        // 排序的时间复杂度为 $$O(N \log N)$$
        Collections.sort(records);
        saveToFile(); // 同步保存到本地文件
    }

    @Override
    public void doDelete(int index) {
        // 注意：传入的 index 是玩家看到的排名 (例如第 1 名)，对应数组下标是 index - 1
        int arrayIndex = index - 1;
        if (arrayIndex >= 0 && arrayIndex < records.size()) {
            records.remove(arrayIndex);
            saveToFile(); // 删除后同步保存到本地文件
            System.out.println("成功删除第 " + index + " 名的记录。");
        } else {
            System.out.println("删除失败：无效的名次。");
        }
    }
}