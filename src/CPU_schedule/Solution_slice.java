package CPU_schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class Solution_slice {
    /**
     * Slice solution
     */

    public static void main(String[] args) {
        PCB P5 = new PCB("P5", null, 2, 0, 0, "ready");
        PCB P4 = new PCB("P4", P5, 2, 0, 0, "ready");
        PCB P3 = new PCB("P3", P4, 2, 0, 0, "ready");
        PCB P2 = new PCB("P2", P3, 2, 0, 0, "ready");
        PCB P1 = new PCB("P1", P2, 2, 0, 0, "ready");
        P5.pointer = P1;
        PCB ready = P1;
        PCB finish = null;
        PCB current = ready;
        int size = 5;


        while(size!=1){


            if(current.needTime != 0){
                current.status="working";
                current.needTime--;
                current.CPUTime++;
                current.status="ready";
            }else{
                current.status="finish";
                size--;
            }
            if(current.status.equals("finish")){
                System.out.println("Encounter a finished process.");
                PCB next = current.pointer.pointer;
                if(finish==null) {
                    finish = current.pointer;
                    finish.pointer =null;
                }else{
                    current.pointer.pointer = finish;
                    finish = current.pointer;
                }
                current.pointer = next;
            }
            current = current.pointer;

        }



        while(finish!=null){
            System.out.print(finish.name+"\t");
            finish = finish.pointer;
        }
    }


    public static class PCB {
        String name;
        PCB pointer;
        int needTime;
        int CPUTime;
        int wait;
        String status;


        public PCB(String name, PCB pointer, int needTime,int CPUTime,int wait, String status) {
            this.name = name;
            this.pointer = pointer;
            this.needTime = needTime;
            this.CPUTime = CPUTime;
            this.wait = wait;
            this.status = status;
        }




        @Override
        public String toString() {
            return name+"\t"+CPUTime+"\t"+needTime+"\t"+status;
        }
    }
}
