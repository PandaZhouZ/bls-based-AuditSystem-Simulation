package com.hp.blsaudit;

import com.hp.blsaudit.configure.Configure;
import com.hp.blsaudit.entity.*;
import com.hp.blsaudit.util.*;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyCostTest {

    @Test
    public void mhtinsertTest(){
        PKG pkg=new PKG();
        MHTNode root=new MHTNode(null);
        DO dataowner=new DO(pkg);
        dataowner.getShards(pkg,"audit3.txt", 128);
        long start;
        for (int i=1;i<=6;++i){
            start=System.currentTimeMillis();
            for (int j=0;j<100*i+9;++j){
                MHTNode newNode=new MHTNode(null);
                root.setLeft(newNode);
                root.setRight(newNode);
                Element h=pkg.hashToG(FileOp.toByteArray("audit3.txt",dataowner.getId(),((int)(Math.random()*500)%500)).toString());
            }
            System.out.println((System.currentTimeMillis()-start)+"ms");
        }
    }

    @Test
    public void mhtupdateTest(){
        PKG pkg=new PKG();
        MHTNode root=new MHTNode(null);
        MHTNode left=new MHTNode(null);
        MHTNode right=new MHTNode(null);
        MHTNode temp;
        root.setLeft(left);
        root.setRight(right);

        DO dataowner=new DO(pkg);
        dataowner.getShards(pkg,"audit3.txt", 128);
        long start;
        int k=105;
        for (int i=1;i<=6;++i){
            switch (i){
                case 1:
                    k=105;
                    break;
                case 2:
                    k=206;
                    break;
                case 3:
                    k=307;
                    break;
                case 4:
                    k=407;
                    break;
                case 5:
                    k=508;
                    break;
                case 6:
                    k=608;
                    break;
                default:
                    break;
            }
            start=System.currentTimeMillis();
            for (int j=0;j<k;++j){
                temp=root.getLeft();
                temp=root.getRight();
                Element h=pkg.hashToG(FileOp.toByteArray("audit3.txt",dataowner.getId(),((int)(Math.random()*500)%500)).toString());
            }
            System.out.println((System.currentTimeMillis()-start)+"ms");
        }
    }

    @Test
    public void computeCostTest() throws IOException {
        long start;
        long end;
        PKG pkg = new PKG();
        DO dataOwner = new DO(pkg);
        FileOp.splitFile("audit3.txt",dataOwner.getId(),128);
        start=System.nanoTime();
        for (int i=0;i<1000;++i){
            Element h=pkg.hashToG(FileOp.toByteArray("audit3.txt",dataOwner.getId(),(int)Math.random()*499).toString());
        }
        end=System.nanoTime();
        System.out.println("1000次hash到G1群上的开销为："+(end-start)+"ns,平均单次开销："+(end-start)/1000+"ns");

        start=System.nanoTime();
        for (int i=0;i<1000;++i){
            Element htozp=Transform.bytesToZp(pkg,FileOp.toByteArray("audit3.txt",dataOwner.getId(),(int)Math.random()*499));
        }
        end=System.nanoTime();
        System.out.println("1000次hash到Zp群上的开销为："+(end-start)+"ns,平均单次开销："+(end-start)/1000+"ns");

        start=System.nanoTime();
        for (int i=0;i<1000;++i){
            Element zpAdd=pkg.getZp().newRandomElement().getImmutable().add(pkg.getZp().newRandomElement().getImmutable()).getImmutable();
        }
        end=System.nanoTime();
        System.out.println("1000次Zp上加法运算的开销为："+(end-start)+"ns,平均单次开销："+(end-start)/1000+"ns");

        start=System.nanoTime();
        for (int i=0;i<1000;++i){
            Element zpMul=pkg.getZp().newRandomElement().getImmutable().mul(pkg.getZp().newRandomElement().getImmutable()).getImmutable();
        }
        end=System.nanoTime();
        System.out.println("1000次Zp上乘法运算的开销为："+(end-start)+"ns,平均单次开销："+(end-start)/1000+"ns");

        start=System.nanoTime();
        for (int i=0;i<1000;++i){
            Element Gpow=pkg.getG1().newRandomElement().getImmutable().powZn(pkg.getZp().newRandomElement().getImmutable()).getImmutable();
        }
        end=System.nanoTime();
        System.out.println("1000次G1上指数运算的开销为："+(end-start)+"ns,平均单次开销："+(end-start)/1000+"ns");


        start=System.nanoTime();
        for (int i=0;i<1000;++i){
            Element Gmul=pkg.getG1().newRandomElement().getImmutable().mul(pkg.getG1().newRandomElement().getImmutable()).getImmutable();
        }
        end=System.nanoTime();
        System.out.println("1000次G1上乘法运算的开销为："+(end-start)+"ns,平均单次开销："+(end-start)/1000+"ns");

    }


    @Test
    public void mhtdeleteTest(){
        PKG pkg=new PKG();
        DO dataowner=new DO(pkg);
        dataowner.getShards(pkg,"audit3.txt", 128);
        MHTNode root= new MHTNode(null);
        MHTNode temp=new MHTNode(null);
        long start = System.currentTimeMillis();
        for (int i=0;i<4;++i){
            Element h=pkg.hashToG(FileOp.toByteArray("audit3.txt",dataowner.getId(),((int)(Math.random()*499))).toString());
        }
        root.setLeft(temp);
        root.setRight(temp);
        System.out.println((System.currentTimeMillis()-start)+"ms");
        start=System.currentTimeMillis();
        for (int i=0;i<3;++i){
            Element h=pkg.hashToG(FileOp.toByteArray("audit3.txt",dataowner.getId(),((int)(Math.random()*499))).toString());
        }
        root.setLeft(temp);
        root.setRight(temp);
        System.out.println((System.currentTimeMillis()-start)+"ms");
        start=System.currentTimeMillis();
        for (int i=0;i<2;++i){
            Element h=pkg.hashToG(FileOp.toByteArray("audit3.txt",dataowner.getId(),((int)(Math.random()*499))).toString());
        }
        System.out.println((System.currentTimeMillis()-start)+"ms");
        start=System.nanoTime();
        root.setLeft(temp);
        root.setRight(temp);
        System.out.println((System.nanoTime()-start)+"ns");
        start=System.currentTimeMillis();
        for (int i=0;i<3;++i){
            Element h=pkg.hashToG(FileOp.toByteArray("audit3.txt",dataowner.getId(),((int)(Math.random()*499))).toString());
        }
        root.setLeft(temp);
        root.setRight(temp);
        System.out.println((System.currentTimeMillis()-start)+"ms");
    }

    @Test
    public void linkTableInsertTest(){
        LinkTableNode head=new LinkTableNode();
        LinkTableNode first=new LinkTableNode();
        PKG pkg=new PKG();
        Element updateData=pkg.getZp().newRandomElement();
        head.setPost(first);
        LinkTableNode temp;
        long start;
        long end;

        for (int i=100;i<=600;i+=100){
            start=System.nanoTime();
            for (int l=0;l<1000;++l){
                for (int j=0;j<1000;++j){
                    temp=head.getPost();
                }
                for (int k=0;k<i;++k){
                    temp=new LinkTableNode(updateData);
                    temp.setPre(head);
                    head.setPost(temp);
                    temp=head.getPost();
                }
            }
            System.out.println((System.nanoTime()-start)/1000+"ns");
        }
    }

    @Test
    public void linkTableUpdateTest(){
        LinkTableNode head=new LinkTableNode();
        LinkTableNode first=new LinkTableNode();
        PKG pkg=new PKG();
        Element updateData=pkg.getZp().newRandomElement();

        head.setPost(first);
        LinkTableNode temp;
        long start;
        long end;

        for (int i=100;i<=600;i+=100){
            start=System.nanoTime();
            for (int l=0;l<1000;++l){
                for (int j=0;j<1000-i;++j){
                    temp=head.getPost();
                }
                for (int k=0;k<i;++k){
                    temp=head.getPost();
                    temp.setValue(updateData);
                }
            }
            System.out.println((System.nanoTime()-start)/1000+"ns");
        }
    }

    @Test
    public void linkTableDeleteTest(){
        LinkTableNode head=new LinkTableNode();
        LinkTableNode first=new LinkTableNode();
        PKG pkg=new PKG();
        Element updateData=pkg.getZp().newRandomElement();

        head.setPost(first);
        LinkTableNode temp=new LinkTableNode();
        long start;
        long end;

        for (int i=100;i<=600;i+=100){
            start=System.nanoTime();
            for (int l=0;l<1000;++l){
                for (int j=0;j<1000;++j){
                    temp=head.getPost();
                }
                temp.setPost(null);
            }
            System.out.println((System.nanoTime()-start)/1000+"ns");
        }
    }

    @Test
    public void fabricTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 1, s = 1, z = 250;
        PKG pkg = new PKG();
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(pkg));
        }

