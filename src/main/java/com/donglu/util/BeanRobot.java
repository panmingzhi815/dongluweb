package com.donglu.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanRobot extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField ipFiled;
    private JTextField dbFiled;
    private JTextField dbNameFiled;
    private JTextField tabField;
    private JTextField packField;
    private JTextField catField;
    private JCheckBox checkBox;
    private JTextField userField;
    private JTextField pwdField;
    private JComboBox dbBox;
    DbUtil dbutil = new DbUtil() ;
    BeanUtil butil = new BeanUtil();
    JLabel labelInfo ;
    private JTable  jtable;
    private MyTableModel  tableModel ;
    HashMap dbInfoMap;
    String[] titles = {"选择","表格名称"} ;

    //配置文件信息
    Map<String,HashMap<String,String>> dbMap;
    public BeanRobot() {

        setTitle("数据库生成javabean小工具");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 544, 374);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblIp = new JLabel("IP:");
        lblIp.setBounds(110, 13, 30, 15);
        panel.add(lblIp);

        ipFiled = new JTextField();
        ipFiled.setText("localhost");
        ipFiled.setBounds(146, 10, 147, 21);
        panel.add(ipFiled);
        ipFiled.setColumns(10);

        JLabel label = new JLabel("数据库:");
        label.setBounds(80, 41, 54, 15);
        panel.add(label);

