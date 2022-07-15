package cuit.pymjl.blog;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/13 11:09
 **/
public class Ticket {
    //总票数
    int total;
    //多线程共享变量：余票数量
    int freeNum;
    //已售出票数
    int soldNum;
    //true表示有足够的票出售，false则表示票数不够
    boolean hasTicket;

    int count = 3;    //线程售票退票次数
    int sellNum = 3;    //单次售票数量
    int returnNum = 2;    //单次退票数量

    public Ticket(int number) {
        total = number;
        freeNum = total;    //售票前：总数与余票数相等
        soldNum = 0;            //已售出票数
        hasTicket = (freeNum >= sellNum);    //余票足够
    }

    synchronized public void sellTicket(int num) {
        if (freeNum < num) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notify();
        freeNum = freeNum - num;
        soldNum = soldNum + num;
        System.out.println("售出" + num + "余票" + freeNum);

    }

    synchronized public void returnTicket(int num) {
        if (num > soldNum || freeNum >= 3) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notify();
        freeNum = freeNum + num;
        soldNum = soldNum - num;
        System.out.println("退回" + num + "余票" + freeNum);

    }

}

class sellTicketThread implements Runnable {
    Ticket tickets;

    sellTicketThread(Ticket tickets) {
        this.tickets = tickets;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            tickets.sellTicket(3);
        }

    }
}

class returnTicketThread implements Runnable {
    Ticket tickets;

    returnTicketThread(Ticket tickets) {
        this.tickets = tickets;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            tickets.returnTicket(2);
        }
    }
}
