package cuit.pymjl.blog;

import java.util.Scanner;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/13 11:07
 **/
public class TicketMain {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        //System.out.println("请输入票数：");
        int num = sc.nextInt();        //从键盘读入总票数
        Ticket tickets = new Ticket(num);    //产生票

        new Thread(new sellTicketThread(tickets)).start();//sellTicketThread售票
        new Thread(new returnTicketThread(tickets)).start();//returnTicketThread退票

        Thread.sleep(50);    //休眠等待售票和退票执行完毕
        System.out.println(tickets.freeNum);
        System.exit(1);
    }
}
