package com.hp.blsaudit;

import com.hp.blsaudit.configure.Configure;
import com.hp.blsaudit.crypto.Digest;
import com.hp.blsaudit.entity.*;
import com.hp.blsaudit.util.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 {

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
        boolean result = fabricBatchAudit(pkg,dos,chalLists,proofLists);
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

        long start, end;
        long avg_1 = 0L, avg_2 = 0L;

        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数"+z+"；");

        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getDredasShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");
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
        start = System.currentTimeMillis();
        Map<Integer, DredasProof> proofLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            proofLists.put(i,cs.dredasProofGen(pkg,dos.get(i),chalLists.get(i)));
        }
        StdOut.println("证明已生成！");
        avg_1 = System.currentTimeMillis()-start;
        System.out.println("平均每个审计任务证明生成耗时:"+(avg_1/1000.0/ (s * n)) + "s");
        StdOut.println("-------------------------------------");
        StdOut.println("开始验证...");
        start = System.currentTimeMillis();
        boolean result = dredasBatchAudit(pkg,dos,chalLists,proofLists);
        end = System.currentTimeMillis();
        avg_2 = (end - start);
        StdOut.println("验证已完成！");
        StdOut.println("验证结果：" + result);
        StdOut.println("-------------------------------------");
        StdOut.println("平均每个审计任务验证过程耗时：" + (avg_2 / 1000.0 / (s * n)) + "s");
    }


    @Test
    public void paperTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 1, s = 1, z = 250;
        PKG pkg = new PKG();
        A1PKG a1PKG = new A1PKG();
//        GM gm = new GM(pkg);
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(String.valueOf(i)));
        }

        long start, end;
        long avg_1 = 0L, avg_2 = 0L;

        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数"+z+"；");

        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getPaperShards(pkg,a1PKG, "audit3.txt", 128);
//            dos.get(i).getShards(pkg,"audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");
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
        start = System.currentTimeMillis();
        Map<Integer, List<Element>> proofLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Element> proofList = new ArrayList<>();
            for (int j = 0; j < s; j++) {
                proofList.add(cs.paperProofGen(pkg, a1PKG,dos.get(i), chalLists.get(i).get(j)));
            }
            proofLists.put(i, proofList);
        }
        StdOut.println("证明已生成！");
        avg_1 = System.currentTimeMillis()-start;
        System.out.println("平均每个审计任务证明生成耗时:"+(avg_1/1000.0/ (s * n)) + "s");
        StdOut.println("-------------------------------------");
        StdOut.println("开始验证...");
        start = System.currentTimeMillis();
