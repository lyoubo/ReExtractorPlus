package org.reextractor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.reextractor.dto.RefactoringDetectionResults;
import org.refactoringminer.api.RefactoringType;

import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Count6 {

    /*public static void main(String[] args) {
        String[] refactoringTypes = {
                "RENAME_CLASS",
                "RENAME_METHOD",
                "RENAME_ATTRIBUTE",
                "RENAME_VARIABLE",
                "MOVE_CLASS",
                "MOVE_RENAME_CLASS",
                "MOVE_OPERATION",
                "MOVE_AND_RENAME_OPERATION",
                "MOVE_ATTRIBUTE",
                "MOVE_RENAME_ATTRIBUTE",
                "PULL_UP_OPERATION",
                "PUSH_DOWN_OPERATION",
                "PULL_UP_ATTRIBUTE",
                "PUSH_DOWN_ATTRIBUTE",
                "EXTRACT_OPERATION",
                "EXTRACT_AND_MOVE_OPERATION",
                "EXTRACT_CLASS",
                "EXTRACT_SUPERCLASS",
                "EXTRACT_SUBCLASS",
                "EXTRACT_INTERFACE",
                "EXTRACT_VARIABLE",
                "INLINE_OPERATION",
                "MOVE_AND_INLINE_OPERATION",
                "INLINE_VARIABLE",
                "CHANGE_TYPE_DECLARATION_KIND",
                "CHANGE_RETURN_TYPE",
                "CHANGE_ATTRIBUTE_TYPE",
                "CHANGE_VARIABLE_TYPE"
        };
        for (String refactoringType : refactoringTypes) {
            temp(refactoringType);
            System.out.println();
        }
    }*/

    public static void main(String[] args) {
        List<RefactoringDetectionResults.Refactoring> commonTPs = new ArrayList<>();
        List<RefactoringDetectionResults.Refactoring> commonFPs = new ArrayList<>();
        List<RefactoringDetectionResults.Refactoring> ourTPs = new ArrayList<>();
        List<RefactoringDetectionResults.Refactoring> ourFPs = new ArrayList<>();
        List<RefactoringDetectionResults.Refactoring> baselineTPs = new ArrayList<>();
        List<RefactoringDetectionResults.Refactoring> baselineFPs = new ArrayList<>();
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
        };
        for (String projectName : projects) {
            new Count6().start(projectName, commonTPs, commonFPs, ourTPs, ourFPs, baselineTPs, baselineFPs);
        }
        int commonTP = 0;
        int commonFP = 0;
        int ourTP = 0;
        int ourFP = 0;
        int ourFN = 0;
        int baselineTP = 0;
        int baselineFP = 0;
        int baselineFN = 0;

        String type = "RENAME_CLASS";
        for (RefactoringDetectionResults.Refactoring refactoring : commonTPs) {
            if (refactoring.getType().equals(type))
                commonTP++;
        }
        for (RefactoringDetectionResults.Refactoring refactoring : commonFPs) {
            if (refactoring.getType().equals(type))
                commonFP++;
        }
        for (RefactoringDetectionResults.Refactoring refactoring : ourTPs) {
            if (refactoring.getType().equals(type))
                ourTP++;
        }
        for (RefactoringDetectionResults.Refactoring refactoring : ourFPs) {
            if (refactoring.getType().equals(type))
                ourFP++;
        }
        for (RefactoringDetectionResults.Refactoring refactoring : baselineTPs) {
            if (refactoring.getType().equals(type))
                baselineTP++;
        }
        for (RefactoringDetectionResults.Refactoring refactoring : baselineFPs) {
            if (refactoring.getType().equals(type))
                baselineFP++;
        }
        ourFN = baselineTP;
        baselineFN = ourTP;
        ourTP += commonTP;
        baselineTP += commonTP;
        ourFP += commonFP;
        baselineFP += commonFP;
        System.out.print(" & ");
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
        System.out.print("\\emph{" + recall_ratio.stripTrailingZeros().toPlainString() + "\\%}) \\\\");
    }

    private void start(String projectName, List<RefactoringDetectionResults.Refactoring> commonTP, List<RefactoringDetectionResults.Refactoring> commonFP, List<RefactoringDetectionResults.Refactoring> ourTP, List<RefactoringDetectionResults.Refactoring> ourFP, List<RefactoringDetectionResults.Refactoring> baselineTP, List<RefactoringDetectionResults.Refactoring> baselineFP) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        try {
            String filePath = "E:\\contributions\\TSE2024\\ReExtractorPlus\\data\\refactoring detection\\ref-Dataset\\" + projectName + ".json";
            FileReader reader = new FileReader(filePath);
            RefactoringDetectionResults results = gson.fromJson(reader, RefactoringDetectionResults.class);
            List<RefactoringDetectionResults.Result> res = results.getResults();
            for (RefactoringDetectionResults.Result result : res) {
                List<RefactoringDetectionResults.Refactoring> commonRefactoring = result.getCommonRefactoring();
                for (RefactoringDetectionResults.Refactoring refactoring : commonRefactoring) {
                    if (refactoring.isValidation()) {
                        commonTP.add(refactoring);
                    } else {
                        commonFP.add(refactoring);
                    }
                }
                List<RefactoringDetectionResults.Refactoring> ourApproach = result.getOurApproach();
                for (RefactoringDetectionResults.Refactoring refactoring : ourApproach) {
                    if (refactoring.isValidation()) {
                        ourTP.add(refactoring);
                    } else {
                        ourFP.add(refactoring);
                    }
                }
                List<RefactoringDetectionResults.Refactoring> baseline = result.getBaseline();
                for (RefactoringDetectionResults.Refactoring refactoring : baseline) {
                    if (refactoring.isValidation()) {
                        baselineTP.add(refactoring);
                    } else {
                        baselineFP.add(refactoring);
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
