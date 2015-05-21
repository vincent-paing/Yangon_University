package aungkyawpaing.yangonuniversity.Utils;

import java.util.ArrayList;

import aungkyawpaing.yangonuniversity.ClassModels.Department;

/**
 * Created by Vincent on 13-May-15.
 */
public class Util {

    public static ArrayList<Department> SortDepartment(ArrayList<Department> dept_list) {

        for (int i = 0; i < dept_list.size() - 1; i++) {
            for (int j = 0; j < dept_list.size() - i - 1; j++) {
                if (dept_list.get(j).getDept_name().compareTo(dept_list.get(j+1).getDept_name()) > 0) {
                    Department temp = dept_list.get(j);
                    dept_list.set(j, dept_list.get(j+1));
                    dept_list.set(j+1, temp);
                }
            }
        }
        return dept_list;
    }

    public static String unescape(String description) {
        return description.replaceAll("\\\\n", "\\\n").replaceAll("\\\\t", "\\\t");
    }
}
