package com.study5;

/**
 * @author caoxuekun 
 * @date 2018年4月9日 下午8:31:06
 * @function:
 * 	GDT下面的8个byte对应会变语言kernel.asm文件中的Descriptor：
 * 			前两个字节( BYTE1| BYTE0 )来表示段长度，因为段长度是20为，两个字节是16为所以还差4为，这个段的高4为在byte6中低4位 --对应kernel.asm中的段界限
 * 			基地址是32为（ BYTE2 | BYTE3 | BYTE4 BYTE7 ） -------对应kernel.asm中的段基址
 * 			BYTE5是用来表示属性 ---对应kernel.asm中的属性
 * 这个类是为了帮助自己理解汇编语言的 Descriptor，没有什么用处
 */			

/*|BYTE7| BYTE6 | BYTE5 | BYTE4 | BYTE3 | BYTE2 | BYTE1 | BYTE0 */
public class GDT {
    byte[] segmentLength_low = new byte[2]; //BYTE1| BYTE0
    
    byte[] baseAddressLow = new byte[3]; //BYTE2 | BYTE3 | BYTE4
    
    byte[] attribute = new byte[2]; // BYTE5 | BYTE6
                                    // BYTE6 的头4个bit 用于segment length, 于是段长度的字长为20bit
    
    byte addressHigh = 0 ; //BYTE7
    
    //实模式下的寻找方式
    //段寄存器 * 16 + 偏移(16bit)
    
}
