package com.sinigr.eventmap;

import java.util.ArrayList;

class Calculations {

    private static ArrayList<int[]> chains = new ArrayList<int[]>();  //Цепочки для построения вариантов
    public static ArrayList<boolean[]> variants = new ArrayList<boolean[]>();  //Варианты
    static ArrayList<SimpleNode> nodes;
    static int calculatedCount = 0;

    /** Построение всех возможных вариантов */
    static void startCalculations() {
        buildResultsArray();
        calcProbabilitys();
        buildChains(new Integer[nodes.size()], 0);
        buildVariants();
        MainScreen.getInstance().createVariants(variants.size());
        printVariants();
    }

    /** Построение массива результатов */
    private static void buildResultsArray() {
        MainScreen main = MainScreen.getInstance();
        nodes = new ArrayList<SimpleNode>();
        for(int i = 0; i < main.nodes.size(); i++) {
            Node node = main.nodes.get(i);
            nodes.add(new SimpleNode(node));
        }
        for(int i = 0; i < main.nodes.size(); i++) {
            Node node = main.nodes.get(i);
            SimpleNode sn = nodes.get(i);
            for(int k = 0; k < main.nodes.size(); k++) {
                if(k != i) {
                    if(node.in1!= null && node.in1.equals(main.nodes.get(k))) {
                        sn.in1 = nodes.get(k);
                        continue;
                    }
                    if(node.in2!= null && node.in2.equals(main.nodes.get(k))) {
                        sn.in2 = nodes.get(k);
                        continue;
                    }
                    if(node.out1!= null && node.out1.equals(main.nodes.get(k))) {
                        sn.out1 = nodes.get(k);
                        continue;
                    }
                    if(node.out2!= null && node.out2.equals(main.nodes.get(k))) {
                        sn.out2 = nodes.get(k);
                        continue;
                    }
                }
            }
        }
    }

    /** Расчет вероятностей */
    private static void calcProbabilitys() {
        SimpleNode first = findFirstNode();
        caclNodeProbability(first, 100);
    }

    /** Найти начальный узел */
    private static SimpleNode findFirstNode() {
        SimpleNode sn = null;
        for(SimpleNode node: nodes) {
            if(node.in1 == null && node.in2 == null) sn = node;
        }
        return sn;
    }

    /** Расчёт вероятности для узла */
    private static void caclNodeProbability(SimpleNode node, double probability) {
        node.visited++;
        int ins = getInCount(node);
        if(ins == 0 || ins == 1) {
            calculatedCount ++ ;
            node.resultProb = node.baseProb * probability / 100D;
            if(calculatedCount == nodes.size()) setProbabilities();
            if(node.out1 != null) caclNodeProbability(node.out1, node.resultProb);
            if(node.out2 != null) caclNodeProbability(node.out2, node.resultProb);
        } else {
            if(node.visited == 1) node.tempProb = probability;
             else {
                calculatedCount ++ ;
                if(node.type.equals("event")) node.resultProb = probability * node.tempProb/100D;
                else if(node.type.equals("or")) node.resultProb = (100-(100-node.tempProb)*(100 - probability)/100D);
                else if(node.type.equals("xor")) node.resultProb = node.tempProb*(100-probability) /100D + probability*(100-node.tempProb) /100D;
                if(calculatedCount == nodes.size()) setProbabilities();
                if(node.out1 != null) caclNodeProbability(node.out1, node.resultProb);
                if(node.out2 != null) caclNodeProbability(node.out2, node.resultProb);
            }
        }

    }

    /** Подставить значения вероятностей */
    private static void setProbabilities() {
        for(int i = 0; i < nodes.size(); i++) {
            Node node = MainScreen.getInstance().nodes.get(i);
            if (node instanceof Event)  {
                Event event = (Event) node;
                event.probability = (int)nodes.get(i).resultProb;
            }
        }
    }

    /** Подсчёт количества исходящих из узла связей */
    private static int getOutCount(SimpleNode node) {
        int outs = 0;
        if(node.out1 != null) outs ++;
        if(node.out2 != null) outs ++;
        return outs;
    }

    /** Подсчёт количества входящих в узел связей */
    private static int getInCount(SimpleNode node) {
        int ins = 0;
        if(node.in1 != null) ins ++;
        if(node.in2 != null) ins ++;
        return ins;
    }

    /** Построение цепочек для вариантов*/
    public static void buildChains(Integer[] array, int order) {
        int[] numbs = {0, 1, 2}; // используемый алфавит
        if (order < array.length){
            for (int k = 0; k < getIndex(nodes.get(order)); k++) {
                SimpleNode node = nodes.get(order);
                String type = node.type;
                array[order] = numbs[k];
                if(type.equals("event")) {
                    if(node.baseProb == 100) array[order] = 1;
                }
                buildChains(array, order+1);
            }
        }
        else {
            int[] tmp = new int[array.length];
            for(int i = 0; i < tmp.length; i++) tmp[i] = array[i];
            chains.add(tmp);
        }
    }

