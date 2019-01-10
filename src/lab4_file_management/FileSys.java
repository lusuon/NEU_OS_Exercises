package file_management;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class FileSys {
    /**
     * 要求设计一个n个用户的简单二级文件系统，每次用户可保存m个文件，用户在一次运行中只能打开一个文件，对文件必须设置保护措施。要求做到以下几点：
     * 1.可以实现下列几条命令（至少4条）：login 用户登录；dir列文件目录；create创建文件；delete删除文件；open打开文件；close关闭文件；read读文件；write写文件。
     * 2.列目录时要列出文件名、物理地址、保护码和文件长度。
     * 3.源文件可以进行读写保护。
     */

    public static void main(String args[]) throws Exception {
        FileSys solution = new FileSys();
        User test0 = new User("test0");
        User test1 = new User("test1");

        MFD.put(test0.uid,test0);
        MFD.put(test1.uid,test1);

        solution.login("test0");

        System.out.println("Create some files.");
        solution.create("test.txt","0000",1);
        solution.create("test1.txt","0001",2);
        solution.dir();

        System.out.println("Deleting test1.txt.");
        solution.delete("test1.txt");
        solution.dir();

        System.out.println("Open test.txt and read/write it,then close.");
        solution.open("test.txt");
        solution.read("test.txt");
        solution.write("test.txt");
        solution.close("test.txt");

    }
    /**
     * （1）设计一个10个用户的文件系统，每次用户可保存10个文件，一次运行用户可以打开5个文件。
     * （2）程序采用二级文件目录（即设置主目录[MFD]）和用户文件目录（UED）。另外，为打开文件设置了运行文件目录（AFD）。
     * （3）为了便于实现，对文件的读写作了简化，在执行读写命令时，只需改读写指针，并不进行实际的读写操作。
     * （4）算法与框图
     * 	因系统小，文件目录的检索使用了简单的线性搜索。
     * 	文件保护简单使用了三位保护码：允许读写执行、对应位为 1，对应位为0，则表示不允许读写、执行。
     * 	程序中使用的主要设计结构如下：
     * 	主文件目录和用户文件目录（MFD、UFD）
     * 	打开文件目录（ AFD）（即运行文件目录）
     * （5）编一个通过屏幕选择命令的文件管理系统，每屏要为用户提供足够的选择信息，不需要打入冗长的命令。
     * （6）设计一个树型目录结构的文件系统，其根目录为 root，各分支可以是目录，也可以是文件，最后的叶子都是文件。
     */
    final static int SAVE_LIMIT = 10;
    final static int USER_LIMIT = 10;
    final static int OPEN_LIMIT = 5;
    private static HashMap<String,User> MFD = new HashMap<>();
    User currentUser = null;

    private static class User{
        String uid;
        HashMap<String,UserFile> UFD = new HashMap<>();//用户文件目录
        HashMap<String,UserFile> AFD = new HashMap<>(); //用户打开文件目录
        


        public User(String uid){
            this.uid = uid;
            this.UFD = new HashMap<>();
            this.AFD = new HashMap<>();
        }

        @Override
        public String toString() {
            return "User\t"+uid;
        }
    }

    public class UserFile{
        String name;
        int protect;
        String address;
        int length;

        public UserFile(String name, String address, int length) {
            this.name = name;
            this.protect = 0;
            this.address = address;
            this.length = length;
        }



        @Override
        public String toString() {
            return name+"\tprotect status:"+(protect==0?"protected":"unProtected")+"\tlength:"+Integer.toString(length)+"\taddress:"+address;
        }
    }



    public void login(String name){
        currentUser = MFD.get(name);
        if(currentUser == null) throw new NoSuchElementException("The user does not existed.");
        System.out.println("Login successfully.Welcome"+currentUser);
    }
    public boolean isLogin(){return currentUser!=null;}


    public void dir(){
        for (Map.Entry<String,User> userEntry:MFD.entrySet()) {
            System.out.println(userEntry.getValue());
            for (Map.Entry<String,UserFile> fileEntry:userEntry.getValue().UFD.entrySet()){
                System.out.print("\t\t");
                System.out.println("\t\t"+fileEntry.getValue());
            }
        }
    }

    public void create(String name,String address,int length) throws Exception {
        if(!isLogin()) throw new NoSuchElementException("Not login");

        HashMap<String,UserFile> currentUFD = currentUser.UFD;
        UserFile file = new UserFile(name,address,length);
        
        if(currentUFD.size()<SAVE_LIMIT){
            currentUFD.put(name,file);
            System.out.println(name+" added");
        }else 
            throw new Exception("User dir full");
    }

    public void delete(String name) throws Exception {
        if(!isLogin()) throw new NoSuchElementException("Not login");
        HashMap<String,UserFile> currentUFD = currentUser.UFD;
        

        if(!currentUFD.isEmpty()){
            if(currentUFD.get(name)==null) throw new NoSuchElementException();
            currentUFD.remove(name);
            System.out.println(name+" removed");
        }else throw new Exception("User dir empty.");
    }

    public UserFile open(String name) throws Exception {
        if(!isLogin()) throw new Exception("Not login");
        HashMap<String,UserFile> currentUFD = currentUser.UFD;
        HashMap<String,UserFile> currentAFD = currentUser.AFD;
        UserFile operating = currentUFD.get(name);

        if(currentAFD.get(name)==null && operating!=null) {
            currentAFD.put(name, operating);
            operating.protect = 1;
        }else if(operating==null){
            throw new Exception("File not found..");
        }else{
            throw new Exception("The file has been opened.");
        }        
        
        return operating;
    }

    public void close(String name) throws Exception {
        if(!isLogin()) throw new NoSuchElementException("Not login");
        HashMap<String,UserFile> currentUFD = currentUser.UFD;
        HashMap<String,UserFile> currentAFD = currentUser.AFD;
        UserFile operating = currentUFD.get(name);

        if(currentAFD.get(name)!=null && operating!=null) {
            currentAFD.remove(name);
            operating.protect = 0;
        }else if(operating==null){
            throw new Exception("File not found..");
        }else{
            throw new Exception("The file has not been opened.");
        }
    }

    public void read(String name) throws Exception {
        if(!isLogin()) throw new NoSuchElementException("Not login");
        HashMap<String,UserFile> currentAFD = currentUser.AFD;
        UserFile operating = currentAFD.get(name);

        if(operating!=null && operating.protect== 1) {
            operating.protect = 0;
            System.out.print("Reading:\n\t");
            System.out.println(operating);
            operating.protect = 1;
        }else if(operating==null){
            throw new Exception("File not opened.");
        }else if(operating.protect == 0){
            throw new Exception("The file is protected.");
        }
        
    }

    public void write(String name) throws Exception {
        if(!isLogin()) throw new NoSuchElementException("Not login");
        HashMap<String,UserFile> currentAFD = currentUser.AFD;
        UserFile operating = currentAFD.get(name);


        if(operating!=null && operating.protect== 1) {
            operating.protect = 0;
            System.out.print("Writing:\n\t");
            System.out.println(operating);
            operating.protect = 1;
        }else if(operating==null){
            throw new Exception("File not opened.");
        }else if(operating.protect == 0){
            throw new Exception("The file is protected.");
        }
    }
}
