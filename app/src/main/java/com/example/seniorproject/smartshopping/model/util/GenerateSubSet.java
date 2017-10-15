package com.example.seniorproject.smartshopping.model.util;

import com.example.seniorproject.smartshopping.model.dao.ProductCrowd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boyburin on 9/25/2017 AD.
 */

public class GenerateSubSet {

    public static ArrayList<ArrayList<ProductCrowd>> powerSet(List<ProductCrowd> intList) {

        ArrayList<ArrayList<ProductCrowd>> result = new ArrayList<ArrayList<ProductCrowd>>();
        result.add(new ArrayList<ProductCrowd>());

        for (ProductCrowd i : intList) {
            ArrayList<ArrayList<ProductCrowd>> temp = new ArrayList<ArrayList<ProductCrowd>>();

            for (ArrayList<ProductCrowd> innerList : result) {
                innerList = new ArrayList<ProductCrowd>(innerList);
                innerList.add(i);
                temp.add(innerList);
            }
            result.addAll(temp);
        }

        return result;
    }
}