    private static int getIndex(SimpleNode node) {
        if(node.type.equals("event")) {
            if(node.baseProb== 100 || node.resultProb == 0) return 1;
            return 2;
        } else if(node.type.equals("or")) {
            if(getInCount(node) == 1) return 3;
            return 1;
        } else if(node.type.equals("xor")) {
            if(getInCount(node) == 1) return 2;
        }
        return 1;
    }

    /** Построение возможных вариантов */
    private static void buildVariants() {
        for(int i = 0; i< chains.size(); i++) {
            setNodesValues(i);
            setTempProbabilities(findFirstNode(), 100);
            for(SimpleNode node: nodes) node.visited = 0;
            buildVariant(findFirstNode(), true);
            variants.add(getVariantArray());
        }
    }

    /** Установка вспомогательных значений вероятности */
    private static void setTempProbabilities(SimpleNode node, double probability) {
        node.visited++;
        int ins = getInCount(node);
        if((ins == 0 || ins == 1) ) {
            if(node.type.equals("event")) {
                node.variantProb = node.baseProb * probability / 100D;
                if(node.out1 != null) setTempProbabilities(node.out1, node.variantProb);
                if(node.out2 != null) setTempProbabilities(node.out2, node.variantProb);
            } else if(node.type.equals("or"))  {
                node.variantProb = node.baseProb * probability / 100D;
                if(node.out1 != null) setTempProbabilities(node.out1, node.variantProb*((node.value == 0 || node.value == 2) ? 100 : 0));
                if(node.out2 != null) setTempProbabilities(node.out2, node.variantProb*((node.value == 1 || node.value == 2) ? 100 : 0));
            } else if(node.type.equals("xor"))  {
                node.variantProb = node.baseProb * probability / 100D;
                if(node.out1 != null) setTempProbabilities(node.out1, node.variantProb*((node.value == 0)? 100 : 0));
                if(node.out2 != null) setTempProbabilities(node.out2, node.variantProb*((node.value == 1)? 100 : 0));
            }
        } else {
            if(node.visited == 1) node.tempProb = probability;
            else {
                node.variantProb = node.tempProb*(100-probability) /100D + probability*(100-node.tempProb) /100D;
                if(node.out1 != null) setTempProbabilities(node.out1, node.variantProb);
                if(node.out2 != null) setTempProbabilities(node.out2, node.variantProb);
            }
        }
    }

    /** Печать в консоль содержимого массива Variant */
    private static boolean[] getVariantArray() {
        boolean[] array = new boolean[nodes.size()];
        for(int i =0; i < nodes.size(); i++) {
            SimpleNode node = nodes.get(i);
            array[i] = node.visible;
        }
        return array;
    }

    /** Построение варианта развития событий */
    private static void buildVariant(SimpleNode node, boolean visible) {
        node.visited++;
        int ins = getInCount(node);
        if((ins == 0 || ins == 1) ) {
            if(node.type.equals("event")) {
                node.visible = node.value == 1 && visible;
                if(node.out1 != null) buildVariant(node.out1, node.visible);
                if(node.out2 != null) buildVariant(node.out2, node.visible);
            } else if(node.type.equals("or"))  {
                node.visible = visible;
                if(node.out1 != null) buildVariant(node.out1, visible &&(node.value == 0 || node.value == 2));
                if(node.out2 != null) buildVariant(node.out2, visible &&(node.value == 1 || node.value == 2));
            } else if(node.type.equals("xor"))  {
                node.visible = visible;
                if(node.out1 != null) buildVariant(node.out1, visible &&(node.value == 0));
                if(node.out2 != null) buildVariant(node.out2, visible &&(node.value == 1));
            }
        } else {
            if(node.visited == 1) node.visible = visible;
            else {
                node.visible = node.visible || visible;
                if(node.out1 != null) buildVariant(node.out1, node.visible);
                if(node.out2 != null) buildVariant(node.out2, node.visible);
            }
        }
    }

    /** Установить значения для узлов */
    private static void setNodesValues(int numb) {
        for(int i = 0; i < nodes.size(); i++) {
            SimpleNode node = nodes.get(i);
            node.visited = 0;
            int[] chain = chains.get(numb);
            node.value = chain[i];
            if (node.variantProb == 100) node.value = 1;
        }
    }

    /** Содержимое массива variants в консоль */
    private static void printVariants() {
        for(int i = 0; i < variants.size(); i++) {
            boolean[] array = variants.get(i);
            for(int k = 0; k < array.length; k++) {
                System.out.print(array[k] + " ");
            }
            System.out.println();
        }
    }
}
