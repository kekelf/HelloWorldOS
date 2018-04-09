package com.study4;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 
 * @author caoxuekun
 * @date 2018年4月9日 下午7:50:39
 * @function:
 * 			kernel.asm是内核源码
 * 			boot.asm是加载器源码，将上面的二者自己手动编译成kernel.bat和boot.bat(如何将nam文件编译成bat文件这里不再赘述)
 * 			思路：
 * 				将内核加载器读入到磁盘第0面，第0柱面，第1个扇区，
 * 				将内核写入到磁盘第0面，第1柱面，第2个扇区
 * 				参照书本的笔记的汇编源码可以知道，先加载内核加载器，内核加载器加载完成后回去都boot.asm中
 * 							“ mov BX, LOAD_ADDR ; ES:BX 数据存储缓冲区”
 * 				而LOAD_ADDR代码对应的是0X8000，刚好是字节写入内核代码的地址便将内核读入到了内存
 */
public class OperatingSystem {
   
    private Floppy floppyDisk = new Floppy();
    private int  MAX_SECTOR_NUM = 18;
    
    
    private void writeFileToFloppy(String fileName, boolean bootable, int cylinder,int beginSec) {
    	File file = new File(fileName);
    	InputStream in = null;
    	
    	try {
    		in = new FileInputStream(file);
    		byte[] buf = new byte[512];
    		if (bootable) {
    			buf[510] = 0x55;
        		buf[511] = (byte) 0xaa;	
    		}
    		
    		while (in.read(buf) > 0) {
    			//将内核加载器读入到磁盘第0面，第0柱面，第1个扇区
    			floppyDisk.writeFloppy(Floppy.MAGNETIC_HEAD.MAGNETIC_HEAD_0, cylinder, beginSec, buf);
    			beginSec++;
    			
    			if (beginSec > MAX_SECTOR_NUM) {
    				beginSec = 1;
    				cylinder++;
    			}
    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    		return;
    	}
    }
    
    public OperatingSystem(String s) {
    	writeFileToFloppy(s, true, 0, 1);
    }
    
    
    public void makeFllopy()   {
    	writeFileToFloppy("kernel.bat", false, 1, 2);
    	
    	floppyDisk.makeFloppy("system.img");
    }
    
   

    public static void main(String[] args) {
    	OperatingSystem op = new OperatingSystem("study4/boot.bat");
    	op.makeFllopy();
    }
}
