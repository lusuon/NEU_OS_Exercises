package main_memory_management;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class MemoryManager {
    int count = 0;//模拟计时
    int blockLength = 128;//内存块长度
    int limit = 5;

    private static ArrayList<PTEntry> PT = new ArrayList<>();//模拟页表

    private static class PTEntry implements Comparable{
        int pageNum;
        int symbol;
        int memBlock;
        String location;
        int modify;
        int time;

        public PTEntry(int pageNum, int symbol, int memBlock, int modify, String location, int time) {
            this.pageNum = pageNum;
            this.symbol = symbol;
            this.memBlock = memBlock;
            this.location = location;
            this.modify = modify;
            this.time = time;
        }

        @Override
        public int compareTo(Object o) {
            PTEntry another =(PTEntry) o;
            return time - another.time;
        }

        @Override
        public String toString() {
            return Integer.toString(pageNum)+"\t"+Integer.toString(symbol)+"\t"+Integer.toString(memBlock)+"\t"+Integer.toString(modify)+"\t"+location+"\t"+Integer.toString(time);
        }
    }

    private String convert(int pageN,int unit){
        String absAddr = "*";
        for (PTEntry e: PT) {
            if(e.pageNum == pageN){
                if(e.symbol == 1){
                    absAddr = Integer.toString(e.memBlock)+"\t"+Integer.toString(blockLength+unit);
                }else{
                    System.out.println("Page not in memory,page number is:"+e.pageNum);
                    absAddr += e.pageNum;
                }
                // 如果找到，则从此退出
                return absAddr;
            }
        }
        //没找到
        System.out.println("Page not found.");
        return absAddr;
    }

    private void handle(String convertResult){
        try{
            int pageNum = Integer.valueOf(convertResult.split("\\*")[1]);
            int running = 0;
            PTEntry newest = null;

            for (PTEntry e:PT) {
                if(e.symbol == 1) running++;
                if(e.pageNum ==pageNum) newest = e;
            }
            if(running >=limit){
                PriorityQueue<PTEntry> queue = new PriorityQueue<>();//用于实现FIFO的队列
                for (PTEntry e:PT){
                    if(e.pageNum!=pageNum) queue.add(e);
                }
                PTEntry oldest = queue.poll();

                //修改页表
                newest.time = count;
                newest.symbol = 1;
                newest.memBlock = oldest.memBlock;
                oldest.memBlock = -1;
                oldest.symbol = 0;

                if(oldest.modify == 1) System.out.println("Move out:page "+oldest.pageNum);
                System.out.println("Move in:page "+pageNum);
            }else{
                newest.symbol = 1;
                allocMem(newest);
            }

        }catch (IndexOutOfBoundsException e){
            System.out.println("Page has been placed in the memory,no interruption occured.");
        }
    }

    private void allocMem(PTEntry e){
        Random random = new Random();
        Boolean duplicate = true;
        while(duplicate){
            duplicate = false;
            e.memBlock =random.nextInt(512);
            for (PTEntry pte:PT) {
                if(pte!=e){
                    if(pte.memBlock==e.memBlock) duplicate =true;
                }
            }
        }
    }

    private void showPT(){
        System.out.println("pageNum\tsymbol\tmemBlock\tmodify\tlocation\ttime");
        for (PTEntry e:PT){
            System.out.println(e);
        }
    }
    public static void main(String args[]){
        MemoryManager mm = new MemoryManager();
        PTEntry p0 = new PTEntry(0,1,5,1,"011",2);
        PTEntry p1 = new PTEntry(1,1,8,0,"012",1);
        PTEntry p2 = new PTEntry(2,1,9,0,"013",0);
        PTEntry p3 = new PTEntry(3,1,1,0,"021",0);
        PTEntry p4 = new PTEntry(4,0,-1,0,"022",0);
        PTEntry p5 = new PTEntry(5,0,-1,0,"023",0);
        PTEntry p6 = new PTEntry(6,0,-1,0,"121",0);

        PT.add(p0);
        PT.add(p1);
        PT.add(p2);
        PT.add(p3);
        PT.add(p4);
        PT.add(p5);
        PT.add(p6);

        mm.showPT();

        System.out.println(mm.convert(0,24));
        mm.handle(mm.convert(4,24));
        System.out.println("Try to handle page3:");
        mm.handle(mm.convert(3,24));

        mm.showPT();
    }
}
