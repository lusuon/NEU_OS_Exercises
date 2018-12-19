package CPU_schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import com.sun.tools.javac.code.Attribute.Array;

public class Solution_slice {
    /**
     * Slice solution
     */

    public static void main(String[] args){
        PCB P5=new PCB("P5",null,2,0,0,1,"ready");
        PCB P4=new PCB("P4",P5,2,0,0,1,"ready");
        PCB P3=new PCB("P3",P4,2,0,0,1,"ready");
        PCB P2=new PCB("P2",P3,2,0,0,1,"ready");
        PCB P1=new PCB("P1",P2,2,0,0,1,"ready");
        P5.pointer = P1;

        PCB ready = P1;

        while(ready.pointer!=ready){
            if(ready.needTime!=0){
                ready.needTime--;
            ready.CPUTime++;
        
            }
        }

        
        for (PCB process:PCBs){
            tasks.add(process);
        }
        

        while(tasks.isEmpty()){
            PCB[] order_PCBs = new PCB[tasks.size()]; 
            PCB working = null;
            current = working;
            int count = 0;
            //数组内为此时为已排序好的
            while(!tasks.isEmpty()){
                order_PCBs[count] = tasks.poll();
                if(count>1) order_PCBs[count-1].pointer = order_PCBs[count];
                else working = order_PCBs[0];
                count++;
            }
            order_PCBs[count-1].pointer = order_PCBs[0];
            //完成构造循环队列
            
            while(first.pointer!=first){
                //遍历链表，确定first
                if()
                first.needTime--;
                first.CPUTime++;
                    
                
            }

            
            
            
            current.status="working";
            
            
            current.needTime--;
            current.CPUTime++;




            for (PCB process: tasks) {
                if (process.priority == current.priority){
                    process.CPUTime++;
                    process.wait++;
                }
            }
            current.priority--;


            if(current.needTime!=0){
                tasks.add(current);
            }else{
                current.status = "finish";
            }

            System.out.println("CPU TIME:"+count);
            System.out.print("NAME\tCPUTIME\tNEEDTIME\tPRIORITY\tSTATE\n");

            for (PCB process:PCBs) {
                System.out.println(process.toString());
            }
            if (!current.status.equals("finish")) current.status="ready";

            //rebuild the heap
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
            System.out.println(process.wait);
        }

    }


    public static class PCB implements Comparable{
        String name;
        PCB pointer;
        int needTime;
        int CPUTime;
        int wait;
        String status;


        public PCB(String name, PCB pointer, int needTime,int CPUTime,int wait , int priority, String status) {
            this.name = name;
            this.pointer = pointer;
            this.needTime = needTime;
            this.CPUTime = CPUTime;
            this.wait = wait;
            this.status = status;
        }



        @Override
        public int compareTo(Object o) {
            PCB another = (PCB)o;
            if (another.priority == this.priority){
                return Integer.valueOf(another.name.split("P")[1])-Integer.valueOf(name.split("P")[1]);
            }else{
                return another.priority - this.priority;
            }
        }

        @Override
        public String toString() {
            return name+"\t"+CPUTime+"\t"+needTime+"\t"+status;
        }
    }
}
