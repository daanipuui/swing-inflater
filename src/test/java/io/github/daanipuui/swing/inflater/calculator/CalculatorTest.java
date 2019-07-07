package io.github.daanipuui.swing.inflater.calculator;

import io.github.daanipuui.swing.inflater.xml.ComponentLoader;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;

import static io.github.daanipuui.swing.inflater.util.TestUtil.resizeFrame;
import static io.github.daanipuui.swing.inflater.util.TestUtil.showFrame;
import static io.github.daanipuui.swing.inflater.util.TestUtil.testComponentBounds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CalculatorTest {

    private static JTextArea LCD_DISPLAY;
    private static JLabel ERROR_DISPLAY;

    private static String STATUS;

    private static double LAST_VALUE;

    private static int PREVIOUS_OPERATION;

    @Test
    public void testCalculator() {
        JFrame frame = loadXml();
        JPanel container = (JPanel) frame.getContentPane().getComponent(0);

        Component[] containerComponents = container.getComponents();
        assertEquals(2, containerComponents.length);

        JPanel mainPanel = (JPanel) containerComponents[0];
        assertEquals("mainPanel", mainPanel.getName());
        assertTrue(mainPanel.getLayout() instanceof BorderLayout);
        testComponentBounds(mainPanel, 0, 0, 218, 154);

        Component[] mainPanelComponents = mainPanel.getComponents();
        assertEquals(4, mainPanelComponents.length);

        JPanel numberPanel = (JPanel) mainPanelComponents[0];
        assertEquals("numberPanel", numberPanel.getName());
        testComponentBounds(numberPanel, 0, 24, 177, 104);

        GridLayout numberPanelLayout = (GridLayout) numberPanel.getLayout();
        assertEquals(4, numberPanelLayout.getRows());
        assertEquals(3, numberPanelLayout.getColumns());

        Component[] numberPanelComponents = numberPanel.getComponents();
        assertEquals(12, numberPanelComponents.length);

        JButton button7 = (JButton) numberPanelComponents[0];
        testNumberButton(button7, 7);
        testComponentBounds(button7, 0, 0, 59, 26);

        JButton button8 = (JButton) numberPanelComponents[1];
        testNumberButton(button8, 8);
        testComponentBounds(button8, 59, 0, 59, 26);

        JButton button9 = (JButton) numberPanelComponents[2];
        testNumberButton(button9, 9);
        testComponentBounds(button9, 118, 0, 59, 26);

        JButton button4 = (JButton) numberPanelComponents[3];
        testNumberButton(button4, 4);
        testComponentBounds(button4, 0, 26, 59, 26);

        JButton button5 = (JButton) numberPanelComponents[4];
        testNumberButton(button5, 5);
        testComponentBounds(button5, 59, 26, 59, 26);

        JButton button6 = (JButton) numberPanelComponents[5];
        testNumberButton(button6, 6);
        testComponentBounds(button6, 118, 26, 59, 26);

        JButton button1 = (JButton) numberPanelComponents[6];
        testNumberButton(button1, 1);
        testComponentBounds(button1, 0, 52, 59, 26);

        JButton button2 = (JButton) numberPanelComponents[7];
        testNumberButton(button2, 2);
        testComponentBounds(button2, 59, 52, 59, 26);

        JButton button3 = (JButton) numberPanelComponents[8];
        testNumberButton(button3, 3);
        testComponentBounds(button3, 118, 52, 59, 26);

        JButton decimalButton = (JButton) numberPanelComponents[9];
        testDecimalButton(decimalButton);
        testComponentBounds(decimalButton, 0, 78, 59, 26);

        JButton button0 = (JButton) numberPanelComponents[10];
        testNumberButton(button0, 0);
        testComponentBounds(button0, 59, 78, 59, 26);

        JButton exitButton = (JButton) numberPanelComponents[11];
        testExitButton(exitButton);
        testComponentBounds(exitButton, 118, 78, 59, 26);

        JPanel operatorPanel = (JPanel) mainPanelComponents[1];
        assertEquals("operatorPanel", operatorPanel.getName());
        testComponentBounds(operatorPanel, 177, 24, 41, 104);

        GridLayout operatorPanelLayout = (GridLayout) operatorPanel.getLayout();
        assertEquals(4, operatorPanelLayout.getRows());
        assertEquals(1, operatorPanelLayout.getColumns());

        Component[] operatorPanelComponents = operatorPanel.getComponents();
        assertEquals(4, operatorPanelComponents.length);

        JButton plusButton = (JButton) operatorPanelComponents[0];
        testOperatorButton(plusButton, "+", Operator.PLUS);
        testComponentBounds(plusButton, 0, 0, 41, 26);

        JButton minusButton = (JButton) operatorPanelComponents[1];
        testOperatorButton(minusButton, "-", Operator.MINUS);
        testComponentBounds(minusButton, 0, 26, 41, 26);

        JButton multiplyButton = (JButton) operatorPanelComponents[2];
        testOperatorButton(multiplyButton, "*", Operator.MULTIPLY);
        testComponentBounds(multiplyButton, 0, 52, 41, 26);

        JButton divideButton = (JButton) operatorPanelComponents[3];
        testOperatorButton(divideButton, "/", Operator.DIVIDE);
        testComponentBounds(divideButton, 0, 78, 41, 26);

        JPanel clearPanel = (JPanel) mainPanelComponents[2];
        assertEquals("clearPanel", clearPanel.getName());
        testComponentBounds(clearPanel, 0, 128, 218, 26);

        Component[] clearPanelComponents = clearPanel.getComponents();
        assertEquals(3, clearPanelComponents.length);

        GridLayout clearPanelLayout = (GridLayout) clearPanel.getLayout();
        assertEquals(1, clearPanelLayout.getRows());
        assertEquals(3, clearPanelLayout.getColumns());

        JButton cButton = (JButton) clearPanelComponents[0];
        testClearButton(cButton, "C");
        testComponentBounds(cButton, 1, 0, 72, 26);

        JButton ceButton = (JButton) clearPanelComponents[1];
        testClearButton(ceButton, "CE");
        testComponentBounds(ceButton, 73, 0, 72, 26);

        JButton equalsButton = (JButton) clearPanelComponents[2];
        testOperatorButton(equalsButton, "=", Operator.EQUALS);
        testComponentBounds(equalsButton, 145, 0, 72, 26);

        JTextArea lcdDisplay = (JTextArea) mainPanelComponents[3];
        assertEquals("lcdDisplay", lcdDisplay.getName());
        assertEquals("0", lcdDisplay.getText());
        testComponentBounds(lcdDisplay, 0, 0, 218, 24);

        Font fontLcdDisplay = lcdDisplay.getFont();
        assertEquals(Font.DIALOG, fontLcdDisplay.getName());
        assertEquals(Font.BOLD, fontLcdDisplay.getStyle());
        assertEquals(18, fontLcdDisplay.getSize());

        JLabel errorDisplay = (JLabel) containerComponents[1];
        assertEquals("errorDisplay", errorDisplay.getName());
        testComponentBounds(errorDisplay, 0, 154, 218, 16);

        Font errorDisplayFont = errorDisplay.getFont();
        assertEquals(Font.DIALOG, errorDisplayFont.getName());
        assertEquals(Font.BOLD, errorDisplayFont.getStyle());
        assertEquals(12, errorDisplayFont.getSize());

        resizeFrame(frame, 1920, 1080, errorDisplay);

        testComponentBounds(mainPanel, 0, 0, 1902, 1017);
        testComponentBounds(numberPanel, 0, 24, 1861, 967);
        testComponentBounds(button7, 0, 1, 620, 241);
        testComponentBounds(button8, 620, 1, 620, 241);
        testComponentBounds(button9, 1240, 1, 620, 241);
        testComponentBounds(button4, 0, 242, 620, 241);
        testComponentBounds(button5, 620, 242, 620, 241);
        testComponentBounds(button6, 1240, 242, 620, 241);
        testComponentBounds(button1, 0, 483, 620, 241);
        testComponentBounds(button2, 620, 483, 620, 241);
        testComponentBounds(button3, 1240, 483, 620, 241);
        testComponentBounds(decimalButton, 0, 724, 620, 241);
        testComponentBounds(button0, 620, 724, 620, 241);
        testComponentBounds(exitButton, 1240, 724, 620, 241);
        testComponentBounds(operatorPanel, 1861, 24, 41, 967);
        testComponentBounds(plusButton, 0, 1, 41, 241);
        testComponentBounds(minusButton, 0, 242, 41, 241);
        testComponentBounds(multiplyButton, 0, 483, 41, 241);
        testComponentBounds(divideButton, 0, 724, 41, 241);
        testComponentBounds(clearPanel, 0, 991, 1902, 26);
        testComponentBounds(cButton, 0, 0, 634, 26);
        testComponentBounds(ceButton, 634, 0, 634, 26);
        testComponentBounds(equalsButton, 1268, 0, 634, 26);
        testComponentBounds(lcdDisplay, 0, 0, 1902, 24);
        testComponentBounds(errorDisplay, 0, 1017, 1902, 16);
    }

    private JFrame loadXml() {
        InputStream inputStream = CalculatorTest.class.getClassLoader().getResourceAsStream("calculator.xml");

        ComponentLoader loader = new ComponentLoader();
        loader.register(Operator.class.getPackage());
        JPanel container = loader.load(inputStream);

        LCD_DISPLAY = loader.getComponent("lcdDisplay");
        ERROR_DISPLAY = loader.getComponent("errorDisplay");

        resetState();

        return showFrame(container);
    }

    private void testNumberButton(JButton button, int value) {
        assertEquals(String.valueOf(value), button.getText());
        assertEquals(value, button.getClientProperty("NUMBER_PROPERTY"));

        ActionListener[] actionListeners = button.getActionListeners();
        assertEquals(1, actionListeners.length);
        assertEquals(onNumberClick(), actionListeners[0]);
    }

    private void testOperatorButton(JButton button, String text, int operator) {
        assertEquals(text, button.getText());
        assertEquals(operator, button.getClientProperty("OPERATOR_PROPERTY"));

        ActionListener[] actionListeners = button.getActionListeners();
        assertEquals(1, actionListeners.length);
        assertEquals(onOperatorClick(), actionListeners[0]);
    }

    private void testDecimalButton(JButton button) {
        assertEquals(".", button.getText());

        ActionListener[] actionListeners = button.getActionListeners();
        assertEquals(1, actionListeners.length);
        assertEquals(onDecimalClick(), actionListeners[0]);
    }

    private void testExitButton(JButton button) {
        assertEquals("EXIT", button.getText());
        assertEquals(KeyEvent.VK_C, button.getMnemonic());

        ActionListener[] actionListeners = button.getActionListeners();
        assertEquals(1, actionListeners.length);
        assertEquals(onExitClick(), actionListeners[0]);
    }

    private void testClearButton(JButton button, String text) {
        assertEquals(text, button.getText());

        ActionListener[] actionListeners = button.getActionListeners();
        assertEquals(1, actionListeners.length);
        assertEquals(onClearClick(), actionListeners[0]);
    }

    @SuppressWarnings("WeakerAccess")
    public static ActionListener onNumberClick() {
        return e -> {
            JComponent source = (JComponent) e.getSource();
            Integer number = (Integer) source.getClientProperty("NUMBER_PROPERTY");
            if (number == null) {
                throw new IllegalStateException("No NUMBER_PROPERTY on component");
            }

            numberButtonPressed(number);
        };
    }

    @SuppressWarnings("WeakerAccess")
    public static ActionListener onDecimalClick() {
        return e -> decimalButtonPressed();
    }

    @SuppressWarnings("WeakerAccess")
    public static ActionListener onExitClick() {
        return e -> System.exit(0);
    }

    @SuppressWarnings("WeakerAccess")
    public static ActionListener onOperatorClick() {
        return e -> {
            JComponent source = (JComponent) e.getSource();
            Integer opCode = (Integer) source.getClientProperty("OPERATOR_PROPERTY");
            if (opCode == null) {
                throw new IllegalStateException("No OPERATOR_PROPERTY on component");
            }

            operatorButtonPressed(opCode);
        };
    }

    @SuppressWarnings("WeakerAccess")
    public static ActionListener onClearClick() {
        return e -> resetState();
    }

    private static void numberButtonPressed(int i) {
        String displayText = LCD_DISPLAY.getText();
        String valueString = Integer.toString(i);

        if (("0".equals(displayText)) || ("FIRST".equals(STATUS))) {
            displayText = "";
        }

        int maxLength = (displayText.contains(".")) ? 21 : 20;
        if (displayText.length() + valueString.length() <= maxLength) {
            displayText += valueString;
            clearError();
        } else {
            setError("Reached the 20 digit max");
        }

        LCD_DISPLAY.setText(displayText);
        STATUS = "VALID";
    }

    private static void decimalButtonPressed() {
        String displayText = LCD_DISPLAY.getText();
        if ("FIRST".equals(STATUS)) {
            displayText = "0";
        }

        if (!displayText.contains(".")) {
            displayText = displayText + ".";
        }
        LCD_DISPLAY.setText(displayText);
        STATUS = "VALID";
    }

    private static void setError(String errorMessage) {
        if (errorMessage.trim().equals("")) {
            errorMessage = " ";
        }
        ERROR_DISPLAY.setText(errorMessage);
    }

    private static void clearError() {
        STATUS = "FIRST";
        ERROR_DISPLAY.setText(" ");
    }

    private static void operatorButtonPressed(int newOperation) {
        double displayValue = Double.valueOf(LCD_DISPLAY.getText());

        switch (PREVIOUS_OPERATION) {
            case Operator.PLUS:
                displayValue = LAST_VALUE + displayValue;
                commitOperation(newOperation, displayValue);
                break;
            case Operator.MINUS:
                displayValue = LAST_VALUE - displayValue;
                commitOperation(newOperation, displayValue);
                break;
            case Operator.MULTIPLY:
                displayValue = LAST_VALUE * displayValue;
                commitOperation(newOperation, displayValue);
                break;
            case Operator.DIVIDE:
                if (displayValue == 0) {
                    setError("ERROR: Division by Zero");
                } else {
                    displayValue = LAST_VALUE / displayValue;
                    commitOperation(newOperation, displayValue);
                }
                break;
            case Operator.EQUALS:
                commitOperation(newOperation, displayValue);
        }
    }

    private static void commitOperation(int operation, double result) {
        STATUS = "FIRST";
        LAST_VALUE = result;
        PREVIOUS_OPERATION = operation;
        LCD_DISPLAY.setText(String.valueOf(result));
    }

    private static void resetState() {
        clearError();
        LAST_VALUE = 0;
        PREVIOUS_OPERATION = Operator.EQUALS;
        LCD_DISPLAY.setText("0");
    }
}
