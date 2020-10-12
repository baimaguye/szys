package work;

import java.util.Stack;
import java.util.HashMap;

public class Calculate {

    public String[] checkout(String formula, int length) {
        Stack<String> stackN = new Stack<>();//操作数
        Stack<String> stackO = new Stack<>();//操作符
        String[] RPN = new String[length];//逆波兰表达式
        // 哈希表存放运算符优先级
        HashMap<String, Integer> hashmap = new HashMap<>();
        hashmap.put("(", 0);
        hashmap.put("＋", 1);
        hashmap.put("－", 1);
        hashmap.put("×", 2);
        hashmap.put("÷", 2);

        for (int i = 0, j = 0; i < formula.length(); ) {
            StringBuilder digit = new StringBuilder();
            //将式子切割
            char c = formula.charAt(i);
            //若c为数字，存入digit
            while (Character.isDigit(c) || c == '/' || c == '\'') {
                digit.append(c);
                i++;
                c = formula.charAt(i);
            }
            //digit里无数字，处理符号
            if (digit.length() == 0) {
                switch (c) {
                    //如果是“(”转化为字符串压入字符栈
                    case '(': {
                        stackO.push(String.valueOf(c));
                        break;
                    }
                    //遇到“)”了，进行计算
                    case ')': {
                        String operator = stackO.pop();
                        //符号栈里有符号时，取操作数运算
                        while (!stackO.isEmpty() && !operator.equals("(")) {
                            String a = stackN.pop();
                            String b = stackN.pop();
                            //后缀表达式变形
                            RPN[j++] = a;
                            RPN[j++] = b;
                            RPN[j++] = operator;
                            String ansString = calculate(b, a, operator);
                            if (ansString == null)
                                return null;
                            //将结果压入栈
                            stackN.push(ansString);
                            //符号指向下一个计算符号
                            operator = stackO.pop();
                        }
                        break;
                    }
                    //遇到了“=”，计算最终结果
                    case '=': {
                        String operator;
                        while (!stackO.isEmpty()) {
                            operator = stackO.pop();
                            String a = stackN.pop();
                            String b = stackN.pop();
                            //后缀表达式变形
                            RPN[j++] = a;
                            RPN[j++] = b;
                            RPN[j++] = operator;
                            String ansString = calculate(b, a, operator);
                            if (ansString == null)
                                return null;
                            stackN.push(ansString);
                        }
                        break;
                    }
                    //其他
                    default: {
                        String operator;
                        while (!stackO.isEmpty()) {
                            operator = stackO.pop();
                            if (hashmap.get(operator) >= hashmap.get(String.valueOf(c))) { //比较优先级
                                String a = stackN.pop();
                                String b = stackN.pop();
                                //后缀表达式变形
                                RPN[j++] = a;
                                RPN[j++] = b;
                                RPN[j++] = operator;
                                String ansString = calculate(b, a, operator);
                                if (ansString == null)
                                    return null;
                                stackN.push(ansString);
                            } else {
                                stackO.push(operator);
                                break;
                            }

                        }
                        stackO.push(String.valueOf(c));  //将符号压入符号栈
                        break;
                    }
                }
            }
            //处理数字，直接压栈
            else {
                stackN.push(digit.toString());
                continue;
            }
            i++;
        }
        //栈顶数字为答案
        RPN[length - 3] = "=";
        RPN[length - 2] = stackN.peek();
        RPN[length - 1] = formula;
        return RPN;
    }


