package com.example.rollcall;

import com.example.rollcall.service.StudentService;
import com.example.rollcall.ui.RollCallFrame;

import javax.swing.*;

public class RollCallApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RollCallFrame(new StudentService()).setVisible(true));
    }
}
