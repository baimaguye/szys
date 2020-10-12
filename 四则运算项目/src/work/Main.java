package work;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while(true) {
            int n = 10;
            int r = 10;
            String submitPath = null;
            String answersPath = null;

            try {
                // 获取输入指令
                System.out.println("请输入指令：");
                Scanner command = new Scanner(System.in);
                String arr[] = command.nextLine().split("\\s");

                if (arr.length > 1) {
                    for (int i = 0; i < arr.length; i = i + 2) {
                        switch (arr[i]) {
                            case "-n":
                                n = Integer.parseInt(arr[i + 1]);
                                if (n > 10000 || n < 1) {
                                    System.out.println("输入错误，请输入10000以内的数字");
                                    return;
                                }
                                break;
                            case "-r":
                                r = Integer.parseInt(arr[i + 1]);
                                if (r < 1) {
                                    System.out.println("输入错误，请输入大于1的数字");
                                    return;
                                }
                                break;
                            case "-e":
                                submitPath = arr[i + 1];
                                if (submitPath == null) {
                                    System.out.println("输入错误，找不到相应的文件路径");
                                    return;
                                }
                                break;
                            case "-a":
                                answersPath = arr[i + 1];
                                if (answersPath == null) {
                                    System.out.println("输入错误，找不到相应的文件路径");
                                    return;
                                }
                                break;
                            default:
                                System.out.println("指令错误！");
                                break;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("输入的指令错误，请重新输入");
            }

            System.out.println("n: " + n + ", r: " + r);
            FlieW makefile = new FlieW();
            if (submitPath != null && answersPath != null)
                makefile.createGradeFile(submitPath,answersPath);
            else
                makefile.createProblemSet(n,r);
        }
    }
}
