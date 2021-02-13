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
    static int countGroupWithSizeMoreOne = 0;
    static TreeSet<Structure> first = new TreeSet<>(new CoordComp());
    static TreeSet<Structure> second = new TreeSet<>(new CoordComp());
    static TreeSet<Structure> third = new TreeSet<>(new CoordComp());
    static TreeSet<Group> groups = new TreeSet<>(new GroupComp());
    static Coord[] arrCoord = new Coord[1000100];



    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        int count = readFileToList(args[0]);
        for (int i = 0; i < count; i++) {
            Group group = new Group();
            Set<Integer> strings = new HashSet<>();
            if(arrCoord[i].getX() == -1 && arrCoord[i].getY() == -1 && arrCoord[i].getZ() == -1){
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
//            deleteStrings(strings, 1);
//            deleteStrings(strings, 2);
//            deleteStrings(strings, 3);
        }

//        System.out.println("_____________________");
//        printTreeSets(first);
//        System.out.println("_____________________");
//        printTreeSets(second);
//        System.out.println("_____________________");
//        printTreeSets(third);
//        System.out.println("_____________________");
        printGroups(groups);
        System.out.println("bad strings: " + badStringCount);
        System.out.println("correct strings: " + correctStringCount);
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

                        Coord coord = new Coord();

                        if (arr[0].matches("\"\"")) {
                            coord.setX(-1);
                        }
                        if (arr[0].matches("\"\\d+\"")) {
                            String temp = arr[0].substring(1, arr[0].length() - 1);
                            coord.setX(Long.parseLong(temp));
                        }
                        if (arr[1].matches("\"\"")) {
                            coord.setY(-1);
                        }
                        if (arr[1].matches("\"\\d+\"")) {
                            String temp = arr[1].substring(1, arr[1].length() - 1);
                            coord.setY(Long.parseLong(temp));
                        }
                        if (arr[2].matches("\"\"")) {
                            coord.setZ(-1);
                        }
                        if (arr[2].matches("\"\\d+\"")) {
                            String temp = arr[2].substring(1, arr[2].length() - 1);
                            coord.setZ(Long.parseLong(temp));
                        }
                        correctStringCount++;
                        arrCoord[i] = coord;
                        if (coord.getX() != -1) {
                            Structure structure = new Structure();
                            structure.number = coord.getX();
                            if (!(first.add(structure))) {
                                first.ceiling(structure).eq.add(i);
                            }
                            first.ceiling(structure).eq.add(i);

                        }
                        if (coord.getY() != -1) {
                            Structure structure = new Structure();
                            structure.number = coord.getY();
                            if (!(second.add(structure))) {
                                second.ceiling(structure).eq.add(i);
                            }
                            second.ceiling(structure).eq.add(i);
                        }
                        if (coord.getZ() != -1) {
                            Structure structure = new Structure();
                            structure.number = coord.getZ();
                            if (!(third.add(structure))) {
                                third.ceiling(structure).eq.add(i);
                            }
                            third.ceiling(structure).eq.add(i);
                        }
                        i++;
                    } else badStringCount++;

                } else badStringCount++;

            }

        return i;
    }

    public static void printGroups(TreeSet<Group> groups){
        int i = 1;
        for (Group group : groups) {
            if(group.size == 0) continue;
            for (Set<Integer> string : group.strings) {
                if(i==40) return;
//                if(string.size() == 0) continue;
                System.out.println("Group number: " + i);
                System.out.println("Size of group: " + string.size());
                for (Integer integer : string) {
                    allStringAtGroups++;
                    System.out.println("X= " + arrCoord[integer].getX() + "; Y= " +
                            arrCoord[integer].getY() + "; Z= " + arrCoord[integer].getZ());
                }
                i++;
            }
        }
        System.out.println("All groups: " + (i-1));
    }

    public static  void printTreeSets(TreeSet<Structure> set){
        for (Structure structure : set) {
            System.out.println("___________________________");
            for (Integer integer : structure.eq) {
                System.out.println("X= " + arrCoord[integer].getX() + "; Y= " +
                        arrCoord[integer].getY() + "; Z= " + arrCoord[integer].getZ());

            }
            System.out.println("___________________________");
        }
    }

    public static Structure getStructure(int index, int rank){
        Structure structure = new Structure();
        structure.index = index;
        if(rank == 1) structure.number = arrCoord[index].getX();
        if(rank == 2) structure.number = arrCoord[index].getY();
        if(rank == 3) structure.number = arrCoord[index].getZ();
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