//        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数"+z+"；");

        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数从50递增到500审计计算开销的变化情况");
        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getFabricShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");

        for (int i=50;i<=500;i+=50){
            if (i==500){
                fabricChalProofVerify(n,s,499,pkg,dos,cs);
                break;
            }
            fabricChalProofVerify(n,s,i,pkg,dos,cs);
        }
    }

    @Test
    public void FabricDOsTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 50, s = 1, z = 250;
        PKG pkg = new PKG();
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(pkg));
        }

//        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数"+z+"；");

        System.out.println("本次测试共从10到50个用户（10，20，30，40，50），单个用户审计任务数量"+s+"，单个审计任务质询块数为500的审计计算开销的变化情况");
        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getFabricShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");

        for (int i=10;i<=50;i+=10){
            fabricChalProofVerify(i,s,z,pkg,dos,cs);
        }
    }

    public static void fabricChalProofVerify(int n,int s,int z,PKG pkg,List<DO> dos,CSP cs){
        StdOut.println("DO开始生成挑战...");
//            Chal chals = dataOwner.chalGen(pkg, "source.txt", 50);
        Map<Integer, List<Chal>> chalLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Chal> chalList = new ArrayList<>();
            for (int j = 1; j <= s; j++) {
                chalList.add(dos.get(i).chalGen(pkg, "audit3.txt", z));
//                chalList.add(dos.get(i).chalGen(pkg, "source.txt", z));
            }
            chalLists.put(i, chalList);
        }
        StdOut.println("挑战已生成！");
        StdOut.println("-------------------------------------");
        StdOut.println("CSP开始生成证明...");
        long start = System.currentTimeMillis();
        Map<Integer, List<Proof>> proofLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Proof> proofList = new ArrayList<>();
            for (int j = 0; j < s; j++) {
                proofList.add(cs.proofGen(pkg, dos.get(i), chalLists.get(i).get(j)));
            }
            proofLists.put(i, proofList);
        }
        StdOut.println("证明已生成！");
        long avg_1 = System.currentTimeMillis()-start;
        System.out.println("审计证明生成总耗时："+(avg_1/1000.0)+"s");
        System.out.println("平均每个审计任务证明生成耗时:"+(avg_1/1000.0/ (s * n)) + "s");
        StdOut.println("-------------------------------------");
        StdOut.println("开始验证...");
        long mid = System.currentTimeMillis();
        boolean result = fabricBatchAudit(pkg,n,dos,chalLists,proofLists);
        long end = System.currentTimeMillis();
        long avg_2 = (end - mid);
        long sum=(end-start);
        StdOut.println("验证已完成！");
        StdOut.println("验证结果：" + result);
        StdOut.println("-------------------------------------");
        System.out.println("验证过程总耗时："+(avg_2/1000.0)+"s");
        StdOut.println("平均每个审计任务验证过程耗时：" + (avg_2 / 1000.0 / (s * n)) + "s");
        System.out.println("证明生成与验证时间总计："+(sum/1000.0)+"s");
    }

    @Test
    public void DredasTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 1, s = 1, z = 250;
        PKG pkg = new PKG();
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(pkg));
        }

