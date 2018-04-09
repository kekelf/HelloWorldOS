package com.study1;

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
 * @date 2018年4月6日 下午3:40:05
 * @function:视频的第一节
 * 				该类的大致思路：
 * 					1.初始化该类的一个实例的时候传入一个字符串，用于内核启动的时候的现实
 * 					2.遍历imgContent数组，这段数据的数据为何是下面，暂时不做考虑，添加到集合imgByteToWrite
 * 					3.输入两个换行符，然后将初始化的字符串追加到imgByteToWrite
 * 					4.查看到0x1fe还剩多少长度，把不足的不为0
 * 					5.头512字节的最后两个字节必须为55aa，否则虚拟机就不会把软盘当作系统加载
 * 					6.其余的用0补充，到长度为0x168000
 * 					7.将imgByteToWrite中的数据写入软盘文件
 */
public class HelloWorldOS {
	/*
	 * 这段二进制数据将是我们HelloWorld OS 的内核，它的作用是让BIOS将其加载到地址0x8000,然后调用BIOS中断在屏幕上打印出一行字符串
	 */
    private int[] imgContent = new int[]{
        0xeb,0x4e,0x90,0x48,0x45,0x4c,0x4c,0x4f,0x49,0x50,0x4c,0x00,0x02,0x01,0x01,0x00,0x02,0xe0,
        0x00,0x40,0x0b,0xf0,0x09,0x00,0x12,0x00,0x02,0x00,0x00,0x00,0x00,0x00,0x40,0x0b,0x00,0x00,0x00,0x00,0x29,
        0xff,0xff,0xff,0xff,0x48,0x45,0x4c,0x4c,0x4f,0x2d,0x4f,0x53,0x20,0x20,0x20,0x46,0x41,0x54,0x31,0x32,
        0x20,0x20,0x20,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0xb8,0x00,0x00,0x8e,
        0xd0,0xbc,0x00,0x7c,0x8e,0xd8,0x8e,0xc0,0xbe,0x74,0x7c,0x8a,
        0x04,0x83,0xc6,0x01,0x3c,0x00,0x74,0x09,0xb4,0x0e,0xbb,0x0f,0x00,0xcd,0x10,0xeb,0xee,0xf4,0xeb,0xfd
    };

    private ArrayList<Integer> imgByteToWrite = new ArrayList<Integer>();



    public HelloWorldOS(String s) {
        for (int i = 0; i < imgContent.length; i++) {
            imgByteToWrite.add(imgContent[i]);
        }
        
        //0a是换行符，可以在自己书本的笔记的第一节的hellos_1_2.asm中查找的到
        imgByteToWrite.add(0x0a);
        imgByteToWrite.add(0x0a);
        for (int j = 0; j < s.length(); j++) {
            imgByteToWrite.add((int)s.charAt(j));
        }
        imgByteToWrite.add(0x0a);

        int len = 0x1fe;
        int curSize = imgByteToWrite.size();
        for (int k = 0; k < len - curSize; k++) {
            imgByteToWrite.add(0);
        }

        /*
         * 要想让机器将软盘的头512字节当做操作系统的内核加载到内存，头512字节的最后两个字节必须是55,aa
         * 如果不是55，aa虚拟机就不会将img当作操作系统加载
         * 头512字节是有内容的，其余都设为0来填充
         */
        imgByteToWrite.add(0x55);
        imgByteToWrite.add(0xaa);
        //这里为何不是0 疑问
        imgByteToWrite.add(0xf0);
        imgByteToWrite.add(0xff);
        imgByteToWrite.add(0xff);

        len = 0x168000; //
        curSize = imgByteToWrite.size();
        for (int l = 0; l < len - curSize; l++) {
            imgByteToWrite.add(0);
        }

    }
    
    /**
     * 构建一个虚拟软盘，将imgByteToWrite写入system.img文件中
     * 
     */
    public void makeFllopy()   {
        try {
        	/*
        	 * 在磁盘上创建一个1474560字节的二进制文件，将imgContent的内容写入该文件的头512字节，这个二进制文件将作为一个1.5M的虚拟软盘用于当做
        	 * 虚拟机的启动软盘
        	 */
            DataOutputStream out = new DataOutputStream(new FileOutputStream("system.img"));
            for (int i = 0; i < imgByteToWrite.size(); i++) {
                out.writeByte(imgByteToWrite.get(i).byteValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
    	//屏幕上要显示的字符串，只能是英文
    	HelloWorldOS op = new HelloWorldOS("it can not use english , bad guy , but it is my first operating system");
        op.makeFllopy();
    }
}