    //计算式子
    private String calculate(String m, String n, String operator) {
        String ansFormula = null;
        char op = operator.charAt(0);
        int[] indexFraction = {m.indexOf('\''), m.indexOf('/'), n.indexOf('\''), n.indexOf('/')};//分数 各部分 切割位置

        //处理分数运算
        if (indexFraction[1] > 0 || indexFraction[3] > 0) {
            int[] denominator = new int[3];
            int[] molecule = new int[3];
            int[] integralPart = new int[3];

            //切割分数
            if (indexFraction[1] > 0) {
                for (int i = 0; i < m.length(); i++) {
                    if (i < indexFraction[0]) {
                        integralPart[0] = Integer.parseInt(integralPart[0] + String.valueOf(m.charAt(i) - '0'));
                    } else if (i > indexFraction[0] && i < indexFraction[1]) {
                        molecule[0] = Integer.parseInt(molecule[0] + String.valueOf(m.charAt(i) - '0'));
                    } else if (i > indexFraction[1]) {
                        denominator[0] = Integer.parseInt(denominator[0] + String.valueOf(m.charAt(i) - '0'));
                    }
                }
            } else {
                integralPart[0] = Integer.parseInt(m);
                denominator[0] = 1;
                molecule[0] = 0;
            }

            if (indexFraction[3] > 0) {
                for (int i = 0; i < n.length(); i++) {
                    if (i < indexFraction[2]) {
                        integralPart[1] = Integer.parseInt(integralPart[1] + String.valueOf(n.charAt(i) - '0'));
                    } else if (i > indexFraction[2] && i < indexFraction[3]) {
                        molecule[1] = Integer.parseInt(molecule[1] + String.valueOf(n.charAt(i) - '0'));
                    } else if (i > indexFraction[3]) {
                        denominator[1] = denominator[1] + n.charAt(i) - '0';
                    }
                }
            } else {
                integralPart[1] = Integer.parseInt(n);
                denominator[1] = 1;
                molecule[1] = 0;
            }

            //分数运算
            switch (op) {
                case '＋': {
                    denominator[2] = denominator[0] * denominator[1];
                    molecule[2] = integralPart[0] * denominator[2] + molecule[0] * denominator[1] + integralPart[1] * denominator[2] + molecule[1] * denominator[0];
                    break;
                }
                case '－': {
                    denominator[2] = denominator[0] * denominator[1];
                    molecule[2] = integralPart[0] * denominator[2] + molecule[0] * denominator[1] - integralPart[1] * denominator[2] - molecule[1] * denominator[0];
                    break;
                }
                default:
                    return null;
            }

            //提取整数部分
            if (molecule[2] >= denominator[2] && molecule[2] > 0) {
                integralPart[2] = molecule[2] / denominator[2];
                molecule[2] = Math.abs(molecule[2] % denominator[2]);
            } else if (molecule[2] < 0) {
                return null;
            }

            //化简分数
            if (molecule[2] != 0) {
                ansFormula = greatFraction(integralPart[2], molecule[2], denominator[2]);
            } else ansFormula = String.valueOf(integralPart[2]);

        } else { //处理整数运算
            int a = Integer.parseInt(m);
            int b = Integer.parseInt(n);

            switch (op) {
                case '＋': {
                    ansFormula = String.valueOf(a + b);
                    break;
                }
                case '－': {
                    if (a - b >= 0)
                        ansFormula = String.valueOf(a - b);
                    else
                        return null;
                    break;
                }
                case '×': {
                    ansFormula = String.valueOf(a * b);
                    break;
                }
                case '÷': {
                    if (b == 0) {
                        return null;
                    } else if (a % b != 0) {
                        ansFormula = a % b + "/" + b;
                        if (a / b > 0) ansFormula = a / b + "'" + ansFormula;
                    } else
                        ansFormula = String.valueOf(a / b);
                    break;
                }
            }
        }
        return ansFormula;
    }

    //化简分数
    private String greatFraction(int integralPart, int molecule, int denominator) {
        String ansFormula;
        int commonFactor = 1;

        //求最大公约数
        Create create = new Create();
        commonFactor = create.commonFactor(denominator, molecule);

        //化简分数
        denominator /= commonFactor;
        molecule /= commonFactor;

        //带分数
        if (integralPart == 0 && molecule > 0) {
            ansFormula = String.valueOf(molecule) + '/' + String.valueOf(denominator);
        } else if (molecule == 0)
            ansFormula = String.valueOf(integralPart);
        else {
            ansFormula = String.valueOf(integralPart) + "'" + String.valueOf(molecule) + '/' + String.valueOf(denominator);
        }
        return ansFormula;
    }
}
