import java.util.Scanner;

public class Test {
    public static Object[][] room = new Object[4][6];//初始化房间

    public static void main(String[] args) {
        System.out.println("欢迎使用***酒店管理系统");
        while (true) {
            System.out.println("功能编号对应功能：【1】表示查看房间列表。【2】表示订房。【3】表示退房。【0】表示退出系统。");
            Scanner sc = new Scanner(System.in);
            int i = sc.nextInt();
            if (i==1) {
                showRoom();
            }else if (i==2) {
                System.out.print("请输入你要订的房间编号!");
                String roomNo = sc.next();
                order(roomNo);
            }else if (i==3) {
                System.out.print("请输出你要退房的房间编号!");
                String roomNo = sc.next();
                exit(roomNo);
            }else if (i==0) {
                return;
            }else {
                System.out.print("请输出正确的功能编号！");
            }
        }
    }

    private static void exit(String roomNo) {
        //退房
        int i = Integer.valueOf(roomNo.substring(0,1));
        int j = Integer.valueOf(roomNo.substring(1));
        if (room[i][j]!=null){
            System.out.println("退房成功，送你5元优惠卷，记得下次再来噢");
            room[i][j]=null;
        }else {
            System.out.println("房间没有被预订，你不能退，你得先给钱，然后才能退");
        }
    }

    private static void order(String roomNo) {
        //订房
        int i = Integer.valueOf(roomNo.substring(0,1));
        int j = Integer.valueOf(roomNo.substring(1));
        if (room[i][j]!=null){
            System.out.println("房间被预订了");
        }else {
            room[i][j] = new Object();
            System.out.println("预定成功");
        }
    }

    private static void showRoom() {
        //遍历房间
        for (int i = 0; i <room.length ; i++) {
            for (int j = 0; j < room[i].length; j++) {
                if (room[i][j]!=null){
                    System.out.print(i+""+j+"间已经有客人入住了\t\t");
                }else {
                    System.out.print(i+""+j+"间可以入住\t\t");
                }
            }
            System.out.println();
        }
    }


}
