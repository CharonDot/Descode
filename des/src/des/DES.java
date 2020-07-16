package des;

import java.util.*;

public class DES {

        public static void main(String[] args) {
                int[][] ip= {{58,50,42,34,26,18,10,2},{60,52,44,36,28,20,12,4}, 
                                {62,54,46,38,30,22,14,6},{64,56,48,40,32,24,16,8},
                                {57,49,41,33,25,17,9,1},{59,51,43,35,27,19,11,3},
                                {61,53,45,37,29,21,13,5},{63,55,47,39,31,23,15,7}};     //初始转置
                int[][] ip_1= {{40,8,48,16,56,24,64,32},
                                       {39,7,47,15,55,23,63,31},
                                       {38,6,46,14,54,22,62,30},
                                       {37,5,45,13,53,21,61,29},
                                       {36,4,44,12,52,20,60,28},
                                       {35,3,43,11,51,19,59,27},
                                       {34,2,42,10,50,18,58,26},
                                       {33,1,41,9,49,17,57,25}};
                menu();
                Scanner y_in=new Scanner(System.in);
                int y=y_in.nextInt();
                while(y!=1&&y!=2)
                {
                        System.out.println("输入错误，请重新输入");
                        menu();
                        y=y_in.nextInt();
                }
                char[] mingwen=new char[8];
                int[][] mingwen2=new int[8][8];
                int[][] mingwenip=new int[8][8];
                int[][] miwen2=new int[8][8];
                int[][] miwenip=new int[8][8];
                int[] miwen1=new int[66];
                int[] miwen6=new int[11];
                char[] miwen=new char[12]; 
                char[] miyao=new char[8];
                int[][][] L=new int[17][8][4];
                int[][][] R=new int[17][8][4];
                
                if(y==1)                                                        //加密
                {
                        
                        System.out.println("请输入明文数组(8个字节)：");
                        String s=new Scanner(System.in).nextLine();
                        System.out.println();
                        for(int i=0;i<8;i++)
                        {
                                mingwen[i]=s.charAt(i);
                        }
                        System.out.println("请输入密钥（8个字节）：");
                        String sk=new Scanner(System.in).nextLine();
                        System.out.println();
                        for(int i=0;i<8;i++)
                        {
                                miyao[i]=sk.charAt(i);
                        }
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<8;j++)
                                {
                                        mingwen2[i][j]=(mingwen[i]>>(7-j))&1;                   //明文转变为二进制
                                        //System.out.print(mingwen2[i][j]);
                                }
                                //System.out.println();
                        }
                        
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<8;j++)
                                {
                                        mingwenip[i][j]=mingwen2[(ip[i][j]-1)/8][(ip[i][j]-1)%8];
                                        //System.out.print(mingwenip[i][j]);
                                }
                                //System.out.println();
                        }
                        
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<4;j++)
                                {
                                        L[0][i][j]=mingwenip[i][j];
                                        R[0][i][j]=mingwenip[i][j+4];                        //定义三维数组L和R，其中第一位表示第i轮迭代
                                }
                        }
                        for(int i=0;i<16;i++)
                        {
                                for(int j=0;j<8;j++)
                                {
                                        for(int k=0;k<4;k++)
                                        {
                                                L[i+1][j][k]=R[i][j][k];
                                        }
                                }
                                R[i+1]=f(L[i],R[i],key(i+1,miyao));
                        }
                        int[][] Lfinal=R[16];
                    int[][] Rfinal=L[16];
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<4;j++)
                                {
                                        miwen2[i][j]=Lfinal[i][j];
                                }
                                for(int j=4;j<8;j++)
                                {
                                        miwen2[i][j]=Rfinal[i][j-4];
                                }
                        }
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<8;j++)
                                {
                                        miwenip[i][j]=miwen2[(ip_1[i][j]-1)/8][(ip_1[i][j]-1)%8];
                                }
                        }
                        
                        System.out.println();
                        System.out.println("该明文和密钥对应的密文为：(二进制)");
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<8;j++)
                                {
                                        System.out.print(miwenip[i][j]+" ");
                                        //System.out.print(miwen2[i][j]+" ");
                                }
                                System.out.println();
                        }
                        System.out.println("base-64密文为：(以等号结束)");
                        
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<8;j++)
                                {
                                        miwen1[i*8+j]=miwenip[i][j];
                                }
                        }
                        miwen1[64]=miwen1[65]=0;
                        
                        for(int i=0;i<11;i++)
                        {
                                miwen6[i]=miwen1[i*6]*32+miwen1[i*6+1]*16
                                                +miwen1[i*6+2]*8+miwen1[i*6+3]*4
                                                +miwen1[i*6+4]*2+miwen1[i*6+5];
                                if(miwen6[i]<26)
                                {
                                        miwen[i]=(char)(miwen6[i]+65);
                                }
                                else if(miwen6[i]<52)
                                {
                                        miwen[i]=(char)(miwen6[i]+71);
                                }
                                else if(miwen6[i]<62)
                                {
                                        miwen[i]=(char)(miwen6[i]-4);
                                }
                                else if(miwen6[i]==62)
                                {
                                        miwen[i]='+';
                                }
                                else
                                {
                                        miwen[i]='/';
                                }
                        }
                        miwen[11]='=';
                        System.out.println(miwen);
                }
                if(y==2)                                                        //解密
                {
                        System.out.println("请输入11个字符的base-64编码");
                        String smi=new Scanner(System.in).nextLine();
                        for(int i=0;i<11;i++)
                        {
                                miwen[i]=smi.charAt(i);
                        }
                        System.out.println("请输入密钥（8个字节）：");
                        String sk=new Scanner(System.in).nextLine();
                        System.out.println();
                        for(int i=0;i<8;i++)
                        {
                                miyao[i]=sk.charAt(i);
                        }
                        for(int i=0;i<11;i++)
                        {
                                if(miwen[i]<='Z'&&miwen[i]>='A')
                                {
                                        miwen6[i]=(miwen[i]-65);
                                }
                                else if(miwen[i]<='z'&&miwen[i]>='a')
                                {
                                        miwen6[i]=(miwen[i]-71);
                                }
                                else if(miwen[i]<='9'&&miwen[i]>='0')
                                {
                                        miwen6[i]=(miwen[i]+4);
                                }
                                else if(miwen[i]=='+')
                                {
                                        miwen6[i]=62;
                                }
                                else
                                {
                                        miwen6[i]=63;
                                }
                        }
                        for(int i=0;i<64;i++)
                        {
                                miwen1[i]=(miwen6[i/6]>>(5-i%6))&1;
                        }
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<8;j++)
                                {
                                        miwen2[i][j]=miwen1[i*8+j];
                                }
                        }
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<8;j++)
                                {
                                        miwenip[i][j]=miwen2[(ip[i][j]-1)/8][(ip[i][j]-1)%8];
                                }
                        }
                        
                        int[][] Lfinal=new int[8][4];
                        int[][] Rfinal=new int[8][4];
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<4;j++)
                                {
                                        Lfinal[i][j]=miwenip[i][j];
                                        Rfinal[i][j]=miwenip[i][j+4];
                                }
                        }
                        R[16]=Lfinal;
                        L[16]=Rfinal;
                        for(int i=15;i>=0;i--)
                        {
                                R[i]=L[i+1];
                                L[i]=f(R[i+1],L[i+1],key(i+1,miyao));
                        }
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<4;j++)
                                {
                                        mingwenip[i][j]=L[0][i][j];
                                        mingwenip[i][j+4]=R[0][i][j];
                                }
                        }
                        for(int i=0;i<8;i++)
                        {
                                for(int j=0;j<8;j++)
                                {
                                        mingwen2[i][j]=mingwenip[(ip_1[i][j]-1)/8][(ip_1[i][j]-1)%8];
                                }
                        }
                        for(int i=0;i<8;i++)
                        {
                                mingwen[i]=(char)(mingwen2[i][1]*64+mingwen2[i][2]*32
                                                         +mingwen2[i][3]*16+mingwen2[i][4]*8
                                                         +mingwen2[i][5]*4+mingwen2[i][6]*2
                                                         +mingwen2[i][7]);
                        }
                        System.out.println("该明文在密钥下对应的密文为：");
                        System.out.println(mingwen);
                }

        }
        public static void menu()                                          //菜单
        {
                System.out.println("***************Des密码***************");
                System.out.println("*************作者:胡煌伟*************");
                System.out.println("*******1键进入加密，2键进入解密*******");
        }
        
        