//        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数"+z+"；");

        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数从50递增到500审计计算开销的变化情况");
        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getDredasShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");

        for (int i=50;i<=500;i+=50){
            if (i==500){
                dredasChalProofVerify(n,s,499,pkg,dos,cs);
                break;
            }
            dredasChalProofVerify(n,s,i,pkg,dos,cs);
        }

    }

    @Test
    public void DredasDOsTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 50, s = 1, z = 250;
        PKG pkg = new PKG();
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(pkg));
        }

//        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数"+z+"；");

        System.out.println("本次测试共从10到50个用户（10，20，30，40，50），单个用户审计任务数量"+s+"，单个审计任务质询块数为500的审计计算开销的变化情况");
        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getDredasShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");

        for (int i=10;i<=50;i+=10){
            dredasChalProofVerify(i,s,z,pkg,dos,cs);
        }
    }

    public static void dredasChalProofVerify(int n,int s,int z,PKG pkg,List<DO> dos,CSP cs){
        StdOut.println("DO开始生成挑战...");
//            Chal chals = dataOwner.chalGen(pkg, "source.txt", 50);
        Map<Integer, List<Chal>> chalLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Chal> chalList = new ArrayList<>();
            for (int j = 1; j <= s; j++) {
                chalList.add(dos.get(i).chalGen(pkg, "audit3.txt", z));
//                chalList.add(dos.get(i).chalGen(pkg, "source.txt", z));
            }
            chalLists.put(i, chalList);
        }
        StdOut.println("挑战已生成！");
        StdOut.println("-------------------------------------");
        StdOut.println("CSP开始生成证明...");
        long start = System.currentTimeMillis();
        Map<Integer, DredasProof> proofLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            proofLists.put(i, cs.dredasProofGen(pkg, dos.get(i), chalLists.get(i)));
        }
        StdOut.println("证明已生成！");
        long avg_1 = System.currentTimeMillis()-start;
        System.out.println("审计证明生成总耗时："+(avg_1/1000.0)+"s");
        System.out.println("平均每个审计任务证明生成耗时:"+(avg_1/1000.0/ (s * n)) + "s");
        StdOut.println("-------------------------------------");
        StdOut.println("开始验证...");
        long mid = System.currentTimeMillis();
        boolean result = dredasBatchAudit(pkg,n,dos,chalLists,proofLists);
        long end = System.currentTimeMillis();
        long avg_2 = (end - mid);
        long sum=(end-start);
        StdOut.println("验证已完成！");
        StdOut.println("验证结果：" + result);
        StdOut.println("-------------------------------------");
        System.out.println("验证过程总耗时："+(avg_2/1000.0)+"s");
        StdOut.println("平均每个审计任务验证过程耗时：" + (avg_2 / 1000.0 / (s * n)) + "s");
        System.out.println("证明生成与验证时间总计："+(sum/1000.0)+"s");
    }

    public static void paperChalProofVerify(int n,int s,int z,PKG pkg,List<DO> dos,CSP cs,A1PKG a1PKG){
        StdOut.println("DO开始生成挑战...");
//            Chal chals = dataOwner.chalGen(pkg, "source.txt", 50);
        Map<Integer, List<Chal>> chalLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Chal> chalList = new ArrayList<>();
            for (int j = 1; j <= s; j++) {
                chalList.add(dos.get(i).chalGen(pkg, "audit3.txt", z));
//                chalList.add(dos.get(i).chalGen(pkg, "source.txt", z));
            }
            chalLists.put(i, chalList);
        }
        StdOut.println("挑战已生成！");
        StdOut.println("-------------------------------------");
        StdOut.println("CSP开始生成证明...");
        long start = System.currentTimeMillis();
        Map<Integer, List<Element>> proofLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Element> proofList = new ArrayList<>();
            for (int j = 0; j < s; j++) {
                proofList.add(cs.paperProofGen(pkg, a1PKG,dos.get(i), chalLists.get(i).get(j)));
            }
            proofLists.put(i, proofList);
        }
        StdOut.println("证明已生成！");
        long avg_1 = System.currentTimeMillis()-start;
        System.out.println("审计证明生成总耗时："+(avg_1/1000.0)+"s");
        System.out.println("平均每个审计任务证明生成耗时:"+(avg_1/1000.0/ (s * n)) + "s");
        StdOut.println("-------------------------------------");
        StdOut.println("开始验证...");
        long mid = System.currentTimeMillis();
        boolean result = paperBatchAudit(pkg,a1PKG,n,dos,chalLists,proofLists);
        long end = System.currentTimeMillis();
        long avg_2 = (end - mid);
        long sum=(end-start);
        StdOut.println("验证已完成！");
        StdOut.println("验证结果：" + result);
        StdOut.println("-------------------------------------");
        System.out.println("验证过程总耗时："+(avg_2/1000.0)+"s");
        StdOut.println("平均每个审计任务验证过程耗时：" + (avg_2 / 1000.0 / (s * n)) + "s");
        System.out.println("证明生成与验证时间总计："+(sum/1000.0)+"s");
    }

    @Test
    public void paperTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 1, s = 1, z = 250;
        PKG pkg = new PKG();
        A1PKG a1PKG = new A1PKG();
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(String.valueOf(i)));
        }

        long start, end;
        long avg_1 = 0L, avg_2 = 0L;

        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数从50递增到500审计计算开销的变化情况");

        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getPaperShards(pkg,a1PKG, "audit3.txt", 128);
