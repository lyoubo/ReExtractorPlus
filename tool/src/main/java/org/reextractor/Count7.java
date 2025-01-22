package org.reextractor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.reextractor.dto.RefactoringDetectionResults;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Count7 {

    public static void main(String[] args) {
        List<RefactoringDetectionResults.Refactoring> commonTPs = new ArrayList<>();
        List<RefactoringDetectionResults.Refactoring> commonFPs = new ArrayList<>();
        List<RefactoringDetectionResults.Refactoring> ourTPs = new ArrayList<>();
        List<RefactoringDetectionResults.Refactoring> ourFPs = new ArrayList<>();
        List<RefactoringDetectionResults.Refactoring> baselineTPs = new ArrayList<>();
        List<RefactoringDetectionResults.Refactoring> baselineFPs = new ArrayList<>();
        String[] projects = new String[]{
//                "checkstyle",
//                "commons-io",
//                "commons-lang",
//                "elasticsearch",
//                "flink",
//                "hadoop",
//                "hibernate-orm",
//                "hibernate-search",
//                "intellij-community",
                "javaparser",
//                "jetty.project",
//                "jgit",
//                "junit4",
//                "junit5",
//                "lucene-solr",
//                "mockito",
//                "okhttp",
//                "pmd",
//                "spring-boot",
//                "spring-framework",
        };
        for (String projectName : projects) {
            System.out.println(projectName);
            new Count7().start(projectName, commonTPs, commonFPs, ourTPs, ourFPs, baselineTPs, baselineFPs);
        }
        int commonTP = 0;
        int commonFP = 0;
        int ourTP = 0;
        int ourFP = 0;
        int ourFN = 0;
        int baselineTP = 0;
        int baselineFP = 0;
        int baselineFN = 0;

//        String type = "MOVE_CLASS";
        for (RefactoringDetectionResults.Refactoring refactoring : commonTPs) {
//            if (refactoring.getType().equals(type))
                commonTP++;
        }
        for (RefactoringDetectionResults.Refactoring refactoring : commonFPs) {
//            if (refactoring.getType().equals(type))
                commonFP++;
        }
        for (RefactoringDetectionResults.Refactoring refactoring : ourTPs) {
//            if (refactoring.getType().equals(type))
                ourTP++;
        }
        for (RefactoringDetectionResults.Refactoring refactoring : ourFPs) {
//            if (refactoring.getType().equals(type))
                ourFP++;
        }
        for (RefactoringDetectionResults.Refactoring refactoring : baselineTPs) {
//            if (refactoring.getType().equals(type))
                baselineTP++;
        }
        for (RefactoringDetectionResults.Refactoring refactoring : baselineFPs) {
//            if (refactoring.getType().equals(type))
                baselineFP++;
        }
        ourFN = baselineTP;
        baselineFN = ourTP;
        ourTP += commonTP;
        baselineTP += commonTP;
        ourFP += commonFP;
        baselineFP += commonFP;
        System.out.println(baselineTP);
        System.out.println(baselineFP);
        System.out.println(baselineFN);
        /*System.out.print(" & ");
        System.out.print(ourTP);
        BigDecimal ourPrecision = new BigDecimal(ourTP * 100.0 / (ourTP + ourFP)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal ourRecall = new BigDecimal(ourTP * 100.0 / (ourTP + ourFN)).setScale(2, RoundingMode.HALF_UP);
        System.out.print(" & ");
        System.out.print("" + ourPrecision.stripTrailingZeros().toPlainString() + "\\%");
        System.out.print(" & ");
        System.out.print("" + ourRecall.stripTrailingZeros().toPlainString() + "\\%");
        System.out.print(" & ");
        System.out.print(baselineTP);
        BigDecimal baselinePrecision = new BigDecimal(baselineTP * 100.0 / (baselineTP + baselineFP)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal baselineRecall = new BigDecimal(baselineTP * 100.0 / (baselineTP + baselineFN)).setScale(2, RoundingMode.HALF_UP);
        System.out.print(" & ");
        System.out.print("" + baselinePrecision.stripTrailingZeros().toPlainString() + "\\%");
        System.out.print(" & ");
        System.out.print("" + baselineRecall.stripTrailingZeros().toPlainString() + "\\%");
        System.out.print(" & ");
        System.out.print("\\emph{" + (ourTP - baselineTP) + "}\\,(") ;
        BigDecimal tp_ratio = new BigDecimal((ourTP - baselineTP) * 100.0 / baselineTP).setScale(1, RoundingMode.HALF_UP);
        System.out.print("\\emph{" + tp_ratio.stripTrailingZeros().toPlainString() + "\\%})");
        System.out.print(" & ");
        System.out.print("\\emph{" + ourPrecision.subtract(baselinePrecision).stripTrailingZeros().toPlainString() + "\\%}\\,(");
        BigDecimal precision_ratio = ourPrecision.subtract(baselinePrecision).multiply(new BigDecimal(100)).divide(baselinePrecision, 1, RoundingMode.HALF_UP);
        System.out.print("\\emph{" + precision_ratio.stripTrailingZeros().toPlainString() + "\\%})");
        System.out.print(" & ");
        System.out.print("\\emph{" + ourRecall.subtract(baselineRecall).stripTrailingZeros().toPlainString() + "\\%}\\,(");
        BigDecimal recall_ratio = ourRecall.subtract(baselineRecall).multiply(new BigDecimal(100)).divide(baselineRecall, 1, RoundingMode.HALF_UP);
        System.out.print("\\emph{" + recall_ratio.stripTrailingZeros().toPlainString() + "\\%})");*/
    }

    private void start(String projectName, List<RefactoringDetectionResults.Refactoring> commonTP, List<RefactoringDetectionResults.Refactoring> commonFP, List<RefactoringDetectionResults.Refactoring> ourTP, List<RefactoringDetectionResults.Refactoring> ourFP, List<RefactoringDetectionResults.Refactoring> baselineTP, List<RefactoringDetectionResults.Refactoring> baselineFP) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        try {
            String filePath = "E:\\contributions\\TSE2024\\ReExtractorPlus\\data\\refactoring detection\\abc\\" + projectName + ".json";
            FileReader reader = new FileReader(filePath);
            RefactoringDetectionResults results = gson.fromJson(reader, RefactoringDetectionResults.class);
            List<RefactoringDetectionResults.Result> res = results.getResults();
            for (RefactoringDetectionResults.Result result : res) {
                int acommonTP = 0;
                int acommonFP = 0;
                int aourTP = 0;
                int aourFP = 0;
                int aourFN = 0;
                int abaselineTP = 0;
                int abaselineFP = 0;
                int abaselineFN = 0;
                List<RefactoringDetectionResults.Refactoring> commonRefactoring = result.getCommonRefactoring();
                for (RefactoringDetectionResults.Refactoring refactoring : commonRefactoring) {
                    if (refactoring.isValidation()) {
                        commonTP.add(refactoring);
                        acommonTP++;
                    } else {
                        commonFP.add(refactoring);
                        acommonFP++;
                    }
                }
                List<RefactoringDetectionResults.Refactoring> ourApproach = result.getOurApproach();
                for (RefactoringDetectionResults.Refactoring refactoring : ourApproach) {
                    if (refactoring.isValidation()) {
                        ourTP.add(refactoring);
                        aourTP++;
                    } else {
                        ourFP.add(refactoring);
                        aourFP++;
                    }
                }
                List<RefactoringDetectionResults.Refactoring> baseline = result.getBaseline();
                for (RefactoringDetectionResults.Refactoring refactoring : baseline) {
                    if (refactoring.isValidation()) {
                        baselineTP.add(refactoring);
                        abaselineTP++;
                    } else {
                        baselineFP.add(refactoring);
                        abaselineFP++;
                    }
                }
//                System.out.println(acommonTP + aourTP);
//                System.out.println(acommonFP + aourFP);
//                System.out.println(abaselineTP);
                System.out.print(acommonTP + abaselineTP);
                System.out.print("\t");
                System.out.print("\t");
                System.out.println(aourTP);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
