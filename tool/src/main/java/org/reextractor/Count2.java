package org.reextractor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.reextractor.dto.RefactoringDetectionResults;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Count2 {

    public static void main(String[] args) {
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
            new Count2().start(projectName);
        }

    }

    private void start(String projectName) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        try {
            String filePath = "E:\\contributions\\TSE2024\\ReExtractorPlus\\data\\refactoring detection\\ref-Dataset\\" + projectName + ".json";
            FileReader reader = new FileReader(filePath);
            RefactoringDetectionResults results = gson.fromJson(reader, RefactoringDetectionResults.class);
            List<RefactoringDetectionResults.Result> res = results.getResults();
            for (RefactoringDetectionResults.Result result : res) {
                int commonTP = 0;
                int commonFP = 0;
                int ourTP = 0;
                int ourFP = 0;
                int ourFN = 0;
                int baselineTP = 0;
                int baselineFP = 0;
                int baselineFN = 0;
                List<RefactoringDetectionResults.Refactoring> commonRefactoring = result.getCommonRefactoring();
                for (RefactoringDetectionResults.Refactoring refactoring : commonRefactoring) {
                    if (refactoring.isValidation()) {
                        commonTP ++;
                    } else {
                        commonFP ++;
                    }
                }
                List<RefactoringDetectionResults.Refactoring> ourApproach = result.getOurApproach();
                for (RefactoringDetectionResults.Refactoring refactoring : ourApproach) {
                    if (refactoring.isValidation()) {
                        ourTP ++;
                    } else {
                        ourFP ++;
                    }
                }
                List<RefactoringDetectionResults.Refactoring> baseline = result.getBaseline();
                for (RefactoringDetectionResults.Refactoring refactoring : baseline) {
                    if (refactoring.isValidation()) {
                        baselineTP ++;
                    } else {
                        baselineFP ++;
                    }
                }
                ourFN = baselineTP;
                baselineFN = ourTP;
                ourTP += commonTP;
                baselineTP += commonTP;
                ourFP += commonFP;
                baselineFP += commonFP;
                System.out.print(ourTP);
                System.out.print("\t");
                System.out.print(ourFP);
                System.out.print("\t");
                System.out.print(ourFN);
                System.out.print("\t");
                System.out.print(baselineTP);
                System.out.print("\t");
                System.out.print(baselineFP);
                System.out.print("\t");
                System.out.print(baselineFN);
                System.out.println();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