//            dos.get(i).getShards(pkg,"audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");
        for (int i=50;i<=500;i+=50){
            if (i==500){
                paperChalProofVerify(n,s,499,pkg,dos,cs,a1PKG);
                break;
            }
            paperChalProofVerify(n,s,i,pkg,dos,cs,a1PKG);
        }
    }

    @Test
    public void paperDOsTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 50, s = 1, z =250;
        PKG pkg = new PKG();
        A1PKG a1PKG = new A1PKG();
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(String.valueOf(i)));
        }
        System.out.println("本次测试共从10到50个用户（10，20，30，40，50），单个用户审计任务数量"+s+"，单个审计任务质询块数为500的审计计算开销的变化情况");

        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getPaperShards(pkg,a1PKG, "audit3.txt", 128);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");
        for (int i=10;i<=50;i+=10){
            paperChalProofVerify(i,s,z,pkg,dos,cs,a1PKG);
        }
    }

    public static void hpChalProofVerify(int n,int s,int z,PKG pkg,List<DO> dos,CSP cs,GM gm){
        StdOut.println("DO开始生成挑战...");
//            Chal chals = dataOwner.chalGen(pkg, "source.txt", 50);
        Map<Integer, List<Chal>> chalLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Chal> chalList = new ArrayList<>();
            for (int j = 1; j <= s; j++) {
                chalList.add(dos.get(i).chalGen(pkg, "audit3.txt", z));
//                chalList.add(dos.get(i).chalGen(pkg, "source.txt", z));
            }
            chalLists.put(i, chalList);
        }
        StdOut.println("挑战已生成！");
        StdOut.println("-------------------------------------");
        StdOut.println("CSP开始生成证明...");
        long start = System.currentTimeMillis();
        Map<Integer, List<Proof>> proofLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Proof> proofList = new ArrayList<>();
            for (int j = 0; j < s; j++) {
                proofList.add(cs.proofGen(pkg, dos.get(i), chalLists.get(i).get(j)));
            }
            proofLists.put(i, proofList);
        }
        StdOut.println("证明已生成！");
        long avg_1 = System.currentTimeMillis()-start;
        System.out.println("审计证明生成总耗时："+(avg_1/1000.0)+"s");
        System.out.println("平均每个审计任务证明生成耗时:"+(avg_1/1000.0/ (s * n)) + "s");
        StdOut.println("-------------------------------------");
        StdOut.println("开始验证...");
        long mid = System.currentTimeMillis();
        boolean result = batchAudit(pkg,gm,n,dos,chalLists,proofLists);
        long end = System.currentTimeMillis();
        long avg_2 = (end - mid);
        long sum=(end-start);
        StdOut.println("验证已完成！");
        StdOut.println("验证结果：" + result);
        StdOut.println("-------------------------------------");
        System.out.println("验证过程总耗时："+(avg_2/1000.0)+"s");
        StdOut.println("平均每个审计任务验证过程耗时：" + (avg_2 / 1000.0 / (s * n)) + "s");
        System.out.println("证明生成与验证时间总计："+(sum/1000.0)+"s");
    }

    @Test
    public void newhpTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 1, s = 1, z = 250;
        PKG pkg = new PKG();
        GM gm = new GM(pkg);
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(pkg, gm));
        }

        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数从50递增到500审计计算开销的变化情况");

        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getNewhpShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");
