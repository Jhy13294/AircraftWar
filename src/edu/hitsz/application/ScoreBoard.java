package edu.hitsz.application;

import edu.hitsz.dao.PlayerRecord;
import edu.hitsz.dao.RecordDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ScoreBoard {
    private JPanel mainPanel;
    private JTable scoreTable;
    private DefaultTableModel tableModel;
    private RecordDao recordDao;

    public ScoreBoard(RecordDao recordDao) {
        this.recordDao = recordDao;
        initUI();
        loadData();
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());

        // 1. 顶部标题面板
        JPanel topPanel = new JPanel();
        JLabel titleLabel = new JLabel("得 分 排 行 榜");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        topPanel.add(titleLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 2. 中间表格面板
        String[] columnNames = {"名次", "玩家名字", "得分", "记录时间"};
        // 创建不可编辑的表格模型
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scoreTable = new JTable(tableModel);
        scoreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选

        // 必须加上滚动面板，否则表头不显示
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 3. 底部删除按钮面板
        JPanel bottomPanel = new JPanel();
        JButton deleteBtn = new JButton("删除选中记录");

        // ==========================================
        // 重点修改：删除按钮点击事件适配你的 DAO
        // ==========================================
        deleteBtn.addActionListener(e -> {
            // 获取玩家在表格中选中的行号 (从 0 开始)
            int selectedRow = scoreTable.getSelectedRow();

            if (selectedRow != -1) {
                // 弹出确认框
                int confirm = JOptionPane.showConfirmDialog(
                        mainPanel,
                        "确定要删除这条记录吗？",
                        "确认删除",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // 因为表格的数据顺序和 DAO 中的 List 顺序是一一对应的
                    // 所以直接将 selectedRow 作为 index 传给你的 doDelete 方法
                    recordDao.doDelete(selectedRow);

                    // 重新从 DAO 加载数据，刷新表格显示
                    loadData();
                }
            } else {
                JOptionPane.showMessageDialog(mainPanel, "请先选中一条记录！");
            }
        });

        bottomPanel.add(deleteBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    // 从 DAO 读取数据并填充到表格
    public void loadData() {
        tableModel.setRowCount(0); // 清空旧数据
        List<PlayerRecord> records = recordDao.getAllRecords();

        // 遍历所有记录，添加到表格模型中
        for (int i = 0; i < records.size(); i++) {
            PlayerRecord r = records.get(i);
            // 往表格中添加一行：名次是 i+1
            tableModel.addRow(new Object[]{i + 1, r.getUserName(), r.getScore(), r.getRecordTime()});
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
