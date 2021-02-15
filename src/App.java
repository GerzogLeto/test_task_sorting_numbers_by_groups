import com.sun.source.tree.Tree;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class App {

    static int badStringCount = 0;
    static int correctStringCount = 0;
    static int allStringAtGroups = 0;
    static int counterGroups = 0;
    static TreeSet<Structure> first = new TreeSet<>(new CoordComp());
    static TreeSet<Structure> second = new TreeSet<>(new CoordComp());
    static TreeSet<Structure> third = new TreeSet<>(new CoordComp());
    static TreeSet<Group> groups = new TreeSet<>(new GroupComp());
    static long[] arrString = new long[3000300];



    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        int count = readFileToList(args[0]);
        for (int i = 0; i < count; i += 3) {
            Group group = new Group();
            Set<Integer> strings = new HashSet<>();
            if(arrString[i] == -1 && arrString[i+1] == -1 && arrString[i+2] == -1){
                strings.add(i);
                group.strings.add(strings);
                group.size = strings.size();
                if(!(groups.add(group))){
                    groups.ceiling(group).strings.add(strings);
                }
                continue;
            }
            int tempSize = strings.size();
            addToGroup(strings, getStructure(i, 1), 1);
            if(tempSize == strings.size()) addToGroup(strings, getStructure(i, 2), 2);
            if(tempSize == strings.size()) addToGroup(strings, getStructure(i, 3), 3);
            while (tempSize != strings.size()){
                tempSize = strings.size();
                adding(strings);
            }
            group.strings.add(strings);
            group.size = strings.size();
            if(!(groups.add(group))){
                groups.ceiling(group).strings.add(strings);
            }
        }

        allStringAtGroups = countStringToGroup(groups);
//        printGroups(groups);
        System.out.println("bad strings: " + badStringCount);
        System.out.println("correct strings: " + correctStringCount);
        System.out.println("all  groups with size more than 1 element: " + counterGroups);
        System.out.println("all strings at groups: " + allStringAtGroups);
        Long finish = System.currentTimeMillis();
        System.out.println("Time: " + ((finish - start) / 1000));
    }
    public static List<String> readTextFile(String fileName){
        Path path = Paths.get(fileName);
        List<String> list = new ArrayList<>();
        try {
            list =  Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static int readFileToList(String path) {
        int i = 0;
        for (String line : readTextFile(path)) {
                String[] arr = line.split(";");
                if (arr.length == 3) {
                    if ((arr[0].matches("\"\\d*\"")) && (arr[1].matches("\"\\d*\"")) &&
                            (arr[2].matches("\"\\d*\""))) {
                        if (arr[0].matches("\"\"")) {
                            arrString[i] = -1;
                        }else{
                            arrString[i] =  (Long.parseLong(arr[0].substring(1, arr[0].length() - 1)));
                        }
                        if (arr[1].matches("\"\"")) {
                            arrString[i+1] = -1;
                        }else{
                            arrString[i+1] = (Long.parseLong(arr[1].substring(1, arr[1].length() - 1)));
                        }
                        if (arr[2].matches("\"\"")) {
                            arrString[i+2] = -1;
                        }else{
                            arrString[i+2] = (Long.parseLong(arr[2].substring(1, arr[2].length() - 1)));
                        }
                        correctStringCount++;
                        if (arrString[i] != -1) {
                            Structure structure = new Structure();
                            structure.number = arrString[i];
                            if (!(first.add(structure))) {
                                first.ceiling(structure).eq.add(i);
                            }
                            first.ceiling(structure).eq.add(i);

                        }
                        if (arrString[i+1] != -1) {
                            Structure structure = new Structure();
                            structure.number = arrString[i+1];
                            if (!(second.add(structure))) {
                                second.ceiling(structure).eq.add(i);
                            }
                            second.ceiling(structure).eq.add(i);
                        }
                        if (arrString[i+2] != -1) {
                            Structure structure = new Structure();
                            structure.number = arrString[i+2];
                            if (!(third.add(structure))) {
                                third.ceiling(structure).eq.add(i);
                            }
                            third.ceiling(structure).eq.add(i);
                        }
                    } else badStringCount++;

                } else badStringCount++;
            i += 3;

            }

        return i;
    }

    public static int countStringToGroup(TreeSet<Group> groups){
        int temp = 0;
        for (Group group : groups) {
            if(group.size > 1) counterGroups += group.strings.size();
            for (Set<Integer> string : group.strings) {
                temp += string.size();
            }
        }
        return  temp;
    }

    public static void printGroups(TreeSet<Group> groups){
        int i = 1;
        for (Group group : groups) {
            if(group.size == 0) continue;
            for (Set<Integer> string : group.strings) {
                if(i==101) return; //show 100 first groups
                System.out.println("Group number: " + i);
                System.out.println("Size of group: " + string.size());
                for (Integer integer : string) {
                    System.out.println("X= " + arrString[integer] + "; Y= " +
                            arrString[integer+1] + "; Z= " + arrString[integer+2]);
                }
                i++;
            }
        }
        System.out.println("All groups: " + (i-1));
    }


    public static Structure getStructure(int index, int rank){
        Structure structure = new Structure();
        structure.index = index;
        if(rank == 1) structure.number = arrString[index];
        if(rank == 2) structure.number = arrString[index+1];
        if(rank == 3) structure.number = arrString[index+2];
        return structure;
    }

    public static Set<Integer> addToGroup(Set<Integer> stringIndex, Structure str, int rank){
        if(rank == 1){
            if(first.contains(str)){
                Structure strFromSet = first.ceiling(str);
                for (Integer integer : strFromSet.eq) {
                    stringIndex.add(integer);
                }
            }
            deleteStrings(stringIndex, rank);
            return stringIndex;
        }
        if(rank == 2){
            if(second.contains(str)){
                Structure strFromSet = second.ceiling(str);
                for (Integer integer : strFromSet.eq) {
                    stringIndex.add(integer);
                }
            }
            deleteStrings(stringIndex, rank);
            return stringIndex;
        }
        if(rank == 3){
            if(third.contains(str)){
                Structure strFromSet = third.ceiling(str);
                for (Integer integer : strFromSet.eq) {
                    stringIndex.add(integer);
                }
            }
            deleteStrings(stringIndex, rank);
            return stringIndex;
        }
        return stringIndex;
    }

    public static void deleteStrings(Set<Integer> indexString, int rank){
        if(rank == 1){
            for (Integer integer : indexString) {
                Structure temp = getStructure(integer, 1);
                first.remove(temp);
            }

        }
        if(rank == 2){
            for (Integer integer : indexString) {
                Structure temp = getStructure(integer, 2);
                second.remove(temp);
            }

        }
        if(rank == 3){
            for (Integer integer : indexString) {
                Structure temp = getStructure(integer, 3);
                third.remove(temp);
            }

        }
    }

    public static void adding(Set<Integer> strings){
        Set<Integer> temp = new HashSet<>();
        for (Integer integer : strings) {
            addToGroup(temp, getStructure(integer, 1), 1);
        }
        for (Integer integer : temp) {
            strings.add(integer);
        }
        temp.clear();
        for (Integer integer : strings) {
            addToGroup(temp, getStructure(integer, 2), 2);
        }
        for (Integer integer : temp) {
            strings.add(integer);
        }
        temp.clear();
        for (Integer integer : strings) {
            addToGroup(temp, getStructure(integer, 3), 3);
        }
        for (Integer integer : temp) {
            strings.add(integer);
        }
    }



}
