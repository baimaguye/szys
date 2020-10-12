package work;

import java.util.ArrayList;
import java.util.Arrays;

public class Check {
    private ArrayList<String> returnList = new ArrayList<>();
    private ArrayList<String> txtList = new ArrayList<>();
    private ArrayList<String> ansList = new ArrayList<>();
    private ArrayList<String[]> ansFoList = new ArrayList<>();

    //生成暂存习题和答案
    public ArrayList generate(int n,int r) {
        Create create = new Create();
        //生成n条不重复的式子
        for(int i=0;i<n;){
            //随机生成式子
            String[] ansFormula = create.createFormula(r);
            //判断生成的式子是否重复
            if (ansFormula!=null) {
                if (!ifRepeat(ansFormula)) {
                    System.out.println((i+1)+":"+ansFormula[ansFormula.length-1]);
                    i++;
                }
            }
        }

        //把式子及运算结果添加到returnList
        for (int i = 0;i < 2*n;i++) {
            if(i < n) {
                returnList.add(txtList.get(i));
            } else {
                returnList.add(ansList.get(i-n));
            }
        }
        return returnList;
    }

    //判断式子是否重复
    private boolean ifRepeat(String[] ansFormula) {
        String formula = ansFormula[ansFormula.length-1];
        String[] rPNotation = new String[ansFormula.length-1];
        System.arraycopy(ansFormula, 0, rPNotation, 0, ansFormula.length-1);
        boolean ifRepeat = false;

        for (String[] ansFo: ansFoList) {
            if (Arrays.equals(ansFo,rPNotation)) {
                ifRepeat = true;
            } else if (ansFo.length == rPNotation.length && ansFo[ansFo.length-1].equals(rPNotation[rPNotation.length-1])){//若运算结果及长度一致，则式子可能重复，进一步比较
                int j=0;
                for (j=0;j<rPNotation.length-2;) {
                    boolean opRight = ansFo[j+2].equals("＋")||ansFo[j+2].equals("×");
                    boolean exRight = ansFo[j].equals(rPNotation[j + 1]) && ansFo[j + 1].equals(rPNotation[j]) && ansFo[j + 2].equals(rPNotation[j + 2]);
                    boolean copRight = ansFo[j].equals(rPNotation[j]) && ansFo[j + 1].equals(rPNotation[j + 1]) && ansFo[j + 2].equals(rPNotation[j + 2]);
                    //运算符前后两个操作数交换比较
                    if (exRight&&opRight) {
                        j = j + 3;
                    } else if (copRight) {
                        j = j + 3;
                    } else {
                        break;
                    }
                }
                if (j == rPNotation.length-2) {
                    ifRepeat = true;
                    break;
                }
            }
        }

        if (!ifRepeat) {
            this.txtList.add(formula);
            this.ansList.add(rPNotation[rPNotation.length-1]);
            this.ansFoList.add(rPNotation);
        }
        return ifRepeat;
    }
}