//      String dbStyles[] = { };
//      dbBox = new JComboBox(dbStyles);
        dbBox = new JComboBox();
        dbBox.setBounds(146, 39, 147, 21);
        dbBox.setVisible(true);
        dbBox.setMaximumRowCount(3);
        panel.add(dbBox);

        JLabel dbNamelabel = new JLabel("数据库名:");
        dbNamelabel.setBounds(70, 67, 60, 20);
        panel.add(dbNamelabel);

        dbNameFiled = new JTextField();
        dbNameFiled.setBounds(146, 68, 147, 21);
        dbNameFiled.setText("onecard");
        panel.add(dbNameFiled);
        dbNameFiled.setColumns(10);

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setBounds(80, 98, 54, 15);
        panel.add(userLabel);

        userField = new JTextField();
        userField.setText("sa");
        userField.setBounds(145, 97, 148, 21);
        panel.add(userField);
        userField.setColumns(10);

        JLabel pwdLabel = new JLabel("密码:");
        pwdLabel.setBounds(95, 129, 54, 15);
        panel.add(pwdLabel);

        pwdField = new JTextField();
        pwdField.setText("1");
        pwdField.setBounds(145, 126, 147, 21);
        panel.add(pwdField);
        pwdField.setColumns(10);

        JLabel packLabel = new JLabel("包名:");
        packLabel.setBounds(95, 160, 54, 15);
        panel.add(packLabel);

        packField = new JTextField();
        packField.setText("");
        packField.setBounds(146, 155, 147, 21);
        panel.add(packField);
        packField.setColumns(10);

        JLabel catlogLabel = new JLabel("输出目录：");
        catlogLabel.setBounds(70, 193, 65, 15);
        panel.add(catlogLabel);

        catField = new JTextField();
        catField.setBounds(146, 190, 147, 21);
        panel.add(catField);
        catField.setColumns(10);

        checkBox = new JCheckBox("生成包结构目录");
        checkBox.setSelected(true);
        checkBox.setBounds(145, 220, 147, 23);
        panel.add(checkBox);


        JLabel mustdbLabel = new JLabel("* 选择数据库");
        mustdbLabel.setForeground(Color.RED);
        mustdbLabel.setBounds(303, 39, 176, 15);
        panel.add(mustdbLabel);
        JLabel mustIPLabel = new JLabel("* IP地址及端口号");
        mustIPLabel.setForeground(Color.RED);
        mustIPLabel.setBounds(303, 13, 176, 15);
        panel.add(mustIPLabel);

        JLabel mustPacklabel = new JLabel("* 包结构");
        mustPacklabel.setForeground(Color.RED);
        mustPacklabel.setBounds(303, 155, 79, 15);
        panel.add(mustPacklabel);

        JLabel catlabel = new JLabel("默认D:// ；注意格式");
        catlabel.setForeground(Color.RED);
        catlabel.setBounds(303, 193, 179, 15);
        panel.add(catlabel);


        JButton button = new JButton("查询");
        // 按钮增加动作执行go()方法
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                go();

            }
        });
        button.setBounds(145, 267, 93, 23);
        panel.add(button);

        JButton crButton = new JButton("生成Bean");
        // 按钮增加动作执行go()方法
        crButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                create();

            }
        });
        crButton.setBounds(280, 267, 93, 23);
        panel.add(crButton);


        // 增加关闭事件监听，关闭相关操作
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                close();
                System.exit(0);
            }

        });
        //设置表格

        Object[][] tableData = {};
        tableModel = new MyTableModel(tableData,titles);
        jtable = new JTable(this.tableModel) ;
        JScrollPane scr = new JScrollPane(this.jtable) ;
        scr.setBounds(430, 10, 200, 290);
        panel.add(scr);
        //添加标格监听事件
        jtable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int row=jtable.getSelectedRow();
                if(jtable.getSelectedColumn()==0)//如果是第一列的单元格，则返回，不响应点击
                    return;
                //列响应操作
            }
        });

        //显示操作信息label
        labelInfo = new JLabel("");
        labelInfo.setForeground(Color.RED);
        labelInfo.setBounds(20, 317, 600, 60);
        panel.add(labelInfo);

        //初始化配置信息和数据库下拉列表
        dbMap = dbutil.getDbConfigMap();
        for(String key : dbMap.keySet()){
            this.getDbBox().addItem(key);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // 创建对象
        BeanRobot dtb = new BeanRobot();
        // 设置可见
        dtb.setVisible(true);
        // 点击X关闭窗口
        dtb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 调用设置居中显示
        dtb.setSizeAndCentralizeMe(680, 440);

    }

    // 设置居中
    private void setSizeAndCentralizeMe(int width, int height) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(width, height);
        this.setLocation(screenSize.width / 2 - width / 2, screenSize.height
                / 2 - height / 2);
    }

    public void create(){
        String sucessName = "";
        this.getLabelInfo().setText("");
        initInfo();
        //是否勾选生成目录标识
        boolean createPackage = this.getCheckBox().getSelectedObjects() != null;

        //判断目录是否存在
        if (dbInfoMap.get("catName") != null ) {
            if(dbInfoMap.get("catName").toString().equals("")){
                dbInfoMap.put("catName", "D://");
            }
            if((new File(dbInfoMap.get("catName").toString()).isDirectory())!=true){
                this.getLabelInfo().setText("目录不存在，请重新输入");
            }else{
                if(createPackage){
                    if (dbInfoMap.get("packName") != null && !dbInfoMap.get("packName").toString().equals("")) {
                        String catPack = dbInfoMap.get("catName").toString()+dbInfoMap.get("packName").toString();
                        catPack = catPack.replace(".", "/");
                        new File(catPack).mkdirs();
                        dbInfoMap.put("catName", catPack);
                    }
                    //勾选
                    int rowCount = this.jtable.getRowCount();
                    for(int i = 0 ; i< rowCount ; i++){
                        if(this.jtable.getValueAt(i,0).toString().equals("true")){
                            String tabName = this.jtable.getValueAt(i,1).toString();
                            List<Map<String, String>> tabFileds = dbutil.getColumnNames(dbInfoMap, tabName);
                            butil.createBean(tabName, tabFileds,dbInfoMap);
                            sucessName+=tabName+",";
                        }
                    }
                }else{
                    int rowCount = this.jtable.getRowCount();
                    for(int i = 0 ; i< rowCount ; i++){
                        if(this.jtable.getValueAt(i,0).toString().equals("true")){
                            String tabName = this.jtable.getValueAt(i,1).toString();
                            List<Map<String, String>> tabFileds = dbutil.getColumnNames(dbInfoMap, tabName);
                            butil.createBean(tabName, tabFileds,dbInfoMap);
                            sucessName+=tabName+",";
                        }
                    }

                }
                this.getLabelInfo().setText("表"+sucessName+"生成成功");
            }
        }
    }

    public void go() {
        this.getLabelInfo().setText("");
        initInfo();
        String selTableStr = dbInfoMap.get("showTable").toString();
        //获取表名
        List<String> tableList = dbutil.getTableNames(dbInfoMap,dbInfoMap.get("dbName").toString());
        if(tableList == null){
            int rowCount = this.getTableModel().getRowCount();
            int delInd = 0;
            while(delInd < rowCount){
                this.getTableModel().removeRow(0);
                delInd++;
            }
            this.getLabelInfo().setText("数据库连接异常");
        }else{
            int rowCount = this.getTableModel().getRowCount();
            int delInd = 0;
            while(delInd < rowCount){
                this.getTableModel().removeRow(0);
                delInd++;
            }
            for(String tName : tableList){
                Object[] rowData = {new Boolean(false),tName};
                this.getTableModel().addRow(rowData);
            }


        }

    }

    public void initInfo(){
        //读取配置文件数据库配置
        String user = this.getUserField().getText();
        String pass = this.getPwdField().getText();
        String ip = this.getIpFiled().getText();
        String database = this.getDbNameFiled().getText();
        String dbName = this.getDbBox().getSelectedItem().toString();
        String packName =this.getPackField().getText();
        String catName =this.getCatField().getText();
        //处理界面数据
        dbInfoMap = new HashMap();
        dbInfoMap = dbMap.get(dbName);
        dbInfoMap.put("userName", user);
        dbInfoMap.put("userpwd", pass);
        dbInfoMap.put("jdbc", dbMap.get(dbName).get("JdbcURL")+database);
        dbInfoMap.put("driver", dbMap.get(dbName).get("driverClassName"));
        dbInfoMap.put("dbName", database);
        dbInfoMap.put("packName", packName);
        dbInfoMap.put("catName", catName);

    }
    private void close() {
        System.out.println("关闭事件");
    }

    public JTextField getIpFiled() {
        return ipFiled;
    }

    public void setIpFiled(JTextField ipFiled) {
        this.ipFiled = ipFiled;
    }

    public JTextField getDbFiled() {
        return dbFiled;
    }

    public void setDbFiled(JTextField dbFiled) {
        this.dbFiled = dbFiled;
    }

    public JTextField getTabField() {
        return tabField;
    }

    public void setTabField(JTextField tabField) {
        this.tabField = tabField;
    }

    public JTextField getPackField() {
        return packField;
    }

    public void setPackField(JTextField packField) {
        this.packField = packField;
    }

    public JTextField getCatField() {
        return catField;
    }

    public void setCatField(JTextField catField) {
        this.catField = catField;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }


    public JTextField getUserField() {
        return userField;
    }

    public void setUserField(JTextField userField) {
        this.userField = userField;
    }

    public JTextField getPwdField() {
        return pwdField;
    }

    public void setPwdField(JTextField pwdField) {
        this.pwdField = pwdField;
    }

    public JTextField getDbNameFiled() {
        return dbNameFiled;
    }

    public void setDbNameFiled(JTextField dbNameFiled) {
        this.dbNameFiled = dbNameFiled;
    }

    public JComboBox getDbBox() {
        return dbBox;
    }

    public void setDbBox(JComboBox dbBox) {
        this.dbBox = dbBox;
    }

    public JLabel getLabelInfo() {
        return labelInfo;
    }

    public void setLabelInfo(JLabel labelInfo) {
        this.labelInfo = labelInfo;
    }

    public JTable getJtable() {
        return jtable;
    }

    public void setJtable(JTable jtable) {
        this.jtable = jtable;
    }


    public MyTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(MyTableModel tableModel) {
        this.tableModel = tableModel;
    }


    class MyTableModel extends DefaultTableModel{
        public MyTableModel(Object[][] data,String[] columns)
        {
            super(data,columns);
        }
        public boolean isCellEditable(int row,int column){ //设置Table单元格是否可编辑
            if(column==0)return true;
            return false;
        }
        public Class<?> getColumnClass(int columnIndex)
        {
            if(columnIndex==0)
            { return Boolean.class;
            }
            return Object.class;
        }

    }
}