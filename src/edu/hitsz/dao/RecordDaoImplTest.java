package edu.hitsz.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecordDaoImplTest {

    private RecordDao recordDao;
    private final String TEST_DIFFICULTY = "test_mode"; // 专用测试难度标识
    private final String TEST_FILE_NAME = "record_test_mode.txt"; // 专用测试文件

    @BeforeEach
    @DisplayName("每个测试前执行：环境初始化")
    void setUp() {
        System.out.println("--- 初始化测试环境 ---");
        // 确保测试前没有残留的脏数据文件
        File file = new File(TEST_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        // 实例化 DAO，这会自动创建一个空的测试排行榜文件
        recordDao = new RecordDaoImpl(TEST_DIFFICULTY);
    }

    @AfterEach
    @DisplayName("每个测试后执行：清理战场")
    void tearDown() {
        System.out.println("--- 清理测试环境 ---");
        // 测试结束后，将生成的临时测试文件删掉，不污染项目目录
        File file = new File(TEST_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("测试 doAdd 方法及自动降序排序功能")
    void testDoAddAndSort() {
        // 1. 模拟乱序添加 3 条记录
        recordDao.doAdd(new PlayerRecord("PlayerA", 100, "10:00"));
        recordDao.doAdd(new PlayerRecord("PlayerB", 500, "10:01"));
        recordDao.doAdd(new PlayerRecord("PlayerC", 300, "10:02"));

        // 2. 获取所有的记录
        List<PlayerRecord> records = recordDao.getAllRecords();

        // 3. 断言测试 (Assertions)
        // 预期记录总数为 3
        assertEquals(3, records.size(), "记录的总数应该为3");

        // 预期最高分 (500分) 排在第 0 位 (第一名)
        assertEquals("PlayerB", records.get(0).getUserName(), "第一名应该是 PlayerB");
        assertEquals(500, records.get(0).getScore(), "最高分应该是 500");

        // 预期最低分 (100分) 排在第 2 位 (第三名)
        assertEquals("PlayerA", records.get(2).getUserName(), "第三名应该是 PlayerA");
        assertEquals(100, records.get(2).getScore(), "最低分应该是 100");
    }

    @Test
    @DisplayName("测试 doDelete 方法")
    void testDoDelete() {
        // 1. 先准备两条数据
        recordDao.doAdd(new PlayerRecord("PlayerA", 100, "10:00"));
        recordDao.doAdd(new PlayerRecord("PlayerB", 500, "10:01"));
        // 此时排序应为: [PlayerB(500), PlayerA(100)]

        // 2. 执行删除操作：删除第 1 名 (即 500 分的 PlayerB)
        recordDao.doDelete(1);

        // 3. 断言测试
        List<PlayerRecord> records = recordDao.getAllRecords();
        assertEquals(1, records.size(), "删除一条后，记录总数应该为 1");

        // 剩下的唯一一条记录，应该是原来的第二名 PlayerA
        assertEquals("PlayerA", records.get(0).getUserName(), "剩余的记录应该是 PlayerA");
        assertEquals(100, records.get(0).getScore(), "剩余的分数应该是 100");
    }
}
