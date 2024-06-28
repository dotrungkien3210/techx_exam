package project.pdfToElastic.core.extract.FunctionSupport;

import java.util.*;


public class CollectionsProcessing {
    public List<String> mergeAdjacentElements(List<String> originalList) {
        List<String> mergedList = new ArrayList<>();

        // Iterate through original list to merge adjacent elements
        for (int i = 0; i < originalList.size() - 1; i++) {
            String mergedItem = originalList.get(i) + "  " + originalList.get(i + 1).trim();;
            mergedList.add(mergedItem);
        }

        return mergedList;
    }

    public void contentsFormatted(String tableOfContents){

//
        List<String> filteredLines = new ArrayList<>();

        String removeDot = tableOfContents.replaceAll("\\.", "").replace("Table of Contents", "").replaceFirst("\n","");
        List<String> linesList = Arrays.asList(tableOfContents.split("\n"));
        for (String line : linesList) {
            if (line.contains(".....")) {
                filteredLines.add(line.replaceAll("\\.", "").replaceFirst("\n",""));
            }
        }


        List<String> finalFormat = reFormatList(filteredLines);
        System.out.println(finalFormat);
    }

    public List<String> reFormatList(List<String> list){
        CollectionsProcessing listMapProcessing = new CollectionsProcessing();
        List<String> newList = listMapProcessing.mergeAdjacentElements(list);
        return newList;
    }
}
