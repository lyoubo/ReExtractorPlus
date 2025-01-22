package org.reextractor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.reextractor.dto.EntityMatchingResults;
import org.reextractor.dto.RefactoringOracle;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Count5 {

    public static void main(String[] args) {
        String[] projects = new String[]{
                "data"
        };
        for (String projectName : projects) {
            new Count5().start(projectName);
        }

    }

    private void start(String projectName) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        try {
            String filePath1 = "E:\\contributions\\TSE2024\\ReExtractorPlus\\tool\\data2\\refactoring detection\\" + projectName + ".json";
            String filePath2 = "E:\\contributions\\TSE2024\\ReExtractorPlus\\data\\refactoring detection\\RefactoringMiner Oracle\\" + projectName + ".json";
            FileReader reader1 = new FileReader(filePath1);
            FileReader reader2 = new FileReader(filePath2);
            RefactoringOracle results1 = gson.fromJson(reader1, RefactoringOracle.class);
            RefactoringOracle results2 = gson.fromJson(reader2, RefactoringOracle.class);
            List<RefactoringOracle.Result> res1 = results1.getResults();
            List<RefactoringOracle.Result> res2 = results2.getResults();

            for (RefactoringOracle.Result result1 : res1) {
                boolean found1 = false;
                List<RefactoringOracle.Refactoring> ref1 = new ArrayList<>();
                ref1.addAll(result1.getCommonRefactoring());
                ref1.addAll(result1.getOurApproach());
                ref1.addAll(result1.getBaseline());
                for (RefactoringOracle.Result result2 : res2) {
                    if (result1.getId() == result2.getId()) {
                        found1 = true;
                        List<RefactoringOracle.Refactoring> ref2 = new ArrayList<>();
                        ref2.addAll(result2.getCommonRefactoring());
                        ref2.addAll(result2.getOurApproach());
                        ref2.addAll(result2.getBaseline());
                        for (RefactoringOracle.Refactoring refactoring1 : ref1) {
                            boolean found2 = false;
                            for (RefactoringOracle.Refactoring refactoring2 : ref2) {
                                if (refactoring1.getDescription().equals(refactoring2.getDescription())) {
                                    refactoring1.setValidation(refactoring2.isValidation());
                                    String jsonString = gson.toJson(results1, RefactoringOracle.class);
                                    BufferedWriter out = new BufferedWriter(new FileWriter(filePath1));
                                    out.write(jsonString);
                                    out.close();
                                    found2 = true;
                                    break;
                                }
                            }
                            if (!found2) {
                                System.out.println(result1.getRepository() + "\t" + result1.getSha1() + "\t" + refactoring1.getDescription());
                            }
                        }
                        break;
                    }
                }
                if (!found1) {
                    System.out.println(result1.getRepository() + "\t" + result1.getSha1() + "\t");
                }
            }
            reader1.close();
            reader2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