//        for (int i=50;i<=500;i+=50){
//            if (i==500){
//                newhpChalProofVerify(n,s,499,pkg,dos,cs,gm);
//                break;
//            }
//            newhpChalProofVerify(n,s,i,pkg,dos,cs,gm);
//        }
        newhpChalProofVerify(n,s,z,pkg,dos,cs,gm);
    }

    @Test
    public void newHpDOsTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 50, s = 1, z = 250;
        PKG pkg = new PKG();
        GM gm = new GM(pkg);
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(pkg, gm));
        }

        System.out.println("本次测试共从10到50个用户（10，20，30，40，50），单个用户审计任务数量"+s+"，单个审计任务质询块数为500的审计计算开销的变化情况");

        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getNewhpShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");
        for (int i=10;i<=50;i+=10){
            System.out.println("共"+i+"个用户，每个用户质询50个块的情况的情况：");
            newhpChalProofVerify(i,s,50,pkg,dos,cs,gm);
            System.out.println("共"+i+"个用户，每个用户质询500个块的情况的情况：");
            newhpChalProofVerify(i,s,499,pkg,dos,cs,gm);
        }
//        newhpChalProofVerify(n,s,z,pkg,dos,cs,gm);
    }

    public static void newhpChalProofVerify(int n,int s,int z,PKG pkg,List<DO> dos,CSP cs,GM gm){
        StdOut.println("DO开始生成挑战...");
//            Chal chals = dataOwner.chalGen(pkg, "source.txt", 50);
        Map<Integer, List<Chal>> chalLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Chal> chalList = new ArrayList<>();
            for (int j = 1; j <= s; j++) {
                chalList.add(dos.get(i).chalGen(pkg, "audit3.txt", z));
//                chalList.add(dos.get(i).chalGen(pkg, "source.txt", z));
            }
            chalLists.put(i, chalList);
        }
        StdOut.println("挑战已生成！");
        StdOut.println("-------------------------------------");
        StdOut.println("CSP开始生成证明...");
        long start = System.currentTimeMillis();
        Map<Integer, Proof> proofList = new HashMap<>();
        for (int i = 0; i < n; i++) {
            proofList.put(i,cs.newhpProofGen(pkg, dos.get(i), chalLists.get(i)));
        }
        Element dp =pkg.getZp().newZeroElement().getImmutable();
        for (Proof tempProof:proofList.values()){
            dp=dp.add(tempProof.getDataProof()).getImmutable();
        }

        StdOut.println("证明已生成！");
        long avg_1 = System.currentTimeMillis()-start;
        System.out.println("审计证明生成总耗时："+(avg_1/1000.0)+"s");
        System.out.println("平均每个审计任务证明生成耗时:"+(avg_1/1000.0/ (s * n)) + "s");
        StdOut.println("-------------------------------------");
        StdOut.println("开始验证...");
        long mid = System.currentTimeMillis();
        boolean result = newhpBatchAudit(pkg,gm,n,dos,chalLists,proofList,dp);
        long end = System.currentTimeMillis();
        long avg_2 = (end - mid);
        long sum=(end-start);
        StdOut.println("验证已完成！");
        StdOut.println("验证结果：" + result);
        StdOut.println("-------------------------------------");
        System.out.println("验证过程总耗时："+(avg_2/1000.0)+"s");
        StdOut.println("平均每个审计任务验证过程耗时：" + (avg_2 / 1000.0 / (s * n)) + "s");
        System.out.println("证明生成与验证时间总计："+(sum/1000.0)+"s");
    }

    @Test
    public void singleAuditTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 1, s = 1, z = 250;
        PKG pkg = new PKG();
        GM gm = new GM(pkg);
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(pkg, gm));
        }

        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数从50递增到500审计计算开销的变化情况");

        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");
        for (int i=50;i<=500;i+=50){
            if (i==500){
                hpChalProofVerify(n,s,499,pkg,dos,cs,gm);
                break;
            }
            hpChalProofVerify(n,s,i,pkg,dos,cs,gm);
        }
