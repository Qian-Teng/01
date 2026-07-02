package com.example.rollcall.ui;

import com.example.rollcall.model.Student;
import com.example.rollcall.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RollCallFrame extends JFrame {
    private final StudentService studentService;
    private final JTextField studentNoField = new JTextField(12);
    private final JComboBox<String> genderBox = new JComboBox<>(new String[]{"全部", "男", "女"});
    private final JSpinner countSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 30, 1));
    private final DefaultTableModel selectedModel = createTableModel();
    private final DefaultTableModel rankingModel = createTableModel();
    private List<Student> selectedStudents = new ArrayList<>();

    public RollCallFrame(StudentService studentService) {
        super("老师课堂点名系统（MyBatis + H2）");
        this.studentService = studentService;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        add(createControlPanel(), BorderLayout.NORTH);
        add(createTablesPanel(), BorderLayout.CENTER);
        refreshRanking();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        panel.add(new JLabel("点名数量:"));
        panel.add(countSpinner);
        panel.add(new JLabel("学号筛选:"));
        panel.add(studentNoField);
        panel.add(new JLabel("性别:"));
        panel.add(genderBox);

        JButton confirmButton = new JButton("确认点名");
        confirmButton.addActionListener(event -> rollCall());
        panel.add(confirmButton);

        JButton addButton = new JButton("所选学生 +1 分");
        addButton.addActionListener(event -> adjustSelectedScore(1));
        panel.add(addButton);

        JButton minusButton = new JButton("所选学生 -1 分");
        minusButton.addActionListener(event -> adjustSelectedScore(-1));
        panel.add(minusButton);

        JButton rankingButton = new JButton("查看课堂分数排名");
        rankingButton.addActionListener(event -> refreshRanking());
        panel.add(rankingButton);
        return panel;
    }

    private JSplitPane createTablesPanel() {
        JTable selectedTable = new JTable(selectedModel);
        JTable rankingTable = new JTable(rankingModel);
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                wrapTable("本次点名结果（姓名、学号、性别、课堂分数）", selectedTable),
                wrapTable("课堂分数排名", rankingTable)
        );
        splitPane.setResizeWeight(0.45);
        return splitPane;
    }

    private JScrollPane wrapTable(String title, JTable table) {
        table.setRowHeight(26);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(title));
        return scrollPane;
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(new Object[]{"排名", "学号", "姓名", "性别", "课堂分数"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void rollCall() {
        int count = (Integer) countSpinner.getValue();
        String studentNo = studentNoField.getText().trim();
        String gender = (String) genderBox.getSelectedItem();
        selectedStudents = studentService.rollCall(studentNo, gender, count);
        fillTable(selectedModel, selectedStudents, false);
        if (selectedStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有找到符合条件的学生，请调整学号或性别筛选。", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void adjustSelectedScore(int delta) {
        if (selectedStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先点击“确认点名”选择学生。", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        studentService.adjustScore(selectedStudents, delta);
        refreshRanking();
        JOptionPane.showMessageDialog(this, "已为本次点名学生" + (delta > 0 ? "加" : "减") + "1 分。", "完成", JOptionPane.INFORMATION_MESSAGE);
    }


    private void refreshRanking() {
        List<Student> ranking = studentService.findScoreRanking();
        fillTable(rankingModel, ranking, true);
        if (!selectedStudents.isEmpty()) {
            selectedStudents = studentService.findAll().stream()
                    .filter(student -> selectedStudents.stream().anyMatch(selected -> selected.getId().equals(student.getId())))
                    .toList();
            fillTable(selectedModel, selectedStudents, false);
        }
    }

    private void fillTable(DefaultTableModel model, List<Student> students, boolean showRank) {
        model.setRowCount(0);
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            model.addRow(new Object[]{showRank ? i + 1 : "", student.getStudentNo(), student.getName(), student.getGender(), student.getClassScore()});
        }
    }
}
