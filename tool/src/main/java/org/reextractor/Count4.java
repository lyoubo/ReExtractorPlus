package org.reextractor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.reextractor.dto.EntityMatchingResults;

import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Count4 {

    public static void main(String[] args) {
        List<EntityMatchingResults.Entity> commonTPs = new ArrayList<>();
        List<EntityMatchingResults.Entity> commonFPs = new ArrayList<>();
        List<EntityMatchingResults.Entity> ourTPs = new ArrayList<>();
        List<EntityMatchingResults.Entity> ourFPs = new ArrayList<>();
        List<EntityMatchingResults.Entity> baselineTPs = new ArrayList<>();
        List<EntityMatchingResults.Entity> baselineFPs = new ArrayList<>();
        String[] projects = new String[]{
                "checkstyle",
                "commons-io",
                "commons-lang",
                "elasticsearch",
                "flink",
                "hadoop",
                "hibernate-orm",
                "hibernate-search",
                "intellij-community",
                "javaparser",
                "jetty.project",
                "jgit",
                "junit4",
                "junit5",
                "lucene-solr",
                "mockito",
                "okhttp",
                "pmd",
                "spring-boot",
                "spring-framework",
//                "data"
        };
        for (String projectName : projects) {
            new Count4().start(projectName, commonTPs, commonFPs, ourTPs, ourFPs, baselineTPs, baselineFPs);
        }
        int commonTP = 0;
        int commonFP = 0;
        int ourTP = 0;
        int ourFP = 0;
        int ourFN = 0;
        int baselineTP = 0;
        int baselineFP = 0;
        int baselineFN = 0;

//        String type = "Method";
        for (EntityMatchingResults.Entity entity : commonTPs) {
//            if (entity.getLeftSideLocation().getType().equals(type))
                commonTP++;
        }
        for (EntityMatchingResults.Entity entity : commonFPs) {
//            if (entity.getLeftSideLocation().getType().equals(type))
                commonFP++;
        }
        for (EntityMatchingResults.Entity entity : ourTPs) {
//            if (entity.getLeftSideLocation().getType().equals(type))
                ourTP++;
        }
        for (EntityMatchingResults.Entity entity : ourFPs) {
//            if (entity.getLeftSideLocation().getType().equals(type))
                ourFP++;
        }
        for (EntityMatchingResults.Entity entity : baselineTPs) {
//            if (entity.getLeftSideLocation().getType().equals(type))
                baselineTP++;
        }
        for (EntityMatchingResults.Entity entity : baselineFPs) {
//            if (entity.getLeftSideLocation().getType().equals(type))
                baselineFP++;
        }
        ourFN = baselineTP;
        baselineFN = ourTP;
        ourTP += commonTP;
        baselineTP += commonTP;
        ourFP += commonFP;
        baselineFP += commonFP;
        int ourMST = ourFP + ourFN;
        int baselineMST = baselineFP + baselineFN;
        System.out.print(ourMST);
        System.out.print("\t");
        System.out.print(ourFP);
        System.out.print("\t");
        System.out.print(ourFN);
        System.out.print("\t");

        System.out.print(baselineMST);
        System.out.print("\t");
        System.out.print(baselineFP);
        System.out.print("\t");
        System.out.print(baselineFN);
        System.out.print("\t");

        BigDecimal ourPrecision = new BigDecimal(ourTP * 100.0 / (ourTP + ourFP)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal ourRecall = new BigDecimal(ourTP * 100.0 / (ourTP + ourFN)).setScale(2, RoundingMode.HALF_UP);
        System.out.print(ourPrecision);
        System.out.print("\t");
        System.out.print(ourRecall);
        System.out.print("\t");
        System.out.print(baselineMST);
        System.out.print("\t");
        System.out.print(baselineFP);
        System.out.print("\t");
        System.out.print(baselineFN);
        System.out.print("\t");
        BigDecimal baselinePrecision = new BigDecimal(baselineTP * 100.0 / (baselineTP + baselineFP)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal baselineRecall = new BigDecimal(baselineTP * 100.0 / (baselineTP + baselineFN)).setScale(2, RoundingMode.HALF_UP);
        System.out.print(baselinePrecision);
        System.out.print("\t");
        System.out.print(baselineRecall);
        System.out.print("\t");
        System.out.print(ourMST- baselineMST);
        System.out.print("\t");
        System.out.print(ourFP - baselineFP);
        System.out.print("\t");
        System.out.print(ourFN - baselineFN);
        System.out.print("\t");
        System.out.print(ourPrecision.subtract(baselinePrecision).stripTrailingZeros().toPlainString());
        System.out.print("\t");
        System.out.print(ourRecall.subtract(baselineRecall).stripTrailingZeros().toPlainString());
        System.out.print("\t");
        System.out.println();
    }

    private void start(String projectName, List<EntityMatchingResults.Entity> commonTP, List<EntityMatchingResults.Entity> commonFP, List<EntityMatchingResults.Entity> ourTP, List<EntityMatchingResults.Entity> ourFP, List<EntityMatchingResults.Entity> baselineTP, List<EntityMatchingResults.Entity> baselineFP) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        try {
            String filePath = "E:\\contributions\\TSE2024\\ReExtractorPlus\\data\\entity matching\\" + projectName + ".json";
            FileReader reader = new FileReader(filePath);
            EntityMatchingResults results = gson.fromJson(reader, EntityMatchingResults.class);
            List<EntityMatchingResults.Result> res = results.getResults();
            for (EntityMatchingResults.Result result : res) {
                List<EntityMatchingResults.Entity> commonRefactoring = result.getCommonMatching();
                for (EntityMatchingResults.Entity entity : commonRefactoring) {
                    if (entity.isValidation()) {
                        commonTP.add(entity);
                    } else {
                        commonFP.add(entity);
                    }
                }
                List<EntityMatchingResults.Entity> ourApproach = result.getOurApproach();
                for (EntityMatchingResults.Entity entity : ourApproach) {
                    if (entity.isValidation()) {
                        ourTP.add(entity);
                    } else {
                        ourFP.add(entity);
                    }
                }
                List<EntityMatchingResults.Entity> baseline = result.getBaseline();
                for (EntityMatchingResults.Entity entity : baseline) {
                    if (entity.isValidation()) {
                        baselineTP.add(entity);
                    } else {
                        baselineFP.add(entity);
                    }
                }
//                System.out.println(commonTP + ourTP);
//                System.out.println(commonFP + ourFP);
//                System.out.println(baselineTP);
//                System.out.println(commonTP + baselineTP);
//                System.out.println(commonFP + baselineFP);
//                System.out.println(ourTP);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
