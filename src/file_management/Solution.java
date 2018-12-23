package file_management;

import java.util.ArrayList;
import java.util.HashMap;

public class Solution {
    /**
     * 要求设计一个n个用户的简单二级文件系统，每次用户可保存m个文件，用户在一次运行中只能打开一个文件，对文件必须设置保护措施。要求做到以下几点：
     * 1.可以实现下列几条命令（至少4条）：login 用户登录；dir列文件目录；create创建文件；delete删除文件；open打开文件；close关闭文件；read读文件；write写文件。
     * 2.列目录时要列出文件名、物理地址、保护码和文件长度。
     * 3.源文件可以进行读写保护。
     */

    /**
     * （1）设计一个10个用户的文件系统，每次用户可保存10个文件，一次运行用户可以打开5个文件。
     * （2）程序采用二级文件目录（即设置主目录[MFD]）和用户文件目录（UED）。另外，为打开文件设置了运行文件目录（AFD）。
     * （3）为了便于实现，对文件的读写作了简化，在执行读写命令时，只需改读写指针，并不进行实际的读写操作。
     * （4）算法与框图
     * 	因系统小，文件目录的检索使用了简单的线性搜索。
     * 	文件保护简单使用了三位保护码：允许读写执行、对应位为 1，对应位为0，则表示不允许读写、执行。
     * 	程序中使用的主要设计结构如下：
     * 	主文件目录和用户文件目录（ MFD、UFD）
     * 	打开文件目录（ AFD）（即运行文件目录）
     * （5）编一个通过屏幕选择命令的文件管理系统，每屏要为用户提供足够的选择信息，不需要打入冗长的命令。
     * （6）设计一个树型目录结构的文件系统，其根目录为 root，各分支可以是目录，也可以是文件，最后的叶子都是文件。
     */
    final static int SAVE_LIMIT = 10;
    final static int USER_LIMIT = 10;
    final static int OPEN_LIMIT = 5;
    ArrayList<User> MFD;

    private class User{
        int uid;
        int openLimit = OPEN_LIMIT;
        int saveLimit = SAVE_LIMIT;
        UserFile[] UFD;
        UserFile[] AFD;


        public User(int uid){
            this.uid = uid;
            this.UFD = new UserFile[SAVE_LIMIT];
            this.AFD = new UserFile[OPEN_LIMIT];
        }

        @Override
        public String toString() {
            return Integer.toString(this.uid);
        }
    }

    public class UserFile{
        String name;
        int protect;
        int address;
        int length;

        @Override
        public String toString() {
            return name+"\tprotect status:"+(protect==0?"protected":"unProtected")+"\tlength:"+Integer.toString(length)+"\taddress:"+Integer.toString(address);
        }
    }


    public void login(){

    }

    public void dir(){
        for (User u:MFD) {
            System.out.println(u);
            for (UserFile f:u.UFD){
                System.out.print("\t\t");
                System.out.println(f);
            }
        }
    }

    public void create(){

    }

    public void delete(){

    }

    public void open(){

    }

    public void close(){

    }

    public void read(){

    }

    public void write(){

    }
}
