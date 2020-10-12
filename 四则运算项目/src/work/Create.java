package work;

import java.util.Random;

public class Create {

    // 生成算式
    public String[] createFormula(int r){
        Random random = new Random();
        String[] operator = {"＋","－","×","÷","＝"};

        String[] totalO = new String[1 + random.nextInt(3)];//运算符
        String[] totalF = new String[totalO.length+1];//操作数
        String formula = new String();
        //是否有分数
        Boolean hasFraction = false;

        //随机产生操作数：
        for (int i = 0;i < totalF.length;i++) {

            // 随机确定生成整数或分数
            int fractionOrNot = random.nextInt(2);
            if (fractionOrNot == 0) { //生成整数
                int integralPart = random.nextInt(r+1);
                totalF[i] = String.valueOf(integralPart);
            } else { //生成分数
                int denominator = 1+random.nextInt(r);
                int molecule = random.nextInt(denominator);
                int integralPart = random.nextInt(r+1);

                //化简分数
                if (molecule != 0) {
                    int commonFactor = commonFactor(denominator, molecule);
                    denominator /= commonFactor;
                    molecule /= commonFactor;
                }

                //输出最简分数
                if (integralPart == 0 && molecule > 0) {
                    totalF[i] = molecule + "/" + denominator;
                    hasFraction = true;
                }
                else if (molecule == 0)
                    totalF[i] = String.valueOf(integralPart);
                else {
                    totalF[i] = integralPart + "'" + molecule + "/" + denominator;
                    hasFraction = true;
                }
            }
        }

        //随机生成运算符：
        for (int i = 0;i < totalO.length;i++) {
            if (hasFraction)
                totalO[i] = operator[random.nextInt(2)];
            else
                totalO[i] = operator[random.nextInt(4)];
        }

        int choose = totalF.length;
        if (totalF.length != 2 )
            choose = random.nextInt(totalF.length);

        //生成式子
        for (int i = 0;i < totalF.length;i++) {
            if (i == choose && choose < totalO.length) {
                formula = formula + "(" + totalF[i] + totalO[i] ;
            } else if (i == totalF.length - 1 && i == choose+1 && choose<totalO.length) {
                formula = formula + totalF[i] + ")" + "=";
            } else if (i == choose + 1 && choose < totalO.length) {
                formula = formula + totalF[i] + ")" + totalO[i];
            } else if (i == totalF.length - 1) {
                formula = formula + totalF[i] + "=";
            } else {
                formula = formula + totalF[i] + totalO[i];
            }
        }

        Calculate checkAns = new Calculate();
        String[] ansFormula = checkAns.checkout(formula,3*totalO.length+2+1);

        if (ansFormula!=null)
            return ansFormula;
        return null;
    }

    //求最大公因数
    public int commonFactor(int a,int b) {
        while(true)
        {
            if(a%b == 0)return b;
            int temp = b;
            b = a%b;
            a = temp;
        }
    }

}
