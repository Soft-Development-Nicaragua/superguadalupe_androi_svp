/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.sdn.tableview;

import android.content.Context;

import com.sdn.bd.local.BDUtil;
import com.sdn.tableview.model.Cell;
import com.sdn.tableview.model.ColumnHeader;
import com.sdn.tableview.model.RowHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class TableViewModel {
    private Context mContext;

    ArrayList<String> Columnas=new ArrayList<String> ();
    ArrayList<ArrayList> Filas=new ArrayList<ArrayList> ();

    public TableViewModel(Context context) {
        mContext = context;
    }

    public TableViewModel(Context context, String  Query) {
        mContext = context;
        Columnas = BDUtil.getColumnNames(context,Query);
        Filas = BDUtil.getData(context,Query);
    }

    private List<RowHeader> getMyRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = Filas.size(); i > 0; i--) {
            RowHeader header = new RowHeader(String.valueOf(i), "" + i);
            list.add(header);
        }
        return list;
    }

    private List<ColumnHeader> getMyColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();

        for (int i = 0; i < Columnas.size(); i++) {
            String title = Columnas.get(i);
            ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
            list.add(header);
        }

        return list;
    }

    private List<List<Cell>> getCellListForSortingTest() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < Filas.size(); i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < Columnas.size(); j++) {
                try{
                    cellList.add(new Cell(j + "" + i, Filas.get(i).get(j).toString()));
                }catch (Exception e){
                    cellList.add(new Cell(j + "" + i, ""));
                }

            }
            list.add(cellList);
        }

        return list;
    }

    public List<List<Cell>> getCellList() {
        return getCellListForSortingTest();
    }

    public List<RowHeader> getRowHeaderList() {
        return getMyRowHeaderList();
    }

    public List<ColumnHeader> getColumnHeaderList() {
        return getMyColumnHeaderList();
    }

    public int getRowCount() {
        return Filas.size();
    }

    public int getColumnCount() {
        return Columnas.size();
    }



}
