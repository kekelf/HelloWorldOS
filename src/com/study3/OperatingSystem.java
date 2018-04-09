package com.study3;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author caoxuekun
 * @date 2018年4月9日 下午8:07:51
 * @function:
 * 		1.这一节主要是先了解软盘的结构，为java去模拟软盘的物理结构做准备，
 * 		2.将对应内核二进制文件写入到模拟软盘对应的物理结构中的位置
 * 		3.模拟软盘的读取功能，为内核加载器做准备(这里的读取软盘的文件并不是利用java来读取，
 * 									其实还是用会变语言来读取，
 * 									只不过是用java将字符串写到磁盘第0面，第1柱面，第2个扇区， 
 * 									再用会变来读取，与下一节唯一不同的地方就是，下一节不是字符串，
 *									而是内核代码)
 */
public class OperatingSystem {
   
    private Floppy floppyDisk = new Floppy();
    
    
    /**
     * 该方法是直接将boot_helloworld.asm编译成的boot.bat文件写入到system.img形成内核，故输出就是boot_helloworld.asm设定的hello world
     */
    private void writeFileToFloppy(String fileName) {
    	File file = new File(fileName);
    	InputStream in = null;
    	
    	try {
    		in = new FileInputStream(file);
    		byte[] buf = new byte[512];
    		//头512字节的最后两个字节必须为55aa
    		buf[510] = 0x55;
    		buf[511] = (byte) 0xaa;
    		if (in.read(buf) != -1) {
    			//将内核读入到磁盘第0面，第0柱面，第1个扇区
    			floppyDisk.writeFloppy(Floppy.MAGNETIC_HEAD.MAGNETIC_HEAD_0, 0, 1, buf);
    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    		return;
    	}
    }
    
    public OperatingSystem(String s) {
    	writeFileToFloppy(s);
    }
    
    public void makeFllopy()   {
    	//下面注释的是虚拟软盘的读的功能，从读取下面字符串s，现实到屏幕上   需要利用boot_readstring_from_sector.asm文件，将其编译成boot.bat文件在启动java生成system.img文件
    	
    	/*
    	//创建系统启动时候显示的字符串
    	String s = "This is a text from cylinder 1 and sector 2";
    	//将内核读入到磁盘第0面，第1柱面，第2个扇区 
    	floppyDisk.writeFloppy(Floppy.MAGNETIC_HEAD.MAGNETIC_HEAD_0, 1, 2, s.getBytes());
    	*/
    	floppyDisk.makeFloppy("system.img");
    }
    
   

    public static void main(String[] args) {
    	//将准备好的boot.bat中的数据写入软盘，对应软盘的位置跟代码
    	OperatingSystem op = new OperatingSystem("boot.bat");
    	op.makeFllopy();
    }
}