//        hpChalProofVerify(1,s,z,pkg,dos,cs,gm);
//        hpChalProofVerify(2,s,z,pkg,dos,cs,gm);
    }

    @Test
    public void hpDOsTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 50, s = 1, z = 250;
        PKG pkg = new PKG();
        GM gm = new GM(pkg);
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(pkg, gm));
        }

        System.out.println("本次测试共从1到50个用户（1，5，10，15，20，25，30，35，40，45，50），单个用户审计任务数量"+s+"，单个审计任务质询块数为500的审计计算开销的变化情况");

        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");


        for (int i=5;i<=50;i+=5){
            hpChalProofVerify(i,s,z,pkg,dos,cs,gm);
        }
    }

    public static boolean batchAudit(PKG pkg, GM gm, int n,List<DO> dos, Map<Integer, List<Chal>> chalLists,  Map<Integer, List<Proof>> proofLists) {
        Element left = pkg.getGT().newOneElement();
        for (int i = 0; i < n; i++) {
            Element tp = pkg.getG1().newOneElement();
            List<Proof> tmp = proofLists.get(i);
            for (int j = 0; j < tmp.size(); j++) {
                tp = tp.mul(tmp.get(j).getTagProof()).getImmutable();
            }
            left = left.mul(pkg.getPairing().pairing(tp, dos.get(i).getGamma())).getImmutable();
        }

        Element dbi = pkg.getG1().newOneElement();

        for (int i = 0; i <n; i++) {
            List<Chal> chals = chalLists.get(i);
            for (int j = 0; j < chals.size(); j++) {
                List<Item> items = dos.get(i).getEact().getMetadata().get(chals.get(j).getFileName());
                for (Map.Entry<Integer, Element> entry : chals.get(j).getChal().entrySet()) {
                    int index = entry.getKey();
                    Element random = entry.getValue();

                    Item tmp = items.get(index);
                    dbi = dbi.mul(pkg.hashToG("" + tmp.getVersion() + tmp.getTimestamp()).powZn(random)).getImmutable();
                }
            }
        }
        dbi = pkg.getPairing().pairing(dbi, gm.getGpk()).getImmutable();

        Element dp = pkg.getZp().newZeroElement();
        for (int i = 0; i < n; i++) {
            List<Proof> tmp = proofLists.get(i);
            for (int j = 0; j < tmp.size(); j++) {
                dp = dp.add(tmp.get(j).getDataProof()).getImmutable();
            }
        }
        Element right = dbi.mul(pkg.getPairing().pairing(pkg.getg2().powZn(dp), gm.getGpk())).getImmutable();

        return left.isEqual(right);
    }

    public static boolean newhpBatchAudit(PKG pkg, GM gm, int n,List<DO> dos, Map<Integer, List<Chal>> chalLists,  Map<Integer, Proof> proofList,Element dp) {
        Element left = pkg.getGT().newOneElement().getImmutable();

        for (int i = 0; i < n; i++) {
            left = left.mul(pkg.getPairing().pairing(proofList.get(i).getTagProof().getImmutable(), dos.get(i).getGamma().getImmutable())).getImmutable();
        }

        for (int i = 0; i <n; i++) {
            List<Chal> chals = chalLists.get(i);
            for (int j = 0; j < chals.size(); j++) {
                List<Item> items = dos.get(i).getEact().getMetadata().get(chals.get(j).getFileName());
                for (Map.Entry<Integer, Element> entry : chals.get(j).getChal().entrySet()) {
                    int index = entry.getKey();
                    Element random = entry.getValue();
                    Item tmp = items.get(index);

                    dp = dp.add(Transform.bytesToZp(pkg,("" + tmp.getVersion() + tmp.getTimestamp()).getBytes()).getImmutable().mul(random).getImmutable()).getImmutable();
                }
            }
        }

        Element right = pkg.getPairing().pairing(pkg.getg2().powZn(dp), gm.getGpk()).getImmutable();

        return left.isEqual(right);
    }

    public static boolean fabricBatchAudit(PKG pkg,int n,List<DO> dos,Map<Integer, List<Chal>> chalLists,  Map<Integer, List<Proof>> proofLists){
        Element sigma = pkg.getG1().newOneElement().getImmutable();
        Element right = pkg.getGT().newOneElement().getImmutable();
        Element Kphik;
        Element Kmuik;
        Element Kpair;

        for (int i=0;i<n;++i){
            Kphik = pkg.getG1().newOneElement().getImmutable();
            Kmuik = pkg.getZp().newZeroElement().getImmutable();

            List<Proof> proofs=proofLists.get(i);
            for (int j=0;j<proofs.size();++j){
                Proof proof=proofs.get(j);
                sigma=sigma.mul(proof.getTagProof()).getImmutable();
                Kmuik=Kmuik.add(proof.getDataProof()).getImmutable();
            }

            List<Chal> chals = chalLists.get(i);
            for (int j = 0; j < chals.size(); j++) {
                for (Map.Entry<Integer, Element> entry : chals.get(j).getChal().entrySet()) {
                    int index = entry.getKey();
                    Element random = entry.getValue();
                    Element h = pkg.hashToG(FileOp.toByteArray(chals.get(j).getFileName(),dos.get(i).getId(),index).toString()).getImmutable();
                    h=h.powZn(random).getImmutable();
                    Kphik=Kphik.mul(h).getImmutable();
                }
            }
            Element uk=dos.get(i).getU().powZn(Kmuik).getImmutable();
            Kphik=Kphik.mul(uk).getImmutable();
            Kpair=pkg.getPairing().pairing(Kphik,dos.get(i).getPK()).getImmutable();
            right=right.mul(Kpair).getImmutable();
        }
        Element left=pkg.getPairing().pairing(sigma,pkg.getg2()).getImmutable();
        return left.isEqual(right);
    }

    public static boolean dredasBatchAudit(PKG pkg, int n ,List<DO> dos, Map<Integer, List<Chal>> chalLists,  Map<Integer, DredasProof> proofLists) {
        Element left;
        Element right = pkg.getGT().newOneElement().getImmutable();
        Element sigma = pkg.getG1().newOneElement().getImmutable();
        Element KRandk = pkg.getG1().newOneElement().getImmutable();

        Element Kphik;
        Element Kmuik;
        Element KRightLeftk;
        Element KPairk;

        DredasProof dredasProof;


        for (int i = 0; i < n; i++) {
            Kphik = pkg.getG1().newOneElement().getImmutable();
            Kmuik = pkg.getZp().newZeroElement().getImmutable();
            dredasProof=proofLists.get(i);

            KRandk=KRandk.mul(dredasProof.getRandk().powZn(Transform.bytesToZp(pkg,dredasProof.getRandk().toBytes()))).getImmutable();

            for (Element value:dredasProof.getTagProof()){
                sigma=sigma.mul(value).getImmutable();
            }

            for (Element value:dredasProof.getHashProof()){
                Kphik=Kphik.mul(value).getImmutable();
            }

            for (Element value:dredasProof.getDataProof()){
                Kmuik=Kmuik.add(value).getImmutable();
            }
            List<Chal> chals = chalLists.get(i);
            for (int j = 0; j < chals.size(); j++) {
                for (Map.Entry<Integer, Element> entry : chals.get(j).getChal().entrySet()) {
                    int index = entry.getKey();
                    Element random = entry.getValue();
                    Kmuik=Kmuik.add(random.mul(pkg.getZp().newElement(index))).getImmutable();
                }
            }
            KRightLeftk = dos.get(i).getUsk().powZn(Kmuik).mul(Kphik).getImmutable();
            KPairk = pkg.getPairing().pairing(KRightLeftk,dos.get(i).getPK()).getImmutable();
            right = right.mul(KPairk).getImmutable();
        }

        left = pkg.getPairing().pairing(sigma.mul(KRandk).getImmutable(),pkg.getg2());
        return left.isEqual(right);
    }

    public static boolean paperBatchAudit(PKG pkg,A1PKG a1PKG,int n, List<DO> dos, Map<Integer, List<Chal>> chalLists,  Map<Integer, List<Element>> proofLists) {
        Element left = a1PKG.getG1().newOneElement().getImmutable();
        Element tempTi;


        for (int i = 0; i < n; i++) {
            List<Chal> chals = chalLists.get(i);
            for (int j = 0; j < chals.size(); j++) {
                List<Item> items = dos.get(i).getEact().getMetadata().get(chals.get(j).getFileName());
                for (Map.Entry<Integer, Element> entry : chals.get(j).getChal().entrySet()) {
                    int index = entry.getKey();
                    Element random = entry.getValue();

                    tempTi=items.get(index).getTag().powZn(random).getImmutable();
                    left=left.mul(tempTi).getImmutable();
                }
            }
        }

        Element dp = pkg.getZp().newZeroElement();
        for (int i = 0; i < n; i++) {
            List<Element> tmp = proofLists.get(i);
            for (int j = 0; j < tmp.size(); j++) {
                dp = dp.add(tmp.get(j)).getImmutable();
            }
        }
        Element right = a1PKG.getg().powZn(dp).getImmutable();

        return left.isEqual(right);
    }
}
