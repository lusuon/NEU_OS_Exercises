package main_memory_management;

public class solution{
    int blockLength = 128;

    private class PTEntry{
        int pageNum;
        int symbol;
        int memBlock;
        String location;
        int modify;
    }
    private ArrayList<PTEntry> PT = new ArrayList<>();

    private String convert(int pageN,int unit){
        String absAddr = "*";
        for (PTEntry ptE: PT) {
            if(ptE.pageNum == pageN){
                if(ptE.symbol == 1){
                    adsAddr = Integer.toString(ptE.memBlock)+"\t"+Integer.toString(blockLength+unit); 
                }else{
                    System.out.println("Page not in memory,page number is:"+ptE.pageNum);
                    adsAddr += ptE.pageNum;
                }
                // 如果找到，则从此退出
                return "";
            }
        }
        //没找到
        System.out.println("Page not found.");
        return absAddr;
    }

    private void handle(String convertResult){
        
    }


    public static void main(String args[]){
        
    }

}