//        boolean result = batchAudit(pkg, gm, dos, chalLists, proofLists);
        boolean result = paperBatchAudit(pkg,a1PKG,dos,chalLists,proofLists);
        end = System.currentTimeMillis();
        avg_2 = (end - start);
        StdOut.println("验证已完成！");
        StdOut.println("验证结果：" + result);
        StdOut.println("-------------------------------------");
        StdOut.println("平均每个审计任务验证过程耗时：" + (avg_2 / 1000.0 / (s * n)) + "s");
    }


    @Test
    public void a1typeParameterGen(){
        TypeA1CurveGenerator pg = new TypeA1CurveGenerator(2, 512);
        PairingParameters typeA1Params = pg.generate();
//将参数写入文件a1.properties中，同样使用了Princeton大学封装的文件输出库
        Out out = new Out("a1.properties");
        out.println(typeA1Params);
    }

    @Test
    public void zppowZnTest() {
        PKG pkg=new PKG();
        A1PKG a1PKG=new A1PKG();
        Element test=a1PKG.getg().powZn(pkg.getZp().newRandomElement());
    }


    @Test
    public void fileTest(){
        String toDir = Configure.FILEROOT + FileOp.getPrefix("audit3.txt") +"abc".hashCode()+ "Blks";
        File dir = new File(toDir);
        File blkFile;
        System.out.println("文件名："+toDir);
        System.out.println("文件夹路径："+dir.getPath());
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("文件夹存在性："+dir.exists());
        }
    }

    @Test
    public void singleAuditTest(){
        //n:审计的用户数量，s：单个用户审计的数量，z：单个审计任务质询的块数
        int n = 1, s = 2, z = 250;
        PKG pkg = new PKG();
        GM gm = new GM(pkg);
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(pkg, gm));
        }

        long start, end;
        long avg_1 = 0L, avg_2 = 0L;

        System.out.println("本次测试共"+n+"个用户，单个用户审计任务数量"+s+"，单个审计任务质询块数"+z+"；");

        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");
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
        start = System.currentTimeMillis();
        Map<Integer, List<Proof>> proofLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Proof> proofList = new ArrayList<>();
            for (int j = 0; j < s; j++) {
                proofList.add(cs.proofGen(pkg, dos.get(i), chalLists.get(i).get(j)));
            }
            proofLists.put(i, proofList);
        }
        StdOut.println("证明已生成！");
        avg_1 = System.currentTimeMillis()-start;
        System.out.println("平均每个审计任务证明生成耗时:"+(avg_1/1000.0/ (s * n)) + "s");
        StdOut.println("-------------------------------------");
        StdOut.println("开始验证...");
        start = System.currentTimeMillis();
        boolean result = batchAudit(pkg, gm, dos, chalLists, proofLists);
        end = System.currentTimeMillis();
        avg_2 = (end - start);
        StdOut.println("验证已完成！");
        StdOut.println("验证结果：" + result);
        StdOut.println("-------------------------------------");
        StdOut.println("平均每个审计任务验证过程耗时：" + (avg_2 / 1000.0 / (s * n)) + "s");
    }
    public static void main(String[] args) {
        int n = 10, s = 10, z = 250;
        PKG pkg = new PKG();
        GM gm = new GM(pkg);
        CSP cs = new CSP(pkg);
        List<DO> dos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dos.add(new DO(pkg, gm));
        }

        long start, end;
        long avg_1 = 0L, avg_2 = 0L;

        StdOut.println("-------------------------------------");
        StdOut.println("文件开始预处理...");
        for (int i = 0; i < n; i++) {
            dos.get(i).getShards(pkg, "audit3.txt", 128);
//            dos.get(i).getShards(pkg, "source.txt", 64000);
        }
        StdOut.println("文件预处理完成！");
        StdOut.println("-------------------------------------");
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
        Map<Integer, List<Proof>> proofLists = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Proof> proofList = new ArrayList<>();
            for (int j = 0; j < s; j++) {
                proofList.add(cs.proofGen(pkg, dos.get(i), chalLists.get(i).get(j)));
            }
            proofLists.put(i, proofList);
        }
        StdOut.println("证明已生成！");
        StdOut.println("-------------------------------------");
        StdOut.println("开始验证...");
        start = System.currentTimeMillis();
        boolean result = batchAudit(pkg, gm, dos, chalLists, proofLists);
        end = System.currentTimeMillis();
        avg_2 += (end - start);
        StdOut.println("验证已完成！");
        StdOut.println("验证结果：" + result);
        StdOut.println("-------------------------------------");
        StdOut.println("验证过程耗费了：" + (avg_2 / 1000.0 / (s * n)) + "s");
    }

    public static boolean batchAudit(PKG pkg, GM gm, List<DO> dos, Map<Integer, List<Chal>> chalLists,  Map<Integer, List<Proof>> proofLists) {
        Element left = pkg.getGT().newOneElement();
        for (int i = 0; i < dos.size(); i++) {
            Element tp = pkg.getG1().newOneElement();
            List<Proof> tmp = proofLists.get(i);
            for (int j = 0; j < tmp.size(); j++) {
                tp = tp.mul(tmp.get(j).getTagProof()).getImmutable();
            }
            left = left.mul(pkg.getPairing().pairing(tp, dos.get(i).getGamma())).getImmutable();
        }

        Element dbi = pkg.getG1().newOneElement();
        List<Item> items = dos.get(0).getEact().getMetadata().get(chalLists.get(0).get(0).getFileName());
        for (int i = 0; i < dos.size(); i++) {
            List<Chal> chals = chalLists.get(i);
            for (int j = 0; j < chals.size(); j++) {
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
        for (int i = 0; i < dos.size(); i++) {
            List<Proof> tmp = proofLists.get(i);
            for (int j = 0; j < tmp.size(); j++) {
                dp = dp.add(tmp.get(j).getDataProof()).getImmutable();
            }
        }
        Element right = dbi.mul(pkg.getPairing().pairing(pkg.getg2().powZn(dp), gm.getGpk())).getImmutable();

        return left.isEqual(right);
    }

    public static boolean fabricBatchAudit(PKG pkg,List<DO> dos,Map<Integer, List<Chal>> chalLists,  Map<Integer, List<Proof>> proofLists){
        Element sigma = pkg.getG1().newOneElement().getImmutable();
        Element right = pkg.getGT().newOneElement().getImmutable();
        Element Kphik;
        Element Kmuik;
        Element Kpair;

        for (int i=0;i<dos.size();++i){
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

    public static boolean dredasBatchAudit(PKG pkg, List<DO> dos, Map<Integer, List<Chal>> chalLists,  Map<Integer, DredasProof> proofLists) {
        Element left;
        Element right = pkg.getGT().newOneElement().getImmutable();
        Element sigma = pkg.getG1().newOneElement().getImmutable();
        Element KRandk = pkg.getG1().newOneElement().getImmutable();

        Element Kphik;
        Element Kmuik;
        Element KRightLeftk;
        Element KPairk;

        DredasProof dredasProof;


        for (int i = 0; i < dos.size(); i++) {
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

    public static boolean paperBatchAudit(PKG pkg,A1PKG a1PKG, List<DO> dos, Map<Integer, List<Chal>> chalLists,  Map<Integer, List<Element>> proofLists) {
        Element left = a1PKG.getG1().newOneElement().getImmutable();
        Element tempTi;


        for (int i = 0; i < dos.size(); i++) {
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
        for (int i = 0; i < dos.size(); i++) {
            List<Element> tmp = proofLists.get(i);
            for (int j = 0; j < tmp.size(); j++) {
                dp = dp.add(tmp.get(j)).getImmutable();
            }
        }
        Element right = a1PKG.getg().powZn(dp).getImmutable();

        return left.isEqual(right);
    }
}
