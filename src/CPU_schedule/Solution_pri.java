package CPU_schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Solution_pri {
    /**
     * Priority solution
     */

    public static void main(String[] args){
        ArrayList<PCB> PCBs = new ArrayList<>();
        PCBs.add(new PCB("P1",null,2,0,1,"new"));
        PCBs.add(new PCB("P2",null,4,0,5,"new"));
        PCBs.add(new PCB("P3",null,5,0,3,"new"));
        PCBs.add(new PCB("P4",null,7,0,4,"new"));
        PCBs.add(new PCB("P5",null,8,0,2,"new"));

        PriorityQueue<PCB> tasks = new PriorityQueue<>();
        for (PCB process:PCBs){
            tasks.add(process);
        }
        int count = 0;//计时器

        while(tasks.size()!=0){
            count++;
            //取出优先级最高进程，模拟运行
            PCB current = tasks.poll();
            current.status="working";
            current.needTime--;
            current.CPUTime++;
            for (PCB process: PCBs) {
                //已启动的程序的计时数+1
                if (process.status.equals("ready")){
                    process.CPUTime++;
                }
            }
            //进程老化，优先级降低
            current.priority--;
            //如果进程未完成，重新放入队列
            if(current.needTime!=0){
                current.status = "ready";
                tasks.add(current);
            }else{
                current.status = "finish";
                current.finish = count;
            }

            System.out.println("CPU TIME:"+count);
            System.out.print("NAME\tCPUTIME\tNEEDTIME\tPRIORITY\tSTATE\n");

            for (PCB process:PCBs) {
                System.out.println(process.toString());
            }
            if (!current.status.equals("finish")) current.status="ready";

            //重新建堆
            ArrayList<PCB> temp = new ArrayList<>();
            while(!tasks.isEmpty()){
                PCB pop = tasks.poll();
                temp.add(pop);
            }
            for (PCB process:temp) {
                tasks.add(process);
            }

        }
        System.out.print("NAME\tRoundTime\tWaitingTime\n");
        for (PCB process:PCBs) {
            System.out.print(process.name+"\t");
            System.out.print(process.finish+"\t");
            System.out.println(process.finish-process.burst);
        }

    }


    public static class PCB implements Comparable{
        String name;
        PCB pointer;
        int needTime;
        int CPUTime;
        int priority;
        int burst;
        int finish;
        String status;


        public PCB(String name, PCB pointer, int needTime,int CPUTime, int priority, String status) {
            this.name = name;
            this.pointer = pointer;
            this.needTime = needTime;
            this.CPUTime = CPUTime;
            this.priority = priority;
            this.burst = needTime;
            this.status = status;
        }



        @Override
        public int compareTo(Object o) {
            PCB another = (PCB)o;
            if (another.priority == this.priority){
                return CPUTime-another.CPUTime;
            }else{
                return another.priority - this.priority;
            }
        }

        @Override
        public String toString() {
            return name+"\t"+CPUTime+"\t"+needTime+"\t"+priority+"\t"+status;
        }
    }
}
