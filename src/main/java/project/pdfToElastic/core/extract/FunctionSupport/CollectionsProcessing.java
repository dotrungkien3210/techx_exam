package project.pdfToElastic.core.extract.FunctionSupport;

import java.util.*;


public class CollectionsProcessing {
    public List<String> mergeAdjacentElements(List<String> originalList) {
        List<String> mergedList = new ArrayList<>();

        // Iterate through original list to merge adjacent elements
        for (int i = 0; i < originalList.size() - 1; i++) {
            String mergedItem = originalList.get(i) + "  " + originalList.get(i + 1).trim();
            mergedList.add(mergedItem);
        }

        return mergedList;
    }

    public List<String> contentsFormatted(String tableOfContents){
        List<String> filteredLines = new ArrayList<>();

        String[] linesList = tableOfContents.split("\n");
        for (String line : linesList) {
            if (line.contains(".....")) {
                filteredLines.add(line.replaceAll("\\.", "").replaceFirst("\n","").trim());
            }
        }
        return reFormatList(filteredLines);
    }

    public List<String> reFormatList(List<String> list){
        CollectionsProcessing listMapProcessing = new CollectionsProcessing();
        return listMapProcessing.mergeAdjacentElements(list);
    }
}
