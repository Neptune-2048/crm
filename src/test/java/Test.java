
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
//        f5();
//        f6();
//        f7();
//        f8();
//        f9();

        /*
        * 1、接受用户输入的一共大于等于0的整数，把该数字倒着输出
        * 例如：用户输入1234，则输出4321
        *
        * 2、输出三位数之间的水仙花数
        *
        * */
        //text1();
        text2();
    }
    private static void text1(){
        System.out.println("请输入一个大于等于0的整数");
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int num = 0;
        while (x != 0){
            num = num*10;
            num = num + x%10;
            x = x/10;
        }
        System.out.println("输出"+num);

    }
    private static void text2(){
        int a =0 ,b =0,c =0;
        for (int i =100; i < 1000; i++ ){
            int sum=0;
            int d =i;
            a=d%10;
            d=d/10;
            b=d%10;
            d=d/10;
            c=d%10;
            sum= (int) (Math.pow(a,3)+Math.pow(b,3)+Math.pow(c,3));
            if (sum==i){
                System.out.println("100-999中水莲花数为"+sum);
            }
        }
    }


    private static void f9() {
        for (int i =1 ; i<=9 ;i++){
            for (int j=1 ; j<=i ;j++){
                System.out.print(j+"*"+i+"="+i*j);
                System.out.print("    ");
            }
            System.out.println();
        }
    }

    private static void f8() {
        int sum =0;
        for (int i =1;i<=100;i++){
            sum+= i % 2 !=0 ? i : 0;
        }
        System.out.println(sum);
    }

    private static void f7() {
        /*
        * 用户输入年月，输出该年该月的天数
        *
        * */
        System.out.println("请输入年月 ： yyyy-MM");
        Scanner sc = new Scanner(System.in);
        String Date = sc.next();
        String yearstr = Date.substring(0,4);
        String monthstr = Date.substring(5);
        Integer year = Integer.valueOf(yearstr);
        Integer month = Integer.valueOf(monthstr);
        System.out.println(month);
        if (month==1||month==3||month==5||month==7||month==8||month==10||month==12){
            System.out.println("该月31天");
        }else if (month==4||month==6||month==9||month==11){
            System.out.println("该月30天");
        }else {
            if ((year%4==0 && year%100!=0) || (year%400==0)){
                System.out.println("该月29");
            }else {
                System.out.println("该月28天");
            }
        }


    }

    private static void f6() {
        System.out.println("分数区间0到100");
        System.out.println("请输入语文分数");
        Scanner sc = new Scanner(System.in);
        float a = sc.nextFloat();
        System.out.println("请输入数学分数");
        float b = sc.nextFloat();
        if (a<60 && b<60){
            System.out.println("语文数学都需要补考");
        }else if (a>=60 && b<60){
            System.out.println("数学需要补考");
        }else if (a<60 && b>=60 ){
            System.out.println("语文需要补考");
        }else if (a>=60 && b>60){
            System.out.println("都及格了");
        }

    }

    private static void f5() {
        System.out.println("分数区间0到100");
        System.out.println("请输入英语分数");
        Scanner sc = new Scanner(System.in);
        float a = sc.nextFloat();
        if (0<a && a<60){
            System.out.println("D");
        }else if (a<80){
            System.out.println("C");
        }else if (a<90){
            System.out.println("B");
        }else if (a<=100){
            System.out.println("A");
        }else {
            System.out.println("输入不合法");
        }

    }


}
