package com.ShazaSoft;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;


public class EmployeeSalaryMaintenance {
    private JPanel Main;
    private JTextField txtEmpId;
    private JButton btnSearch;
    private JLabel lblEmpId;
    private JLabel lblEid;
    private JLabel lblEidValue;
    private JLabel lblEname;
    private JLabel lblEnameValue;
    private JLabel lblEage;
    private JLabel lblEageValue;
    private JLabel lblEsal;
    private JLabel lblEsalValue;
    private JLabel lblEnsalValue;
    private JLabel lblEnsal;
    private JLabel lblSalMnthsValue;
    private JLabel lblSalMnths;
    private JButton btnClear;


    public static void main(String[] args) {
        JFrame frame = new JFrame("EmployeeSalaryMaintenance");
        frame.setContentPane(new EmployeeSalaryMaintenance().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public EmployeeSalaryMaintenance() {
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                ArrayList<Integer> empIds = new ArrayList<>();
                ArrayList<String>  empNames = new ArrayList<>();
                ArrayList<Integer> empAges = new ArrayList<>();
                ArrayList<Double> empSalaries = new ArrayList<>();
                ArrayList<Double> empNSals = new ArrayList<>();
                ArrayList<ArrayList<String>> grpPaidMonths = new ArrayList<>();

                try {
                    String url = "jdbc:mysql://localhost:3306/shazasoft_com";
                    String user = "root";
                    String pass = "";
                    Connection connection = DriverManager.getConnection(url, user, pass);
                    Statement statement = connection.createStatement();

                    ResultSet empResult = statement.executeQuery("select * from employee");

                    while (empResult.next()) {

                        int id = empResult.getInt("Eid");
                        String name = empResult.getString("Ename");
                        int age = empResult.getInt("Eage");
                        double sal = empResult.getDouble("Esalary");

                        empNames.add(name);
                        empIds.add(id);
                        empAges.add(age);
                        empSalaries.add(sal);

                        double netSalary = Allowance(sal) + sal;
                        empNSals.add(netSalary);

                    }

                    ResultSet salMonResult = statement.executeQuery("select * from paid_salary_month");
                    ResultSetMetaData metaData = salMonResult.getMetaData();

                    while (salMonResult.next()) {
                        ArrayList<String> paidMonthsPerEmp = new ArrayList<>();
                        int colCount = metaData.getColumnCount();
                        for (int x = 2; x <= colCount; x++) {
                            if (salMonResult.getBoolean(x)) {
                                paidMonthsPerEmp.add(metaData.getColumnName(x));
                            }
                        }
                        grpPaidMonths.add(paidMonthsPerEmp);
                    }

                }
                catch (Exception ex){
                    ex.printStackTrace();
                }


                String id = txtEmpId.getText();
                int eid = Integer.parseInt(id);
                int index = empIds.indexOf(eid);

                if (empIds.contains(eid)) {
                    lblEidValue.setText("E" + eid);
                    lblEnameValue.setText(empNames.get(index));
                    lblEageValue.setText(Integer.toString(empAges.get(index)));
                    lblEsalValue.setText("LKR." + empSalaries.get(index));
                    lblEnsalValue.setText("LKR." + empNSals.get(index));
                    lblSalMnthsValue.setText(String.valueOf(grpPaidMonths.get(index)));
                }

                else {
                    ClearFields();

                    JOptionPane.showMessageDialog(null,
                            "Employee ID is not found.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });


        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClearFields();
            }
        });
    }

    private void ClearFields(){
        txtEmpId.setText("");
        lblEidValue.setText("");
        lblEnameValue.setText("");
        lblEageValue.setText("");
        lblEsalValue.setText("");
        lblEnsalValue.setText("");
        lblSalMnthsValue.setText("");
    }

    public static double Allowance(double x){
        if (x >= 30000 && x < 40000){
            return 0.2 * x;
        } else if (x >= 40000 && x < 50000) {
            return 0.25 * x;
        } else if (x >= 50000) {
            return 0.3 * x;
        } else {
            return x;
        }
    }



}
