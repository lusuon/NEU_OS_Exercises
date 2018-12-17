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
        PCBs.add(new PCB("P1",null,2,0,0,1,"ready"));
        PCBs.add(new PCB("P2",null,3,0,0,5,"ready"));
        PCBs.add(new PCB("P3",null,1,0,0,3,"ready"));
        PCBs.add(new PCB("P4",null,2,0,0,4,"ready"));
        PCBs.add(new PCB("P5",null,4,0,0,2,"ready"));
        PriorityQueue<PCB> tasks = new PriorityQueue<>();
        for (PCB process:PCBs){
            tasks.add(process);
        }
        int count = 0;

        while(tasks.size()!=0){
            count++;
            PCB current = tasks.poll();



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
        int priority;
        int wait;
        String status;


        public PCB(String name, PCB pointer, int needTime,int CPUTime,int wait , int priority, String status) {
            this.name = name;
            this.pointer = pointer;
            this.needTime = needTime;
            this.CPUTime = CPUTime;
            this.wait = wait;
            this.priority = priority;
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
            return name+"\t"+CPUTime+"\t"+needTime+"\t"+priority+"\t"+status;
        }
    }
}
