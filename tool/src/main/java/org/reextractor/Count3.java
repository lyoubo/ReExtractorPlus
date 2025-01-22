package org.reextractor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.reextractor.dto.EntityMatchingResults;
import org.reextractor.dto.RefactoringDetectionResults;

import java.io.FileReader;
import java.util.List;

public class Count3 {

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
            new Count3().start(projectName);
        }

    }

    private void start(String projectName) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        try {
            String filePath = "E:\\contributions\\TSE2024\\ReExtractorPlus\\tool\\data2\\entity matching\\" + projectName + ".json";
            FileReader reader = new FileReader(filePath);
            EntityMatchingResults results = gson.fromJson(reader, EntityMatchingResults.class);
            List<EntityMatchingResults.Result> res = results.getResults();
            for (EntityMatchingResults.Result result : res) {
                int commonTP = 0;
                int commonFP = 0;
                int ourTP = 0;
                int ourFP = 0;
                int ourFN = 0;
                int baselineTP = 0;
                int baselineFP = 0;
                int baselineFN = 0;
                List<EntityMatchingResults.Entity> commonMatching = result.getCommonMatching();
                for (EntityMatchingResults.Entity entity : commonMatching) {
                    if (entity.isValidation()) {
                        commonTP ++;
                    } else {
                        commonFP ++;
                    }
                }
                List<EntityMatchingResults.Entity> ourApproach = result.getOurApproach();
                for (EntityMatchingResults.Entity entity : ourApproach) {
                    if (entity.isValidation()) {
                        ourTP ++;
                    } else {
                        ourFP ++;
                    }
                }
                List<EntityMatchingResults.Entity> baseline = result.getBaseline();
                for (EntityMatchingResults.Entity entity : baseline) {
                    if (entity.isValidation()) {
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
//                System.out.print(baselineTP);
//                System.out.print("\t");
//                System.out.print(baselineFP);
//                System.out.print("\t");
//                System.out.print(baselineFN);
                System.out.println();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