public static int[][] key(int n,char[] k)                         //密钥编排,n为轮次
        {
                int[][] pc_1= {{57,49,41,33,25,17,9},{1,58,50,42,34,26,18},
                                {10,2,59,51,43,35,27},{19,11,3,60,52,44,36},
                                {63,55,47,39,31,23,15},{7,62,54,46,38,30,22},
                                {14,6,61,53,45,37,29},{21,13,5,28,20,12,4}};
                int[][] pc_2= {{14,17,11,24,1,5},{3,28,15,6,21,10},
                                {23,19,12,4,26,8},{16,7,27,20,13,2},
                                {41,52,31,37,47,55},{30,40,51,45,33,48},
                                {44,49,39,56,34,53},{46,42,50,36,29,32}};
                int[] zuoyi= {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};

                int[][][] C=new int[17][4][7];
                int[][][] D=new int[17][4][7];
                int[][][] CD=new int[17][8][7];
                for(int z=1;z<=n;z++)
                {
                        if(n==1)
                        {
                                int[][] k2=new int[8][8];
                                for(int i=0;i<8;i++)
                                {
                                        for(int j=0;j<8;j++)
                                        {
                                                k2[i][j]=(k[i]>>(7-j))&1;
                                        }
                                }
                                int[][] kpc_1=new int[8][7];
                                for(int i=0;i<8;i++)
                                {
                                        for(int j=0;j<7;j++)
                                        {
                                                kpc_1[i][j]=k2[(pc_1[i][j]-1)/8][(pc_1[i][j]-1)%8];
                                        }
                                }
                                for(int i=0;i<4;i++)
                                {
                                        for(int j=0;j<7;j++)
                                        {
                                                C[0][i][j]=kpc_1[i][j];
                                                D[0][i][j]=kpc_1[i+4][j];
                                        }
                                }
                        }
                            int[] erwtoyiwc=new int [28];                               //C,D左移
                                int[] erwtoyiwd=new int [28];
                                for(int i=0;i<4;i++)
                                        for(int j=0;j<7;j++)
                                        {
                                                erwtoyiwc[i*7+j]=C[z-1][i][j];
                                                erwtoyiwd[i*7+j]=D[z-1][i][j];
                                        }
                                for(int i=0;i<28;i++)
                                {
                                        erwtoyiwc[i]=erwtoyiwc[(i+28-zuoyi[0])%28];
                                        erwtoyiwd[i]=erwtoyiwd[(i+28-zuoyi[0])%28];
                                }
                                for(int i=0;i<4;i++)
                                        for(int j=0;j<7;j++)
                                        {
                                                C[z][i][j]=erwtoyiwc[i*7+j];
                                                CD[z-1][i][j]=C[z][i][j];
                                                D[z][i][j]=erwtoyiwd[i*7+j];
                                                CD[z-1][i+4][j]=D[z][i][j];
                                        }
                }
                                int[][] result=new int[8][6];
                                for(int i=0;i<8;i++)
                                {
                                        for(int j=0;j<6;j++)
                                        {
                                                result[i][j]=CD[n-1][(pc_2[i][j]-1)/7][(pc_2[i][j]-1)%7];
                                        }
                                }
                                
                
                return result;
}
        
        public static int[][] f(int[][] l,int[][] r,int[][] k)                             //f函数
        {
                int[][] ebox= {{32,1,2,3,4,5},{4,5,6,7,8,9},
                                {8,9,10,11,12,13},{12,13,14,15,16,17},
                                {16,17,18,19,20,21},{20,21,22,23,24,25},
                                {24,25,26,27,28,29},{28,29,30,31,32,1}};
                int[][][] sbox = {
                {
                        { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                        { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                        { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                        { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
                },
                { 
                        { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
                        { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                        { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                        { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
                },
                { 
                        { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
                        { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
                        { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
                        { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
                },
                { 
                        { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
                        { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
                        { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
                        { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
                },
                { 
                        { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
                        { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
                        { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
                        { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }
                },
                { 
                        { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
                        { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
                        { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
                        { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }
                },
                { 
                        { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
                        { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
                        { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
                        { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }
                },
                { 
                        { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
                        { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
                        { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
                        { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 }
                }
                };
       int[][] pbox= 
               {
                               {16,7,20,21},
                               {29,12,28,17},
                               {1,15,23,26},
                               {5,18,31,10},
                               {2,8,24,14},
                               {32,27,3,9},
                               {19,13,30,6},
                               {20,11,4,25}
               };
       int[][] re=new int[8][6];                                                  //选择扩展E
       for(int i=0;i<8;i++)
       {
               for(int j=0;j<6;j++)
               {
                       re[i][j]=r[(ebox[i][j]-1)/4][(ebox[i][j]-1)%4];
               }
       }
       int[][] rk=new int[8][6];                                                   //异或
       for(int i=0;i<8;i++)
       {
               for(int j=0;j<6;j++)
               {
                       rk[i][j]=re[i][j]^k[i][j];
               }
       }
       int[][] rs=new int[8][4];                                                   //选择压缩S
       for(int i=0;i<8;i++)
       {
               int x=rk[i][0]+rk[i][5]*2;
               int y=rk[i][4]*8+rk[i][3]*4+rk[i][2]*2+rk[i][1];
               for(int j=0;j<4;j++)
               {
                       rs[i][j]=(sbox[i][x][y]>>j)&1;
               }
       }
       int[][] rp=new int[8][4];                                                     //P盒置换
       for(int i=0;i<8;i++)
       {
               for(int j=0;j<4;j++)
               {
                       rp[i][j]=rs[(pbox[i][j]-1)/4][(pbox[i][j]-1)%4];
               }
       }
       int[][] rnext=new int[8][4];
       for(int i=0;i<8;i++)
       {
               for(int j=0;j<4;j++)
               {
                       rnext[i][j]=l[i][j]^r[i][j];
               }
       }
       return rnext;
        }
}
